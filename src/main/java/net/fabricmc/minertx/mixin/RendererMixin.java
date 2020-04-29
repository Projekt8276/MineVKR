package net.fabricmc.minertx.mixin;

import net.fabricmc.minertx.jivix.JiviXBase;
import net.minecraft.client.gui.screen.TitleScreen;
import org.lwjgl.PointerBuffer;
import org.lwjgl.vulkan.VkApplicationInfo;
import org.lwjgl.vulkan.VkInstance;
import org.lwjgl.vulkan.VkInstanceCreateInfo;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.fabricmc.minertx.*;
import net.fabricmc.minertx.MineRTX;

import java.nio.ByteBuffer;

import static org.lwjgl.glfw.GLFWVulkan.glfwGetRequiredInstanceExtensions;
import static org.lwjgl.system.MemoryUtil.*;
import static org.lwjgl.vulkan.EXTDebugReport.VK_EXT_DEBUG_REPORT_EXTENSION_NAME;
import static org.lwjgl.vulkan.VK10.*;
import static org.lwjgl.vulkan.VK10.VK_SUCCESS;
import static org.lwjgl.vulkan.VK11.VK_API_VERSION_1_1;

import net.fabricmc.minertx.jivix.*;
import net.fabricmc.minertx.jivix.JiviXBase;
import net.fabricmc.minertx.jivix.JiviXCore;
import net.fabricmc.minertx.jivix.JiviXBase.*;
import net.fabricmc.minertx.jivix.JiviXCore.*;
import static net.fabricmc.minertx.jivix.JiviXBase.*;
import static net.fabricmc.minertx.jivix.JiviXCore.*;

@Mixin(TitleScreen.class)
public class RendererMixin {
	//public static JiviXBase.Driver driver;

	@Inject(at = @At("HEAD"), method = "init()V")
	private void init(CallbackInfo info) {

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

		long instance = MineRTX.driver.createInstance();
		System.out.println("This line is printed by an example mod mixin! With create VkInstance:" + instance + "...");
	}
}
