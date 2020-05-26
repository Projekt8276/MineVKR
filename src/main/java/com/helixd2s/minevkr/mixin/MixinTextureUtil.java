package com.helixd2s.minevkr.mixin;

import com.helixd2s.minevkr.MineVKR;
import com.helixd2s.minevkr.ducks.IETextureUtil;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.TextureUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TextureUtil.class)
abstract public class MixinTextureUtil implements IETextureUtil {
    @Inject(method = "method_24961", at = @At(value = "HEAD"), cancellable = true)
    private static void onTextureInit(NativeImage.GLFormat gLFormat, int i, int j, int k, int l, CallbackInfo ci) {
        MineVKR.Companion.uTextureInit(gLFormat, i, j, k, l);
        ci.cancel();
    }
}
