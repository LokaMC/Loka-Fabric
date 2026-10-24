package com.lokamc.ui.presence;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Supplier;

/** Compiles real Discord payloads with the installed CraftPresence release, without sending them. */
public final class CraftPresenceChecks {
    private static void check(boolean valid, String label) {
        if (!valid) throw new AssertionError(label);
    }
    public static void main(String[] args) throws Exception {
        String prefix = "com.gitlab.cdagaming.craftpresence.core.";
        var type = Class.forName(prefix + "integrations.discord.DiscordUtils");
        var client = type.getConstructor().newInstance();
        Class.forName(prefix + "integrations.discord.FunctionsLib").getMethod("init", type).invoke(null, client);
        var config = JsonParser.parseString(Files.readString(Path.of(args[0]))).getAsJsonObject();
        var dataType = Class.forName(prefix + "config.element.PresenceData");
        var data = new Gson().fromJson(config.getAsJsonObject("displaySettings").get("presenceData"), dataType);
        var sync = type.getMethod("syncArgument", String.class, Supplier.class, boolean.class);
        sync.invoke(client, "data.general.time", (Supplier<Object>) () -> "1700000000000", true);
        var compile = type.getMethod("compilePresence", dataType, boolean.class);
        var pair = compile.invoke(client, data, false);
        Object rich = pair.getClass().getMethod("getSecond").invoke(pair);
        JsonObject generic = (JsonObject) rich.getClass().getMethod("toJson").invoke(rich);
        check(generic.get("details").getAsString().equals("Forge Your Story"), "Safe fallback when bridge is absent");
        check(generic.get("state").getAsString().equals(config.getAsJsonObject("displaySettings").getAsJsonObject("dynamicVariables").get("pack").getAsString()), "Pack fallback");
        check(generic.getAsJsonObject("assets").get("small_image").getAsString().equals("grass"), "Minecraft fallback before account is ready");
        sync.invoke(client, "player.uuid.short", (Supplier<Object>) () -> "f52fbd09a374407c803302acd1c9cc40", true);
        sync.invoke(client, "player.name", (Supplier<Object>) () -> "Player", true);
        pair = compile.invoke(client, data, false);
        rich = pair.getClass().getMethod("getSecond").invoke(pair);
        JsonObject accountOnly = (JsonObject) rich.getClass().getMethod("toJson").invoke(rich);
        final String expectedIcon = "https://minotar.net/helm/f52fbd09a374407c803302acd1c9cc40/128.png";
        check(accountOnly.getAsJsonObject("assets").get("small_image").getAsString().equals(expectedIcon), "Player head before the Loka bridge is ready");
        check(accountOnly.getAsJsonObject("assets").get("small_text").getAsString().equals("Player • Loka Minecraft"), "Account badge tooltip: " + accountOnly.getAsJsonObject("assets").get("small_text"));
        for (var entry : new String[][]{{"loka.details","Player | Guardian"},
                {"loka.state","{world.name} of Test Alliance"},{"loka.player_icon","https://mc-heads.net/avatar/test"},
                {"loka.player_badge","Player • Loka Minecraft"},{"loka.started","1800000000000"}}) {
            sync.invoke(client, entry[0], (Supplier<Object>) () -> entry[1], true);
        }
        pair = compile.invoke(client, data, false);
        rich = pair.getClass().getMethod("getSecond").invoke(pair);
        JsonObject published = (JsonObject) rich.getClass().getMethod("toJson").invoke(rich);
        check(published.get("details").getAsString().equals("Player | Guardian"), "Username and rank on first line");
        check(published.get("state").getAsString().equals("{world.name} of Test Alliance"), "Literal affiliation on second line; names never evaluate placeholders");
        check(published.get("name").getAsString().equals("Loka Minecraft"), "Loka Minecraft application title");
        var assets = published.getAsJsonObject("assets");
        check(assets.get("large_image").getAsString().equals(config.getAsJsonObject("displaySettings").getAsJsonObject("presenceData").get("largeImageKey").getAsString()), "Configured circular standard Loka Fabric logo");
        check(assets.get("large_url").getAsString().equals("https://lokamc.com/"), "Image click-through");
        check(assets.get("small_image").getAsString().equals(expectedIcon), "Player head does not depend on bridge artwork");
        check(published.getAsJsonObject("timestamps").get("start").getAsLong() == 1800000000000L, "Session timestamp");
        check(published.getAsJsonArray("buttons").size() == 2, "Both link buttons");
        check(!published.has("party") && !published.has("secrets"), "No join/location payload");
        sync.invoke(client, "loka.player_icon", (Supplier<Object>) () -> "", true);
        sync.invoke(client, "player.uuid.short", (Supplier<Object>) () -> "069a79f444e94726a5befca90e38aaf5", true);
        sync.invoke(client, "player.name", (Supplier<Object>) () -> "OtherPlayer", true);
        pair = compile.invoke(client, data, false);
        rich = pair.getClass().getMethod("getSecond").invoke(pair);
        JsonObject switched = (JsonObject) rich.getClass().getMethod("toJson").invoke(rich);
        check(switched.getAsJsonObject("assets").get("small_image").getAsString().equals("https://minotar.net/helm/069a79f444e94726a5befca90e38aaf5/128.png"), "Empty bridge and account switch cannot retain or hide the old head");
        sync.invoke(client, "player.uuid.short", (Supplier<Object>) () -> "", true);
        pair = compile.invoke(client, data, false);
        rich = pair.getClass().getMethod("getSecond").invoke(pair);
        JsonObject loadingAccount = (JsonObject) rich.getClass().getMethod("toJson").invoke(rich);
        check(loadingAccount.getAsJsonObject("assets").get("small_image").getAsString().equals("grass"), "Account reloading uses fallback rather than a broken avatar URL");
        System.out.println("Installed CraftPresence payload checks passed.");
    }
}
