# Validation and scope

## 2.0.3 — 2026-10-29

The Loka UI build checks centered Colosseum crops and independent watermark
placement at standard, ultrawide, portrait, odd and small viewport sizes. Both
staged archives and all 81 distinct indexed downloads pass their integrity and
hash checks.

Isolated Minecraft 26.3 runs used Fabric Loader 0.20.1 and both staged packs.
CraftPresence was disabled only in the test instances. Both reached a local
single-player world, showing the Colosseum and its separate bottom-left watermark
when loading and preserving the blurred world when paused. In Fabric+, the title,
Options, world list and graphics settings showed the menu video; Controls and
Key Binds showed the slate. A resource reload showed the centered gold Loka logo
as well.

The image and video were also checked in 1280×540 and 540×960 game windows;
the watermark remained visible and the Colosseum cropped around its center.
These were window-shape tests, not tests on physical ultrawide or portrait
monitors. No server was joined. A separate Mod Menu check confirmed the Loka
Fabric icon and updated description in Loka UI 1.0.3. Both published GitHub
archives match their verified local builds.

## 2.0.3-preview.1 — 2026-10-27

The Loka UI build checks centered, undistorted crops at 16:9, 21:9-like,
32:9, 4:3, portrait and odd-sized resolutions. Both unpublished preview packs
pass archive, override-hash, shared-content and background-setting checks.
The new crop was previewed from the artwork and a video frame. No game was
launched for this preview, so the final in-game rendering has not been checked.

## 2.0.2 — 2026-10-24

Real CraftPresence 2.7.1 / UniLib 1.2.1 payload checks pass for both edition
configs, covering missing accounts, missing/empty/stale Loka artwork placeholders,
account switches and account reloads. Badge URLs use the signed-in UUID directly;
other presence fields and privacy checks pass. Both rebuilt archives pass the
repository's index, override-hash, integrity, branding, shared-content and default
checks. Production mod jars and dependency pins are unchanged from 2.0.1.

No game was launched, no server login was made and no Discord activity was sent.
No visible Discord-card verification is claimed. See `PRESENCE_ICON_2.0.2.md`.

## 2.0.1 — 2026-10-24

Loka Fabric contains 53 top-level mod jars; Loka Fabric+ contains 80 and includes
every base-pack file. ETF and EMF are absent, including recursively bundled jars.
All 81 distinct indexed downloads pass their official hash and size checks.
Fabric Loader's version predicates pass 1,010 required-dependency and conflict
checks, including bundled libraries. Both assembled archives pass the repository's
integrity, pinned-index, override-hash, shared-content and launch-default checks.
Voxy Extra's two new keybindings default to unbound.

No game was launched, no Loka connection was made and no existing launcher profile
was changed. The reported scene has not been reproduced in game. See
[the crash investigation](CRASH_FIX_2.0.1.md) and [exact updates](UPDATES_2.0.1.json).

## Earlier 2.0.0 validation

Minecraft 26.3, release 2.0.0: Loka Fabric has 55 top-level mod jars; Loka Fabric+ has 82 and contains every base mod. Indexed client files are required in both editions. Release archive integrity and SHA-512 hashes are checked, as are shared-file equality and launch defaults.

Earlier in-game checks covered startup, title/pause layouts at normal and wide sizes, terrain loading in a local world and on Loka, multiplayer buttons, Voxy/Voxy Extra and shader rendering. In the final 2026-10-14 pass, both current profiles reached the title screen; the singleplayer video, Deep Dark settings background, title footer spacing and Flashback tooltip were checked. The repaired CraftPresence defaults survived a subsequent startup without fallback resets or irrelevant launcher-detection errors.

The latest Loka UI source builds with pinned Loom 1.17.21 and passes affiliation/privacy checks and real payload compilation against the installed CraftPresence 2.7.1 / UniLib 1.2.1 releases. Discord was unavailable for a visible activity-card check. A sustained PvP session and recording/export are outside this pass. Both title screenshots are native 1920x1080 captures without window chrome. See RELEASE_CHECKLIST.md for the Modrinth upload handoff.

No launcher accounts, saved worlds, player logs, caches or machine-specific build directories are included.

Better Mount HUD 1.3.1 was added to both 2.0.0 editions after the in-game checks above. Its official Fabric 26.3 download hashes and dependencies were verified; static inspection found no new config files or keybinds. Both rebuilt archives pass integrity and shared-content checks. Mounted HUD placement with AppleSkin and the armor HUD has not yet been checked in game.

No Chat Reports was removed from both 2.0.0 archives on 2026-10-15, along with its unused interface sprites. Static archive, hash, default-setting and superset checks passed; no additional in-game check was performed.
