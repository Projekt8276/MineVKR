package com.qouteall.immersive_portals.ducks;

import net.minecraft.client.util.math.Vector4f;
import net.minecraft.util.math.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;

//@Mixin(Vector4f.class)
public interface IEVector4f {
    public void transform(Matrix4f matrix) throws NoSuchFieldException, IllegalAccessException;
}
