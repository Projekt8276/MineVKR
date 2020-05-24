package com.helixd2s.minevkr

import com.helixd2s.jivix.JiviX
import com.helixd2s.minevkr.ducks.IEFormat
import com.helixd2s.minevkr.ducks.IEFormatElement
import com.helixd2s.minevkr.ducks.IEVBuffer
import net.fabricmc.api.ModInitializer
import net.minecraft.client.gl.VertexBuffer
import net.minecraft.client.render.*
import net.minecraft.client.render.chunk.ChunkBuilder
import net.minecraft.client.util.Window
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Matrix4f
import org.bytedeco.javacpp.Pointer
import org.lwjgl.opengl.GL30.glBeginTransformFeedback
import org.lwjgl.opengl.GL30.glEndTransformFeedback
import org.lwjgl.opengl.GL32.*
import org.lwjgl.opengl.GL44.GL_CLIENT_STORAGE_BIT
import org.lwjgl.opengl.GL44.GL_DYNAMIC_STORAGE_BIT
import org.lwjgl.opengl.GL44.glBufferStorage
import org.lwjgl.vulkan.VkDevice
import org.lwjgl.vulkan.VkInstance
import org.lwjgl.vulkan.VkPhysicalDevice
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo
import java.io.File

open class MineVKR : ModInitializer {

    object CurrentChunk {
        open lateinit var vVertexBuffer: VertexBuffer
        open lateinit var vBlockPos: BlockPos
        open lateinit var vCurrentChunk: ChunkBuilder.BuiltChunk
        open lateinit var vVertexFormat: VertexFormat
        open var vCPosition: DoubleArray = doubleArrayOf(0.0, 0.0, 0.0)
    }

    object GLStuff { // For OpenGL needs pointable objects
        open var vTransformFeedbackVertexShader: UIntArray = uintArrayOf(0u);
        open var vTransformFeedbackGeometryShader: UIntArray = uintArrayOf(0u);
        open var vTransformFeedbackProgram: UIntArray = uintArrayOf(0u);

        open var vQuadTransformFeedbackVertexShader: UIntArray = uintArrayOf(0u);
        open var vQuadTransformFeedbackGeometryShader: UIntArray = uintArrayOf(0u);
        open var vQuadTransformFeedbackProgram: UIntArray = uintArrayOf(0u);

        open fun vCreateShader(shader: UIntArray, type: Int, path: String): UIntArray {
            var success = intArrayOf(0)
            return shader
                    .also { it[0] = glCreateShader(type).toUInt() }
                    .also {
                        glShaderSource(it[0].toInt(), File(path).readText())
                        glCompileShader(it[0].toInt())
                        var success = intArrayOf(0).also { ms -> glGetShaderiv(it[0].toInt(), GL_COMPILE_STATUS, ms) }
                        if (success[0] == 0) {
                            println(glGetShaderInfoLog(it[0].toInt(), 512))
                            error("LOL")
                        }
                    }
            // glGetShaderiv(vertexShader, GL_COMPILE_STATUS, &success);
        }
    }

    companion object {
        open lateinit var vWindow: Window;
        open lateinit var vWorldRenderer: WorldRenderer;
        open lateinit var vRenderer: JiviX.Renderer

        ///open lateinit var window: Window;
        open var vWidth = 1600
        open var vHeight = 1200

        open var vPhysicalDeviceHandle = 0UL
        open lateinit var vPhysicalDevice: VkPhysicalDevice

        open var vDeviceHandle = 0UL
        open lateinit var vDevice: VkDevice

        open var vInstanceHandle = 0UL
        open lateinit var vInstance: VkInstance

        //open var vInitialized by Delegates.notNull<Boolean>();
        open var vInitialized: Boolean = false

        //
        open var vGLTestBuffer = intArrayOf(0);

        //
        open lateinit var vDriver: JiviX.Driver
        open lateinit var vContext: JiviX.Context
        open lateinit var vMaterials: JiviX.Material
        open var vBindingsEntity = arrayOf<JiviX.MeshBinding>()
        open var vBindingsChunksOpaque = arrayOf<JiviX.MeshBinding>()
        open var vBindingsChunksTranslucent = arrayOf<JiviX.MeshBinding>()
        open var vBindingsChunksCutout = arrayOf<JiviX.MeshBinding>()

        //
        open var vNode = arrayOf<JiviX.Node>()

        // TODO: Settings For Constants
        const val vMaxChunkBindings = 16
        const val vMaxEntityBindings = 16
        const val vMaxEntityParts = 16


        //
        open fun vRenderBegin(matrices: MatrixStack?, tickDelta: Float, limitTime: Long, renderBlockOutline: Boolean, camera: Camera?, gameRenderer: GameRenderer?, lightmapTextureManager: LightmapTextureManager?, matrix4f: Matrix4f?, ci: CallbackInfo) {
            MineVKR.vMaterials.resetMaterials()     // TODO: Static Material
            MineVKR.vMaterials.resetSampledImages() // TODO: Static Texture
        }

        //
        open fun vRenderEnd(matrices: MatrixStack?, tickDelta: Float, limitTime: Long, renderBlockOutline: Boolean, camera: Camera?, gameRenderer: GameRenderer?, lightmapTextureManager: LightmapTextureManager?, matrix4f: Matrix4f?, ci: CallbackInfo) {

        }

        //
        open fun vChunkDraw(renderLayer: RenderLayer, matrixStack: MatrixStack, d: Double, e: Double, f: Double, ci: CallbackInfo) {
            // For View Only
            /*
            val vertexBuffer: VertexBuffer = MineVKR.CurrentChunk.vCurrentChunk.getBuffer(renderLayer)
            matrixStack.push()
            val blockPos: BlockPos = MineVKR.CurrentChunk.vCurrentChunk.getOrigin()
            matrixStack.translate(blockPos.x.toDouble() - d, blockPos.y.toDouble() - e, blockPos.z.toDouble() - f)
            vertexBuffer.bind()
            MineVKR.CurrentChunk.vVertexFormat.startDrawing(0L)
            vertexBuffer.draw(matrixStack.peek().model, 7)
            matrixStack.pop()
            */

            val vertexBuffer: VertexBuffer = MineVKR.CurrentChunk.vCurrentChunk.getBuffer(renderLayer)


            glUseProgram(GLStuff.vQuadTransformFeedbackProgram[0].toInt())

            vertexBuffer.bind() // EXPERIMENTAL ONLY!
            for (id in 0 until MineVKR.CurrentChunk.vVertexFormat.elements.size) {
                var element = MineVKR.CurrentChunk.vVertexFormat.elements[id];
                var offset = (MineVKR.CurrentChunk.vVertexFormat as IEFormat).offsets().getInt(id);
                if (element.type.name == "Position") { glVertexAttribPointer(0, (element as IEFormatElement).count(), element.format.glId, false, MineVKR.CurrentChunk.vVertexFormat.vertexSize, offset.toLong()) }
                if (element.type.name == "UV"      ) { glVertexAttribPointer(1, (element as IEFormatElement).count(), element.format.glId, false, MineVKR.CurrentChunk.vVertexFormat.vertexSize, offset.toLong()) }
            }

            // TODO: Correct Working `I` of `vBindingsChunksOpaque[I]`
            //println("What is: GL-Buffers[" + vBindingsChunksOpaque[0].bindingBufferGL().toInt() + "] ?") // Only For DEBUG!
            glBindBufferRange(GL_TRANSFORM_FEEDBACK_BUFFER, 0, vBindingsChunksOpaque[0].bindingBufferGL().toInt(), 0L, 80L*(vertexBuffer as IEVBuffer).vertexCount())
            //glBindBufferRange(GL_TRANSFORM_FEEDBACK_BUFFER, 0, vGLTestBuffer[0], 0L, 80L*(vertexBuffer as IEVBuffer).vertexCount())

            //
            glBeginTransformFeedback(GL_TRIANGLES)
            glEnable(GL_RASTERIZER_DISCARD)
            vertexBuffer.draw(matrixStack.peek().model, GL_LINES_ADJACENCY)
            glDisable(GL_RASTERIZER_DISCARD)
            glEndTransformFeedback()

            //
            //glDisable(GL_RASTERIZER_DISCARD)
            glUseProgram(0)
        }

        @JvmStatic
        open fun vInitializeRenderer() {
            if (!vInitialized) {
                vInitialized = true
                println("This line is printed by an example mod mixin!")

                //
                MineVKR.vInstanceHandle = MineVKR.vDriver.createInstance()
                MineVKR.vInstance = MineVKR.vDriver.instanceClass()

                // TODO: Support Other GPU's
                MineVKR.vPhysicalDevice = MineVKR.vDriver.physicalDeviceClass()
                MineVKR.vPhysicalDeviceHandle = MineVKR.vDriver.physicalDevice()

                //
                MineVKR.vDevice = MineVKR.vDriver.createDevice(MineVKR.vPhysicalDevice)
                MineVKR.vDeviceHandle = MineVKR.vDriver.device()

                //
                MineVKR.vContext = JiviX.Context(MineVKR.vDriver)
                MineVKR.vMaterials = JiviX.Material(MineVKR.vContext)
                MineVKR.vNode = arrayOf(JiviX.Node(MineVKR.vContext)) // TODO: Node Max Instance Count Support
                MineVKR.vRenderer = JiviX.Renderer(MineVKR.vContext)

                //
                MineVKR.vRenderer.linkMaterial(MineVKR.vMaterials)
                MineVKR.vRenderer.linkNode(MineVKR.vNode[0])
                println("Link Node and Materials...")

                //
                MineVKR.vContext.initialize(MineVKR.vWidth.toUInt(), MineVKR.vHeight.toUInt())
                println("Initialize Context...")

                //
                MineVKR.vBindingsChunksOpaque = Array<JiviX.MeshBinding>(vMaxChunkBindings) { _ -> JiviX.MeshBinding(MineVKR.vContext, 2048UL); }
                MineVKR.vBindingsChunksCutout = Array<JiviX.MeshBinding>(vMaxChunkBindings) { _ -> JiviX.MeshBinding(MineVKR.vContext, 2048UL); }
                MineVKR.vBindingsChunksTranslucent = Array<JiviX.MeshBinding>(vMaxChunkBindings) { _ -> JiviX.MeshBinding(MineVKR.vContext, 2048UL); }
                println("Create chunk bindings itself...")

                //
                for (element in vBindingsChunksOpaque) MineVKR.vNode[0].pushMesh(element)
                for (element in vBindingsChunksCutout) MineVKR.vNode[0].pushMesh(element)
                for (element in vBindingsChunksTranslucent) MineVKR.vNode[0].pushMesh(element)
                println("Add chunk bindings into Node...")

                //
                var vPartsSize = ULongArray(vMaxEntityParts) { _ -> 32UL }
                MineVKR.vBindingsEntity = Array<JiviX.MeshBinding>(vMaxEntityBindings) { _ -> JiviX.MeshBinding(MineVKR.vContext, 512UL, vPartsSize) }
                println("Create entity bindings itself...")

                //
                for (element in vBindingsEntity) MineVKR.vNode[0].pushMesh(element)
                println("Add entity bindings into Node...")

                //
                var varyings = arrayOf<CharSequence>("fPosition", "fTexcoord", "fNormal", "fTangent", "fBinormal");
                GLStuff.vCreateShader(GLStuff.vTransformFeedbackVertexShader, GL_VERTEX_SHADER, "./gl-shaders/tf.vert")
                GLStuff.vCreateShader(GLStuff.vTransformFeedbackGeometryShader, GL_GEOMETRY_SHADER, "./gl-shaders/tf.geom")

                //
                GLStuff.vTransformFeedbackProgram[0] = glCreateProgram().toUInt()
                var programID = GLStuff.vTransformFeedbackProgram[0].toInt()
                glTransformFeedbackVaryings(programID, varyings, GL_INTERLEAVED_ATTRIBS)
                glAttachShader(programID, GLStuff.vTransformFeedbackVertexShader[0].toInt())
                glAttachShader(programID, GLStuff.vTransformFeedbackGeometryShader[0].toInt())
                glLinkProgram(programID)
                var success = intArrayOf(0).also { glGetProgramiv(programID, GL_LINK_STATUS, it) }
                if (success[0] == 0) { println(glGetProgramInfoLog(programID, 512)); error("LOL") }

                //
                GLStuff.vCreateShader(GLStuff.vQuadTransformFeedbackVertexShader, GL_VERTEX_SHADER, "./gl-shaders/tf-quad.vert")
                GLStuff.vCreateShader(GLStuff.vQuadTransformFeedbackGeometryShader, GL_GEOMETRY_SHADER, "./gl-shaders/tf-quad.geom")
                println("GL Transform Feedback Shaders Created!")

                //
                //var programID = 0; var success = intArrayOf(0); // used only for debug
                GLStuff.vQuadTransformFeedbackProgram[0] = glCreateProgram().toUInt()
                programID = GLStuff.vQuadTransformFeedbackProgram[0].toInt()
                glTransformFeedbackVaryings(programID, varyings, GL_INTERLEAVED_ATTRIBS)
                glAttachShader(programID, GLStuff.vQuadTransformFeedbackVertexShader[0].toInt())
                glAttachShader(programID, GLStuff.vQuadTransformFeedbackGeometryShader[0].toInt())
                glLinkProgram(programID)
                success = intArrayOf(0).also { glGetProgramiv(programID, GL_LINK_STATUS, it) }
                if (success[0] == 0) { println(glGetProgramInfoLog(programID, 512)); error("LOL") }
                println("GL Transform Feedback Programs Created!")

                // For Test ONLY!
                glGenBuffers(vGLTestBuffer)
                glBindBuffer(GL_TRANSFORM_FEEDBACK_BUFFER, vGLTestBuffer[0])
                glBufferStorage(GL_TRANSFORM_FEEDBACK_BUFFER, 80 * 128 * 6, GL_CLIENT_STORAGE_BIT.or(GL_DYNAMIC_STORAGE_BIT))

                // For Save...
                // JavaCPP Pointer For Kotlin (By Address)
                //val p: Pointer = object : Pointer() { init { address = 0xDEADBEEFL } }
            }
        }
    }

    @Override
    override fun onInitialize() {
        MineVKR.vDriver = JiviX.Driver()
    }

}
