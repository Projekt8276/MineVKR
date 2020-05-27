package com.helixd2s.minevkr.mixin

import com.helixd2s.minevkr.MineVKR
import com.helixd2s.minevkr.ReflectionUtil
import com.helixd2s.minevkr.ducks.IEMultiPhase
import com.helixd2s.minevkr.ducks.IERenderLayer
import net.minecraft.client.render.BufferBuilder
import net.minecraft.client.render.RenderLayer
import net.minecraft.client.render.RenderPhase
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.Shadow
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Inject
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo

@Mixin(RenderLayer::class)
abstract class MixinRenderLayer : IERenderLayer {

    //
    @Inject(method = ["draw"], at = [At(value = "TAIL")])
    open fun draw(buffer: BufferBuilder, cameraX: Int, cameraY: Int, cameraZ: Int, ci: CallbackInfo) {
        MineVKR.onRenderLayerDraw(this as RenderLayer, buffer, cameraX, cameraY, cameraZ, ci)
    }

    // i.e. need to forced access (used `RenderLayer.MultiPhase` as `RenderLayer`, use as `IERenderLayer`, get access by reflection)
    override fun getPhases(): RenderLayer.MultiPhaseParameters? {
        return ReflectionUtil.getFieldValue(this, "phases");
    }

}
