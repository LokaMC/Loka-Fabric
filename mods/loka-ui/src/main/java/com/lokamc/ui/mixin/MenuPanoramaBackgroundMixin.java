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

/** Replace the shared out-of-world panorama, including mod menus using Screen. */
@Mixin(Screen.class)
public abstract class MenuPanoramaBackgroundMixin {
    @Inject(method = "extractPanorama", at = @At("HEAD"), cancellable = true)
    private void loka$menuPanorama(GuiGraphicsExtractor graphics, float delta,
                                   CallbackInfo callback) {
        LoadingBackground.draw(graphics);
        callback.cancel();
    }

    @Inject(method = "extractBlurredBackground", at = @At("HEAD"), cancellable = true)
    private void loka$keepDeepDarkReadable(GuiGraphicsExtractor graphics,
                                          CallbackInfo callback) {
        // Preserve the normal blur over a running world. FancyMenu controls the
        // title/world-list video backgrounds; other menu artwork stays crisp.
        Object screen = this;
        if (Minecraft.getInstance().level == null &&
            !(screen instanceof TitleScreen || screen instanceof SelectWorldScreen)) {
            callback.cancel();
        }
    }
}
