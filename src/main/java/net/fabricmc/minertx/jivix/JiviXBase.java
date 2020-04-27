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
        "JiviX/JiviX.hpp"
}, link="./lib/vulkan-1", define={"ENABLE_OPENGL_INTEROP","WIN32","OS_WIN","VK_ENABLE_BETA_EXTENSIONS","VK_USE_PLATFORM_WIN32_KHR"})

@Name("")
public class JiviXBase extends Pointer {
    static { Loader.load(); }
    
    // int8_t default
    @Name("vkt::Vector<int8_t>")
    public static class ByteVector extends Pointer {
        static { Loader.load(); }
        public ByteVector()       { allocate();  }
        public ByteVector(Pointer p) { super(p); } // this = (vector<vector<void*> >*)p
        private native void allocate();

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
        public UByteVector()       { allocate();  }
        public UByteVector(Pointer p) { super(p); } // this = (vector<vector<void*> >*)p
        private native void allocate();

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

        // Java have NOT support `VkDeviceOrHostAddressKHR` or `VkDeviceOrHostAddressConstKHR`, and become rude var...
        public native @Cast("uintptr_t") long deviceAddress();
    };

    @Name("jvx::Context")
    public static class Context extends Pointer {
        static { Loader.load(); }
        public Context() { allocate(); }
        private native void allocate();
        public native void createDescriptorSet();
    };

    @Name("jvx::Thread")
    public static class Thread extends Pointer {
        static { Loader.load(); }
        public Thread() { allocate(); }
        private native void allocate();
        public native void createDescriptorSet();
    };

    @Name("jvx::Driver")
    public static class Driver extends Pointer {
        static { Loader.load(); }
        public Driver() { allocate(); }
        private native void allocate();
        public native void createDescriptorSet();
    };


    @Name("jvx::BufferViewSet")
    public static class BufferViewSet extends Pointer {
        static { Loader.load(); }

        public BufferViewSet() { allocate(); }
        private native void allocate();

        public BufferViewSet(JiviXBase.Context context) { allocate(context); }
        private native void allocate(JiviXBase.Context context);

        public native long getBufferCount();
        public native void createDescriptorSet();
        public native @ByRef JiviXBase.ByteVector get(int id);
        public native @SharedPtr BufferViewSet pushBufferView(JiviXBase.ByteVector vector);
        //public native @StdVector @ByRef JiviXBase.ByteVector[] getBufferViewList();
        public native @ByRef @Cast("vk::DescriptorSet&") LongPointer getDescriptorSet();
        public native @ByRef @Cast("vk::DescriptorSetLayout&") LongPointer getDescriptorLayout();
    };

    @Name("jvx::MeshInput")
    public static class MeshInput extends Pointer {
        static { Loader.load(); }

        public MeshInput(JiviXBase.Context context) { allocate(context); }
        private native void allocate(JiviXBase.Context context);

        public MeshInput() { allocate(); }
        private native void allocate();

        public native void createDescriptorSet();

        public native @SharedPtr MeshInput makeQuad();
        public native @SharedPtr MeshInput copyMeta(@Cast("vk::CommandBuffer") long commandBuffer);
        public native @SharedPtr MeshInput manifestIndex(@Cast("vk::IndexType") int indexType);

        // Use Address from LWJGL-3 structures
        public native @SharedPtr MeshInput addBinding(int bufferID, @Cast("vkh::VkVertexInputBindingDescription*") long bindingAddress);
        public native @SharedPtr MeshInput addAttribute(@Cast("vkh::VkVertexInputAttributeDescription*") long attributeAddress);

        public native @SharedPtr MeshInput setIndexOffset(@Cast("vk::DeviceSize") long offset);
        public native @SharedPtr MeshInput setIndexCount(@Cast("vk::DeviceSize") long count);
        public native @SharedPtr MeshInput setIndexCount(@Cast("vk::DeviceSize") long count, @Cast("vk::IndexType") int indexType);
        public native @SharedPtr MeshInput setPrimitiveCount(@Cast("vk::DeviceSize") long count);
        public native @ByRef @Cast("vk::DeviceSize&") LongPointer getIndexCount(); // Return Reference

        public native @SharedPtr MeshInput linkBViewSet(@Cast("std::shared_ptr<jvx::BufferViewSet>") BufferViewSet bufferViewSet);
    };

    @Name("jvx::MeshBinding")
    public static class MeshBinding extends Pointer {
        static { Loader.load(); }

        public MeshBinding() { allocate(); }
        private native void allocate();

        public MeshBinding(JiviXBase.Context context) { allocate(context); }
        private native void allocate(JiviXBase.Context context);

        public native void createDescriptorSet();

        public native @SharedPtr MeshBinding setThread(@SharedPtr JiviXBase.Thread thread);
        public native @SharedPtr MeshBinding setDriver(@SharedPtr JiviXBase.Driver thread);
        public native JiviXBase.ByteVector getBindingBuffer(long i);
        public native JiviXBase.ByteVector getIndexBuffer();

        public native @ByRef IntPointer getBindingBufferGL(long i);
        public native @ByRef IntPointer getIndexBufferGL();

        public native @SharedPtr MeshBinding setIndexCount(@Cast("vk::DeviceSize") long count);
        public native @SharedPtr MeshBinding setPrimitiveCount(@Cast("vk::DeviceSize") long count);
        public native @SharedPtr MeshBinding setTransformData(long address, int stride);
        public native @SharedPtr MeshBinding setTransformData(long address);

        public native @SharedPtr MeshBinding buildGeometry(@Cast("vk::CommandBuffer") long cmdbufAddress, @Cast("glm::uvec4*") long meshData);

        public native @SharedPtr MeshBinding addMeshInput(@SharedPtr MeshInput input, int materialID, int instances);
        public native @SharedPtr MeshBinding addMeshInput(@SharedPtr MeshInput input, int materialID);
        public native @SharedPtr MeshBinding addMeshInput(@SharedPtr MeshInput input, @StdVector int materialIDs[]);
    };

    @Name("jvx::Node")
    public static class Node extends Pointer {
        static { Loader.load(); }

        public Node() { allocate(); }
        private native void allocate();

        public Node(JiviXBase.Context context) { allocate(context); }
        private native void allocate(JiviXBase.Context context);

        public native void createDescriptorSet();

        public native @SharedPtr Node pushInstance(@Cast("vkh::VsGeometryInstance*") long address);
        public native long pushMesh(@SharedPtr MeshBinding binding);
    };

    @Name("jvx::Material")
    public static class Material extends Pointer {
        static { Loader.load(); }

        public Material() { allocate(); }
        private native void allocate();

        public Material(JiviXBase.Context context) { allocate(context); }
        private native void allocate(JiviXBase.Context context);

        public native void createDescriptorSet();

        public native @SharedPtr Material setRawMaterials(@Cast("vkt::Vector<jvx::MaterialUnit>") JiviXBase.ByteVector rawMaterials, long materialCount);
        public native @SharedPtr Material setGpuMaterials(@Cast("vkt::Vector<jvx::MaterialUnit>") JiviXBase.ByteVector rawMaterials);
        public native @SharedPtr Material pushMaterial(@Cast("jvx::MaterialUnit") long materialAddress);
        public native @SharedPtr Material resetMaterials();
        public native @SharedPtr Material pushSampledImage(@Cast("vkh::VkDescriptorImageInfo") long imageDescAddress);
        public native @SharedPtr Material resetSampledImages();
    };

    @Name("jvx::Renderer")
    public static class Renderer extends Pointer {
        static { Loader.load(); }

        public Renderer() { allocate(); }
        private native void allocate();

        public Renderer(JiviXBase.Context context) { allocate(context); }
        private native void allocate(JiviXBase.Context context);

        public native void createDescriptorSet();
    };

}
