package com.lokamc.ui;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.fabricmc.fabric.api.client.screen.v1.Screens;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.gui.screens.PauseScreen;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.components.SpriteIconButton;
import net.minecraft.client.gui.components.PlainTextButton;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.resources.Identifier;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.contents.TranslatableContents;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.WeakHashMap;
import java.util.LinkedHashMap;
import com.lokamc.ui.mixin.SpriteIconButtonAccessor;

/**
 * Flashback performs another automatic placement pass on the title's first tick.
 * Pin its own functional button after init/tick and before rendering, so its
 * callback, icon, tooltip and incompatibility checks remain Flashback's.
 * The optional integration uses its class name to avoid requiring Flashback in Core.
 */
public final class LokaUiClient implements ClientModInitializer {
    private static final String REPLAY_BUTTON = "com.moulberry.flashback.screen.FlashbackButton";
    private static final int SIZE = 16;
    private static final Tooltip REPLAY_TOOLTIP = Tooltip.create(Component.literal("Flashback Replays"));
    private static final Identifier PAUSE_LOGO = Identifier.fromNamespaceAndPath(
        "loka_ui", "textures/gui/loka_logo.png");
    private final java.util.Map<Screen, PauseLayout> pauseLayouts = new WeakHashMap<>();

    private record PauseLayout(List<AbstractWidget> icons, AbstractWidget options,
                               AbstractWidget worldOptions, int optionsY, int iconsY) {}

    @Override
    public void onInitializeClient() {
        ScreenEvents.AFTER_INIT.register((client, screen, width, height) -> {
            if (!(screen instanceof TitleScreen) && !(screen instanceof PauseScreen)) return;
            pauseLayouts.remove(screen);
            updateScreen(screen);
            ScreenEvents.afterTick(screen).register(this::updateScreen);
            ScreenEvents.beforeExtract(screen).register(
                (current, graphics, mouseX, mouseY, delta) -> updateScreen(current));
            if (screen instanceof PauseScreen) {
                ScreenEvents.afterExtract(screen).register(
                    (current, graphics, mouseX, mouseY, delta) -> drawPauseLogo(current, graphics));
            }
        });
    }

    private void updateScreen(Screen screen) {
        if (screen instanceof PauseScreen) {
            // Replace only the native pause heading; leave every control intact.
            for (var widget : Screens.getWidgets(screen)) {
                if (widget instanceof StringWidget && hasTranslation(widget, "menu.game")) {
                    widget.visible = false;
                }
            }
            pinRecordingRow(screen);
            return;
        }
        for (var widget : Screens.getWidgets(screen)) {
            if (widget instanceof PlainTextButton) {
                int textWidth = Minecraft.getInstance().font.width(widget.getMessage());
                widget.setWidth(textWidth);
                widget.setHeight(10);
                widget.setPosition(screen.width - textWidth - 2, screen.height - 11);
                widget.visible = true;
            }
            if (widget.getClass().getName().contains(".modmenu.") ||
                widget.getMessage().getString().matches("Mods(?: \\(.*\\))?")) widget.setTooltip(null);
            if (!widget.getClass().getName().equals(REPLAY_BUTTON)) continue;
            widget.setWidth(SIZE);
            widget.setHeight(SIZE);
            widget.setTooltip(REPLAY_TOOLTIP);
            // Match the other utilities' three-pixel gaps and clear the footer.
            widget.setPosition(Math.max(2, screen.width - 75), Math.max(2, screen.height - 43));
        }
        // Locate native utilities by type, independently of original coordinates
        // or Mod Menu's chosen style. Auto GUI scale changes those coordinates.
        var utilities = Screens.getWidgets(screen).stream()
            .filter(widget -> widget instanceof SpriteIconButton &&
                !widget.getClass().getName().contains(".modmenu."))
            .toList();
        if (utilities.size() == 3) {
            int x = screen.width - 56;
            for (var widget : utilities) {
                widget.setWidth(SIZE);
                widget.setHeight(SIZE);
                widget.setPosition(x, screen.height - 43);
                x += 19;
            }
        }
    }

    private static void drawPauseLogo(Screen screen, GuiGraphicsExtractor graphics) {
        if (!(screen instanceof PauseScreen pause) || !pause.showsPauseMenu()) return;
        var back = Screens.getWidgets(screen).stream()
            .filter(widget -> hasTranslation(widget, "menu.returnToGame"))
            .findFirst().orElse(null);
        if (back == null) return;
        // Preserve the original 3001:1000 image ratio, with twelve GUI pixels
        // between the wordmark and Back to Game. Recalculate after every resize.
        int height = Math.min(30, Math.max(1, back.getY() - 18));
        int width = Math.round(height * 3001 / 1000f);
        int y = back.getY() - 12 - height;
        int x = (screen.width - width) / 2;
        graphics.blit(PAUSE_LOGO, x, y, x + width, y + height,
                      0f, 1f, 0f, 1f);
    }

    private void pinRecordingRow(Screen screen) {
        var recordings = new ArrayList<AbstractWidget>();
        for (var widget : Screens.getWidgets(screen)) {
            if (widget.getClass().getName().equals(REPLAY_BUTTON)) recordings.add(widget);
        }
        var layout = pauseLayouts.get(screen);
        if (layout == null) {
            var rows = new HashMap<Integer, ArrayList<AbstractWidget>>();
            AbstractWidget options = null, worldOptions = null;
            for (var widget : Screens.getWidgets(screen)) {
                if (hasTranslation(widget, "menu.options")) options = widget;
                if (hasTranslation(widget, "options.worldOptions.button")) worldOptions = widget;
                if (!recordings.contains(widget) && widget.getWidth() == 20 &&
                    widget.getHeight() == 20 &&
                    Math.abs(widget.getX() + 10 - screen.width / 2) < 110 &&
                    widget.getY() < screen.height - 30) {
                    rows.computeIfAbsent(widget.getY(), key -> new ArrayList<>()).add(widget);
                }
            }
            var icons = rows.values().stream().filter(group -> group.size() >= 3)
                .max(Comparator.comparingInt(ArrayList::size)).orElse(null);
            if (icons == null || options == null || worldOptions == null) return;
            icons.sort(Comparator.comparingInt(AbstractWidget::getX));
            // Capture native rows once per init; repeated render passes must not
            // swap the rows back or change the original icon order.
            layout = new PauseLayout(List.copyOf(icons), options, worldOptions,
                icons.getFirst().getY(), options.getY());
            pauseLayouts.put(screen, layout);
        }
        layout.options().setY(layout.optionsY());
        layout.worldOptions().setY(layout.optionsY());
        var row = new ArrayList<AbstractWidget>();
        var mods = new ArrayList<AbstractWidget>();
        for (var widget : layout.icons()) {
            if (widget.getClass().getName().contains(".modmenu.")) mods.add(widget);
            else row.add(widget);
        }
        // SWD creates native sprite buttons. Find their namespace so they remain
        // identifiable after moving them out of their old bottom-left positions.
        var downloads = new LinkedHashMap<String, AbstractWidget>();
        var staleDownloads = new ArrayList<AbstractWidget>();
        for (var widget : Screens.getWidgets(screen)) {
            if (widget instanceof SpriteIconButton && widget instanceof SpriteIconButtonAccessor sprite &&
                sprite.loka$getSprites().enabled().getNamespace().equals("swd")) {
                String path = sprite.loka$getSprites().enabled().getPath();
                boolean settings = path.equals("icon/settings");
                var old = downloads.put(settings ? "settings" : "download", widget);
                if (old != null) staleDownloads.add(old);
                widget.setTooltip(Tooltip.create(Component.literal(settings ? "World Downloader Settings" :
                    path.equals("icon/stop") ? "Stop World Download" : "Start World Download")));
            }
        }
        // SWD refreshes its buttons on toggle. Keep the current functional pair.
        Screens.getWidgets(screen).removeAll(staleDownloads);
        row.addAll(downloads.values());
        row.addAll(recordings);
        row.addAll(mods);
        if (downloads.isEmpty() && recordings.isEmpty()) {
            // Core's five controls form one compact, symmetrical centered row.
            int size = 20;
            int gap = 4;
            int rowWidth = row.size() * size + (row.size() - 1) * gap;
            int left = layout.options().getX();
            int right = layout.worldOptions().getX() + layout.worldOptions().getWidth();
            int start = (left + right - rowWidth) / 2;
            for (int i = 0; i < row.size(); i++) {
                var widget = row.get(i);
                widget.setWidth(size);
                widget.setHeight(size);
                widget.setPosition(start + i * (size + gap), layout.iconsY());
            }
        } else {
            // Charged's two groups each span their own options column. Anchor
            // both ends exactly, distributing integer rounding inside the group.
            // Extra native recording controls still fit without overlapping.
            int leftCount = (row.size() + 1) / 2;
            int size = row.size() > 8 ? 16 : 20;
            spreadGroup(row, 0, leftCount, layout.options(), size, layout.iconsY());
            spreadGroup(row, leftCount, row.size() - leftCount,
                        layout.worldOptions(), size, layout.iconsY());
        }
    }

    private static void spreadGroup(List<AbstractWidget> row, int offset, int count,
                                    AbstractWidget column, int size, int y) {
        for (int i = 0; i < count; i++) {
            int x = column.getX() + (count == 1 ? (column.getWidth() - size) / 2 :
                Math.round(i * (column.getWidth() - size) / (float) (count - 1)));
            var widget = row.get(offset + i);
            widget.setWidth(size);
            widget.setHeight(size);
            widget.setPosition(x, y);
        }
    }

    private static boolean hasTranslation(AbstractWidget widget, String key) {
        return widget.getMessage().getContents() instanceof TranslatableContents contents &&
            contents.getKey().equals(key);
    }
}
