package com.lokamc.ui;

/** Regression checks for centered cropping and a screen-anchored watermark. */
public final class BackgroundChecks {
    private static final float EPSILON = 0.00001f;

    public static void main(String[] args) {
        check(2880, 1676, 0f, 0f);
        check(3440, 1440, 0f, 0.14033968f);
        check(3840, 1080, 0f, 0.25835322f);
        check(1600, 1200, 0.11203704f, 0f);
        check(1080, 1920, 0.33632813f, 0f);
        check(1365, 767, 0f, 0.01721787f);
        for (int[] size : new int[][] {{1920, 1080}, {3440, 1440}, {1080, 1920}, {20, 10}}) {
            LoadingBackground.Watermark mark = LoadingBackground.watermark(size[0], size[1]);
            if (mark.x() < 0 || mark.y() < 0 || mark.x() + mark.width() > size[0] ||
                mark.y() + mark.height() > size[1] ||
                mark.y() + mark.height() != size[1] - mark.x()) {
                throw new AssertionError("Watermark is not within the bottom-left margin");
            }
        }
        System.out.println("Background crop and watermark checks passed.");
    }

    private static void check(int width, int height, float u0, float v0) {
        LoadingBackground.Crop crop = LoadingBackground.centeredCrop(width, height, 2880, 1676);
        near(crop.u0(), u0);
        near(crop.v0(), v0);
        near(crop.u0(), 1f - crop.u1());
        near(crop.v0(), 1f - crop.v1());
        near((crop.u1() - crop.u0()) * 2880f / ((crop.v1() - crop.v0()) * 1676f),
            width / (float) height);
    }

    private static void near(float actual, float expected) {
        if (!Float.isFinite(actual) || Math.abs(actual - expected) > EPSILON) {
            throw new AssertionError("Expected " + expected + ", got " + actual);
        }
    }
}
