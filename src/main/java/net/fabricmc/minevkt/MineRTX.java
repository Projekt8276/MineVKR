package net.fabricmc.minertx;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.minertx.jivix.*;
import net.fabricmc.minertx.jivix.JiviXBase;
import net.fabricmc.minertx.jivix.JiviXCore;
import net.fabricmc.minertx.jivix.JiviXBase.*;
import net.fabricmc.minertx.jivix.JiviXCore.*;
import net.minecraft.client.gl.VertexBuffer;
import net.minecraft.client.render.chunk.ChunkBuilder;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Shadow;

import static net.fabricmc.minertx.jivix.JiviXBase.*;
import static net.fabricmc.minertx.jivix.JiviXCore.*;

public class MineRTX implements ModInitializer {
	public static JiviXBase.Driver vDriver;
	public static long vInstance;
	public static boolean vInitialized;
	public static VertexBuffer vVertexBuffer;
	public static BlockPos vBlockPos;
	public static ChunkBuilder.BuiltChunk vCurrentChunk;

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		//driver = new JiviXBase.Driver();
		System.out.println("Hello Fabric world!");
	}



}
