package com.helixd2s.jivix;

import org.bytedeco.javacpp.*;
import org.bytedeco.javacpp.annotation.*;

// "jniJiviXBase", "JiviX"

@Platform(include = {
        "./include/GLFW/glfw3.h",
        "./include/vkt3/fw.hpp",
        "./include/JiviX/JiviX.hpp",
}, link = {"vulkan-1", "glfw", "glbinding", "glbinding-aux"}, define = {"ENABLE_OPENGL_INTEROP", "WIN32", "OS_WIN", "VK_ENABLE_BETA_EXTENSIONS", "VK_USE_PLATFORM_WIN32_KHR", "VMA_IMPLEMENTATION", "SHARED_PTR_NAMESPACE std", "UNIQUE_PTR_NAMESPACE std"})
@Name("") //
public class JiviXCore extends Pointer {
    static {
        Loader.load();
    }

    // Pointer for xvk::Device
    @Name("xvk::Device")
    public static class Device extends Pointer {
        static {
            Loader.load();
        }

        public Device(Pointer p) {
            super(p);
        }

        public Device() {
            allocate();
        }

        private native void allocate();
    }

    // Pointer for xvk::Instance
    @Name("xvk::Instance")
    public static class Instance extends Pointer {
        static {
            Loader.load();
        }

        public Instance(Pointer p) {
            super(p);
        }

        public Instance() {
            allocate();
        }

        private native void allocate();
    }


    @Name("vkt::VmaMemoryInfo")
    public static class VmaMemoryInfo extends Pointer {
        static {
            Loader.load();
        }

        public VmaMemoryInfo(Pointer p) {
            super(p);
        }

        // 
        public VmaMemoryInfo() {
            allocate();
        }

        private native void allocate();

        // 
        public native @Cast("VmaMemoryUsage*")
        @ValueGetter
        @ByRef
        IntPointer memUsage();

        //
        public native @ByRef
        @SharedPtr
        JiviXCore.Device getDeviceDispatch();

        public native @ByRef
        VmaMemoryInfo setDeviceDispatch(@SharedPtr JiviXCore.Device device);

        public native @ByRef
        @SharedPtr
        JiviXCore.Instance getInstanceDispatch();

        public native @ByRef
        VmaMemoryInfo setInstanceDispatch(@SharedPtr JiviXCore.Instance instance);
    }


    @Name("vkt::ImageAllocation")
    public static class ImageAllocation extends Pointer {
        static {
            Loader.load();
        }

        public ImageAllocation(Pointer p) {
            super(p);
        }

        public ImageAllocation(@SharedPtr ImageAllocation alloc) {
            allocate(alloc);
        }

        public ImageAllocation(@Cast("vkt::MemoryAllocationInfo*") long allocationInfoAddress, @Cast("vkh::VkImageCreateInfo*") long createInfoAddress) {
            allocate(allocationInfoAddress, createInfoAddress);
        }

        public ImageAllocation() {
            allocate();
        }

        private native void allocate(@SharedPtr ImageAllocation alloc);

        private native void allocate(@Cast("vkt::MemoryAllocationInfo*") long allocationInfoAddress, @Cast("vkh::VkImageCreateInfo*") long createInfoAddress);

        private native void allocate();

        //private native @SharedPtr ImageAllocation uniPtr();

        public native int getGLImage();

        public native int getGLMemory();
    }


    @Name("vkt::VmaImageAllocation")
    public static class VmaImageAllocation extends Pointer {
        static {
            Loader.load();
        }

        public VmaImageAllocation(Pointer p) {
            super(p);
        }

        public VmaImageAllocation() {
            allocate();
        }

        public VmaImageAllocation(@SharedPtr ImageAllocation alloc) {
            allocate(alloc);
        }

        public VmaImageAllocation(@Cast("VmaAllocator*") long vmaAllocator, @Cast("vkh::VkImageCreateInfo*") long createInfo, @Cast("vkt::VmaMemoryInfo*") long memInfoPtr) {
            allocate(vmaAllocator, createInfo, memInfoPtr);
        }

        public VmaImageAllocation(@Cast("VmaAllocator*") long vmaAllocator, @Cast("vkh::VkImageCreateInfo*") long createInfo, VmaMemoryInfo memInfo) {
            allocate(vmaAllocator, createInfo, memInfo);
        }

        private native void allocate();

        private native void allocate(@SharedPtr ImageAllocation alloc);

        private native void allocate(@Cast("VmaAllocator*") long vmaAllocator, @Cast("vkh::VkImageCreateInfo*") long createInfo, @Cast("vkt::VmaMemoryInfo*") long memInfoPtr);

        private native void allocate(@Cast("VmaAllocator*") long vmaAllocator, @Cast("vkh::VkImageCreateInfo*") long createInfo, VmaMemoryInfo memInfo);

        //private native @SharedPtr ImageAllocation uniPtr();

        public native int getGLImage();

        public native int getGLMemory();
    }


    @Name("vkt::BufferAllocation")
    public static class BufferAllocation extends Pointer {
        static {
            Loader.load();
        }

        public BufferAllocation(Pointer p) {
            super(p);
        }

        public BufferAllocation(@SharedPtr BufferAllocation alloc) {
            allocate(alloc);
        }

        public BufferAllocation(@Cast("vkt::MemoryAllocationInfo*") long allocationInfoAddress, @Cast("vkh::VkBufferCreateInfo*") long createInfoAddress) {
            allocate(allocationInfoAddress, createInfoAddress);
        }

        public BufferAllocation() {
            allocate();
        }

        private native void allocate(@SharedPtr BufferAllocation alloc);

        private native void allocate(@Cast("vkt::MemoryAllocationInfo*") long allocationInfoAddress, @Cast("vkh::VkBufferCreateInfo*") long createInfoAddress);

        private native void allocate();

        //private native @SharedPtr BufferAllocation uniPtr();
    }


    @Name("vkt::VmaBufferAllocation")
    public static class VmaBufferAllocation extends Pointer {
        static {
            Loader.load();
        }

        public VmaBufferAllocation(Pointer p) {
            super(p);
        }

        public VmaBufferAllocation() {
            allocate();
        }

        public VmaBufferAllocation(@SharedPtr BufferAllocation alloc) {
            allocate(alloc);
        }

        public VmaBufferAllocation(@Cast("VmaAllocator*") long vmaAllocator, @Cast("vkh::VkBufferCreateInfo*") long createInfo, @Cast("vkt::VmaMemoryInfo*") long memInfoPtr) {
            allocate(vmaAllocator, createInfo, memInfoPtr);
        }

        public VmaBufferAllocation(@Cast("VmaAllocator*") long vmaAllocator, @Cast("vkh::VkBufferCreateInfo*") long createInfo, VmaMemoryInfo memInfo) {
            allocate(vmaAllocator, createInfo, memInfo);
        }

        private native void allocate();

        private native void allocate(@SharedPtr BufferAllocation alloc);

        private native void allocate(@Cast("VmaAllocator*") long vmaAllocator, @Cast("vkh::VkBufferCreateInfo*") long createInfo, @Cast("vkt::VmaMemoryInfo*") long memInfoPtr);

        private native void allocate(@Cast("VmaAllocator*") long vmaAllocator, @Cast("vkh::VkBufferCreateInfo*") long createInfo, VmaMemoryInfo memInfo);

        //private native @SharedPtr BufferAllocation uniPtr();
    }


    @Name("jvi::Context")
    public static class Context extends Pointer {
        static {
            Loader.load();
        }

        public Context(Pointer p) {
            super(p);
        }

        public Context() {
            allocate();
        }

        private native void allocate();
    }


    @Name("jvi::Thread")
    public static class Thread extends Pointer {
        static {
            Loader.load();
        }

        public Thread(Pointer p) {
            super(p);
        }

        public Thread() {
            allocate();
        }

        private native void allocate();
    }


    @Name("jvi::Driver")
    public static class Driver extends Pointer {
        static {
            Loader.load();
        }

        public Driver(Pointer p) {
            super(p);
        }

        public Driver() {
            allocate();
        }

        private native void allocate();
    }


    @Name("jvi::BufferViewSet")
    public static class BufferViewSet extends Pointer {
        static {
            Loader.load();
        }

        public BufferViewSet(Pointer p) {
            super(p);
        }

        public BufferViewSet() {
            allocate();
        }

        public BufferViewSet(Context context) {
            allocate(context);
        }

        private native void allocate();

        private native void allocate(Context context);
    }


    @Name("jvi::MeshInput")
    public static class MeshInput extends Pointer {
        static {
            Loader.load();
        }

        public MeshInput(Pointer p) {
            super(p);
        }

        public MeshInput(Context context) {
            allocate(context);
        }

        public MeshInput() {
            allocate();
        }

        private native void allocate(Context context);

        private native void allocate();
    }


    @Name("jvi::MeshBinding")
    public static class MeshBinding extends Pointer {
        static {
            Loader.load();
        }

        public MeshBinding(Pointer p) {
            super(p);
        }

        public MeshBinding() {
            allocate();
        }

        public MeshBinding(Context context) {
            allocate(context);
        }

        private native void allocate();

        private native void allocate(Context context);
    }


    @Name("jvi::Node")
    public static class Node extends Pointer {
        static {
            Loader.load();
        }

        public Node(Pointer p) {
            super(p);
        }

        public Node() {
            allocate();
        }

        public Node(Context context) {
            allocate(context);
        }

        private native void allocate();

        private native void allocate(Context context);
    }


    @Name("jvi::Material")
    public static class Material extends Pointer {
        static {
            Loader.load();
        }

        public Material(Pointer p) {
            super(p);
        }

        public Material() {
            allocate();
        }

        public Material(Context context) {
            allocate(context);
        }

        private native void allocate();

        private native void allocate(Context context);
    }


    @Name("jvi::Renderer")
    public static class Renderer extends Pointer {
        static {
            Loader.load();
        }

        public Renderer(Pointer p) {
            super(p);
        }

        public Renderer() {
            allocate();
        }

        public Renderer(Context context) {
            allocate(context);
        }

        private native void allocate();

        private native void allocate(Context context);
    }

    @Name("jvi::MaterialUnit")
    public static class MaterialUnit extends Pointer {
        static {
            Loader.load();
        }

        public MaterialUnit(Pointer p) {
            super(p);
        }

        // 
        public MaterialUnit() {
            allocate();
        }

        private native void allocate();

        // Channels
        public native @Cast("float*")
        @ValueGetter
        @ByRef
        FloatPointer diffuse();

        public native @Cast("float*")
        @ValueGetter
        @ByRef
        FloatPointer specular();

        public native @Cast("float*")
        @ValueGetter
        @ByRef
        FloatPointer normals();

        public native @Cast("float*")
        @ValueGetter
        @ByRef
        FloatPointer emission();

        // Channels
        public native @Cast("int*")
        @ValueGetter
        @ByRef
        IntPointer diffuseTexture();

        public native @Cast("int*")
        @ValueGetter
        @ByRef
        IntPointer specularTexture();

        public native @Cast("int*")
        @ValueGetter
        @ByRef
        IntPointer normalsTexture();

        public native @Cast("int*")
        @ValueGetter
        @ByRef
        IntPointer emissionTexture();
    }

    @Name("vkh::VsGeometryInstance")
    public static class VsGeometryInstance extends Pointer {
        static {
            Loader.load();
        }

        public VsGeometryInstance(Pointer p) {
            super(p);
        }

        // 
        public VsGeometryInstance() {
            allocate();
        }

        private native void allocate();

        // Transform
        public native @Cast("float*")
        @ValueGetter
        @ByRef
        FloatPointer transform();

        // 
        public native @Cast("byte*")
        @ValueGetter
        @ByRef
        BytePointer mask();

        public native @Cast("byte*")
        @ValueGetter
        @ByRef
        BytePointer flags();

        public native @Cast("int*")
        @ValueGetter
        @ByRef
        IntPointer instanceId();

        public native @Cast("int*")
        @ValueGetter
        @ByRef
        IntPointer instanceOffset();

        // 
        public native @Cast("int64_t*")
        @ValueGetter
        @ByRef
        LongPointer accelerationStructureHandle();
    }

}
