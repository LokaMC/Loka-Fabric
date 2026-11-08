package com.lokamc.ui.mixin;

import com.lokamc.ui.LoadingBackground;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractSelectionList;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.options.controls.ControlsScreen;
import net.minecraft.client.gui.screens.options.controls.KeyBindsScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** Keep the text-heavy pre-world controls lists on slate. */
@Mixin(AbstractSelectionList.class)
public abstract class MenuListBackgroundMixin {
    @Inject(method = "extractListBackground", at = @At("HEAD"), cancellable = true)
    private void loka$slateList(GuiGraphicsExtractor graphics, CallbackInfo callback) {
        Minecraft client = Minecraft.getInstance();
        Screen screen = client.gui.screen();
        // Other 26.3 lists are translucent; retain them so the video or the
        // in-world view remains visible behind world and server lists.
        if (client.level != null || !(screen instanceof ControlsScreen || screen instanceof KeyBindsScreen)) return;
        AbstractSelectionList<?> list = (AbstractSelectionList<?>) (Object) this;
        LoadingBackground.drawSlate(graphics, list.getX(), list.getY(), list.getWidth(), list.getHeight());
        callback.cancel();
    }
}
