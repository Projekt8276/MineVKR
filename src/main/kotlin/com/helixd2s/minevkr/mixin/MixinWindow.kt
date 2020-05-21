package com.helixd2s.minevkr.mixin

import com.helixd2s.jivix.JiviXBase
import com.helixd2s.minevkr.MineVKR
import net.minecraft.client.util.Window
import org.lwjgl.glfw.GLFW
import org.lwjgl.glfw.GLFW.Functions.GetProcAddress
import org.lwjgl.opengl.GL
import org.lwjgl.opengl.GLCapabilities
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Redirect


@Mixin(Window::class)
abstract class MixinWindow {

    // Oh... thanks?
    @Redirect(at = At(value = "INVOKE", target = "Lorg/lwjgl/opengl/GL;createCapabilities()Lorg/lwjgl/opengl/GLCapabilities;", remap = false), method = ["<init>"]) // doesn't work...
    private fun OnGlCreateCapabilities(): GLCapabilities {
        println("Try Inject Before GL.createCapabilities")
        MineVKR.vWindow = this as Window;
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

    // TODO: Add operable Int, UInt, Long, ULong referenced type for Kotlin
    @Redirect(at = At(value = "INVOKE", target = "Lorg/lwjgl/glfw/GLFW;glfwCreateWindow(IILjava/lang/CharSequence;JJ)J", remap = false), method = ["<init>"])
    private fun OnGlfwCreateWindow(width: Int, height: Int, title: CharSequence, monitor: Long, share: Long): Long {
        MineVKR.vWidth = width; MineVKR.vHeight = height
        return GLFW.glfwCreateWindow(MineVKR.vWidth, MineVKR.vHeight, title, monitor, share)
    }

    /*// Not Found
    @Redirect(at = At(value = "INVOKE", target = "Lorg/lwjgl/glfw/GLFW;glfwCreateWindow(IILjava/nio/ByteBuffer;JJ)J", remap = false), method = ["<init>"])
    private fun OnGlfwCreateWindow(width: Int, height: Int, title: ByteBuffer, monitor: Long, share: Long): Long {
        MineVKR.width = width; MineVKR.height = height;
        return GLFW.glfwCreateWindow(width, height, title, monitor, share);
    }*/
}
