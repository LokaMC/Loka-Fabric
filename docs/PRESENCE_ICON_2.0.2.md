# Player-head badge fix — 2.0.2

The small Minecraft head in the Discord activity was configured only as
`getOrDefault(loka.player_icon)`. Loka UI returns an empty value whenever it has
no active Loka session, and the optional bridge registers its arguments only
after CraftPresence becomes available. There was no fallback image in those
states. The large Loka logo is a separate field and is unchanged.

The shipped badge now uses CraftPresence's built-in `player.uuid.short` from the
signed-in Minecraft account, independently of affiliation lookup and server
connection state. The direct image is a 128px PNG from Minotar's documented
helmet-layer endpoint, using the compact UUID without a username lookup or
UUID-normalization redirect. A registered Minecraft `grass` icon is used until
a valid account UUID is available. The tooltip uses the account name.

Both the previous image endpoint and Minotar returned valid PNG images during
the investigation. No image-provider outage or Discord-side render failure was
reproduced. This fix addresses the confirmed empty-placeholder configuration
and replaces the old avatar URL, which also avoids reusing any cached failure
for that URL. It does not claim to prevent every third-party image outage.

Validation used the installed CraftPresence 2.7.1 and UniLib 1.2.1 to compile
real payloads for both editions. Checks cover:

- No account and no Loka bridge: registered fallback instead of an empty image.
- Account ready before the bridge: the account's PNG avatar and tooltip.
- Stale or empty Loka artwork arguments: avatar remains tied to the account.
- Account switch: avatar changes to the new UUID.
- Account UUID reloading: fallback instead of a malformed URL or old head.
- Existing logo, rank/membership, elapsed time, link buttons and privacy fields.

The tests send no Discord activity. No game was launched, no Minecraft server
login was performed and no existing profile was modified. Update the pack to
2.0.2 and restart Minecraft to load the corrected configuration. The production
mod jars and dependency pins are unchanged from 2.0.1.

[Minotar's official endpoint documentation](https://minotar.net/)
