package com.helixd2s.minevkr.mixin

import com.helixd2s.minevkr.ducks.IEBufferBuilder
import net.minecraft.client.render.BufferBuilder
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.Shadow
import java.nio.ByteBuffer

@Mixin(BufferBuilder::class)
abstract class MixinBufferBuilder : IEBufferBuilder {
    @Shadow override var buffer: ByteBuffer? = null
}
