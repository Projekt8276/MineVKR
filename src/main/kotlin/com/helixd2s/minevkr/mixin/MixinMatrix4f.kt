package com.helixd2s.minevkr.mixin

import com.qouteall.immersive_portals.ducks.IEMatrix4f;
import net.minecraft.util.math.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

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

    override fun loadFromArray(arr: FloatArray) {
        a00 = arr[0]
        a01 = arr[1]
        a02 = arr[2]
        a03 = arr[3]
        a10 = arr[4]
        a11 = arr[5]
        a12 = arr[6]
        a13 = arr[7]
        a20 = arr[8]
        a21 = arr[9]
        a22 = arr[10]
        a23 = arr[11]
        a30 = arr[12]
        a31 = arr[13]
        a32 = arr[14]
        a33 = arr[15]
    }
}
