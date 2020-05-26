package com.helixd2s.minevkr.mixin

import com.helixd2s.minevkr.ducks.IETexture
import net.minecraft.client.render.RenderPhase
import net.minecraft.util.Identifier
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.Shadow
import java.util.*

@Mixin(RenderPhase::class)
abstract class MixinRenderPhase {

    @Mixin(RenderPhase.Texture::class)
    abstract class MixinTexture : IETexture {
        @Shadow lateinit var id: Optional<Identifier>

        // Identifier now can be return by `RenderLayer as IETexture`
        override fun id(): Optional<Identifier> { return id; }
    }

}
