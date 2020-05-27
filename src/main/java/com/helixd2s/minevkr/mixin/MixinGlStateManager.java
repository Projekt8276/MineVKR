package com.helixd2s.minevkr.mixin;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.lwjgl.opengl.GL45;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static org.lwjgl.opengl.GL45.glCreateTextures;

@Mixin(GlStateManager.class)
public class MixinGlStateManager {

    @Inject(method = "genTextures", at = @At(value = "HEAD"), cancellable = true)
    private static void OnGenTexture(CallbackInfoReturnable<Integer> ci) {
        int[] glID = { 0 };
        RenderSystem.assertThread(RenderSystem::isOnRenderThreadOrInit);
        glCreateTextures(GL11.GL_TEXTURE_2D, glID);
        ci.setReturnValue(glID[0]);
        ci.cancel();
    }

}
