package com.helixd2s.minevkr.mixin

import com.helixd2s.minevkr.MineVKR
import net.minecraft.client.texture.AbstractTexture
import net.minecraft.client.texture.TextureManager
import net.minecraft.util.Identifier
import org.lwjgl.vulkan.VK10.*
import org.lwjgl.vulkan.VkSamplerCreateInfo
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Inject
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo

@Mixin(TextureManager::class)
class MixinTextureManager {

    // Use Vulkan API image for official registry (i.e. remap from TextureManager)
    @Inject(method = ["registerTexture"], at = [At(value = "TAIL")])
    open fun onRegisterTexture(identifier: Identifier, abstractTexture: AbstractTexture, ci: CallbackInfo) {
        println("Register with GL Texture[${abstractTexture.glId}]...")
        var image = MineVKR.GLStuff.vTexVkMap.get(Integer(abstractTexture.glId));

        if (image != null) {
            var sampler = longArrayOf(0)
            vkCreateSampler(MineVKR.vDevice, VkSamplerCreateInfo.create().also{
                it.magFilter(VK_FILTER_NEAREST)
                it.minFilter(VK_FILTER_NEAREST)
                it.addressModeU(VK_SAMPLER_ADDRESS_MODE_REPEAT)
                it.addressModeV(VK_SAMPLER_ADDRESS_MODE_REPEAT)
                it.unnormalizedCoordinates(false)
            }, null, sampler)

            println("Managed Texture Mapped with Materials Hub")
            if (image.descriptor != null) {
                MineVKR.GLStuff.vIdxMtMap.plus(Pair(identifier, image.descriptor?.sampler(sampler[0])?.address()?.toULong()?.let { MineVKR.vMaterials.pushSampledImage(it) }))
            }
        } else {
            println("Not Managed Texture By Vulkan API")
        }
    }

}
