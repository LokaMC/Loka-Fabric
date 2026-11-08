package com.lokamc.ui;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.resources.Identifier;

/** Centered backgrounds and a separate, screen-anchored loading watermark. */
public final class LoadingBackground {
    private static final Identifier COLOSSEUM = Identifier.fromNamespaceAndPath(
        "loka_ui", "textures/gui/loading_colosseum.png");
    private static final Identifier SLATE = Identifier.fromNamespaceAndPath(
        "loka_ui", "textures/gui/menu_slate.png");
    private static final Identifier WATERMARK = Identifier.fromNamespaceAndPath(
        "loka_ui", "textures/gui/loka_watermark.png");

    private LoadingBackground() {}

    public static void draw(GuiGraphicsExtractor graphics) {
        int width = graphics.guiWidth();
        int height = graphics.guiHeight();
        drawCover(graphics, COLOSSEUM, 0, 0, width, height, 2880, 1676);
        Watermark watermark = watermark(width, height);
        if (watermark.width() > 0 && watermark.height() > 0) {
            graphics.blit(WATERMARK, watermark.x(), watermark.y(),
                watermark.x() + watermark.width(), watermark.y() + watermark.height(),
                0f, 1f, 0f, 1f);
        }
    }

    public static void drawSlate(GuiGraphicsExtractor graphics, int x, int y, int width, int height) {
        drawCover(graphics, SLATE, x, y, width, height, 2560, 1440);
    }

    private static void drawCover(GuiGraphicsExtractor graphics, Identifier image,
                                  int x, int y, int width, int height,
                                  int imageWidth, int imageHeight) {
        if (width <= 0 || height <= 0) return;
        Crop crop = centeredCrop(width, height, imageWidth, imageHeight);
        graphics.blit(image, x, y, x + width, y + height,
            crop.u0(), crop.u1(), crop.v0(), crop.v1());
    }

    static Crop centeredCrop(int width, int height, int imageWidth, int imageHeight) {
        float imageAspect = imageWidth / (float) imageHeight;
        float screenAspect = width / (float) height;
        float u0 = (1f - Math.min(1f, screenAspect / imageAspect)) / 2f;
        float v0 = (1f - Math.min(1f, imageAspect / screenAspect)) / 2f;
        return new Crop(u0, 1f - u0, v0, 1f - v0);
    }

    static Watermark watermark(int width, int height) {
        if (width <= 0 || height <= 0) return new Watermark(0, 0, 0, 0);
        int margin = Math.min(5, Math.min((width - 1) / 2, (height - 1) / 2));
        float scale = Math.min(1f, Math.min((width - 2f * margin) / 61f,
                                            (height - 2f * margin) / 15f));
        int logoWidth = Math.max(1, Math.round(61f * scale));
        int logoHeight = Math.max(1, Math.round(15f * scale));
        return new Watermark(margin, height - margin - logoHeight, logoWidth, logoHeight);
    }

    record Crop(float u0, float u1, float v0, float v1) {}
    record Watermark(int x, int y, int width, int height) {}
}
