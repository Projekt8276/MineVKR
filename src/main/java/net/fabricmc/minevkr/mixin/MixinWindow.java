package net.fabricmc.minevkr.mixin;

import net.fabricmc.minevkr.jivix.JiviXBase;
import net.minecraft.client.util.Window;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GLCapabilities;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import static org.lwjgl.glfw.GLFW.Functions.*;

@Mixin(Window.class)
public class MixinWindow {

    // Oh... thanks?
    @Redirect( at = @At(value = "INVOKE", target = "Lorg/lwjgl/opengl/GL;createCapabilities()Lorg/lwjgl/opengl/GLCapabilities;", remap = false), method = "<init>" ) // doesn't work...
    private GLCapabilities OnGlCreateCapabilities() {
        System.out.println("Try Inject Before GL.createCapabilities");
        JiviXBase.initializeGL( GetProcAddress ); // GetProcAddress
        return GL.createCapabilities();
    };

    // F.U.C.K, Damn IT!
    @Redirect( at = @At(value = "INVOKE", target = "Lorg/lwjgl/glfw/GLFW;glfwWindowHint(II)V", remap = false), method = "<init>" )
    private void OnGlfwWindowHint(int hint, int value){
        System.out.println("Try Redirect GLFW.glfwWindowHint");
        if (hint == 139266) { GLFW.glfwWindowHint(139266, 4); } else
        if (hint == 139267) { GLFW.glfwWindowHint(139267, 6); } else //
        if (hint == 139272) { GLFW.glfwWindowHint(139272, 0x00032002); } else
        { GLFW.glfwWindowHint(hint, value); };
    };

}
