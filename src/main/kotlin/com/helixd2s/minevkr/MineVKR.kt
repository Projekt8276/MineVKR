package com.helixd2s.minevkr

import com.helixd2s.jivix.JiviX
import net.fabricmc.api.ModInitializer
import net.minecraft.client.gl.VertexBuffer
import net.minecraft.client.render.WorldRenderer
import net.minecraft.client.render.chunk.ChunkBuilder
import net.minecraft.client.util.Window
import net.minecraft.util.math.BlockPos
import org.lwjgl.vulkan.VkDevice
import org.lwjgl.vulkan.VkInstance
import org.lwjgl.vulkan.VkPhysicalDevice

open class MineVKR : ModInitializer {

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
        open lateinit var vVertexBuffer: VertexBuffer

        open lateinit var vBlockPos: BlockPos
        open lateinit var vCurrentChunk: ChunkBuilder.BuiltChunk
        open var vCPosition: DoubleArray = doubleArrayOf(0.0, 0.0, 0.0)

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

        @JvmStatic
        open fun vInitializeRenderer() {
            if (!vInitialized) {
                vInitialized = true
                println("This line is printed by an example mod mixin!")

                //
                MineVKR.vInstanceHandle = MineVKR.vDriver.createInstance()
                MineVKR.vInstance = MineVKR.vDriver.createInstanceClass()

                // TODO: Support Other GPU's
                MineVKR.vPhysicalDevice = MineVKR.vDriver.createPhysicalDeviceClass()
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
            }
        }
    }

    @Override
    override fun onInitialize() {
        MineVKR.vDriver = JiviX.Driver()
        MineVKR.vCPosition = doubleArrayOf(0.0, 0.0, 0.0)
    }

}
