package net.fabricmc.minertx.jivix;

import org.bytedeco.javacpp.IntPointer;
import org.bytedeco.javacpp.BytePointer;
import org.bytedeco.javacpp.Loader;
import org.bytedeco.javacpp.LongPointer;
import org.bytedeco.javacpp.Pointer;
import org.bytedeco.javacpp.annotation.*;

// TODO: Engine Wrappers for std::shared_ptr, like "vkt::Vector<uint8_t>"
@Platform(include={
        "./include/vkt2/fw.hpp",
        "./include/JiviX/JiviX.hpp",
        "./jniJiviXCore.h"
}, link="./lib/vulkan-1", define={"ENABLE_OPENGL_INTEROP","WIN32","OS_WIN","VK_ENABLE_BETA_EXTENSIONS","VK_USE_PLATFORM_WIN32_KHR","VMA_IMPLEMENTATION","SHARED_PTR_NAMESPACE std", "UNIQUE_PTR_NAMESPACE std"})

@Name("")
public class JiviXBase extends Pointer {
    static { Loader.load(); }

    // int8_t default
    @Name("vkt::Vector<int8_t>")
    public static class ByteVector extends Pointer {
        static { Loader.load(); }
        public ByteVector()       { allocate();  }

        //
        public ByteVector(Pointer p) { super(p); } // this = (vector<vector<void*> >*)p
        private native void allocate();

        //
        //public ByteVector(JiviXCore.VmaBufferAllocation a, long offset, long size) { allocate(a, offset, size); } // this = (vector<vector<void*> >*)p
        //private native void allocate(JiviXCore.VmaBufferAllocation a, long offset, long size);

        //
        @Name("operator=")
        public native @ByRef ByteVector put(@ByRef ByteVector x);

        //
        @Name("operator[]")
        public native @ByRef BytePointer get(long n);
        public native @ByRef BytePointer at(long n);

        //
        public native long size();
        public native long range();

        // map buffer data
        public native BytePointer mapped();
        public native BytePointer map();
        public native BytePointer data();

        // Java have NOT support `VkDeviceOrHostAddressKHR` or `VkDeviceOrHostAddressConstKHR`, and become rude var...
        public native @Cast("uintptr_t") long deviceAddress();
    };

    // uint8_t version (C++)
    @Name("vkt::Vector<uint8_t>")
    public static class UByteVector extends Pointer {
        static { Loader.load(); }

        //
        public UByteVector()       { allocate();  }
        public UByteVector(Pointer p) { super(p); } // this = (vector<vector<void*> >*)p
        private native void allocate();

        //
        //public UByteVector(JiviXCore.VmaBufferAllocation a, long offset, long size) { allocate(a, offset, size); } // this = (vector<vector<void*> >*)p
        //private native void allocate(JiviXCore.VmaBufferAllocation a, long offset, long size);

        //
        @Name("operator=")
        public native @ByRef UByteVector put(@ByRef UByteVector x);

        //
        @Name("operator[]")
        public native @Cast("uint8_t*") @ByRef BytePointer get(long n);
        public native @Cast("uint8_t*") @ByRef BytePointer at(long n);

        //
        public native long size();
        public native long range();

        // map buffer data
        public native @Cast("uint8_t*") BytePointer mapped();
        public native @Cast("uint8_t*") BytePointer map();
        public native @Cast("uint8_t*") BytePointer data();

        // Java have NOT support `VkDeviceOrHostAddressKHR` or `VkDeviceOrHostAddressConstKHR`, and become rude var...
        public native @Cast("uintptr_t") long deviceAddress();
    };

    @Name("vkt::MakeVmaVector")
    public static native UByteVector MakeVmaVector(JiviXCore.VmaBufferAllocation allocation, long offset, long size);

    @Name("jvx::Context")
    public static class Context extends Pointer {
        static { Loader.load(); }

        public Context() { allocate(); }
        private native void allocate();

        public Context(@SharedPtr JiviXCore.Context object) { allocate(object); }
        private native void allocate(@SharedPtr JiviXCore.Context object);

        public Context(@SharedPtr JiviXCore.Driver object) { allocate(object); }
        private native void allocate(@SharedPtr JiviXCore.Driver object);

        public Context(Driver object) { allocate(object); }
        private native void allocate(Driver object);

        //public native void createDescriptorSet();
        public native @SharedPtr JiviXCore.Context sharedPtr();
    };

    @Name("jvx::Thread")
    public static class Thread extends Pointer {
        static { Loader.load(); }

        public Thread() { allocate(); }
        private native void allocate();

        public Thread(@SharedPtr JiviXCore.Thread object) { allocate(object); }
        private native void allocate(@SharedPtr JiviXCore.Thread object);

        //public native void createDescriptorSet();
        public native @SharedPtr JiviXCore.Thread sharedPtr();

        //
        public native void submitCmd(@Cast("VkCommandBuffer") long commandBuf, @Cast("vk::SubmitInfo*") long submitInfoAddress);
        public native void submitCmdAsync(@Cast("VkCommandBuffer") long commandBuf, @Cast("vk::SubmitInfo*") long submitInfoAddress);
    };

    @Name("jvx::Driver")
    public static class Driver extends Pointer {
        static { Loader.load(); }

        public Driver() { allocate(); }
        private native void allocate();

        public Driver(@SharedPtr JiviXCore.Driver object) { allocate(object); }
        private native void allocate(@SharedPtr JiviXCore.Driver object);
    };

    @Name("jvx::BufferViewSet")
    public static class BufferViewSet extends Pointer {
        static { Loader.load(); }

        public BufferViewSet() { allocate(); }
        private native void allocate();

        public BufferViewSet(Context context) { allocate(context); }
        private native void allocate(Context context);

        public BufferViewSet(@SharedPtr JiviXCore.BufferViewSet object) { allocate(object); }
        private native void allocate(@SharedPtr JiviXCore.BufferViewSet object);

        public native @SharedPtr JiviXCore.BufferViewSet sharedPtr();

        public native long getBufferCount();
        public native void createDescriptorSet();
        public native @ByRef UByteVector get(int id);
        public native long pushBufferView(@ByRef UByteVector vector);
        //public native @StdVector @ByRef ByteVector[] getBufferViewList();

        // Value based handle
        public native @Cast("VkDescriptorSet") long getDescriptorSet();
        public native @Cast("VkDescriptorSetLayout") long getDescriptorLayout();

        // Swinka Tramvaya
        //public native @Cast("VkDescriptorSet*") @ByRef LongPointer getDescriptorSet();
        //public native @Cast("VkDescriptorSetLayout*") @ByRef LongPointer getDescriptorLayout();
    };

    @Name("jvx::MeshInput")
    public static class MeshInput extends Pointer {
        static { Loader.load(); }

        public MeshInput() { allocate(); }
        private native void allocate();

        public MeshInput(Context context) { allocate(context); }
        private native void allocate(Context context);

        public MeshInput(@SharedPtr JiviXCore.MeshInput object) { allocate(object); }
        private native void allocate(@SharedPtr JiviXCore.MeshInput object);

        public native @SharedPtr JiviXCore.MeshInput sharedPtr();

        public native void createDescriptorSet();

        public native JiviXCore.MeshInput makeQuad();
        public native JiviXCore.MeshInput copyMeta(@Cast("VkCommandBuffer") long commandBuffer);
        public native JiviXCore.MeshInput manifestIndex(@Cast("vk::IndexType") int indexType);

        // Use Address from LWJGL-3 structures
        public native JiviXCore.MeshInput addBinding(int bufferID, @Cast("vkh::VkVertexInputBindingDescription*") long bindingAddress);
        public native JiviXCore.MeshInput addAttribute(@Cast("vkh::VkVertexInputAttributeDescription*") long attributeAddress);

        public native JiviXCore.MeshInput setIndexData(int bufferID, @Cast("vk::IndexType") int indexType);
        public native JiviXCore.MeshInput setIndexOffset(@Cast("vk::DeviceSize") long offset);
        public native JiviXCore.MeshInput setIndexCount(@Cast("vk::DeviceSize") long count);
        public native JiviXCore.MeshInput setPrimitiveCount(@Cast("vk::DeviceSize") long count);

        // Reference Based
        // public native @Cast("vk::DeviceSize&") @ByRef LongPointer getIndexCount(); // Return Reference

        // Value Based
        public native long getIndexCount(); // Return Reference

        public native JiviXCore.MeshInput linkBViewSet(JiviXCore.BufferViewSet bufferViewSet);
    };

    @Name("jvx::MeshBinding")
    public static class MeshBinding extends Pointer {
        static { Loader.load(); }

        public MeshBinding() { allocate(); }
        private native void allocate();

        public MeshBinding(Context context) { allocate(context); }
        private native void allocate(Context context);

        public MeshBinding(@SharedPtr JiviXCore.MeshBinding object) { allocate(object); }
        private native void allocate(@SharedPtr JiviXCore.MeshBinding object);

        public native @SharedPtr JiviXCore.MeshBinding sharedPtr();

        public native JiviXCore.MeshBinding setThread(JiviXCore.Thread thread);
        public native JiviXCore.MeshBinding setDriver(JiviXCore.Driver thread);

        public native @ByRef UByteVector getBindingBuffer(long i);
        public native @ByRef UByteVector getIndexBuffer();

        public native int getBindingBufferGL(long i);
        public native int getIndexBufferGL();

        public native JiviXCore.MeshBinding setIndexCount(@Cast("vk::DeviceSize") long count);
        public native JiviXCore.MeshBinding setPrimitiveCount(@Cast("vk::DeviceSize") long count);
        public native JiviXCore.MeshBinding setTransformData(@Cast("vkt::Vector<glm::mat3x4>*") long address, int stride);
        public native JiviXCore.MeshBinding setTransformData(@Cast("vkt::Vector<glm::mat3x4>*") long address);

        public native JiviXCore.MeshBinding buildGeometry(@Cast("VkCommandBuffer") long cmdbufAddress, @Cast("glm::uvec4*") long meshData);
        public native JiviXCore.MeshBinding addMeshInput(JiviXCore.MeshInput input, int materialID, int instances);
        public native JiviXCore.MeshBinding addMeshInput(JiviXCore.MeshInput input, int materialID);
        //public native JiviXCore.MeshBinding addMeshInput(JiviXCore.MeshInput input, @StdVector int materialIDs[]);
    };

    @Name("jvx::Node")
    public static class Node extends Pointer {
        static { Loader.load(); }

        public Node() { allocate(); }
        private native void allocate();

        public Node(Context context) { allocate(context); }
        private native void allocate(Context context);

        public Node(@SharedPtr JiviXCore.Node object) { allocate(object); }
        private native void allocate(@SharedPtr JiviXCore.Node object);

        public native @SharedPtr JiviXCore.Node sharedPtr();

        public native void createDescriptorSet();

        public native JiviXCore.Node pushInstance(@Cast("vkh::VsGeometryInstance*") long address);
        public native long pushMesh(JiviXCore.MeshBinding binding);
    };

    @Name("jvx::Material")
    public static class Material extends Pointer {
        static { Loader.load(); }

        public Material() { allocate(); }
        private native void allocate();

        public Material(Context context) { allocate(context); }
        private native void allocate(Context context);

        public Material(@SharedPtr JiviXCore.Material object) { allocate(object); }
        private native void allocate(@SharedPtr JiviXCore.Material object);

        public native @SharedPtr JiviXCore.Material sharedPtr();

        public native void createDescriptorSet();
        public native long pushMaterial(JiviXCore.MaterialUnit materialAddress);

        public native JiviXCore.Material pushSampledImage(@Cast("vkh::VkDescriptorImageInfo*") long imageDescAddress);
        public native JiviXCore.Material setRawMaterials(@Cast("vkt::Vector<jvi::MaterialUnit>*") UByteVector rawMaterials, long materialCount);
        public native JiviXCore.Material setGpuMaterials(@Cast("vkt::Vector<jvi::MaterialUnit>*") UByteVector rawMaterials);
        public native JiviXCore.Material resetMaterials();
        public native JiviXCore.Material resetSampledImages();
    };

    @Name("jvx::Renderer")
    public static class Renderer extends Pointer {
        static { Loader.load(); }

        public Renderer() { allocate(); }
        private native void allocate();

        public Renderer(Context context) { allocate(context); }
        private native void allocate(Context context);

        public Renderer(@SharedPtr JiviXCore.Renderer object) { allocate(object); }
        private native void allocate(@SharedPtr JiviXCore.Renderer object);

        public native @SharedPtr JiviXCore.Renderer sharedPtr();
        //public native void createDescriptorSet();
    };

};
