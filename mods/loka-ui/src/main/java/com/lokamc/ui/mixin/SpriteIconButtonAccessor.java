package com.lokamc.ui.mixin;

import net.minecraft.client.gui.components.SpriteIconButton;
import net.minecraft.client.gui.components.WidgetSprites;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

/** Identify native mod icons without depending on their original positions. */
@Mixin(SpriteIconButton.class)
public interface SpriteIconButtonAccessor {
    @Accessor("sprite")
    WidgetSprites loka$getSprites();
}
