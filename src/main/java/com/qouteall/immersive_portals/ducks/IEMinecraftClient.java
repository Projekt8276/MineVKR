package com.qouteall.immersive_portals.ducks;

import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.render.WorldRenderer;

public interface IEMinecraftClient {
    void setFrameBuffer(Framebuffer buffer);
    
    Screen getCurrentScreen();
    
    void setWorldRenderer(WorldRenderer r);
}
