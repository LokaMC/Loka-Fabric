package com.lokamc.ui.presence;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/** Called only on the worker thread. HTTPS, bounded pages/size/time, no game commands. */
public final class LokaApi {
    private static final String BASE = "https://api.lokamc.com";
    private final HttpClient http = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(8))
        .followRedirects(HttpClient.Redirect.NEVER).build();

    public LokaMembership lookup(UUID uuid) throws IOException, InterruptedException {
        JsonObject player = get("/players/search/findByUuid?uuid=" + uuid);
        if (!uuid.toString().equalsIgnoreCase(LokaMembership.text(player, "uuid")))
            throw new IOException("Player UUID mismatch");
        return LokaMembership.resolve(player, collection("/towns/search/findAll", "towns"),
                                               collection("/alliances", "alliances"));
    }

    private JsonArray collection(String path, String key) throws IOException, InterruptedException {
        JsonArray rows = new JsonArray();
        for (int page = 0; page < 20; page++) {
            JsonObject response = get(path + "?size=100&page=" + page);
            if (!response.has("_embedded") || !response.get("_embedded").isJsonObject()
                || !response.getAsJsonObject("_embedded").has(key)
                || !response.getAsJsonObject("_embedded").get(key).isJsonArray()
                || !response.has("page") || !response.get("page").isJsonObject())
                throw new IOException("Incomplete membership listing");
            rows.addAll(response.getAsJsonObject("_embedded").getAsJsonArray(key));
            JsonObject pagination = response.getAsJsonObject("page");
            if (!pagination.has("totalPages") || !pagination.has("number")
                || pagination.get("number").getAsInt() != page)
                throw new IOException("Invalid membership pagination");
            int total = pagination.get("totalPages").getAsInt();
            if (total < 0 || total > 20) throw new IOException("Membership page limit exceeded");
            if (page + 1 >= total) return rows;
        }
        throw new IOException("Incomplete membership pagination");
    }

    private JsonObject get(String path) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder(URI.create(BASE + path))
            .timeout(Duration.ofSeconds(12)).header("Accept", "application/json")
            .header("User-Agent", "LokaFabricPresence/1.0 (client affiliation lookup)").GET().build();
        var pending = http.sendAsync(request, HttpResponse.BodyHandlers.ofByteArray());
        try {
            // Includes the complete response body, so a stalled stream cannot pin the worker.
            HttpResponse<byte[]> response = pending.get(15, TimeUnit.SECONDS);
            if (response.statusCode() != 200) throw new IOException("Loka API HTTP " + response.statusCode());
            byte[] bytes = response.body();
            if (bytes.length > 8 * 1024 * 1024) throw new IOException("Membership response too large");
            try {
                return JsonParser.parseString(new String(bytes, StandardCharsets.UTF_8)).getAsJsonObject();
            } catch (RuntimeException invalid) {
                throw new IOException("Invalid membership JSON", invalid);
            }
        } catch (ExecutionException | TimeoutException failure) {
            throw new IOException("Affiliation request failed", failure);
        } finally {
            pending.cancel(true);
        }
    }
}
