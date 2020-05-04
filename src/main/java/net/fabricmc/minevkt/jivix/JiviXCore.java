package net.fabricmc.minevkt.jivix;

import org.bytedeco.javacpp.IntPointer;
import org.bytedeco.javacpp.BytePointer;
import org.bytedeco.javacpp.FloatPointer;
import org.bytedeco.javacpp.Loader;
import org.bytedeco.javacpp.LongPointer;
import org.bytedeco.javacpp.Pointer;
import org.bytedeco.javacpp.annotation.*;

// "jniJiviXBase", "JiviX"

@Platform(include={
        "./include/GLFW/glfw3.h",
        "./include/vkt2/fw.hpp",
        "./include/JiviX/JiviX.hpp",
}, link={"vulkan-1","glfw","glbinding","glbinding-aux"}, define={"ENABLE_OPENGL_INTEROP","WIN32","OS_WIN","VK_ENABLE_BETA_EXTENSIONS","VK_USE_PLATFORM_WIN32_KHR","VMA_IMPLEMENTATION","SHARED_PTR_NAMESPACE std", "UNIQUE_PTR_NAMESPACE std"})
@Name("") //
public class JiviXCore extends Pointer {
    static { Loader.load(); }

    @Name("vkt::ImageAllocation")
    public static class ImageAllocation extends Pointer {
        static { Loader.load(); }
        public ImageAllocation(Pointer p) { super(p); }

        public ImageAllocation(@SharedPtr ImageAllocation alloc) { allocate(alloc); };
        private native void allocate(@SharedPtr ImageAllocation alloc);

        public ImageAllocation(@Cast("vkt::MemoryAllocationInfo*") long allocationInfoAddress, @Cast("vkh::VkImageCreateInfo*") long createInfoAddress) { allocate(allocationInfoAddress, createInfoAddress); }
        private native void allocate(@Cast("vkt::MemoryAllocationInfo*") long allocationInfoAddress, @Cast("vkh::VkImageCreateInfo*") long createInfoAddress);

        public ImageAllocation() { allocate(); };
        private native void allocate();

        //private native @SharedPtr ImageAllocation uniPtr();

        public native int getGLBuffer();
        public native int getGLMemory();
    };


    @Name("vkt::VmaImageAllocation")
    public static class VmaImageAllocation extends Pointer {
        static { Loader.load(); }
        public VmaImageAllocation(Pointer p) { super(p); }

        public VmaImageAllocation() { allocate(); }
        private native void allocate();

        public VmaImageAllocation(@SharedPtr ImageAllocation alloc) { allocate(alloc); };
        private native void allocate(@SharedPtr ImageAllocation alloc);

        public VmaImageAllocation(@Cast("VmaAllocator*") long vmaAllocator, @Cast("vkh::VkImageCreateInfo*") long createInfo, @Cast("VmaMemoryUsage") int vmaUsage) { allocate(vmaAllocator, createInfo, vmaUsage); };
        private native void allocate(@Cast("VmaAllocator*") long vmaAllocator, @Cast("vkh::VkImageCreateInfo*") long createInfo, @Cast("VmaMemoryUsage") int vmaUsage);

        //private native @SharedPtr ImageAllocation uniPtr();

        public native int getGLBuffer();
        public native int getGLMemory();
    };


    @Name("vkt::BufferAllocation")
    public static class BufferAllocation extends Pointer {
        static { Loader.load(); }
        public BufferAllocation(Pointer p) { super(p); }

        public BufferAllocation(@SharedPtr BufferAllocation alloc) { allocate(alloc); };
        private native void allocate(@SharedPtr BufferAllocation alloc);

        public BufferAllocation(@Cast("vkt::MemoryAllocationInfo*") long allocationInfoAddress, @Cast("vkh::VkBufferCreateInfo*") long createInfoAddress) { allocate(allocationInfoAddress, createInfoAddress); }
        private native void allocate(@Cast("vkt::MemoryAllocationInfo*") long allocationInfoAddress, @Cast("vkh::VkBufferCreateInfo*") long createInfoAddress);

        public BufferAllocation() { allocate(); };
        private native void allocate();

        //private native @SharedPtr BufferAllocation uniPtr();
    };


    @Name("vkt::VmaBufferAllocation")
    public static class VmaBufferAllocation extends Pointer {
        static { Loader.load(); }
        public VmaBufferAllocation(Pointer p) { super(p); }

        public VmaBufferAllocation() { allocate(); }
        private native void allocate();

        public VmaBufferAllocation(@SharedPtr BufferAllocation alloc) { allocate(alloc); };
        private native void allocate(@SharedPtr BufferAllocation alloc);

        public VmaBufferAllocation(@Cast("VmaAllocator*") long vmaAllocator, @Cast("vkh::VkBufferCreateInfo*") long createInfo, @Cast("VmaMemoryUsage") int vmaUsage) { allocate(vmaAllocator, createInfo, vmaUsage); };
        private native void allocate(@Cast("VmaAllocator*") long vmaAllocator, @Cast("vkh::VkBufferCreateInfo*") long createInfo, @Cast("VmaMemoryUsage") int vmaUsage);

        //private native @SharedPtr BufferAllocation uniPtr();
    };


    @Name("jvi::Context")
    public static class Context extends Pointer {
        static { Loader.load(); }
        public Context(Pointer p) { super(p); }

        public Context() { allocate(); }
        private native void allocate();
    };


    @Name("jvi::Thread")
    public static class Thread extends Pointer {
        static { Loader.load(); }
        public Thread(Pointer p) { super(p); }

        public Thread() { allocate(); }
        private native void allocate();
    };


    @Name("jvi::Driver")
    public static class Driver extends Pointer {
        static { Loader.load(); }
        public Driver(Pointer p) { super(p); }

        public Driver() { allocate(); }
        private native void allocate();
    };


    @Name("jvi::BufferViewSet")
    public static class BufferViewSet extends Pointer {
        static { Loader.load(); }
        public BufferViewSet(Pointer p) { super(p); }

        public BufferViewSet() { allocate(); }
        private native void allocate();

        public BufferViewSet(Context context) { allocate(context); }
        private native void allocate(Context context);
    };


    @Name("jvi::MeshInput")
    public static class MeshInput extends Pointer {
        static { Loader.load(); }
        public MeshInput(Pointer p) { super(p); }

        public MeshInput(Context context) { allocate(context); }
        private native void allocate(Context context);

        public MeshInput() { allocate(); }
        private native void allocate();
    };


    @Name("jvi::MeshBinding")
    public static class MeshBinding extends Pointer {
        static { Loader.load(); }
        public MeshBinding(Pointer p) { super(p); }

        public MeshBinding() { allocate(); }
        private native void allocate();

        public MeshBinding(Context context) { allocate(context); }
        private native void allocate(Context context);
    };


    @Name("jvi::Node")
    public static class Node extends Pointer {
        static { Loader.load(); }
        public Node(Pointer p) { super(p); }

        public Node() { allocate(); }
        private native void allocate();

        public Node(Context context) { allocate(context); }
        private native void allocate(Context context);
    };


    @Name("jvi::Material")
    public static class Material extends Pointer {
        static { Loader.load(); }
        public Material(Pointer p) { super(p); }

        public Material() { allocate(); }
        private native void allocate();

        public Material(Context context) { allocate(context); }
        private native void allocate(Context context);
    };


    @Name("jvi::Renderer")
    public static class Renderer extends Pointer {
        static { Loader.load(); }
        public Renderer(Pointer p) { super(p); }

        public Renderer() { allocate(); }
        private native void allocate();

        public Renderer(Context context) { allocate(context); }
        private native void allocate(Context context);
    };

/*
    @Name("jvi::MaterialUnit")
    public static class MaterialUnit extends Pointer {
        static { Loader.load(); }
        public MaterialUnit(Pointer p) { super(p); }

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
*/

/*
    @Name("vkh::VsGeometryInstance")
    public static class VsGeometryInstance extends Pointer {
        static { Loader.load(); }
        public VsGeometryInstance(Pointer p) { super(p); }

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
*/
};
