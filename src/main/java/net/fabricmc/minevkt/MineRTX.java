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
import org.lwjgl.vulkan.VkInstance;
import org.spongepowered.asm.mixin.Shadow;

import static net.fabricmc.minevkt.jivix.JiviXBase.*;
import static net.fabricmc.minevkt.jivix.JiviXCore.*;

public class MineRTX implements ModInitializer {
	public static JiviXBase.Driver vDriver;
	public static long vInstanceHandle;
	public static VkInstance vInstance;
	public static boolean vInitialized;
	public static VertexBuffer vVertexBuffer;
	public static BlockPos vBlockPos;
	public static ChunkBuilder.BuiltChunk vCurrentChunk;
	public static double[] vCPosition;

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		//driver = new JiviXBase.Driver();
		System.out.println("Hello Fabric world!");
		this.vCPosition = new double[3];
	}



}
