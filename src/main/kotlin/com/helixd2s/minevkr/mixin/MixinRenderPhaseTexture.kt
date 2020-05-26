package com.helixd2s.minevkr.mixin

import com.helixd2s.minevkr.ducks.IETexture
import net.minecraft.client.render.RenderPhase
import net.minecraft.util.Identifier
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.Shadow
import java.util.*

@Mixin(RenderPhase.Texture::class)
abstract class MixinRenderPhaseTexture : IETexture {
    @Shadow var id: Optional<Identifier>? = null

    // Identifier now can be return by `RenderLayer as IETexture`
    override fun id(): Optional<Identifier>? { return id; }
}
