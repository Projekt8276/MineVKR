package com.helixd2s.minevkr.mixin

import com.helixd2s.jivix.JiviXBase
import com.helixd2s.minevkr.MineVKR
import net.minecraft.client.util.Window
//import org.lwjgl.glfw.GLFW
import org.lwjgl.glfw.GLFW.*
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
        MineVKR.vWindow = this as Window
        JiviXBase.initializeGL(GetProcAddress) // GetProcAddress
        MineVKR.vInitializeDriver()
        return GL.createCapabilities()
    }

    // F.U.C.K, Damn IT!
    @Redirect(at = At(value = "INVOKE", target = "Lorg/lwjgl/glfw/GLFW;glfwWindowHint(II)V", remap = false), method = ["<init>"])
    private fun OnGlfwWindowHint(hint: Int, value: Int) {
        println("Try Redirect GLFW.glfwWindowHint")
        if (hint == 139266) { glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 4) } else
        if (hint == 139267) { glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 6) } else
        if (hint == 139272) {
            glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_COMPAT_PROFILE)
            //glfwWindowHint(GLFW_CONTEXT_CREATION_API, GLFW_NATIVE_CONTEXT_API)
        } else
        { glfwWindowHint(hint, value) }
    }

    // TODO: Add operable Int, UInt, Long, ULong referenced type for Kotlin
    @Redirect(at = At(value = "INVOKE", target = "Lorg/lwjgl/glfw/GLFW;glfwCreateWindow(IILjava/lang/CharSequence;JJ)J", remap = false), method = ["<init>"])
    private fun OnGlfwCreateWindow(width: Int, height: Int, title: CharSequence, monitor: Long, share: Long): Long {
        //MineVKR.vWidth = width; MineVKR.vHeight = height
        println("GLFW Create Window Redirected...")
        return glfwCreateWindow(width, height, title, monitor, share)
    }
}
