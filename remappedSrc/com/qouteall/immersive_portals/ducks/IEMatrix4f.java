package com.qouteall.immersive_portals.ducks;

import org.spongepowered.asm.mixin.Mixin;
import net.minecraft.util.math.Matrix4f;

//@Mixin(IEMatrix4f.class)
public interface IEMatrix4f {
    void loadFromArray(float[] arr);
}
