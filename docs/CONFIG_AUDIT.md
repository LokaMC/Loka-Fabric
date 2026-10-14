# Loka 26.3 configuration audit

Final local pass: 2026-10-14. Every installed top-level mod is listed in `PER_MOD_CONFIG_REVIEW.md`; exact configuration file hashes and raw key overlaps are in `CONFIG_REVIEW_INVENTORY.json`.

## Shared shipped configuration

Shared configurations are byte-identical. Loka Fabric+ additionally contains the local Voxy jars and shader assets; Loka Fabric excludes them. User accounts, chat history, worlds, waypoints, replay files, hardware fingerprints, generated UI positions, and caches are excluded.

| Setting | Shipped decision |
| --- | --- |
| Window and GUI | 1280×720 (16:9), GUI scale Auto; other video/audio/accessibility settings use Minecraft defaults. |
| Servers | Exactly one entry: Loka, play.lokamc.com. |
| FancyMenu | Modpack mode on; editing overlay hidden; normal customization shortcut cannot open the editor. Deliberate editing requires changing config/fancymenu/options.txt. |
| Main menu | Gold borders, brighter purple-slate fills, original proportional Loka wordmark centered between the top and Singleplayer; footer shares a right edge. Small utility buttons sit three GUI pixels higher, with equal three-pixel gaps above the version text; Uku's extra button is disabled. |
| Flashback Replay | Native title button uses "Flashback Replays" and stays in the lower-right row from the first title screen. Native recording and World Downloader controls share the pause-menu row below Options/World Options and above Disconnect. Loka Fabric+ has two independently justified groups of four matching each options column. Loka Fabric has one compact centered row of five. Mods is far right, then Flashback and World Downloader to its left. No automatic recording. |
| Loading screen | Supplied Deep Dark image, aspect ratio preserved, reduced Loka wordmark; early window matches the default 16:9 game window. |
| MiniHUD | Starts with main rendering off; H toggles the HUD, H+C opens configuration. |
| Litematica / MaLiLib | All 15 M-related Litematica chords move to F8 equivalents; MaLiLib A+C becomes F9. Atlas keeps M. |
| Resource packs | Original Modrinth JermsyBoy pack installed but unselected. Loka Interface supplies themed multiplayer utility sprites and is selected. Polytone is removed from Loka Fabric+. Server Resource Pack Checker keeps and lists cached server packs. |
| Shaders | Loka Fabric+ includes Iris, both Complementary variants and Euphoria; shaders start off with no selected pack. Loka Fabric does not ship shader mods or shader packs. |
| Bobby / Voxy | Loka Fabric+ only: Bobby clears unused regions after 30 days; Voxy uses four service threads. Their settings remain identical wherever shared. |
| Discord RPC | Loka Minecraft activity title, standard circular Loka Fabric icon, username/public rank, public membership town/alliance, player-head badge, elapsed time and Discord/modpack buttons. No location, world, dimension or join data. Valid Loka fallback text prevents config resets; only Modrinth pack detection is enabled. The installed CraftPresence payload checks passed. Discord unavailable for visible-card verification. |
| WaterMedia | Local file server disabled. Menu video uses local bundled media. |

## Keybind priority

| Conflicting shortcuts | Resolution |
| --- | --- |
| Gamma / Quick Actions G | Gamma retains G; Quick Actions unbound. |
| Atlas / Litematica M | Atlas M; every Litematica M chord becomes an F8 chord. |
| Zoom C while strafing A / MaLiLib A+C | MaLiLib F9. |
| MiniHUD / Gamma night vision H | MiniHUD retains H; Gamma night vision unbound. |
| Zoom / creative toolbar C | Zoom retains C; toolbar save/load comma/period. |
| Iris reload / Axiom replacement R | Iris retains R; Axiom replacement unbound. |
| Social Interactions / Flashback Extras P | Social retains P; Extras menu unbound, reachable through config. |
| Zoom secondary / Camera Utils F6 | Zoom retains F6; Camera 1 unbound, Camera 2 F7. |
| Sprint / NBT Display left Ctrl | Sprint retains Ctrl; NBT Display backslash. |

Remaining raw overlaps are vanilla F3 debug chords or middle mouse for Pick Block versus Spectator Hotbar. Do a Barrel Roll and its keybinds are removed. Flashback's editor keybinds and Axiom's editor shortcuts operate in their own screens. Shared options include Charged-only bindings, which Loka Fabric ignores.

Atlas M, Litematica F8, MiniHUD H+C, and MaLiLib F9 were opened in game. Litematica's saved config has no remaining M chords. The previous claim that only its six menu shortcuts completed the migration was incomplete; the final override now covers all 15 affected chords.

## Defaults worth the owner's attention

- Chat Patches stores persistent local chat history, up to 16,384 messages. This is upstream behavior, retained for convenient chat search; no review chat is bundled.
- Server Resource Pack Checker can keep a server pack selected after leaving. Visiting another server may require changing Resource Packs manually.
- Euphoria embeds shader settings in screenshots by default, allowing a screenshot to reproduce the same shader settings.
- Loka Fabric+'s Presence Footsteps, Sound Physics, Better Clouds, fancy dynamic lights, Falling Leaves, and Subtle Effects add atmosphere and cost frame time. Sound Physics changes directional cues; player PvP feedback remains useful.
- Simple World Downloader is manual, but includes entities, player data, and server resources when used. Flashback automatic recording is also off.
- EntityCulling retains upstream safe mode and its normal name-tag behavior. ImmediatelyFast and BadOptimizations retain compatibility hooks; risky experimental switches are off.
- Voxy cache disk growth is independent of Bobby's 30-day cleanup. Four service threads are a sensible shared default, not a per-machine benchmark result.
- Atlas territory/town data and the map loaded. Some older API refreshes returned null alliance data; that is an API/data issue rather than a shipped settings failure.
- CraftPresence resolves the correct pack identity, but the Discord card could not be seen without Discord. One earlier transition logged a ConcurrentModificationException; it did not stop play. No rendered-card verification is claimed.

## Compatibility evidence

- Previous Loka Fabric+ startup loaded 230 modules. The revised build has 83 top-level jars after removing Polytone, Do a Barrel Roll, Spawn Animations and Not Enough Animations; module counts include nested dependencies.
- Replay opens correctly on the first menu; its settings confirm automatic recording is off.
- Loka Fabric+ joined play.lokamc.com and retained JermsyBoy plus the cached Loka server pack after reload.
- Both Reimagined and Unbound with Euphoria compiled, rendered, and created Voxy's Iris pipeline. Initial shader compilation previously timed out once; the subsequent join and shader switch succeeded.
- JermsyBoy is now left disabled at first launch, avoiding its old asset warnings by default. Its assets remain unmodified and it stays available for manual selection.
- Menu checks include 4:3 at 960×720 and 21:9 at 1400×600 with GUI scale 2, plus prior 854×512 and 2560×1024 checks. Additional 1280×720 and 1920×1080 16:9 checks and a 1280×800 16:10 check at GUI scale 3 passed. The final utility spacing was rechecked at GUI scales 2 and 3.
- Latest Auto-scale check: 1280×720 and a maximized 2560×1009 client area. Utilities and both footer lines stay anchored to the same right edge. Loka Fabric+ joined Loka with MiniHUD off; its Start Recording button is in the centered six-icon pause-menu row and retains its native tooltip.
- All generated JSON/TOML configurations parse. Mods without generated settings use their upstream defaults; that does not imply every mod feature was exercised.
- Release builder checks ZIP integrity, pinned Loka Fabric inclusion in Loka Fabric+, identical shared overrides, and release hashes.

Long PvP play, distant LOD coverage, and actual replay recording/export are beyond these compatibility checks. They should not be described as tested.
