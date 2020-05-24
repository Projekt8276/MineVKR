package com.helixd2s.minevkr.mixin

import com.helixd2s.minevkr.ducks.IEFormat
import it.unimi.dsi.fastutil.ints.IntList
import net.minecraft.client.render.VertexFormat
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.Shadow

@Mixin(VertexFormat::class)
class MixinVertexFormat : IEFormat {
    @Shadow private lateinit var offsets: IntList

    override fun offsets(): IntList {
        return offsets;
    }
}
