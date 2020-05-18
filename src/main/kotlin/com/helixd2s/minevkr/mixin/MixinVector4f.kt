package com.helixd2s.minevkr.mixin

import com.helixd2s.minevkr.ReflectionUtil
import com.qouteall.immersive_portals.ducks.IEVector4f
import net.minecraft.client.util.math.Vector4f
import net.minecraft.util.math.Matrix4f
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.Shadow

@Mixin(Vector4f::class)
class MixinVector4f : IEVector4f {
    @Shadow var x = 0f
    @Shadow var y = 0f
    @Shadow var z = 0f
    @Shadow var w = 0f

    @Throws(NoSuchFieldException::class, IllegalAccessException::class)
    override fun transform(matrix: Matrix4f) {
        val f = x
        val g = y
        val h = z
        val i = w
        x = ReflectionUtil.getFieldFValue(matrix, "a00") * f + ReflectionUtil.getFieldFValue(matrix, "a01") * g + ReflectionUtil.getFieldFValue(matrix, "a02") * h + ReflectionUtil.getFieldFValue(matrix, "a03") * i
        y = ReflectionUtil.getFieldFValue(matrix, "a10") * f + ReflectionUtil.getFieldFValue(matrix, "a11") * g + ReflectionUtil.getFieldFValue(matrix, "a12") * h + ReflectionUtil.getFieldFValue(matrix, "a13") * i
        z = ReflectionUtil.getFieldFValue(matrix, "a20") * f + ReflectionUtil.getFieldFValue(matrix, "a21") * g + ReflectionUtil.getFieldFValue(matrix, "a22") * h + ReflectionUtil.getFieldFValue(matrix, "a23") * i
        w = ReflectionUtil.getFieldFValue(matrix, "a30") * f + ReflectionUtil.getFieldFValue(matrix, "a31") * g + ReflectionUtil.getFieldFValue(matrix, "a32") * h + ReflectionUtil.getFieldFValue(matrix, "a33") * i
    }
}
