package net.fabricmc.minertx.jivix;

import org.bytedeco.javacpp.BytePointer;
import org.bytedeco.javacpp.Loader;
import org.bytedeco.javacpp.LongPointer;
import org.bytedeco.javacpp.Pointer;
import org.bytedeco.javacpp.annotation.*;

// TODO: Engine Wrappers for std::shared_ptr, like "vkt::Vector<uint8_t>"
@Platform(include="./include/JiviX/mesh.hpp", link="./lib/vulkan-1")
@Namespace("jvx")

public class Mesh extends Pointer {
    static { Loader.load(); }

    public static class MeshBinding extends Pointer {
        static { Loader.load(); }
        public MeshBinding() { allocate(); }
        private native void allocate();
        public native void createDescriptorSet();
    }

}
