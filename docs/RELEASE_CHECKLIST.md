# 2.0.3 release candidate — 2026-10-29

**Hold publication until New Gank Isle is out.** These are local, unpublished
Minecraft 26.3 packages for Fabric Loader 0.20.1:

- `out/Loka-Fabric-26.3-2.0.3.mrpack` — SHA-512 `1b90bd6946eb0d63677115dc6c765ad0d5263c0ca8f95c0ebdf6d7590e4be633d10b5f129ba18cb5a15fb0a301abed8365c69257ba1ed192ef17264cc32c9ec3`
- `out/Loka-Fabric-Plus-26.3-2.0.3.mrpack` — SHA-512 `e2977a18ec5564eaea653cd81d4cebbb8c438eeaf99d20eea5df11c79c86fe18e3fbbd26078b9bae0859a18b9bf407355d367b5d384e19b928fa5b240c3c64f8`

- [x] Build and verify both archives, overrides and indexed downloads.
- [x] Check the menu video, slate Controls and Key Binds, Colosseum loading
      image, watermark, gold loading logo and blurred in-world pause screen.
- [x] Check the background and watermark in wide and portrait game windows.
- [ ] After New Gank Isle is out, publish the approved candidate versions.

The runtime checks used isolated test instances, with CraftPresence disabled in
those instances. No server was joined. See [validation scope](VALIDATION.md).

---

# 2.0.2 release — 2026-10-24

Minecraft 26.3, Fabric Loader 0.20.1, release channel.

- [x] Make the small player-head badge independent of the optional Loka bridge.
- [x] Use a direct compact-UUID 128px PNG avatar with a Minecraft loading fallback.
- [x] Pass real CraftPresence/UniLib payload checks for both edition configurations.
- [x] Cover startup, absent/empty/stale bridge data, account switching and account reloads.
- [x] Preserve existing activity text, logo, timestamps, buttons and privacy fields.
- [x] Retain all 2.0.1 production mod jars, dependencies and the ETF/EMF removal.
- [x] Verify both pack archives, pinned indexes, override hashes, defaults and shared contents.
- [x] Keep accounts, worlds, logs, caches and local machine paths out of release files.
- [x] Preserve the FoxyBearGames Git author/committer identity.
- [x] Publish the source and 2.0.2 GitHub release assets.
- [x] Publish Loka Fabric 2.0.2 on Modrinth and submit Fabric+ 2.0.2 to its existing project.

Loka Fabric is public. Fabric+ remains under Modrinth project review; both tested
archives are publicly available from the
[2.0.2 GitHub release](https://github.com/LokaMC/Loka-Fabric/releases/tag/v2.0.2-26.3).

No game was launched, no Minecraft server login was made, no Discord activity was
sent by the tests and no existing profile was modified. A visible Discord card
has not been checked. Restart Minecraft after updating to load the configuration.
The prior shared-assets archive remains hash-pinned at its original 2.0.0 URL.

See [the icon fix](PRESENCE_ICON_2.0.2.md) and [validation scope](VALIDATION.md).
