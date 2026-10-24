# 2.0.1 rendering crash investigation — 2026-10-24

The supplied log fails during `LevelRenderer.executeOit` with:

```text
IllegalStateException: Render type armor_cutout_no_cull_glint does not have OIT pipelines set up.
```

The immediate failure is a render layer being executed in an order-independent
transparency (OIT) group without OIT pipelines. It is not a scoreboard, account,
network, memory-allocation or Java Unsafe failure. The later debug-group exception
is suppressed while the failed render pass is closing.

ETF is the likely source of the incompatible custom glint draw. The report logs
an ETF-featured player skin near the crash. ETF 7.2.5 and 7.2.6 use
`RenderTypes.armorCutoutNoCullGlint` in their custom enchantment rendering;
`getFoilBuffer` can request that layer from an existing feature-renderer group,
including a translucent group. The exact layer name matches the exception.
The latest ETF release notes describe an NBT optimization, not an OIT fix.
This is a source-and-log diagnosis; the reporter's precise skin/world scene was
not reproduced.

The owner confirmed ETF is unused. Both ETF and EMF are removed in both editions.
EMF declares ETF as a required dependency, so retaining EMF would prevent launch.
No ETF/EMF jars or configurations are shipped by 2.0.1, including nested mod jars.
Normal Minecraft skins and standard enchanted items and armor still work without
these optional custom entity/skin effects. Other custom resource packs that need
EMF/ETF will lose their associated features.

All 80 existing indexed projects were checked against the official Modrinth API
for compatible Minecraft 26.3 Fabric releases. Nine retained mods are updated;
the two removed mods are excluded from the update list. Fabric Loader is updated
to the latest stable version, 0.20.1. The two additional official Voxy-family
26.3 versions replace local builds in Fabric+. Exact changes are recorded in
`UPDATES_2.0.1.json` and the changelog. Voxy remains an upstream beta; its declared
Sodium and Iris constraints match the pinned Sodium 0.9.2 and Iris 1.11.7.

Validation:

- Official SHA-1/SHA-512 hashes and sizes checked for all 81 distinct indexed files.
- Required dependencies and declared conflicts, including recursively bundled
  libraries and Loader's bundled MixinExtras, checked with Fabric Loader's own
  version predicate implementation: 1,010 checks passed.
- Both archives pass integrity, index, override-hash, version/branding, shared
  settings and base-edition inclusion checks. Mod counts are 53 and 80.
- Voxy Extra's two new toggles default to unbound, avoiding new shortcut conflicts.
- No game was launched, no account was used, no server connection was made and no
  existing launcher profile was changed. This release does not claim an in-game
  reproduction or rendering benchmark.

For a manually maintained installation, remove both old ETF and EMF jars when
updating. A normal Modrinth pack update should remove the files dropped from the
new index; avoid copying the new mods over a directory that retains old jars.

Sources:

- [ETF enchantment rendering](https://github.com/Traben-0/Entity_Texture_Features/blob/6bea7d723966116e5e955ed77fca268f4022af30/src/main/java/traben/entity_texture_features/utils/ETFUtils2.java)
- [ETF player skin enchantment handling](https://github.com/Traben-0/Entity_Texture_Features/blob/6bea7d723966116e5e955ed77fca268f4022af30/src/main/java/traben/entity_texture_features/features/player/ETFPlayerTexture.java)
- [ETF 7.2.6 release notes](https://modrinth.com/mod/entitytexturefeatures/version/7.2.6-fabric-26.3)
- [EMF's required ETF dependency](https://modrinth.com/mod/entity-model-features/version/3.3.12-fabric-26.3)
