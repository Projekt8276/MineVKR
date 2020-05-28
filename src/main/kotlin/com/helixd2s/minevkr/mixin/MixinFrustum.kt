package com.helixd2s.minevkr.mixin

import net.minecraft.client.render.Frustum
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Inject
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable

@Mixin(Frustum::class)
class MixinFrustum {

    @Inject(method = arrayOf("Lnet/minecraft/client/render/Frustum;isVisible(DDDDDD)Z"), at = arrayOf(At("HEAD")), cancellable = true)
    private fun onIntersectionTest(
            double_1: Double,
            double_2: Double,
            double_3: Double,
            double_4: Double,
            double_5: Double,
            double_6: Double,
            cir: CallbackInfoReturnable<Boolean>
    ) { // Disable Frustum for Ray-Tracers
        cir.setReturnValue(true);
        cir.cancel();
    }

}
