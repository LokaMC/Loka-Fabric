package com.lokamc.ui.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.ConnectScreen;
import net.minecraft.client.gui.screens.GenericMessageScreen;
import net.minecraft.client.gui.screens.LevelLoadingScreen;
import net.minecraft.client.gui.screens.ProgressScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.options.controls.ControlsScreen;
import net.minecraft.client.gui.screens.options.controls.KeyBindsScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/** Allow the shared FancyMenu video on ordinary pre-world screens. */
@Pseudo
@Mixin(targets = "de.keksuccino.fancymenu.customization.ScreenCustomization", remap = false)
public abstract class FancyMenuEnableMixin {
    @Shadow public static boolean isScreenBlacklisted(Screen screen) { throw new AssertionError(); }
    @Shadow public static boolean isScreenCustomizationEnabled() { throw new AssertionError(); }

    @Inject(method = "isCustomizationEnabledForScreen(Lnet/minecraft/client/gui/screens/Screen;Z)Z",
            at = @At("RETURN"), cancellable = true, remap = false)
    private static void loka$enablePreWorldMenu(Screen screen, boolean ignoreGlobalSetting,
                                                CallbackInfoReturnable<Boolean> callback) {
        if (callback.getReturnValueZ() || screen == null || Minecraft.getInstance().level != null ||
            (!ignoreGlobalSetting && !isScreenCustomizationEnabled()) || isScreenBlacklisted(screen)) return;
        if (screen instanceof ControlsScreen || screen instanceof KeyBindsScreen ||
            screen instanceof ConnectScreen || screen instanceof GenericMessageScreen ||
            screen instanceof ProgressScreen || screen instanceof LevelLoadingScreen ||
            screen.getClass().getName().startsWith("de.keksuccino.")) return;
        callback.setReturnValue(true);
    }
}
