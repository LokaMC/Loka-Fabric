# 2.0.1 release — 2026-10-24

Minecraft 26.3, Fabric Loader 0.20.1, release channel.

- [x] Remove ETF and its required dependent EMF from both editions.
- [x] Review all indexed projects for compatible upstream updates.
- [x] Update nine retained mods and replace both local Voxy-family builds with official downloads.
- [x] Verify all 81 unique indexed downloads against official hashes and sizes.
- [x] Pass 1,010 required-dependency and conflict version checks with Fabric Loader's predicates.
- [x] Verify archive integrity, override hashes, branding/version consistency, defaults and shared contents.
- [x] Confirm Fabric+ includes every base-pack file (53 / 80 top-level mod jars).
- [x] Prepare edition-specific version metadata and current release notes.
- [x] Keep accounts, worlds, logs, caches and local machine paths out of release files.
- [x] Preserve the FoxyBearGames Git author/committer identity.
- [ ] Publish the source and 2.0.1 GitHub release assets.
- [ ] Publish both 2.0.1 versions on their corresponding Modrinth projects.

No game was launched and no Minecraft server connection was made for this release.
Existing launcher profiles are untouched. The prior shared-assets archive remains
hash-pinned at its original 2.0.0 URL and is reused by the assembler.

See [validation scope](VALIDATION.md), [crash diagnosis](CRASH_FIX_2.0.1.md), and
[exact upstream updates](UPDATES_2.0.1.json).
