# Loka Fabric

The official Fabric client packs for Loka, for Minecraft **26.3**.

- **Loka Fabric** is the everyday pack: performance, AntiStutter, Atlas, useful client tools and the Loka interface.
- **Loka Fabric+** includes every base-pack mod and adds shaders, distant terrain, connected textures, replays, building tools and atmosphere.

## Install

Download the appropriate `.mrpack` from [GitHub Releases](https://github.com/LokaMC/Loka-Fabric/releases/tag/v2.0.1-26.3) and import it into Modrinth. The archives are larger than GitHub's regular file limit, so downloads live in Releases.

## Repository

| Folder | Contents |
| --- | --- |
| `packs/loka-fabric/26.3` | Base pack's pinned index, metadata and edition overrides |
| `packs/loka-fabric-plus/26.3` | Expanded pack's pinned index, metadata and edition overrides |
| `shared/overrides` | Shared configuration, menus, server list and interface assets |
| `mods/loka-ui` | Complete Loka UI source, Gradle wrapper and packaged mod jar |
| `assets` | Loading artwork, pack icons, editable branding and screenshots |
| `docs` | Changelog, config/keybind review, release hashes and test scope |
| `scripts` | Pack assembler and release verification |

## Build a pack

Requires Python 3.10+. From the repository root:

```sh
python scripts/build_pack.py loka-fabric
python scripts/build_pack.py loka-fabric-plus
python scripts/verify.py
```

The builder downloads the hash-pinned shared-assets release archive once. To build before a release exists or while offline, supply `--assets /path/to/Loka-Fabric-26.3-shared-assets.zip`. Outputs go to `out/`. Assembly preserves all indexed downloads and every override byte. ZIP compression metadata can differ from the original release.

## Build Loka UI

Requires Java 25. Run `gradlew.bat build` inside `mods/loka-ui` on Windows, or `gradle build` with Gradle 9.5.1 on other systems. The build runs presence affiliation/privacy regression checks. The packaged jar is also in `mods/loka-ui/releases` and GitHub Releases.

## Defaults

Release 2.0.1 removes unused Entity Texture Features and Entity Model Features to
avoid their custom-glint/OIT crash path. See the [diagnosis and validation](docs/CRASH_FIX_2.0.1.md).

1280Ã—720 window, automatic GUI scale, MiniHUD off, shaders off, JermsyBoy installed but unselected, curated keybinds, and one server: **Loka â€” play.lokamc.com**. Shared controls and settings match across editions. FancyMenu editing controls are hidden by modpack mode. Loading terrain uses the original watermarked Deep Dark image; the title and resource-loading screens use the Loka theme.

[Loka website](https://lokamc.com/) Â· [Loka Discord](https://discord.gg/loka)
