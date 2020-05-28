package com.helixd2s.minevkr.mixin

import com.helixd2s.minevkr.ducks.GLFormat
import net.minecraft.client.texture.NativeImage
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.Shadow

@Mixin(NativeImage.GLFormat::class)
abstract class MixinGLFormat : GLFormat {
    @Shadow override var glConstant: Int = 0;
}
