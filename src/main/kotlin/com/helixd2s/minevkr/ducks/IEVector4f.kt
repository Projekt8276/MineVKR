package com.helixd2s.minevkr.ducks

import net.minecraft.util.math.Matrix4f

interface IEVector4f {
    @Throws(NoSuchFieldException::class, IllegalAccessException::class)
    fun transform(matrix: Matrix4f)
    fun loadFromArray(arr: FloatArray?)
    fun toArray(): FloatArray
}
