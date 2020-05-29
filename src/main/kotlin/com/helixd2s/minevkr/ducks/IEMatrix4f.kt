package com.helixd2s.minevkr.ducks

import net.minecraft.util.math.Matrix4f

interface IEMatrix4f {
    fun loadFromArray(arr: FloatArray?)
    fun toArray(): FloatArray
}
