package net.fabricmc.minertx.jivix;

import org.bytedeco.javacpp.BytePointer;
import org.bytedeco.javacpp.Loader;
import org.bytedeco.javacpp.LongPointer;
import org.bytedeco.javacpp.Pointer;
import org.bytedeco.javacpp.annotation.*;

// TODO: Engine Wrappers for std::shared_ptr, like "vkt::Vector<uint8_t>"
@Platform(include={"../include/JiviX/mesh.hpp"}, link="../lib/vulkan-1")
@Name("jvx::Mesh")
//@Name("std::shared_ptr<jvx::Mesh>")
public class Mesh extends Pointer {
    static { Loader.load(); }

    //
    public native ByteVector getBindingBuffer(long bindingID);
    public native ByteVector getBindingBuffer();
    public native ByteVector getIndexBuffer();
    public native LongPointer getBindingMemoryHandle(long bindingID);
    public native LongPointer getBindingMemoryHandle();

    //
    public native Mesh setMaterialID(int materialID);

    // mostly based on 64-bit pointers
    public native Mesh addBinding(                        @ByRef ByteVector  rawData, @Cast("vkh::VkVertexInputBindingDescription*") @ByRef long bindingPtr);
    public native Mesh addBinding(             @StdVector @ByRef BytePointer rawData, @Cast("vkh::VkVertexInputBindingDescription*") @ByRef long bindingPtr);
    public native Mesh addBinding(@Cast("GLuint")                       int  rawData, @Cast("vkh::VkVertexInputBindingDescription*") @ByRef long bindingPtr); // OpenGL-Based
    public native Mesh addBinding(@Cast("vkh::VkDescriptorBufferInfo*") long rawData, @Cast("vkh::VkVertexInputBindingDescription*") @ByRef long bindingPtr); // Vulkan-Object-Less
}
