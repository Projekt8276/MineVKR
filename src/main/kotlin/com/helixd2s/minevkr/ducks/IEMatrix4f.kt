package com.helixd2s.minevkr.ducks

interface IEMatrix4f {
    fun loadFromArray(arr: FloatArray?)
    fun toArray(): FloatArray
}
