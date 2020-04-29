package net.fabricmc.minertx;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.minertx.jivix.*;
import net.fabricmc.minertx.jivix.JiviXBase;
import net.fabricmc.minertx.jivix.JiviXCore;
import net.fabricmc.minertx.jivix.JiviXBase.*;
import net.fabricmc.minertx.jivix.JiviXCore.*;
import static net.fabricmc.minertx.jivix.JiviXBase.*;
import static net.fabricmc.minertx.jivix.JiviXCore.*;

public class MineRTX implements ModInitializer {
	public static JiviXBase.Driver driver;

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		driver = new JiviXBase.Driver();
		System.out.println("Hello Fabric world!");
	}
}
