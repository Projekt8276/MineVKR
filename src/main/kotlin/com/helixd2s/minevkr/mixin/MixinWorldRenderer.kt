package com.helixd2s.minevkr.mixin

import com.helixd2s.minevkr.MineVKR
import com.helixd2s.minevkr.MineVKR.Companion.vInitializeRenderer
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gl.VertexBuffer
import net.minecraft.client.render.*
import net.minecraft.client.render.chunk.ChunkBuilder.BuiltChunk
import net.minecraft.client.texture.TextureManager
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.util.math.BlockPos
import org.spongepowered.asm.mixin.Final
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.Shadow
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Inject
import org.spongepowered.asm.mixin.injection.Redirect
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo


@Mixin(WorldRenderer::class)
abstract class MixinWorldRenderer {
    //public static JiviXBase.Driver driver;
    @Final
    @Shadow private val textureManager: TextureManager? = null
    @Shadow private val chunks: BuiltChunkStorage? = null

    @Shadow
    protected abstract fun renderLayer(
            renderLayer: RenderLayer?,
            matrices: MatrixStack?,
            cameraX: Double,
            cameraY: Double,
            cameraZ: Double
    )

    // RESERVED! // ChunkBuilder.BuiltChunk builtChunk
    // Will used for rendering chunk in ray-tracing as instance!
    @Inject(method = ["renderLayer"], at = [At(value = "INVOKE", target = "Lnet/minecraft/client/util/math/MatrixStack;pop()V", shift = At.Shift.AFTER)])
    private fun onRenderLayerPop(renderLayer: RenderLayer, matrixStack: MatrixStack, d: Double, e: Double, f: Double, ci: CallbackInfo) {
        //System.out.println("Radio-POP"); // For Debug!
        //matrixStack.pop();
    }

    @Redirect(method = ["renderLayer"], at = At(value = "INVOKE", target = "Lnet/minecraft/client/render/chunk/ChunkBuilder\$BuiltChunk;getOrigin()Lnet/minecraft/util/math/BlockPos;"))
    private fun onBlockPosGet(builtChunk: BuiltChunk): BlockPos {
        return builtChunk.also { MineVKR.CurrentChunk.vCurrentChunk = it }.origin.also { MineVKR.CurrentChunk.vBlockPos = it }
    }

    // Steal Vertex Buffer for any other injections
    @Redirect(method = ["renderLayer"], at = At(value = "INVOKE", target = "Lnet/minecraft/client/render/chunk/ChunkBuilder\$BuiltChunk;getBuffer(Lnet/minecraft/client/render/RenderLayer;)Lnet/minecraft/client/gl/VertexBuffer;"))
    private fun onVertexBufferGet(builtChunk: BuiltChunk, layer: RenderLayer): VertexBuffer {
        return builtChunk.also { MineVKR.CurrentChunk.vCurrentChunk = it }.getBuffer(layer).also { MineVKR.CurrentChunk.vVertexBuffer = it }
    }

    // Will used for make ray-tracer bindings
    @Redirect(method = ["renderLayer"], at = At(value = "INVOKE", target = "Lnet/minecraft/client/render/VertexFormat;startDrawing(J)V"))
    private fun onVertexFormatStartDrawing(format: VertexFormat, pointer: Long) {
        MineVKR.CurrentChunk.vVertexFormat = format
        format.startDrawing(pointer)
    }

    //
    @Redirect(method = ["render"], at = At(value = "INVOKE", target = "Lnet/minecraft/client/render/WorldRenderer;renderLayer(Lnet/minecraft/client/render/RenderLayer;Lnet/minecraft/client/util/math/MatrixStack;DDD)V"))
    private fun redirectRenderLayer(
            worldRenderer: WorldRenderer,
            renderLayer: RenderLayer,
            matrices: MatrixStack,
            cameraX: Double,
            cameraY: Double,
            cameraZ: Double
    ) {
        MineVKR.CurrentChunk.vCPosition = doubleArrayOf(cameraX, cameraY, cameraZ)
        renderLayer(renderLayer, matrices, cameraX, cameraY, cameraZ)
    }

    // Vulkan Initialize
    @Inject(at = [At("TAIL")], method = ["<init>"])
    private fun init(client: MinecraftClient, bufferBuilders: BufferBuilderStorage, info: CallbackInfo) {
        if (!MineVKR.vInitialized) {
            MineVKR.vWorldRenderer = this as WorldRenderer
            vInitializeRenderer()
        }
    }
}
