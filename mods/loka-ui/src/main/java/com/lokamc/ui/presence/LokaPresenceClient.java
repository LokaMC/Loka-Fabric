package com.lokamc.ui.presence;

import com.google.gson.JsonParser;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.util.UUID;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Supplier;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import org.slf4j.LoggerFactory;

/** Publishes affiliation only. Does not inspect dimensions, coordinates, screens or chat. */
public final class LokaPresenceClient implements ClientModInitializer {
    private volatile LokaPresenceSession session;
    private final ExecutorService worker = Executors.newSingleThreadExecutor(r -> {
        Thread thread = new Thread(r, "Loka affiliation lookup");
        thread.setDaemon(true);
        return thread;
    });
    private final LokaApi api = new LokaApi();
    private long generation, nextLookup, nextBridge, lookupNotBefore;
    private boolean lookupRunning, showMembership = true, bridgeFailed;
    private int refreshSeconds = 600;
    private final long launched = System.currentTimeMillis();
    private record Cached(LokaMembership membership, long expires) {}
    private final Map<UUID, Cached> cache = new HashMap<>();

    @Override public void onInitializeClient() {
        if (!FabricLoader.getInstance().isModLoaded("craftpresence")) {
            worker.shutdownNow();
            return;
        }
        loadOptions();
        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> begin(client));
        ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> clear());
        ClientLifecycleEvents.CLIENT_STOPPING.register(client -> { clear(); worker.shutdownNow(); });
        ClientTickEvents.END_CLIENT_TICK.register(this::tick);
    }

    private void loadOptions() {
        var path = FabricLoader.getInstance().getConfigDir().resolve("loka-presence.json");
        if (!Files.exists(path)) return;
        try (var reader = Files.newBufferedReader(path)) {
            var config = JsonParser.parseReader(reader).getAsJsonObject();
            if (config.has("showTownAndAlliance")) showMembership = config.get("showTownAndAlliance").getAsBoolean();
            if (config.has("refreshSeconds")) refreshSeconds = Math.clamp(config.get("refreshSeconds").getAsInt(), 600, 3600);
        } catch (Exception invalid) {
            showMembership = false;
            LoggerFactory.getLogger("LokaPresence").warn("Invalid loka-presence.json; affiliation lookup disabled.");
        }
    }

    private void begin(Minecraft client) {
        clear();
        var server = client.getCurrentServer();
        if (server == null || !LokaPresenceSession.isLoka(server.ip) || client.player == null) return;
        UUID uuid = client.player.getUUID();
        long now = System.nanoTime();
        var cached = cache.get(uuid);
        boolean fresh = cached != null && now < cached.expires();
        session = new LokaPresenceSession(uuid, client.player.getName().getString(),
            System.currentTimeMillis(), fresh ? cached.membership() : LokaMembership.UNKNOWN);
        nextLookup = fresh ? cached.expires() : now + 5_000_000_000L;
    }

    private void clear() {
        session = null;
        generation++;
        // An old request may finish later; its result is rejected by the generation check.
    }

    private void tick(Minecraft client) {
        long now = System.nanoTime();
        if (!bridgeFailed && now >= nextBridge) {
            nextBridge = now + 5_000_000_000L;
            syncBridge();
        }
        var current = session;
        if (current == null || !showMembership || lookupRunning || now < nextLookup || now < lookupNotBefore) return;
        lookupRunning = true;
        nextLookup = now + refreshSeconds * 1_000_000_000L;
        lookupNotBefore = now + 60_000_000_000L;
        long requestGeneration = generation;
        UUID uuid = current.uuid();
        worker.execute(() -> {
            LokaMembership result;
            boolean success;
            try {
                result = api.lookup(uuid);
                success = true;
            } catch (Exception unavailable) {
                result = LokaMembership.UNKNOWN;
                success = false;
                // No raw response, player IDs or town data in logs.
                LoggerFactory.getLogger("LokaPresence").debug("Affiliation service unavailable; using generic Loka presence.");
                if (unavailable instanceof InterruptedException) Thread.currentThread().interrupt();
            }
            LokaMembership resolved = result;
            boolean cacheResult = success;
            client.execute(() -> {
                lookupRunning = false;
                var latest = session;
                if (requestGeneration != generation || latest == null || !latest.uuid().equals(uuid)) return;
                cache.entrySet().removeIf(entry -> System.nanoTime() >= entry.getValue().expires());
                if (cacheResult) cache.put(uuid, new Cached(resolved, nextLookup));
                else cache.remove(uuid);
                session = new LokaPresenceSession(latest.uuid(), latest.player(), latest.started(), resolved);
            });
        });
    }

    private String details() {
        var current = session;
        return current == null ? "Forge Your Story" : current.membership().details();
    }
    private String state() {
        var current = session;
        return current == null ? "" : current.membership().state();
    }
    private String icon() { var current = session; return current == null ? "" : current.playerIcon(); }
    private String badge() { var current = session; return current == null ? "" : current.player() + " • Loka"; }
    private String started() { var current = session; return Long.toString(current == null ? launched : current.started()); }

    private void syncBridge() {
        try {
            // Optional CraftPresence API: no bundled duplicate mod or RPC client.
            Object presence = Class.forName("com.gitlab.cdagaming.craftpresence.CraftPresence").getField("CLIENT").get(null);
            if (!(boolean) presence.getClass().getMethod("isAvailable").invoke(presence)) return;
            Method sync = presence.getClass().getMethod("syncArgument", String.class, Supplier.class, boolean.class);
            bind(sync, presence, "loka.details", this::details);
            bind(sync, presence, "loka.state", this::state);
            bind(sync, presence, "loka.player_icon", this::icon);
            bind(sync, presence, "loka.player_badge", this::badge);
            bind(sync, presence, "loka.started", this::started);
        } catch (ReflectiveOperationException | LinkageError unavailable) {
            bridgeFailed = true;
            showMembership = false;
            session = null;
            LoggerFactory.getLogger("LokaPresence").warn("CraftPresence affiliation bridge unavailable; using configured Loka presence.");
        }
    }
    private static void bind(Method sync, Object target, String key, Supplier<Object> value)
        throws ReflectiveOperationException { sync.invoke(target, key, value, true); }
}
