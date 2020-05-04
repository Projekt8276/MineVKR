package net.fabricmc.minevkr;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.minevkr.jivix.*;
import net.fabricmc.minevkr.jivix.JiviXBase;
import net.fabricmc.minevkr.jivix.JiviXCore;
import net.fabricmc.minevkr.jivix.JiviXBase.*;
import net.fabricmc.minevkr.jivix.JiviXCore.*;
import net.minecraft.client.gl.VertexBuffer;
import net.minecraft.client.render.chunk.ChunkBuilder;
import net.minecraft.util.math.BlockPos;
import org.lwjgl.vulkan.*;
import org.lwjgl.glfw.*;
import org.lwjgl.glfw.GLFW.*;
import org.spongepowered.asm.mixin.Shadow;

import static net.fabricmc.minevkr.jivix.JiviXBase.*;
import static net.fabricmc.minevkr.jivix.JiviXCore.*;
//import static org.lwjgl.glfw.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.Functions.*;

public class MineVKR implements ModInitializer  {
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
		MineVKR.vDriver = new JiviXBase.Driver();
		this.vCPosition = new double[3];
	}

	public static void InitializeRenderer(){

		//MineVKR.vDriver.initializeGL( GetProcAddress ); // NOT POSSIBLE!
		System.out.println("This line is printed by an example mod mixin!");

		//
		MineVKR.vInstanceHandle = MineVKR.vDriver.createInstance();
		MineVKR.vInstance = new VkInstance(MineVKR.vInstanceHandle, VkInstanceCreateInfo.createSafe(MineVKR.vDriver.getInstanceCreateInfoAddress())); // LWJGL-3 can read from JavaCPP by same address
		System.out.println("With create VkInstance: [" + MineVKR.vInstanceHandle + "] ...");

		//
		MineVKR.vPhysicalDeviceHandle = MineVKR.vDriver.getPhysicalDevice();
		MineVKR.vPhysicalDevice = new VkPhysicalDevice(MineVKR.vPhysicalDeviceHandle, MineVKR.vInstance);

		//
		MineVKR.vDeviceHandle = MineVKR.vDriver.createDevice(MineVKR.vPhysicalDeviceHandle);
		MineVKR.vDevice = new VkDevice(MineVKR.vDeviceHandle, MineVKR.vPhysicalDevice, VkDeviceCreateInfo.createSafe(MineVKR.vDriver.getDeviceCreateInfoAddress())); // LWJGL-3 can read from JavaCPP by same address
		System.out.println("With create VkDevice: [" + MineVKR.vDeviceHandle + "] ...");

		//
		MineVKR.vContext = new JiviXBase.Context(MineVKR.vDriver);
		MineVKR.vBufferViewSet = new JiviXBase.BufferViewSet(MineVKR.vContext);
		MineVKR.vMaterials = new JiviXBase.Material(MineVKR.vContext);
		MineVKR.vNode = new JiviXBase.Node[]{ new JiviXBase.Node(MineVKR.vContext) };
		MineVKR.vRenderer = new JiviXBase.Renderer(MineVKR.vContext);

		//
		MineVKR.vContext.initialize(1600, 1200); // UNABLE TO DEBUG!!
		MineVKR.vRenderer.linkMaterial(MineVKR.vMaterials.sharedPtr());
		MineVKR.vRenderer.linkNode(MineVKR.vNode[0].sharedPtr());
	}
}
