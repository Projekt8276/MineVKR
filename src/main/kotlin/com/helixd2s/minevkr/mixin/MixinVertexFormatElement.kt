package com.helixd2s.minevkr.mixin

import com.helixd2s.minevkr.ducks.IEFormat
import net.minecraft.client.render.VertexFormatElement
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.Shadow

@Mixin(VertexFormatElement::class)
class MixinVertexFormatElement : IEFormat {
    @Shadow var count = 0;

    override fun count(): Int {
        return this.count;
    }
}
