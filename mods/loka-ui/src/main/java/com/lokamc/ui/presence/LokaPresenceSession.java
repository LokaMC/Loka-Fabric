package com.lokamc.ui.presence;

import java.util.Locale;
import java.util.UUID;

/** Immutable snapshot prevents mixed fields when CraftPresence reads on its own thread. */
public record LokaPresenceSession(UUID uuid, String player, long started, LokaMembership membership) {
    public static boolean isLoka(String address) {
        String host = address.strip().toLowerCase(Locale.ROOT);
        int colon = host.indexOf(':');
        if (colon >= 0) host = host.substring(0, colon);
        if (host.endsWith(".")) host = host.substring(0, host.length() - 1);
        return host.equals("play.lokamc.com") || host.equals("eu.lokamc.com")
            || host.equals("lokamc.com");
    }

    public String playerIcon() { return "https://mc-heads.net/avatar/" + uuid; }
}
