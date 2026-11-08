package com.lokamc.ui.mixin;

import com.lokamc.ui.LoadingBackground;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.gui.screens.worldselection.SelectWorldScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** Provide a slate fallback under out-of-world menus and the controls screens. */
@Mixin(Screen.class)
public abstract class MenuPanoramaBackgroundMixin {
    @Inject(method = "extractPanorama", at = @At("HEAD"), cancellable = true)
    private void loka$menuFallback(GuiGraphicsExtractor graphics, float delta,
                                   CallbackInfo callback) {
        LoadingBackground.drawSlate(graphics, 0, 0, graphics.guiWidth(), graphics.guiHeight());
        callback.cancel();
    }

    @Inject(method = "extractBlurredBackground", at = @At("HEAD"), cancellable = true)
    private void loka$keepMenuBackgroundReadable(GuiGraphicsExtractor graphics,
                                          CallbackInfo callback) {
        Object screen = this;
        if (Minecraft.getInstance().level == null &&
            !(screen instanceof TitleScreen || screen instanceof SelectWorldScreen)) {
            callback.cancel();
        }
    }
}
