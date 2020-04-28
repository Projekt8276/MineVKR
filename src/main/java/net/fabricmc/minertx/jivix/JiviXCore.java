package net.fabricmc.minertx.jivix;

import org.bytedeco.javacpp.IntPointer;
import org.bytedeco.javacpp.BytePointer;
import org.bytedeco.javacpp.FloatPointer;
import org.bytedeco.javacpp.Loader;
import org.bytedeco.javacpp.LongPointer;
import org.bytedeco.javacpp.Pointer;
import org.bytedeco.javacpp.annotation.*;

// TODO: Engine Wrappers for std::shared_ptr, like "vkt::Vector<uint8_t>"
@Platform(include={
        "./include/vkt2/fw.hpp",
        "./include/JiviX/JVI/JiviX.hpp"
}, link="./lib/vulkan-1", define={"ENABLE_OPENGL_INTEROP","WIN32","OS_WIN","VK_ENABLE_BETA_EXTENSIONS","VK_USE_PLATFORM_WIN32_KHR","VMA_IMPLEMENTATION","SHARED_PTR_NAMESPACE std", "UNIQUE_PTR_NAMESPACE std"})

@Name("")
public class JiviXCore extends Pointer {
    static { Loader.load(); }

    @Name("vkt::VmaImageAllocation")
    public static class VmaImagellocation extends Pointer {
        static { Loader.load(); }

        public VmaImagellocation() { allocate(); }
        private native void allocate();

        public VmaImagellocation(@Cast("VmaAllocator*") long vmaAllocator, @Cast("vkh::VkImageCreateInfo*") long createInfo, @Cast("VmaMemoryUsage") int vmaUsage) { allocate(vmaAllocator, createInfo, vmaUsage); };
        private native void allocate(@Cast("VmaAllocator*") long vmaAllocator, @Cast("vkh::VkImageCreateInfo*") long createInfo, @Cast("VmaMemoryUsage") int vmaUsage);
    };

    @Name("vkt::VmaBufferAllocation")
    public static class VmaBufferAllocation extends Pointer {
        static { Loader.load(); }

        public VmaBufferAllocation() { allocate(); }
        private native void allocate();

        public VmaBufferAllocation(@Cast("VmaAllocator*") long vmaAllocator, @Cast("vkh::VkBufferCreateInfo*") long createInfo, @Cast("VmaMemoryUsage") int vmaUsage) { allocate(vmaAllocator, createInfo, vmaUsage); };
        private native void allocate(@Cast("VmaAllocator*") long vmaAllocator, @Cast("vkh::VkBufferCreateInfo*") long createInfo, @Cast("VmaMemoryUsage") int vmaUsage);
    };

    @Name("jvi::Context")
    public static class Context extends Pointer {
        static { Loader.load(); }

        public Context() { allocate(); }
        private native void allocate();
    };

    @Name("jvi::Thread")
    public static class Thread extends Pointer {
        static { Loader.load(); }

        public Thread() { allocate(); }
        private native void allocate();
    };

    @Name("jvi::Driver")
    public static class Driver extends Pointer {
        static { Loader.load(); }

        public Driver() { allocate(); }
        private native void allocate();
    };

    @Name("jvi::BufferViewSet")
    public static class BufferViewSet extends Pointer {
        static { Loader.load(); }

        public BufferViewSet() { allocate(); }
        private native void allocate();

        public BufferViewSet(Context context) { allocate(context); }
        private native void allocate(Context context);

    };

    @Name("jvi::MeshInput")
    public static class MeshInput extends Pointer {
        static { Loader.load(); }

        public MeshInput(Context context) { allocate(context); }
        private native void allocate(Context context);

        public MeshInput() { allocate(); }
        private native void allocate();

    };

    @Name("jvi::MeshBinding")
    public static class MeshBinding extends Pointer {
        static { Loader.load(); }

        public MeshBinding() { allocate(); }
        private native void allocate();

        public MeshBinding(Context context) { allocate(context); }
        private native void allocate(Context context);

    };

    @Name("jvi::Node")
    public static class Node extends Pointer {
        static { Loader.load(); }

        public Node() { allocate(); }
        private native void allocate();

        public Node(Context context) { allocate(context); }
        private native void allocate(Context context);

    };

    @Name("jvi::Material")
    public static class Material extends Pointer {
        static { Loader.load(); }

        public Material() { allocate(); }
        private native void allocate();

        public Material(Context context) { allocate(context); }
        private native void allocate(Context context);

    };

    @Name("jvi::Renderer")
    public static class Renderer extends Pointer {
        static { Loader.load(); }

        public Renderer() { allocate(); }
        private native void allocate();

        public Renderer(Context context) { allocate(context); }
        private native void allocate(Context context);

    };

    @Name("jvi::MaterialUnit")
    public static class MaterialUnit extends Pointer {
        static { Loader.load(); }

        public MaterialUnit() { allocate(); }
        private native void allocate();

        // Channels
        private native @Cast("float*") @ByRef FloatPointer diffuse();
        private native void diffuse(@Cast("glm::vec4&") @ByRef FloatPointer CL);

        // Channels
        private native @Cast("float*") @ByRef FloatPointer specular();
        private native void specular(@Cast("glm::vec4&") @ByRef FloatPointer CL);

        // Channels
        private native @Cast("float*") @ByRef FloatPointer normals();
        private native void normals(@Cast("glm::vec4&") @ByRef FloatPointer CL);

        // Channels
        private native @Cast("float*") @ByRef FloatPointer emission();
        private native void emission(@Cast("glm::vec4&") @ByRef FloatPointer CL);

        // Channels
        private native @ByVal int diffuseTexture(); private native void diffuseTexture(@ByVal int CL);
        private native @ByVal int specularTexture(); private native void specularTexture(@ByVal int CL);
        private native @ByVal int normalsTexture(); private native void normalsTexture(@ByVal int CL);
        private native @ByVal int emissionTexture(); private native void emissionTexture(@ByVal int CL);
    };

    @Name("vkh::VsGeometryInstance")
    public static class VsGeometryInstance extends Pointer {
        static { Loader.load(); }

        public VsGeometryInstance() { allocate(); }
        private native void allocate();

        // Transform
        private native @Cast("float*") @ByRef FloatPointer transform();
        private native void transform(@Cast("glm::mat3x4&") @ByRef FloatPointer transformPtr);

        // Mask
        private native @ByVal byte mask();
        private native void mask(@ByVal byte MS);

        // Flags
        private native @ByVal byte flags();
        private native void flags(@ByVal byte FG);

        // Custom Index
        private native @ByVal int instanceId();
        private native void instanceId(@ByVal int ID);

        // Native Offset
        private native @ByVal int instanceOffset();
        private native void instanceOffset(@ByVal int OF);
    };

};
