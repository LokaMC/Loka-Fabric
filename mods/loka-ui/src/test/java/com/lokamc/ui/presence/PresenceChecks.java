package com.lokamc.ui.presence;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.IOException;
import java.util.UUID;

/** Regression checks for affiliation identity, ambiguity and disclosure boundaries. */
public final class PresenceChecks {
    private static JsonObject object(String json) { return JsonParser.parseString(json).getAsJsonObject(); }
    private static JsonArray array(String json) { return JsonParser.parseString(json).getAsJsonArray(); }
    private static void check(boolean condition, String description) {
        if (!condition) throw new AssertionError(description);
    }
    private static void rejected(JsonObject player, JsonArray towns, JsonArray alliances) throws Exception {
        try { LokaMembership.resolve(player, towns, alliances); }
        catch (IOException expected) { return; }
        throw new AssertionError("Ambiguous/incomplete membership must not publish");
    }
    public static void main(String[] args) throws Exception {
        UUID uuid = UUID.fromString("f52fbd09-a374-407c-8033-02acd1c9cc40");
        JsonObject player = object("{\"id\":\"alt\",\"identityId\":\"person\",\"uuid\":\"" + uuid + "\"}");
        JsonArray towns = array("""
            [{"id":"old","name":"Old Town","deleted":true,"members":{"person":{}}},
             {"id":"home","name":"§6Home Town","deleted":false,"members":{"person":{}},
              "world":"SECRET_WORLD","coordinates":"SECRET_COORDINATES","slogan":"SECRET_SLOGAN"}]
            """);
        JsonArray alliances = array("""
            [{"id":"alliance","name":"The Alliance","townIds":["home"],
              "world":"SECRET_CONTINENT","vulnerabilityWindow":"SECRET_WINDOW"}]
            """);
        LokaMembership result = LokaMembership.resolve(player, towns, alliances);
        check(result.town().equals("Home Town") && result.alliance().equals("The Alliance"), "Identity-based membership including alts");
        check(result.details().equals("Town • Home Town") && result.state().equals("Alliance • The Alliance"), "Display affiliation");
        check(!result.toString().contains("SECRET"), "No world, coordinates, slogan or vulnerability data");
        check(LokaMembership.resolve(player, towns, new JsonArray()).state().equals("Independent Town"), "Town without alliance");
        check(LokaMembership.resolve(player, new JsonArray(), alliances).equals(LokaMembership.UNKNOWN), "Townless fallback");
        check(LokaMembership.UNKNOWN.details().equals("Forge Your Story"), "Unavailable/townless display");
        JsonArray legacy = array("[{\"id\":\"legacy\",\"name\":\"Legacy\",\"deleted\":false,\"members\":{\"key\":{\"uuid\":\"" + uuid + "\"}}}]");
        check(LokaMembership.resolve(player, legacy, new JsonArray()).town().equals("Legacy"), "Legacy UUID records");
        JsonArray owner = array("[{\"id\":\"owned\",\"name\":\"Owned\",\"deleted\":false,\"owner\":\"person\"}]");
        check(LokaMembership.resolve(player, owner, new JsonArray()).town().equals("Owned"), "Town owner");
        JsonArray ambiguous = towns.deepCopy();
        ambiguous.add(object("{\"id\":\"other\",\"name\":\"Other\",\"deleted\":false,\"members\":{\"person\":{}}}"));
        rejected(player, ambiguous, alliances);
        rejected(new JsonObject(), towns, alliances);
        rejected(player, array("[{\"id\":\"home\",\"name\":\"Home\",\"members\":{\"person\":{}}}]"), alliances);
        JsonArray duplicateAlliance = alliances.deepCopy();
        duplicateAlliance.add(object("{\"name\":\"Other Alliance\",\"townIds\":[\"home\"]}"));
        rejected(player, towns, duplicateAlliance);
        check(LokaMembership.clean("§bTown\n\u202eName").equals("Town Name"), "Sanitize formatting and direction controls");
        String longName = "👑".repeat(120);
        check(LokaMembership.clean(longName).codePointCount(0, LokaMembership.clean(longName).length()) == 100, "Unicode-safe field limit");
        for (String host : new String[]{"play.lokamc.com", "EU.LOKAMC.COM:25565", "play.lokamc.com.:25565"})
            check(LokaPresenceSession.isLoka(host), "Public Loka hostname");
        for (String host : new String[]{"play.lokamc.com.evil.test", "notlokamc.com", "localhost", "test.lokamc.com"})
            check(!LokaPresenceSession.isLoka(host), "No lookup outside public Loka");
        var session = new LokaPresenceSession(uuid, "Player", 1234567890000L, result);
        check(session.started() == 1234567890000L && session.playerIcon().endsWith(uuid.toString()), "Stable timestamp and account-specific badge");
        System.out.println("Presence affiliation/privacy checks passed.");
        if (args.length > 0) {
            var live = new LokaApi().lookup(UUID.fromString(args[0]));
            System.out.println("Live Loka API lookup: " + live.details() + " | " + live.state());
        }
    }
}
