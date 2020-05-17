package com.helixd2s.minevkr;

import net.fabricmc.api.ModInitializer;
import com.helixd2s.jivix.JiviXBase;
import net.minecraft.client.gl.VertexBuffer;
import net.minecraft.client.render.chunk.ChunkBuilder;
import net.minecraft.util.math.BlockPos;
import org.lwjgl.vulkan.*;

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
	//public static JiviXBase.BufferViewSet vBufferViewSet; // Un-Used, due OpenGL transform feedback input into Vulkan Memory!
	public static JiviXBase.Context vContext;
	public static JiviXBase.Material vMaterials;
	public static JiviXBase.MeshBinding[] vBindingsEntity;
	public static JiviXBase.MeshBinding[] vBindingsChunksOpaque;
	public static JiviXBase.MeshBinding[] vBindingsChunksTranslucent;
	public static JiviXBase.MeshBinding[] vBindingsChunksCutout;
	//public static JiviXBase.MeshBinding[] vBindings;
	//public static JiviXBase.MeshInput[] vMeshInput; // No Idea!
	public static JiviXBase.Node[] vNode;
	public static JiviXBase.Renderer vRenderer;

	//
	public static int MaxChunkBindings = 16;
	public static int MaxEntityBindings = 16;

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

	public static void InitializeRenderer() {
		System.out.println("This line is printed by an example mod mixin!");

		//
		MineVKR.vInstanceHandle = MineVKR.vDriver.createInstance();
		MineVKR.vInstance = new VkInstance(MineVKR.vInstanceHandle, VkInstanceCreateInfo.createSafe(MineVKR.vDriver.getInstanceCreateInfoAddress())); // LWJGL-3 can read from JavaCPP by same address
		System.out.println("With create VkInstance: [" + MineVKR.vInstanceHandle + "]...");

		//
		MineVKR.vPhysicalDeviceHandle = MineVKR.vDriver.getPhysicalDevice();
		MineVKR.vPhysicalDevice = new VkPhysicalDevice(MineVKR.vPhysicalDeviceHandle, MineVKR.vInstance);
		System.out.println("With got VkPhysicalDevice: [" + MineVKR.vPhysicalDeviceHandle + "]...");

		//
		MineVKR.vDeviceHandle = MineVKR.vDriver.createDevice(MineVKR.vPhysicalDeviceHandle);
		MineVKR.vDevice = new VkDevice(MineVKR.vDeviceHandle, MineVKR.vPhysicalDevice, VkDeviceCreateInfo.createSafe(MineVKR.vDriver.getDeviceCreateInfoAddress())); // LWJGL-3 can read from JavaCPP by same address
		System.out.println("With create VkDevice: [" + MineVKR.vDeviceHandle + "]...");

		//
		MineVKR.vContext = new JiviXBase.Context(MineVKR.vDriver);
		System.out.println("Create Context...");

		//
		MineVKR.vMaterials = new JiviXBase.Material(MineVKR.vContext);
		MineVKR.vNode = new JiviXBase.Node[]{ new JiviXBase.Node(MineVKR.vContext) };
		System.out.println("Create Node and Materials...");

		//
		MineVKR.vRenderer = new JiviXBase.Renderer(MineVKR.vContext);
		System.out.println("Create Renderer...");

		//
		MineVKR.vRenderer.linkMaterial(MineVKR.vMaterials.sharedPtr());
		MineVKR.vRenderer.linkNode(MineVKR.vNode[0].sharedPtr());
		System.out.println("Link Node and Materials...");

		//
		MineVKR.vContext.initialize(1600, 1200); // UNABLE TO DEBUG!!
		System.out.println("Initialize Context...");

		// Declare all chunks to be ray-traced!
		MineVKR.vBindingsChunksOpaque = new JiviXBase.MeshBinding[MaxChunkBindings]; // for chunk range = 4
		MineVKR.vBindingsChunksCutout = new JiviXBase.MeshBinding[MaxChunkBindings]; // for chunk range = 4
		MineVKR.vBindingsChunksTranslucent = new JiviXBase.MeshBinding[MaxChunkBindings]; // for chunk range = 4
		System.out.println("Create array of chunk bindings...");

		// Create bindings per every chunks!
		for (int i=0;i<MaxChunkBindings;i++) { // Reserve per chunk 2048 faces, and 16 chunks possible...
			MineVKR.vBindingsChunksOpaque[i] = new JiviXBase.MeshBinding(MineVKR.vContext, 2048);
			MineVKR.vBindingsChunksCutout[i] = new JiviXBase.MeshBinding(MineVKR.vContext, 2048);
			MineVKR.vBindingsChunksTranslucent[i] = new JiviXBase.MeshBinding(MineVKR.vContext, 2048);
		}
		System.out.println("Create chunk bindings itself...");

		//
		for (int i=0;i<MaxChunkBindings;i++) MineVKR.vNode[0].pushMesh(MineVKR.vBindingsChunksOpaque[i]);
		for (int i=0;i<MaxChunkBindings;i++) MineVKR.vNode[0].pushMesh(MineVKR.vBindingsChunksCutout[i]);
		for (int i=0;i<MaxChunkBindings;i++) MineVKR.vNode[0].pushMesh(MineVKR.vBindingsChunksTranslucent[i]);
		System.out.println("Add chunk bindings into Node...");

		// Declare entity bindings
		MineVKR.vBindingsEntity = new JiviXBase.MeshBinding[MaxEntityBindings];
		System.out.println("Create array of entity bindings...");

		// Needs 16 Parts of Entity, with 16th cube vertices
		long PartsSize[] = new long[] { 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32, 32 };

		// Create bindings per every entity!
		for (int i=0;i<MaxEntityBindings;i++) {
			MineVKR.vBindingsEntity[i] = new JiviXBase.MeshBinding(MineVKR.vContext, 512, PartsSize);
		};
		System.out.println("Create entity bindings itself...");

		//
		for (int i=0;i<MaxEntityBindings;i++) { MineVKR.vNode[0].pushMesh(MineVKR.vBindingsEntity[i]); };
		System.out.println("Add entity bindings into Node...");
	}
}
