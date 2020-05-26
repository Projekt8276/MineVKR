package com.helixd2s.minevkr.mixin

import com.helixd2s.minevkr.MineVKR
import com.helixd2s.minevkr.ducks.IETextureUtil
import net.minecraft.client.texture.NativeImage
import net.minecraft.client.texture.TextureUtil
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Inject
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo

@Mixin(TextureUtil::class)
class MixinTextureUtilBroken : IETextureUtil {

}
