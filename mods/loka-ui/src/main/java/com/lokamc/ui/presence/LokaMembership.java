package com.lokamc.ui.presence;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Set;

/** A strict allowlist of affiliation fields; never reads location or world data. */
public record LokaMembership(String town, String alliance) {
    public static final LokaMembership UNKNOWN = new LokaMembership("", "");

    public static LokaMembership resolve(JsonObject player, JsonArray towns,
                                          JsonArray alliances) throws IOException {
        Set<String> identities = new LinkedHashSet<>();
        for (String field : new String[]{"identityId", "id", "uuid"}) {
            String id = text(player, field);
            if (!id.isEmpty()) identities.add(id);
        }
        if (identities.isEmpty()) throw new IOException("Missing player identity");
        String townId = "", townName = "";
        for (JsonElement entry : towns) {
            JsonObject town = entry.getAsJsonObject();
            if (!town.has("deleted") || !town.get("deleted").isJsonPrimitive()
                || !town.get("deleted").getAsJsonPrimitive().isBoolean())
                throw new IOException("Missing town lifecycle state");
            if (town.get("deleted").getAsBoolean()) continue;
            JsonObject members = town.has("members") && town.get("members").isJsonObject()
                ? town.getAsJsonObject("members") : new JsonObject();
            boolean member = identities.stream().anyMatch(members::has)
                || identities.contains(text(town, "owner"));
            // Older API responses use Minecraft UUIDs in member records.
            if (!member) for (JsonElement value : members.asMap().values()) {
                if (value.isJsonObject() && identities.contains(text(value.getAsJsonObject(), "uuid"))) {
                    member = true;
                    break;
                }
            }
            if (!member) continue;
            String id = text(town, "id");
            String name = clean(text(town, "name"));
            if (id.isEmpty() || name.isEmpty()) throw new IOException("Incomplete town membership");
            if (!townId.isEmpty() && !townId.equals(id)) throw new IOException("Ambiguous town membership");
            townId = id;
            townName = name;
        }
        if (townId.isEmpty()) return UNKNOWN;
        String allianceName = "";
        for (JsonElement entry : alliances) {
            JsonObject alliance = entry.getAsJsonObject();
            boolean member = townId.equals(text(alliance, "leaderId"));
            if (alliance.has("townIds") && alliance.get("townIds").isJsonArray()) {
                for (JsonElement id : alliance.getAsJsonArray("townIds")) {
                    if (id.isJsonPrimitive() && townId.equals(id.getAsString())) member = true;
                }
            }
            if (member) {
                String name = clean(text(alliance, "name"));
                if (name.isEmpty()) throw new IOException("Incomplete alliance membership");
                if (!allianceName.isEmpty() && !allianceName.equals(name))
                    throw new IOException("Ambiguous alliance membership");
                allianceName = name;
            }
        }
        return new LokaMembership(townName, allianceName);
    }

    public String details() { return town.isEmpty() ? "Forge Your Story" : "Town • " + town; }
    public String state() {
        return town.isEmpty() ? "Playing on Loka" : alliance.isEmpty()
            ? "Independent Town" : "Alliance • " + alliance;
    }

    static String text(JsonObject object, String key) {
        JsonElement value = object.get(key);
        return value != null && value.isJsonPrimitive() && value.getAsJsonPrimitive().isString()
            ? value.getAsString() : "";
    }

    static String clean(String input) {
        String value = input.replaceAll("§.", "").replaceAll("[\\p{Cc}\\p{Cf}]", " ")
            .replaceAll("\\s+", " ").strip();
        // Prefixes plus name fit Discord's 128-character text fields.
        int count = Math.min(100, value.codePointCount(0, value.length()));
        return value.substring(0, value.offsetByCodePoints(0, count));
    }
}
