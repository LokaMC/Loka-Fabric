package com.lokamc.ui.mixin;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.LevelLoadingScreen;
import com.lokamc.ui.LoadingBackground;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** Replace the terrain-loading background without touching progress or narration. */
@Mixin(LevelLoadingScreen.class)
public abstract class LevelLoadingScreenMixin {
    @Inject(method = "extractBackground", at = @At("HEAD"), cancellable = true)
    private void loka$terrainBackground(GuiGraphicsExtractor graphics, int mouseX,
                                       int mouseY, float delta, CallbackInfo callback) {
        LoadingBackground.draw(graphics);
        callback.cancel();
    }
}
