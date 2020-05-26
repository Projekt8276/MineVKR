package com.helixd2s.minevkr.ducks

import net.minecraft.client.render.RenderLayer

interface IERenderLayer {
    open fun phases(): RenderLayer.MultiPhaseParameters?;
}
