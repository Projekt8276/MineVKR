package com.helixd2s.minevkr.mixin;

import com.qouteall.immersive_portals.ducks.IEMatrix4f;
import net.minecraft.util.math.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

//mojang does not provide a method to load numbers into matrix
@Mixin(Matrix4f.class)
public class MixinMatrix4f implements IEMatrix4f {
    @Shadow public float a00;
    @Shadow public float a01;
    @Shadow public float a02;
    @Shadow public float a03;
    @Shadow public float a10;
    @Shadow public float a11;
    @Shadow public float a12;
    @Shadow public float a13;
    @Shadow public float a20;
    @Shadow public float a21;
    @Shadow public float a22;
    @Shadow public float a23;
    @Shadow public float a30;
    @Shadow public float a31;
    @Shadow public float a32;
    @Shadow public float a33;
    
    @Override
    public void loadFromArray(float[] arr) {
        a00 = arr[0];
        a01 = arr[1];
        a02 = arr[2];
        a03 = arr[3];
        a10 = arr[4];
        a11 = arr[5];
        a12 = arr[6];
        a13 = arr[7];
        a20 = arr[8];
        a21 = arr[9];
        a22 = arr[10];
        a23 = arr[11];
        a30 = arr[12];
        a31 = arr[13];
        a32 = arr[14];
        a33 = arr[15];
    }
}
