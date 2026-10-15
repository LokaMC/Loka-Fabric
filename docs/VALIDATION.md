# Validation and scope

Minecraft 26.3, release 2.0.0: Loka Fabric has 56 top-level mod jars; Loka Fabric+ has 83 and contains every base mod. Indexed client files are required in both editions. Release archive integrity and SHA-512 hashes are checked, as are shared-file equality and launch defaults.

Earlier in-game checks covered startup, title/pause layouts at normal and wide sizes, terrain loading in a local world and on Loka, multiplayer buttons, Voxy/Voxy Extra and shader rendering. In the final 2026-10-14 pass, both current profiles reached the title screen; the singleplayer video, Deep Dark settings background, title footer spacing and Flashback tooltip were checked. The repaired CraftPresence defaults survived a subsequent startup without fallback resets or irrelevant launcher-detection errors.

The latest Loka UI source builds with pinned Loom 1.17.21 and passes affiliation/privacy checks and real payload compilation against the installed CraftPresence 2.7.1 / UniLib 1.2.1 releases. Discord was unavailable for a visible activity-card check. A sustained PvP session and recording/export are outside this pass. Both title screenshots are native 1920x1080 captures without window chrome. See RELEASE_CHECKLIST.md for the Modrinth upload handoff.

No launcher accounts, saved worlds, player logs, caches or machine-specific build directories are included.

Better Mount HUD 1.3.1 was added to both 2.0.0 editions after the in-game checks above. Its official Fabric 26.3 download hashes and dependencies were verified; static inspection found no new config files or keybinds. Both rebuilt archives pass integrity and shared-content checks. Mounted HUD placement with AppleSkin and the armor HUD has not yet been checked in game.
