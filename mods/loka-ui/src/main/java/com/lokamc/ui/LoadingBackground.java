package com.lokamc.ui;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.resources.Identifier;

/** The unmodified, watermarked 1920x1080 Loka artwork. */
public final class LoadingBackground {
    private static final Identifier IMAGE = Identifier.fromNamespaceAndPath(
        "loka_ui", "textures/gui/loading_deep_dark.png");

    private LoadingBackground() {}

    public static void draw(GuiGraphicsExtractor graphics) {
        int width = graphics.guiWidth();
        int height = graphics.guiHeight();
        float imageAspect = 1920f / 1080f;
        float screenAspect = width / (float) height;
        // Cover without stretching; preserve the original bottom-left watermark.
        float u1 = Math.min(1f, screenAspect / imageAspect);
        float v0 = 1f - Math.min(1f, imageAspect / screenAspect);
        graphics.blit(IMAGE, 0, 0, width, height, 0f, u1, v0, 1f);
    }
}
