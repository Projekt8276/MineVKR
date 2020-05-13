package com.helixd2s.minevkr.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.VertexBuffer;
import net.minecraft.client.render.*;
import net.minecraft.client.render.chunk.ChunkBuilder;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import com.helixd2s.minevkr.MineVKR;


@Mixin(WorldRenderer.class)
public abstract class MixinWorldRenderer {
	//public static JiviXBase.Driver driver;

	@Final
	@Shadow
	private TextureManager textureManager;

	@Shadow
	private BuiltChunkStorage chunks;

	@Shadow
	protected abstract void renderLayer(
			RenderLayer renderLayer,
			MatrixStack matrices,
			double cameraX,
			double cameraY,
			double cameraZ
	);

	// RESERVED! // ChunkBuilder.BuiltChunk builtChunk
	// Will used for rendering chunk in ray-tracing as instance!
	@Inject(method = "renderLayer", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/math/MatrixStack;pop()V", shift = At.Shift.AFTER))
	private void onRenderLayerPop(RenderLayer renderLayer, MatrixStack matrixStack, double d, double e, double f, CallbackInfo ci){
		//System.out.println("Radio-POP"); // For Debug!
		//matrixStack.pop();
	};

	@Redirect(method = "renderLayer", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/chunk/ChunkBuilder$BuiltChunk;getOrigin()Lnet/minecraft/util/math/BlockPos;"))
	private BlockPos onBlockPosGet(ChunkBuilder.BuiltChunk builtChunk) {
		return (MineVKR.vBlockPos = (MineVKR.vCurrentChunk = builtChunk).getOrigin());
	};

	// Steal Vertex Buffer for any other injections
	@Redirect(method = "renderLayer", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/chunk/ChunkBuilder$BuiltChunk;getBuffer(Lnet/minecraft/client/render/RenderLayer;)Lnet/minecraft/client/gl/VertexBuffer;"))
	private VertexBuffer onVertexBufferGet(ChunkBuilder.BuiltChunk builtChunk, RenderLayer layer) {
		return (MineVKR.vVertexBuffer = (MineVKR.vCurrentChunk = builtChunk).getBuffer(layer));
	};

	//
	@Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/WorldRenderer;renderLayer(Lnet/minecraft/client/render/RenderLayer;Lnet/minecraft/client/util/math/MatrixStack;DDD)V"))
	private void redirectRenderLayer(
			WorldRenderer worldRenderer,
			RenderLayer renderLayer,
			MatrixStack matrices,
			double cameraX,
			double cameraY,
			double cameraZ
	) {
		MineVKR.vCPosition = new double[] {cameraX, cameraY, cameraZ};
		renderLayer(renderLayer, matrices, cameraX, cameraY, cameraZ);
	};

	// Vulkan Initialize
	@Inject(at = @At("TAIL"), method = "<init>")
	private void init(MinecraftClient client, BufferBuilderStorage bufferBuilders, CallbackInfo info) {

		/* Look for instance extensions */
		/*
		PointerBuffer requiredExtensions = glfwGetRequiredInstanceExtensions();
		if (requiredExtensions == null) {
			throw new AssertionError("Failed to find list of required Vulkan extensions");
		}

		boolean debug = false; ByteBuffer[] layers = {
				memUTF8("VK_LAYER_LUNARG_standard_validation"),
		};

		VkApplicationInfo appInfo = VkApplicationInfo.calloc()
				.sType(VK_STRUCTURE_TYPE_APPLICATION_INFO)
				.apiVersion(VK_API_VERSION_1_1);
		PointerBuffer ppEnabledExtensionNames = memAllocPointer(requiredExtensions.remaining() + 1);
		ppEnabledExtensionNames.put(requiredExtensions);
		ppEnabledExtensionNames.put(memUTF8(VK_EXT_DEBUG_REPORT_EXTENSION_NAME));
		ppEnabledExtensionNames.flip();
		PointerBuffer ppEnabledLayerNames = memAllocPointer(layers.length);
		for (int i = 0; debug && i < layers.length; i++)
			ppEnabledLayerNames.put(layers[i]);
		ppEnabledLayerNames.flip();
		VkInstanceCreateInfo pCreateInfo = VkInstanceCreateInfo.calloc()
				.sType(VK_STRUCTURE_TYPE_INSTANCE_CREATE_INFO)
				.pApplicationInfo(appInfo)
				.ppEnabledExtensionNames(ppEnabledExtensionNames)
				.ppEnabledLayerNames(ppEnabledLayerNames);
		PointerBuffer pInstance = memAllocPointer(1);
		int err = vkCreateInstance(pCreateInfo, null, pInstance);
		long instance = pInstance.get(0);
		memFree(pInstance);
		if (err != VK_SUCCESS) {
			throw new AssertionError("Failed to create VkInstance: " + err);
		}
		VkInstance ret = new VkInstance(instance, pCreateInfo);

		System.out.println("This line is printed by an example mod mixin! With create VkInstance result:" + ret + "...");
*/

		// LWJGL-3 can interact with JavaCPP... (i.e. curved interop), can't to be Debug
		if (!MineVKR.vInitialized) { MineVKR.vInitialized = true; // Needs create before the filling data...
			MineVKR.InitializeRenderer();
		};

	};
};
