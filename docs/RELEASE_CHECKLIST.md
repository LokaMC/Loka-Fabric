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
- [ ] Publish the source and 2.0.2 GitHub release assets.
- [ ] Publish Loka Fabric 2.0.2 on Modrinth and submit Fabric+ 2.0.2 to its existing project.

No game was launched, no Minecraft server login was made, no Discord activity was
sent by the tests and no existing profile was modified. A visible Discord card
has not been checked. Restart Minecraft after updating to load the configuration.
The prior shared-assets archive remains hash-pinned at its original 2.0.0 URL.

See [the icon fix](PRESENCE_ICON_2.0.2.md) and [validation scope](VALIDATION.md).
