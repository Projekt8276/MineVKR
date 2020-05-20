package com.helixd2s.minevkr.mixin


import com.helixd2s.minevkr.ducks.IEMatrix4f
import net.minecraft.util.math.Matrix4f
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.Shadow

//mojang does not provide a method to load numbers into matrix
@Mixin(Matrix4f::class)
class MixinMatrix4f : IEMatrix4f {
    @Shadow var a00 = 0f
    @Shadow var a01 = 0f
    @Shadow var a02 = 0f
    @Shadow var a03 = 0f
    @Shadow var a10 = 0f
    @Shadow var a11 = 0f
    @Shadow var a12 = 0f
    @Shadow var a13 = 0f
    @Shadow var a20 = 0f
    @Shadow var a21 = 0f
    @Shadow var a22 = 0f
    @Shadow var a23 = 0f
    @Shadow var a30 = 0f
    @Shadow var a31 = 0f
    @Shadow var a32 = 0f
    @Shadow var a33 = 0f

    // TODO: Dynamic Array Size (12x or 16x)
    override fun loadFromArray(arr: FloatArray?) {
        a00 = arr?.get(0) ?: 1.0F
        a01 = arr?.get(1) ?: 0.0F
        a02 = arr?.get(2) ?: 0.0F
        a03 = arr?.get(3) ?: 0.0F
        a10 = arr?.get(4) ?: 0.0F
        a11 = arr?.get(5) ?: 1.0f
        a12 = arr?.get(6) ?: 0.0F
        a13 = arr?.get(7) ?: 0.0F
        a20 = arr?.get(8) ?: 0.0F
        a21 = arr?.get(9) ?: 0.0F
        a22 = arr?.get(10) ?: 1.0F
        a23 = arr?.get(11) ?: 0.0F
        a30 = arr?.get(12) ?: 0.0F
        a31 = arr?.get(13) ?: 0.0F
        a32 = arr?.get(14) ?: 0.0F
        a33 = arr?.get(15) ?: 1.0F
    }

    override fun toArray(): FloatArray {
        return floatArrayOf(
                a00, a01, a02, a03,
                a10, a11, a12, a13,
                a20, a21, a22, a23,
                a30, a31, a32, a33
        )
    }
}
