package com.helixd2s.minevkr.mixin

import com.helixd2s.minevkr.ducks.IEMultiPhase
import net.minecraft.client.render.RenderLayer
import net.minecraft.client.render.RenderPhase
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.Shadow

//
@Mixin(RenderLayer.MultiPhaseParameters::class)
abstract class MixinRenderMultiPhase : IEMultiPhase {
    @Shadow open var texture: RenderPhase.Texture? = null

    override fun texture(): RenderPhase.Texture? { return texture; }
}
