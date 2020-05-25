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
import net.minecraft.util.math.Matrix4f
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
    protected abstract fun renderLayer(renderLayer: RenderLayer?, matrices: MatrixStack?, cameraX: Double, cameraY: Double, cameraZ: Double)

    // Begin-Rendering (reset counters, öбнулись, begin building geometry)
    @Inject(method = ["render"], at = [At(value = "HEAD")])
    open fun onRenderBegin(matrices: MatrixStack?, tickDelta: Float, limitTime: Long, renderBlockOutline: Boolean, camera: Camera?, gameRenderer: GameRenderer?, lightmapTextureManager: LightmapTextureManager?, matrix4f: Matrix4f?, ci: CallbackInfo): Unit {
        MineVKR.vRenderBegin(matrices, tickDelta, limitTime, renderBlockOutline, camera, gameRenderer, lightmapTextureManager, matrix4f, ci);
    }

    // Post-Rendering (show result)
    @Inject(method = ["render"], at = [At(value = "TAIL")])
    open fun onRenderEnd(matrices: MatrixStack?, tickDelta: Float, limitTime: Long, renderBlockOutline: Boolean, camera: Camera?, gameRenderer: GameRenderer?, lightmapTextureManager: LightmapTextureManager?, matrix4f: Matrix4f?, ci: CallbackInfo): Unit {
        MineVKR.vRenderEnd(matrices, tickDelta, limitTime, renderBlockOutline, camera, gameRenderer, lightmapTextureManager, matrix4f, ci);
    }

    //
    @Inject(method = ["renderLayer"], at = [At(value = "HEAD")])
    open fun onRenderLayerBegin(renderLayer: RenderLayer, matrixStack: MatrixStack, d: Double, e: Double, f: Double, ci: CallbackInfo) {
        MineVKR.vRenderLayerBegin(renderLayer, matrixStack, d, e, f, ci)
    }

    //
    @Inject(method = ["renderLayer"], at = [At(value = "TAIL")])
    open fun onRenderLayerEnd(renderLayer: RenderLayer, matrixStack: MatrixStack, d: Double, e: Double, f: Double, ci: CallbackInfo) {
        MineVKR.vRenderLayerEnd(renderLayer, matrixStack, d, e, f, ci)
    }

    // Will used for rendering chunk in ray-tracing as instance!
    @Inject(method = ["renderLayer"], at = [At(value = "INVOKE", target = "Lnet/minecraft/client/util/math/MatrixStack;pop()V", shift = At.Shift.AFTER)])
    open fun onRenderLayerPop(renderLayer: RenderLayer, matrixStack: MatrixStack, d: Double, e: Double, f: Double, ci: CallbackInfo) {
        MineVKR.vChunkDraw(renderLayer, matrixStack, d, e, f, ci);
    }

    //
    @Redirect(method = ["renderLayer"], at = At(value = "INVOKE", target = "Lnet/minecraft/client/render/chunk/ChunkBuilder\$BuiltChunk;getOrigin()Lnet/minecraft/util/math/BlockPos;"))
    open fun onBlockPosGet(builtChunk: BuiltChunk): BlockPos {
        return builtChunk.also { MineVKR.CurrentChunk.vCurrentChunk = it }.origin.also { MineVKR.CurrentChunk.vBlockPos = it }
    }

    // Steal Vertex Buffer for any other injections
    @Redirect(method = ["renderLayer"], at = At(value = "INVOKE", target = "Lnet/minecraft/client/render/chunk/ChunkBuilder\$BuiltChunk;getBuffer(Lnet/minecraft/client/render/RenderLayer;)Lnet/minecraft/client/gl/VertexBuffer;"))
    open fun onVertexBufferGet(builtChunk: BuiltChunk, layer: RenderLayer): VertexBuffer {
        return builtChunk.also { MineVKR.CurrentChunk.vCurrentChunk = it }.getBuffer(layer).also { MineVKR.CurrentChunk.vVertexBuffer = it }
    }

    // Will used for make ray-tracer bindings
    @Redirect(method = ["renderLayer"], at = At(value = "INVOKE", target = "Lnet/minecraft/client/render/VertexFormat;startDrawing(J)V"))
    open fun onVertexFormatStartDrawing(format: VertexFormat, pointer: Long) {
        MineVKR.CurrentChunk.vVertexFormat = format
        format.startDrawing(pointer)
    }

    //
    @Redirect(method = ["render"], at = At(value = "INVOKE", target = "Lnet/minecraft/client/render/WorldRenderer;renderLayer(Lnet/minecraft/client/render/RenderLayer;Lnet/minecraft/client/util/math/MatrixStack;DDD)V"))
    open fun onRedirectRenderLayer(
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
    open fun onInit(client: MinecraftClient, bufferBuilders: BufferBuilderStorage, info: CallbackInfo) {
        if (!MineVKR.vInitialized) {
            MineVKR.vWorldRenderer = this as WorldRenderer
            MineVKR.vInitializeRenderer()
        }
    }
}
