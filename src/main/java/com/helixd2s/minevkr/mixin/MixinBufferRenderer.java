package com.helixd2s.minevkr.mixin;

import com.helixd2s.minevkr.MineVKR;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.render.BufferRenderer;
import net.minecraft.client.render.VertexFormat;
import org.lwjgl.system.MemoryUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.nio.ByteBuffer;

@Mixin(BufferRenderer.class)
public abstract class MixinBufferRenderer {

    @Redirect(method="draw", at=@At(value = "INVOKE", target = "Lnet/minecraft/client/render/BufferRenderer;draw(Ljava/nio/ByteBuffer;ILnet/minecraft/client/render/VertexFormat;I)V"))
    private static void OnDraw(ByteBuffer buffer, int mode, VertexFormat vertexFormat, int count) {
        MineVKR.BufferRendering.buffer = buffer;
        MineVKR.BufferRendering.mode = mode;
        MineVKR.BufferRendering.vertexFormat = vertexFormat;
        MineVKR.BufferRendering.count = count;

        RenderSystem.assertThread(RenderSystem::isOnRenderThread);
        buffer.clear();
        if (count > 0) {
            vertexFormat.startDrawing(MemoryUtil.memAddress(buffer));
            GlStateManager.drawArrays(mode, 0, count);
            vertexFormat.endDrawing();
        }
    }

}
