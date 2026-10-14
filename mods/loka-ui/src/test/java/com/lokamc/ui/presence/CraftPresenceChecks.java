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
        check(generic.get("state").getAsString().equals("Loka Fabric"), "Pack fallback");
        for (var entry : new String[][]{{"loka.details","Town • {world.name}"},
                {"loka.state","Alliance • Test Alliance"},{"loka.player_icon","https://mc-heads.net/avatar/test"},
                {"loka.player_badge","Player • Loka"},{"loka.started","1800000000000"}}) {
            sync.invoke(client, entry[0], (Supplier<Object>) () -> entry[1], true);
        }
        pair = compile.invoke(client, data, false);
        rich = pair.getClass().getMethod("getSecond").invoke(pair);
        JsonObject published = (JsonObject) rich.getClass().getMethod("toJson").invoke(rich);
        check(published.get("details").getAsString().equals("Town • {world.name}"), "API names stay literal, never evaluate placeholders");
        check(published.get("state").getAsString().equals("Alliance • Test Alliance"), "Membership expression integration");
        check(published.get("name").getAsString().equals("Loka"), "Loka application title");
        var assets = published.getAsJsonObject("assets");
        check(assets.get("large_image").getAsString().contains(".gif?size=512"), "Animated Loka image is an image, not a link");
        check(assets.get("large_url").getAsString().equals("https://lokamc.com/"), "Image click-through");
        check(assets.get("small_image").getAsString().equals("https://mc-heads.net/avatar/test"), "Player badge");
        check(published.getAsJsonObject("timestamps").get("start").getAsLong() == 1800000000000L, "Session timestamp");
        check(published.getAsJsonArray("buttons").size() == 2, "Both link buttons");
        check(!published.has("party") && !published.has("secrets"), "No join/location payload");
        System.out.println("Installed CraftPresence payload checks passed.");
    }
}
