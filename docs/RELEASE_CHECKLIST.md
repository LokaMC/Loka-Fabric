# Full release preparation — 2026-10-14

**Version:** `1.0.0+26.3` · **Minecraft:** `26.3` · **Modrinth version type:** `release`

## Prepared

- [x] Loka Fabric: 55 top-level mod jars.
- [x] Loka Fabric+: 82 top-level mod jars, including every base-pack file.
- [x] Every indexed client file is required in its edition.
- [x] Shared configuration matches, except intentional edition branding.
- [x] Pack archives, pinned downloads, local jars and override hashes verified.
- [x] Current Loka UI source builds with Java 25, Gradle 9.5.1 and pinned Loom 1.17.21.
- [x] Affiliation/privacy and installed CraftPresence payload tests passed.
- [x] Both current review profiles reached the title screen.
- [x] Singleplayer video, Deep Dark settings background, title footer spacing and Flashback tooltip checked in game.
- [x] All 82 expanded-pack mods inventoried; 86 generated configuration files inspected with no JSON/TOML parse failures.
- [x] Keybind review refreshed; remaining raw overlaps use vanilla debug modifiers or separate contexts.
- [x] 16:9 launch defaults, Auto GUI scale, MiniHUD off, shaders off, JermsyBoy unselected and one Loka server supplied.
- [x] Descriptions, project/version metadata, changelog and checksums prepared for both editions.
- [x] No accounts, worlds, logs, caches or local machine paths included.
- [x] Git author and committer use FoxyBearGames with a GitHub noreply address.

## Modrinth handoff

Use each edition's `packs/<edition>/26.3/metadata/version-fields.json` and matching `.mrpack` from GitHub Releases. Project descriptions and fields are alongside the version fields. Both project templates link to this repository and its issue tracker.

- [ ] Upload the two `1.0.0+26.3` versions to the corresponding Modrinth projects.
- [ ] Complete any disclosures shown by Modrinth and submit/publish there.

This preparation does not claim a Modrinth upload. Existing beta GitHub assets are retained under their original tag.

## Test scope

Earlier checks covered local-world loading, the Loka server, Voxy/Voxy Extra and both shader variants. This final pass checks the current menu/presence changes and packaged configuration. Sustained PvP, an actual Flashback recording/export and a visible Discord card with Discord running remain useful post-release checks.

Nonfatal upstream resource-path messages and the local Windows performance-counter warning were observed; neither prevented startup. Pack-owned CraftPresence fallback/detection errors were corrected.
