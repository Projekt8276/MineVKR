package net.fabricmc.minertx.jivix;

import org.bytedeco.javacpp.IntPointer;
import org.bytedeco.javacpp.BytePointer;
import org.bytedeco.javacpp.Loader;
import org.bytedeco.javacpp.LongPointer;
import org.bytedeco.javacpp.Pointer;
import org.bytedeco.javacpp.annotation.*;

// "jniJiviXBase", "JiviX"

@Platform(include={
        "./include/vkt2/fw.hpp",
        "./include/JiviX/JiviX.hpp",
        "./jniJiviXCore.h"
}, link={"vulkan-1"}, define={"ENABLE_OPENGL_INTEROP","WIN32","OS_WIN","VK_ENABLE_BETA_EXTENSIONS","VK_USE_PLATFORM_WIN32_KHR","VMA_IMPLEMENTATION","SHARED_PTR_NAMESPACE std", "UNIQUE_PTR_NAMESPACE std"})
@Name("") //
public class JiviXBase extends Pointer {
    static { Loader.load(); }


    @Name("vkt::ImageRegion") //
    public static class ImageRegion extends Pointer {
        static { Loader.load(); }
        public ImageRegion(Pointer p) { super(p); }

        //
        public ImageRegion() { allocate();  }
        private native void allocate();

        //
        public ImageRegion(ImageRegion b) { allocate(b);  }
        private native void allocate(ImageRegion b);

        //
        public ImageRegion(@SharedPtr JiviXCore.ImageAllocation alloc, @Cast("vkh::VkImageViewCreateInfo*") long info, @Cast("vk::ImageLayout") int layout) { allocate(alloc, info, layout);  }
        private native void allocate(@SharedPtr JiviXCore.ImageAllocation alloc, @Cast("vkh::VkImageViewCreateInfo*") long info, @Cast("vk::ImageLayout") int layout);

        //
        public ImageRegion(@SharedPtr JiviXCore.VmaImageAllocation alloc, @Cast("vkh::VkImageViewCreateInfo*") long info, @Cast("vk::ImageLayout") int layout) { allocate(alloc, info, layout);  }
        private native void allocate(@SharedPtr JiviXCore.VmaImageAllocation alloc, @Cast("vkh::VkImageViewCreateInfo*") long info, @Cast("vk::ImageLayout") int layout);

        //
        public native int getGLBuffer();
        public native int getGLMemory();
    };


    @Name("vkt::Vector<int8_t>") // int8_t default
    public static class ByteVector extends Pointer {
        static { Loader.load(); }
        public ByteVector(Pointer p) { super(p); }

        //
        public ByteVector() { allocate();  }
        private native void allocate();

        //
        public ByteVector(@SharedPtr JiviXCore.VmaBufferAllocation a, long offset, long size) { allocate(a, offset, size); } // this = (vector<vector<void*> >*)p
        private native void allocate(@SharedPtr JiviXCore.VmaBufferAllocation a, long offset, long size);

        //
        public ByteVector(@SharedPtr JiviXCore.BufferAllocation a, long offset, long size) { allocate(a, offset, size); } // this = (vector<vector<void*> >*)p
        private native void allocate(@SharedPtr JiviXCore.BufferAllocation a, long offset, long size);

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

        //
        public native int getGLBuffer();
        public native int getGLMemory();
    };


    @Name("vkt::Vector<uint8_t>") // uint8_t version (C++)
    public static class UByteVector extends Pointer {
        static { Loader.load(); }
        public UByteVector(Pointer p) { super(p); } // this = (vector<vector<void*> >*)p

        //
        public UByteVector() { allocate();  }
        private native void allocate();

        //
        public UByteVector(@SharedPtr JiviXCore.VmaBufferAllocation a, long offset, long size) { allocate(a, offset, size); } // this = (vector<vector<void*> >*)p
        private native void allocate(@SharedPtr JiviXCore.VmaBufferAllocation a, long offset, long size);

        //
        public UByteVector(@SharedPtr JiviXCore.BufferAllocation a, long offset, long size) { allocate(a, offset, size); } // this = (vector<vector<void*> >*)p
        private native void allocate(@SharedPtr JiviXCore.BufferAllocation a, long offset, long size);


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


    @Name("jvx::Context")
    public static class Context extends Pointer {
        static { Loader.load(); }
        public Context(Pointer p) { super(p); }

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
        public Thread(Pointer p) { super(p); }

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
        public Driver(Pointer p) { super(p); }

        public Driver() { allocate(); }
        private native void allocate();

        public Driver(@SharedPtr JiviXCore.Driver object) { allocate(object); }
        private native void allocate(@SharedPtr JiviXCore.Driver object);

        // Get Handle Value
        public native @ByVal @Cast("VkPhysicalDevice")  long getPhysicalDevice();
        public native @ByVal @Cast("VkDevice")          long getDevice();
        public native @ByVal @Cast("VkQueue")           long getQueue();
        public native @ByVal @Cast("VkFence")           long getFence();
        public native @ByVal @Cast("VkInstance")        long getInstance();
        public native @ByVal @Cast("VkCommandPool")     long getCommandPool();
        public native @ByVal @Cast("VkPipelineCache")   long getPipelineCache();
        public native @ByVal @Cast("VkDescriptorPool")  long getDescriptorPool();
        public native @ByVal @Cast("VkImageView")       long getDepthImageView();
        public native @ByVal @Cast("VkImage")           long getDepthImage();
        public native @ByVal @Cast("VkInstance")        long createInstance();
        public native @ByVal @Cast("VkDevice")          long createDevice();

        // Get Address of Reference... (but needs wrapped as Pointer?)
        public native @Name("getMemoryProperties")  @ByRef @Cast("int8_t*") BytePointer _getMemoryProperties();
        public native @Name("getAllocator")         @ByRef @Cast("int8_t*") BytePointer _getAllocator();
        public native @Name("getDispatch")          @ByRef @Cast("int8_t*") BytePointer _getDispatch();

        // Get Address from allocator or properties
        public long getDispatch()         { return this._getDispatch().address(); };
        public long getAllocator()        { return this._getAllocator().address(); };
        public long getMemoryProperties() { return this._getMemoryProperties().address(); };
        //
    };


    @Name("jvx::BufferViewSet")
    public static class BufferViewSet extends Pointer {
        static { Loader.load(); }
        public BufferViewSet(Pointer p) { super(p); }

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
        public MeshInput(Pointer p) { super(p); }

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

        public native JiviXCore.MeshInput linkBViewSet(@ByRef BufferViewSet bufferViewSet);
    };


    @Name("jvx::MeshBinding")
    public static class MeshBinding extends Pointer {
        static { Loader.load(); }
        public MeshBinding(Pointer p) { super(p); }

        public MeshBinding() { allocate(); }
        private native void allocate();

        //
        public MeshBinding(Context context, long maxPrimitiveCount, @StdVector long[] perGeometryCount) { allocate(context, maxPrimitiveCount, perGeometryCount); }
        private native void allocate(Context context, long maxPrimitiveCount, @StdVector long[] perGeometryCount);

        //
        public MeshBinding(Context context, long maxPrimitiveCount) { allocate(context, maxPrimitiveCount); }
        private native void allocate(Context context, long maxPrimitiveCount);

        // Not Prefer...
        //public MeshBinding(Context context) { allocate(context); }
        //private native void allocate(Context context);


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
        public native JiviXCore.MeshBinding addMeshInput(@ByRef MeshInput input, int materialID, long instances);
        public native JiviXCore.MeshBinding addMeshInput(@ByRef MeshInput input, int materialID);
        public native JiviXCore.MeshBinding addMeshInput(@ByRef MeshInput input, @StdVector int materialIDs[]);

        //public native @ByRef JiviXCore.MeshBinding addMeshInput(@ByRef MeshInput input, @StdVector int materialIDs[]);
    };


    @Name("jvx::Node")
    public static class Node extends Pointer {
        static { Loader.load(); }
        public Node(Pointer p) { super(p); }

        public Node() { allocate(); }
        private native void allocate();

        public Node(Context context) { allocate(context); }
        private native void allocate(Context context);

        public Node(@SharedPtr JiviXCore.Node object) { allocate(object); }
        private native void allocate(@SharedPtr JiviXCore.Node object);

        public native @SharedPtr JiviXCore.Node sharedPtr();

        public native JiviXCore.Node pushInstance(@Cast("vkh::VsGeometryInstance*") long address);

        public native long pushMesh(@ByRef MeshBinding binding);
        public native void createDescriptorSet();
    };


    @Name("jvx::Material")
    public static class Material extends Pointer {
        static { Loader.load(); }
        public Material(Pointer p) { super(p); }

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
        public Renderer(Pointer p) { super(p); }

        public Renderer() { allocate(); }
        private native void allocate();

        public Renderer(Context context) { allocate(context); }
        private native void allocate(Context context);

        public Renderer(@SharedPtr JiviXCore.Renderer object) { allocate(object); }
        private native void allocate(@SharedPtr JiviXCore.Renderer object);

        public native @SharedPtr JiviXCore.Renderer sharedPtr();

        //
        public native JiviXCore.Renderer linkMaterial(@ByRef Material material);
        public native JiviXCore.Renderer linkNode(@ByRef Node node);
        //public native void createDescriptorSet();
    };


    @Name("jvx::TestClass")
    public static class TestClass extends Pointer {
        static { Loader.load(); }
        public TestClass(Pointer p) { super(p); }

        public TestClass() { allocate(); }
        private native void allocate();
    };

};
