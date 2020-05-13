package net.fabricmc.minevkr.mixin;

import com.qouteall.immersive_portals.ducks.IEMatrix4f;
import com.qouteall.immersive_portals.ducks.IEVector4f;
import net.fabricmc.minevkr.ReflectionUtil;
import net.minecraft.client.util.math.Vector4f;
import net.minecraft.util.math.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Vector4f.class)
public class MixinVector4f implements IEVector4f {
    @Shadow public float x;
    @Shadow public float y;
    @Shadow public float z;
    @Shadow public float w;

    @Override
    public void transform(Matrix4f matrix) throws NoSuchFieldException, IllegalAccessException {
        float f = this.x;
        float g = this.y;
        float h = this.z;
        float i = this.w;

        this.x = ReflectionUtil.getFieldFValue(matrix,"a00") * f + ReflectionUtil.getFieldFValue(matrix,"a01") * g + ReflectionUtil.getFieldFValue(matrix,"a02") * h + ReflectionUtil.getFieldFValue(matrix,"a03") * i;
        this.y = ReflectionUtil.getFieldFValue(matrix,"a10") * f + ReflectionUtil.getFieldFValue(matrix,"a11") * g + ReflectionUtil.getFieldFValue(matrix,"a12") * h + ReflectionUtil.getFieldFValue(matrix,"a13") * i;
        this.z = ReflectionUtil.getFieldFValue(matrix,"a20") * f + ReflectionUtil.getFieldFValue(matrix,"a21") * g + ReflectionUtil.getFieldFValue(matrix,"a22") * h + ReflectionUtil.getFieldFValue(matrix,"a23") * i;
        this.w = ReflectionUtil.getFieldFValue(matrix,"a30") * f + ReflectionUtil.getFieldFValue(matrix,"a31") * g + ReflectionUtil.getFieldFValue(matrix,"a32") * h + ReflectionUtil.getFieldFValue(matrix,"a33") * i;
    }

    @Inject(method = "transform", at = @At("HEAD"), cancellable = true)
    public void onTransform(Matrix4f matrix, CallbackInfo ci) throws NoSuchFieldException, IllegalAccessException {
        float f = this.x;
        float g = this.y;
        float h = this.z;
        float i = this.w;

        this.x = ReflectionUtil.getFieldFValue(matrix,"a00") * f + ReflectionUtil.getFieldFValue(matrix,"a01") * g + ReflectionUtil.getFieldFValue(matrix,"a02") * h + ReflectionUtil.getFieldFValue(matrix,"a03") * i;
        this.y = ReflectionUtil.getFieldFValue(matrix,"a10") * f + ReflectionUtil.getFieldFValue(matrix,"a11") * g + ReflectionUtil.getFieldFValue(matrix,"a12") * h + ReflectionUtil.getFieldFValue(matrix,"a13") * i;
        this.z = ReflectionUtil.getFieldFValue(matrix,"a20") * f + ReflectionUtil.getFieldFValue(matrix,"a21") * g + ReflectionUtil.getFieldFValue(matrix,"a22") * h + ReflectionUtil.getFieldFValue(matrix,"a23") * i;
        this.w = ReflectionUtil.getFieldFValue(matrix,"a30") * f + ReflectionUtil.getFieldFValue(matrix,"a31") * g + ReflectionUtil.getFieldFValue(matrix,"a32") * h + ReflectionUtil.getFieldFValue(matrix,"a33") * i;

        ci.cancel(); return;
    }
}
