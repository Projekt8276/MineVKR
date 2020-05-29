package com.helixd2s.minevkr.mixin

import com.helixd2s.minevkr.ducks.IERenderPhase
import com.helixd2s.minevkr.ducks.IETexture
import net.minecraft.client.render.RenderPhase
import net.minecraft.util.Identifier
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.Shadow
import java.util.*

@Mixin(RenderPhase::class)
abstract class MixinRenderPhase : IERenderPhase {
    @Shadow override var name: String? = null
}
