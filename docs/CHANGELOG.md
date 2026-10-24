# Loka Fabric / Loka Fabric+ 26.3 — 2.0.1

- Remove Entity Texture Features (ETF) and its dependent Entity Model Features (EMF) from both editions. This removes the custom enchanted-skin rendering path implicated in the fatal `armor_cutout_no_cull_glint` / missing OIT pipeline error. Normal Minecraft skins and enchanted armor/items remain available.
- Update Fabric Loader to 0.20.1, Fabric API to 0.165.0+26.3, Gamma Utils to 3.2.2, and ViaFabricPlus to 5.1.2 in both editions.
- Loka Fabric+: update AmbientSounds to 6.3.11, CreativeCore to 2.14.24, DBTools to 2.4.1, 3D Skin Layers to 1.11.4, WaveyCapes to 1.11.3, and WorldEdit to 7.4.6.
- Loka Fabric+: replace the locally built Voxy and Voxy Extra jars with their official Minecraft 26.3 downloads, Voxy 0.2.20-beta and Voxy Extra 0.2.8. Voxy remains an upstream beta.
- All other indexed projects checked for compatible 26.3 Fabric updates; existing compatible pins retained. No Minecraft server login used for this release's validation.

---

# Loka Fabric / Loka Fabric+ 26.3 — 2.0.0

- No Chat Reports removed from both editions, including its unused interface sprites.
- Better Mount HUD 1.3.1 is included in both editions.
- Pack Branding uses the supplied Loka Fabric and Loka Fabric+ window/taskbar icons with their matching window titles.
- Loka Minecraft Discord presence shows Username | Rank above Town of Alliance, with the standard circular Loka Fabric logo, player-head badge, elapsed time and Discord/modpack buttons; no location, world or dimension information is published.
- Fabric client packs for Minecraft 26.3, including AntiStutter 1.0.2.
- Loka Fabric is the base; Loka Fabric+ includes every Loka Fabric mod plus all selected additions.
- Continuity, Iris, Bobby, Voxy/Voxy Extra, Complementary and Euphoria are Loka Fabric+-only.
- Loka title-screen video, gold and purple-slate buttons, and Deep Dark loading artwork.
- Correctly proportioned Loka logos, right-aligned footer, and compact utility buttons.
- Flashback Replay stays in Loka Fabric+'s lower-right utility row from the first title screen.
- Replay tooltip reads "Flashback Replays"; the extra Mods tooltip is removed.
- Pause-menu icons sit below Options/World Options and above Disconnect. Loka Fabric+ uses two independently justified groups matching each options column; Loka Fabric uses one compact centered row of five.
- Flashback recording controls join that row in Loka Fabric+.
- Pause menus use the proportional gold Loka wordmark in place of the Game Menu heading.
- Loading terrain uses the original watermarked Deep Dark image, without a large logo.
- World Downloader controls join the pause row before Flashback, with Mods at the far right.
- Title-screen LokaMC watermark has matching five-pixel left/bottom GUI margins.
- FancyMenu editing controls are hidden in modpack mode.
- First launch defaults to a 1280×720 window and automatic GUI scale.
- Shared curated keybinds; Atlas uses M, Litematica uses F8 chords, and MaLiLib uses F9.
- MiniHUD starts disabled; H toggles it and H+C opens its settings.
- Polytone removed from Loka Fabric+.
- Do a Barrel Roll removed from Loka Fabric+.
- Spawn Animations and Not Enough Animations removed from Loka Fabric+.
- Subtle Effects removed from Loka Fabric+.
- Multiplayer utility sprites use the Loka interface theme.
- Loka is the only supplied server: play.lokamc.com.
- Original JermsyBoy Vanilla Edits installed but not selected; both Complementary variants and Euphoria supplied.
- Shaders are off by default and can be enabled through Iris.
- Local 26.3 Voxy and Voxy Extra builds are included.

- Singleplayer world selection uses the Loka menu video; other out-of-world panoramic menus use the watermarked Deep Dark artwork. In-world menu blur is retained.
- Prepared full-release version 2.0.0 with pinned dependencies and matching shared configuration.
- CraftPresence fallbacks use valid Loka text and detect Modrinth only; empty fallback resets and irrelevant launcher-detection errors are removed.
