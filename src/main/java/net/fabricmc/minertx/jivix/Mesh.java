package net.fabricmc.minertx.jivix;

import org.bytedeco.javacpp.BytePointer;
import org.bytedeco.javacpp.Loader;
import org.bytedeco.javacpp.LongPointer;
import org.bytedeco.javacpp.Pointer;
import org.bytedeco.javacpp.annotation.*;

// TODO: Engine Wrappers for std::shared_ptr, like "vkt::Vector<uint8_t>"
@Platform(include={
        "./include/JiviX/material.hpp",
        "./include/JiviX/mesh.hpp",
        "./include/JiviX/node.hpp",
}, link="./lib/vulkan-1")
@Namespace("jvx")

//
public class Mesh extends Pointer {
    static { Loader.load(); }

    @Name("jvx::BufferViewSet")
    public static class BufferViewSet extends Pointer {
        static { Loader.load(); }
        public BufferViewSet() { allocate(); }
        private native void allocate();
        public native void createDescriptorSet();
    }

    @Name("jvx::MeshInput")
    public static class MeshInput extends Pointer {
        static { Loader.load(); }
        public MeshInput() { allocate(); }
        private native void allocate();
        public native void createDescriptorSet();
    }

    @Name("jvx::MeshBinding")
    public static class MeshBinding extends Pointer {
        static { Loader.load(); }
        public MeshBinding() { allocate(); }
        private native void allocate();
        public native void createDescriptorSet();
    }

    @Name("jvx::Node")
    public static class Node extends Pointer {
        static { Loader.load(); }
        public Node() { allocate(); }
        private native void allocate();
        public native void createDescriptorSet();
    }

    @Name("jvx::Material")
    public static class Material extends Pointer {
        static { Loader.load(); }
        public Material() { allocate(); }
        private native void allocate();
        public native void createDescriptorSet();
    }
}
