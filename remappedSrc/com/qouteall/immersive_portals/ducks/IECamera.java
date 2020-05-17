package com.qouteall.immersive_portals.ducks;

import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.Vec3d;

public interface IECamera {
    void resetState(Vec3d pos, ClientWorld currWorld);
    
    void mySetPos(Vec3d pos);
    
    float getCameraY();
    
    float getLastCameraY();
    
    void setCameraY(float cameraY, float lastCameraY);
}
