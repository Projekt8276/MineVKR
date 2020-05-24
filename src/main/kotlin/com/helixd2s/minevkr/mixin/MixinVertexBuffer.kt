package com.helixd2s.minevkr.mixin

import com.helixd2s.minevkr.ducks.IEVBuffer
import net.minecraft.client.gl.VertexBuffer
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.Shadow

@Mixin(VertexBuffer::class)
abstract class MixinVertexBuffer : IEVBuffer {
    @Shadow private var vertexCount = 0

    override fun vertexCount(): Int { return vertexCount }
}
