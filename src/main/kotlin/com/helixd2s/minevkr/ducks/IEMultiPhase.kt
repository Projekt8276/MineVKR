package com.helixd2s.minevkr.ducks

import net.minecraft.client.render.RenderPhase

interface IEMultiPhase {
    open var texture: RenderPhase.Texture?;
}
