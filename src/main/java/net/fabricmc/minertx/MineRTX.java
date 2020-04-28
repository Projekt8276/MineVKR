package net.fabricmc.minertx;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.minertx.jivix.*;

public class MineRTX implements ModInitializer {
	static JiviXBase.Driver driver;


	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		driver = new JiviXBase.Driver();

		System.out.println("Hello Fabric world!");
	}
}
