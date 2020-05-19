package com.helixd2s.minevkr.mixin

import com.helixd2s.jivix.JiviXBase
import net.minecraft.client.util.Window
import org.lwjgl.glfw.GLFW
import org.lwjgl.glfw.GLFW.Functions.GetProcAddress
import org.lwjgl.opengl.GL
import org.lwjgl.opengl.GLCapabilities
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Redirect


@Mixin(Window::class)
class MixinWindow {
    // Oh... thanks?
    @Redirect(at = At(value = "INVOKE", target = "Lorg/lwjgl/opengl/GL;createCapabilities()Lorg/lwjgl/opengl/GLCapabilities;", remap = false), method = ["<init>"]) // doesn't work...
    private fun OnGlCreateCapabilities(): GLCapabilities {
        println("Try Inject Before GL.createCapabilities")
        JiviXBase.initializeGL(GetProcAddress) // GetProcAddress
        return GL.createCapabilities()
    }

    // F.U.C.K, Damn IT!
    @Redirect(at = At(value = "INVOKE", target = "Lorg/lwjgl/glfw/GLFW;glfwWindowHint(II)V", remap = false), method = ["<init>"])
    private fun OnGlfwWindowHint(hint: Int, value: Int) {
        println("Try Redirect GLFW.glfwWindowHint")
        if (hint == 139266) { GLFW.glfwWindowHint(139266, 4) } else      // GLFW_CONTEXT_VERSION_MAJOR
        if (hint == 139267) { GLFW.glfwWindowHint(139267, 6) } else      // GLFW_CONTEXT_VERSION_MINOR
        if (hint == 139272) { GLFW.glfwWindowHint(139272, 204802) } else // GLFW_OPENGL_PROFILE: GLFW_OPENGL_COMPAT_PROFILE
        { GLFW.glfwWindowHint(hint, value) }
    }
}
