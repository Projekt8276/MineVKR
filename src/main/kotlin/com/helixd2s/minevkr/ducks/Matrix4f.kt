package com.helixd2s.minevkr.ducks


//@Mixin(IEMatrix4f.class)
interface IEMatrix4f {
    fun loadFromArray(arr: FloatArray?)
    fun toArray(): FloatArray
}
