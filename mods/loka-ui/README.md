# Loka UI and presence

Client addon for the Minecraft 26.3 Loka Fabric and Loka Fabric+ packs. Keeps the
shared menu adjustments and supplies optional CraftPresence affiliation placeholders.

The title and Singleplayer world list share the pack's looping menu video.
All other out-of-world menus using Minecraft's panorama renderer show the same
watermarked Deep Dark artwork as terrain loading. Menus over a running world
retain Minecraft's normal world view and blur.

On a public Loka connection, the activity title is `Loka Minecraft`, with the
circular standard Loka Fabric logo and the player's head badge. The first detail
line shows `<username> | <public Loka rank>`; the second shows
`<membership town> of <membership alliance>`. Elapsed time starts on joining.
A town without an alliance omits `of <alliance>`; a missing rank leaves the username
alone. Missing affiliation uses `Forge Your Story`.
The two buttons open https://discord.gg/loka and https://modrinth.com/modpack/loka-fabric.

Only **public rank and membership** are displayed. The integration never reads or publishes a player's
current town/territory, coordinates, world, dimension, continent, combat state,
inventory, health, server population, or location-based artwork. No game commands
are sent, and no chat is intercepted or suppressed.

## Membership lookup

Five seconds after joining `play.lokamc.com`, `eu.lokamc.com`, or `lokamc.com`, a
background worker requests the current account's UUID from the official Loka API,
then matches its identity against active town membership/ownership and alliance
town IDs. The documented roster is used because the API's player `town` field can
be null even when that identity is present in a town's membership roster.

Requests use HTTPS and refresh every ten minutes. Fresh results are cached in memory
for quick reconnects; lookups have a one-minute minimum spacing across connections.
Pagination is bounded, requests time out, and malformed, ambiguous, or unavailable
data produces generic Loka presence. Disconnect clears displayed affiliation, and a
session generation check rejects responses arriving after disconnect or account changes.
Nothing is saved to an identity/membership cache on disk. Time and artwork never change
because of dimension changes, movement, opening inventory, or combat.

`config/loka-presence.json` controls affiliation display:

```json
{
  "showTownAndAlliance": true,
  "refreshSeconds": 600
}
```

Set `showTownAndAlliance` to `false` to stop membership requests and use the generic
Loka card. Changes apply on restart. Refresh values are clamped to 600–3600 seconds.
CraftPresence remains responsible for enabling/disabling Discord activity and sending
RPC updates; it is optional, and its absence does not disable the menu features.

The shared configuration is maintained by the repository's `scripts/loka_presence.py`. In CraftPresence,
`largeImageKey` is the artwork URL; `largeImageUrl` is the click-through website.
Player heads are served by mc-heads.net; the standard logo uses Loka Fabric's existing Modrinth CDN icon.

## Verification

Build with JDK 25 and the Gradle wrapper. `build` runs the executable affiliation/privacy
regression harness. `presenceChecks -PlokaTestUuid=<public UUID>` additionally exercises
the live Loka API. The optional `craftPresenceChecks` task uses the installed CraftPresence
and UniLib jars to compile real payloads without sending Discord activity; pass their
paths as `lokaCraftPresenceJar` / `lokaUniLibJar` and a generated CraftPresence configuration
as `lokaPresenceConfig`.

The payload check covers fallback text, literal untrusted names, standard circular artwork,
badge, timestamp, both buttons, and absence of party/join secrets. Final rendering and
animation are controlled by the viewing Discord client; buttons are visible to other users.
