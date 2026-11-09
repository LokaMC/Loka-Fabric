# 2.0.3 release — 2026-11-08

Minecraft 26.3 packages for Fabric Loader 0.20.1:

- `out/Loka-Fabric-26.3-2.0.3.mrpack` — SHA-512 `5d2c77f516433f527e5ce848d1dc001896db35fb16b35b44d7a8a96d13069ae7f4e7fc3accca2f3cc91d3e38f43a49d290f269dc73d3f1e913b173b307655c3b`
- `out/Loka-Fabric-Plus-26.3-2.0.3.mrpack` — SHA-512 `1792807ea865ea03c2296fb40669ce0379aa20e15e9110ab60e6a87a597cb35b4570093ef6238143cbcbc36355544357b44579872ff4db4e48747c7f1553224c`

- [x] Build and verify both archives, overrides and indexed downloads.
- [x] Check the menu video, slate Controls and Key Binds, Colosseum loading
      image, watermark, gold loading logo and blurred in-world pause screen.
- [x] Check the background and watermark in wide and portrait game windows.
- [x] Publish and verify both archives and Loka UI 1.0.3 on [GitHub Releases](https://github.com/LokaMC/Loka-Fabric/releases/tag/v2.0.3-26.3).
- [ ] Publish Loka Fabric 2.0.3 and submit Fabric+ 2.0.3 on Modrinth.

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
