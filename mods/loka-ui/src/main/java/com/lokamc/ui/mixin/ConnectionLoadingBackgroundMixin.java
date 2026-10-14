package com.lokamc.ui.mixin;

import com.lokamc.ui.LoadingBackground;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.ConnectScreen;
import net.minecraft.client.gui.screens.GenericMessageScreen;
import net.minecraft.client.gui.screens.ProgressScreen;
import net.minecraft.client.gui.screens.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** Keep joining/preparing/saving transitions on the same terrain-loading art. */
@Mixin({Screen.class, GenericMessageScreen.class})
public abstract class ConnectionLoadingBackgroundMixin {
    @Inject(method = "extractBackground", at = @At("HEAD"), cancellable = true)
    private void loka$connectionBackground(GuiGraphicsExtractor graphics, int mouseX,
                                          int mouseY, float delta, CallbackInfo callback) {
        Object screen = this;
        if (!(screen instanceof ConnectScreen || screen instanceof GenericMessageScreen ||
              screen instanceof ProgressScreen)) return;
        LoadingBackground.draw(graphics);
        callback.cancel();
    }
}
