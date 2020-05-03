package net.fabricmc.minevkt;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.minevkt.jivix.*;
import net.fabricmc.minevkt.jivix.JiviXBase;
import net.fabricmc.minevkt.jivix.JiviXCore;
import net.fabricmc.minevkt.jivix.JiviXBase.*;
import net.fabricmc.minevkt.jivix.JiviXCore.*;
import net.minecraft.client.gl.VertexBuffer;
import net.minecraft.client.render.chunk.ChunkBuilder;
import net.minecraft.util.math.BlockPos;
import org.lwjgl.vulkan.*;
import org.spongepowered.asm.mixin.Shadow;

import static net.fabricmc.minevkt.jivix.JiviXBase.*;
import static net.fabricmc.minevkt.jivix.JiviXCore.*;

public class MineRTX implements ModInitializer  {
	public static long vPhysicalDeviceHandle;
	public static VkPhysicalDevice vPhysicalDevice;

	public static long vInstanceHandle;
	public static VkInstance vInstance;

	public static long vDeviceHandle;
	public static VkDevice vDevice;

	public static boolean vInitialized;
	public static VertexBuffer vVertexBuffer;
	public static BlockPos vBlockPos;
	public static ChunkBuilder.BuiltChunk vCurrentChunk;
	public static double[] vCPosition;

	public static JiviXBase.Driver vDriver;
	public static JiviXBase.BufferViewSet vBufferViewSet;
	public static JiviXBase.Context vContext;
	public static JiviXBase.Material vMaterials;
	public static JiviXBase.MeshBinding[] vBindings;
	public static JiviXBase.MeshInput[] vMeshInput;
	public static JiviXBase.Node[] vNode;
	public static JiviXBase.Renderer vRenderer;

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		//driver = new JiviXBase.Driver();
		System.out.println("Hello Fabric world!");
		this.vCPosition = new double[3];
	}

	//@Override
	//public void onInitializeClient() {

	//}

	public static void InitializeRenderer(){
		MineRTX.vDriver = new JiviXBase.Driver();
		System.out.println("This line is printed by an example mod mixin!");

		//
		MineRTX.vInstanceHandle = MineRTX.vDriver.createInstance();
		MineRTX.vInstance = new VkInstance(MineRTX.vInstanceHandle, VkInstanceCreateInfo.createSafe(MineRTX.vDriver.getInstanceCreateInfoAddress())); // LWJGL-3 can read from JavaCPP by same address
		System.out.println("With create VkInstance: [" + MineRTX.vInstanceHandle + "] ...");

		//
		MineRTX.vPhysicalDeviceHandle = MineRTX.vDriver.getPhysicalDevice();
		MineRTX.vPhysicalDevice = new VkPhysicalDevice(MineRTX.vPhysicalDeviceHandle, MineRTX.vInstance);

		//
		MineRTX.vDeviceHandle = MineRTX.vDriver.createDevice(MineRTX.vPhysicalDeviceHandle);
		MineRTX.vDevice = new VkDevice(MineRTX.vDeviceHandle, MineRTX.vPhysicalDevice, VkDeviceCreateInfo.createSafe(MineRTX.vDriver.getDeviceCreateInfoAddress())); // LWJGL-3 can read from JavaCPP by same address
		System.out.println("With create VkDevice: [" + MineRTX.vDeviceHandle + "] ...");

		//
		MineRTX.vContext = new JiviXBase.Context(MineRTX.vDriver);
		MineRTX.vBufferViewSet = new JiviXBase.BufferViewSet(MineRTX.vContext);
		MineRTX.vMaterials = new JiviXBase.Material(MineRTX.vContext);
		MineRTX.vNode = new JiviXBase.Node[]{ new JiviXBase.Node(MineRTX.vContext) };
		MineRTX.vRenderer = new JiviXBase.Renderer(MineRTX.vContext);

		//
		MineRTX.vContext.initialize(1600, 1200); // UNABLE TO DEBUG!!
		MineRTX.vRenderer.linkMaterial(MineRTX.vMaterials.sharedPtr());
		MineRTX.vRenderer.linkNode(MineRTX.vNode[0].sharedPtr());
	}
}
