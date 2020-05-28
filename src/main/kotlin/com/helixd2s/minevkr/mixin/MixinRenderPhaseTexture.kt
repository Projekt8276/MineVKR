package com.helixd2s.minevkr.mixin

import com.helixd2s.minevkr.ducks.IETexture
import net.minecraft.client.render.RenderPhase
import net.minecraft.util.Identifier
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.Shadow
import java.util.*

@Mixin(RenderPhase.Texture::class)
abstract class MixinRenderPhaseTexture : IETexture {
    @Shadow override var id: Optional<Identifier>? = null
}
