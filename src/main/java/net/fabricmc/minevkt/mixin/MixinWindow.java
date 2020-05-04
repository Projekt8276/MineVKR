package net.fabricmc.minevkt.mixin;


import net.fabricmc.minevkt.MineRTX;
import net.fabricmc.minevkt.jivix.JiviXBase;
import net.minecraft.client.WindowEventHandler;
import net.minecraft.client.WindowSettings;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.util.MonitorTracker;
import net.minecraft.client.util.Window;
import org.lwjgl.glfw.*;
import org.lwjgl.glfw.GLFW.*;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GLCapabilities;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.Functions.*;
import static org.lwjgl.opengl.GL.*;

@Mixin(Window.class)
public class MixinWindow {

    //@Redirect( at = @At(value = "INVOKE", target = "Lorg/lwjgl/opengl/GL;createCapabilities()LGLCapabilities;", remap = false), method = "<init>" ) // doesn't work...
    //@Inject  ( at = @At(value = "INVOKE", target = "Lorg/lwjgl/opengl/GL;createCapabilities()LGLCapabilities;", remap = false), method = "<init>" ) // doesn't work...
    @Inject  ( at = @At("TAIL"), method = "<init>" ) // Inject before `GL.createCapabilities()` doesn't work...
    public void OnGlCreateCapabilities(WindowEventHandler eventHandler, MonitorTracker monitorTracker, WindowSettings settings, @Nullable String videoMode, String title, CallbackInfo ci) {
        System.out.println("Try Inject Before GL.createCapabilities");
        JiviXBase.initializeGL( GetProcAddress ); // GetProcAddress
    };

    // F.U.C.K, Damn IT!
    @Redirect( at = @At(value = "INVOKE", target = "Lorg/lwjgl/glfw/GLFW;glfwWindowHint(II)V", remap = false), method = "<init>" )
    public void OnGlfwWindowHint(int hint, int value){
        System.out.println("Try Redirect GLFW.glfwWindowHint");
        if (hint == 139266) { GLFW.glfwWindowHint(139266, 4); } else
        if (hint == 139267) { GLFW.glfwWindowHint(139267, 6); } else //
        if (hint == 139272) { GLFW.glfwWindowHint(139272, 0x00032002); } else
        { GLFW.glfwWindowHint(hint, value); };
    };

}
