package com.helixd2s.jivix

import org.bytedeco.javacpp.annotation.SharedPtr
import org.lwjgl.vulkan.VkBufferCreateInfo
import org.lwjgl.vulkan.VkImageCreateInfo

import org.bytedeco.javacpp.BytePointer
import org.bytedeco.javacpp.FloatPointer
import org.bytedeco.javacpp.Loader
import org.bytedeco.javacpp.Pointer
import org.bytedeco.javacpp.annotation.*
import org.bytedeco.javacpp.indexer.ByteBufferIndexer
import org.bytedeco.javacpp.indexer.UByteBufferIndexer

class JiviX {

    //
    open class Device() {
        open lateinit var core: JiviXCore.Device;
        constructor(core: JiviXCore.Device) { this.core = core; };
    }

    //
    open class Instance() {
        open lateinit var core: JiviXCore.Instance;
        constructor(core: JiviXCore.Instance) { this.core = core; };
    }

    // MOST HARDCORE!
    open class MaterialUnit {
        open lateinit var core: JiviXCore.MaterialUnit;
        constructor(core: JiviXCore.MaterialUnit) { this.core = core; };


        var diffuse: FloatArray
            get() {
                var data = this.core.diffuse();
                var array = FloatArray(4); for (i: Int in 0 until 4) array[i] = data.get(i.toLong());
                return array;
            }
            set(value) {
                var data = this.core.diffuse(); for (i: Int in 0 until 4) data.put(i.toLong(), value[i]);
            }

        var specular: FloatArray
            get() {
                var data = this.core.specular();
                var array = FloatArray(4); for (i: Int in 0 until 4) array[i] = data.get(i.toLong());
                return array;
            }
            set(value) {
                var data = this.core.specular(); for (i: Int in 0 until 4) data.put(i.toLong(), value[i]);
            }

        var normals: FloatArray
            get() {
                var data = this.core.normals();
                var array = FloatArray(4); for (i: Int in 0 until 4) array[i] = data.get(i.toLong());
                return array;
            }
            set(value) {
                var data = this.core.normals(); for (i: Int in 0 until 4) data.put(i.toLong(), value[i]);
            }

        var emission: FloatArray
            get() {
                var data = this.core.emission();
                var array = FloatArray(4); for (i: Int in 0 until 4) array[i] = data.get(i.toLong());
                return array;
            }
            set(value) {
                var data = this.core.emission(); for (i: Int in 0 until 4) data.put(i.toLong(), value[i]);
            }

        var diffuseTexture: Int
            get( ) { return this.core.diffuseTexture().get(0).toInt() }
            set(value) { this.core.diffuseTexture().put(0L, value.toInt()) }

        var specularTexture: Int
            get( ) { return this.core.specularTexture().get(0).toInt() }
            set(value) { this.core.specularTexture().put(0L, value.toInt()) }

        var normalsTexture: Int
            get( ) { return this.core.normalsTexture().get(0).toInt() }
            set(value) { this.core.normalsTexture().put(0L, value.toInt()) }

        var emissionTexture: Int
            get( ) { return this.core.emissionTexture().get(0).toInt() }
            set(value) { this.core.emissionTexture().put(0L, value.toInt()) }
    }

    //
    open class VsGeometryInstance {
        open var core: JiviXCore.VsGeometryInstance = JiviXCore.VsGeometryInstance();

        constructor(data: JiviXCore.VsGeometryInstance){
            this.core = data;
        }

        // TODO: MAKE REAL ARRAY!
        var transform: FloatPointer? = FloatPointer(1.0F,0.0F,0.0F,0.0F, 0.0F,1.0F,0.0F,0.0F, 0.0F,0.0F,1.0F,0.0F)
            get ( ) { return this.core.transform(); }

        var mask: UByte
            get( ) { return this.core.mask().get(0).toUByte() }
            set(value) { this.core.mask().put(0L, value.toByte()) }

        var flags: UByte
            get( ) { return this.core.flags().get(0).toUByte() }
            set(value) { this.core.flags().put(0L, value.toByte()) }

        var instanceId: UInt
            get( ) { return this.core.instanceId().get(0).toUInt().and(0xFFFFFFU) }
            set(value) { this.core.instanceId().put(0L, value.and(0xFFFFFFU).toInt()) }

        var instanceOffset: UInt
            get( ) { return this.core.instanceOffset().get(0).toUInt().and(0xFFFFFFU) }
            set(value) { this.core.instanceOffset().put(0L, value.and(0xFFFFFFU).toInt()) }
    }

    // WARNING! ANY STRUCT SHOULD TO BE ALREADY INITIALIZED!
    open class VmaMemoryInfo() {
        open var core: JiviXCore.VmaMemoryInfo = JiviXCore.VmaMemoryInfo();

        constructor(info: JiviXCore.VmaMemoryInfo){
            this.core = info;
        }

        var memUsage: UInt
            set(value) { core.memUsage().put(0, value.toInt()) }
            get() { return core.memUsage().get(0).toUInt(); }

        var deviceDispatch: Device
            get() { return Device(core.getDeviceDispatch()); }
            set(value) { core.setDeviceDispatch(value.core); };

        var instanceDispatch: Instance
            get() { return Instance(core.getInstanceDispatch()); }
            set(value) { core.setInstanceDispatch(value.core); };
    }

    //
    open class VmaBufferAllocation() {
        open lateinit var core: JiviXCore.VmaBufferAllocation;

        //
        constructor(allocation: JiviXCore.VmaBufferAllocation) {
            this.core = allocation;
        }

        // LWJGL-3 here is now used!
        constructor(vmaAllocator: ULong, bufferCreateInfo: VkBufferCreateInfo, vmaMemInfo: VmaMemoryInfo){
            this.core = JiviXCore.VmaBufferAllocation(vmaAllocator.toLong(), bufferCreateInfo.address(), vmaMemInfo.core);
        };

    }

    //
    open class VmaImageAllocation() {
        open lateinit var core: JiviXCore.VmaImageAllocation;

        //
        constructor(allocation: JiviXCore.VmaImageAllocation) {
            this.core = allocation;
        }

        // LWJGL-3 here is now used!
        constructor(vmaAllocator: ULong, imageCreateInfo: VkImageCreateInfo, vmaMemInfo: VmaMemoryInfo){
            this.core = JiviXCore.VmaImageAllocation(vmaAllocator.toLong(), imageCreateInfo.address(), vmaMemInfo.core);
        };

    }

    //
    open class ImageRegion() {
        open lateinit var core: JiviXBase.ImageRegion;

        //
        constructor(region: JiviXBase.ImageRegion) {
            this.core = region;
        }

        // JavaCPP Have NO ULong, so NEEDS BI-DIRECTIONAL PER-BITS Conversion Between LONG and ULONG! (i.e. ULONG -> LONG -> ULONG WITHOUT any data loss)
        constructor(allocation: VmaImageAllocation, imageCreateInfo: VkImageCreateInfo, layout: UInt) {
            this.core = JiviXBase.ImageRegion(allocation.core, imageCreateInfo.address(), layout.toInt());
        }
    }

    //
    open class ByteVector() {
        open lateinit var core: JiviXBase.ByteVector;

        constructor(vector: JiviXBase.ByteVector) {
            this.core = vector;
        }

        // JavaCPP Have NO ULong, so NEEDS BI-DIRECTIONAL PER-BITS Conversion Between LONG and ULONG! (i.e. ULONG -> LONG -> ULONG WITHOUT any data loss)
        constructor(allocation: VmaBufferAllocation, offset: ULong, range: ULong) {
            this.core = JiviXBase.ByteVector(allocation.core, offset.toLong(), range.toLong());
        }

        // TODO: Indexer Wrapper For Kotlin!
        open fun indexer(): ByteBufferIndexer {
            return core.getIndexer();
        }

        // TODO: FULL REFERENCE SUPPORT
        open operator fun get(index: Long): Byte {
            return core.data().get(index);
        }

        open operator fun set(index: Long, value: Byte) {
            core.data().put(index, value);
        }

        open fun size(): ULong { return this.core.size().toULong(); };
        open fun range(): ULong { return this.core.range().toULong(); };

        open fun data(): BytePointer {
            return core.data();
        }
    }

    //
    open class UByteVector() {
        open lateinit var core: JiviXBase.UByteVector;

        constructor(vector: JiviXBase.UByteVector) {
            this.core = vector;
        }

        // JavaCPP Have NO ULong, so NEEDS BI-DIRECTIONAL PER-BITS Conversion Between LONG and ULONG! (i.e. ULONG -> LONG -> ULONG WITHOUT any data loss)
        constructor(allocation: VmaBufferAllocation, offset: ULong, range: ULong) {
            this.core = JiviXBase.UByteVector(allocation.core, offset.toLong(), range.toLong());
        }

        // TODO: Indexer Wrapper For Kotlin!
        open fun indexer(): UByteBufferIndexer {
            return core.getIndexer();
        }

        // TODO: FULL REFERENCE SUPPORT
        open operator fun get(index: Long): UByte { // SHOULD TO BE LOSSLESS (Bit In Bit!)
            return core.data().get(index).toUByte();
        }

        open operator fun set(index: Long, value: UByte) { // SHOULD TO BE LOSSLESS (Bit In Bit!)
            core.data().put(index, value.toByte());
        }

        open fun size(): ULong { return this.core.size().toULong(); };
        open fun range(): ULong { return this.core.range().toULong(); };

        open fun data(): BytePointer {
            return core.data();
        }
    }

    //
    open class Driver() {
        open lateinit var core: JiviXBase.Driver;

        constructor(obj: JiviXBase.Driver) { this.core = obj; }
        constructor(obj: JiviXCore.Driver) { this.core = JiviXBase.Driver(obj); }

        open fun allocator(): ULong { return this.core._getAllocator().address().toULong(); }
        open fun memoryProperties(): ULong { return this.core._getMemoryProperties().address().toULong(); }

        val deviceDispatch: Device //= Device()
            get() { return Device(core.getDeviceDispatch()); }
            //set(value) { core.setDeviceDispatch(value.core); };

        val instanceDispatch: Instance //= Instance()
            get() { return Instance(core.getInstanceDispatch()); }
            //set(value) { core.setInstanceDispatch(value.core); };
    }

    //
    open class Context() {
        open lateinit var core: JiviXBase.Context;

        constructor(obj: JiviXBase.Context) { this.core = obj; }
        constructor(obj: JiviXCore.Context) { this.core = JiviXBase.Context(obj); }

        constructor(driver: Driver) { this.core = JiviXBase.Context(driver.core); }

        // Core Value
        open fun sharedPtr(): JiviXCore.Context { return core.sharedPtr(); }
    }

    //
    open class Thread() {
        open lateinit var core: JiviXBase.Thread;

        constructor(obj: JiviXBase.Thread) { this.core = obj; }
        constructor(obj: JiviXCore.Thread) { this.core = JiviXBase.Thread(obj); }

        constructor(driver: Driver) { this.core = JiviXBase.Thread(driver.core); }

        // Core Value
        open fun sharedPtr(): JiviXCore.Thread { return core.sharedPtr(); }
    }

    //
    open class BufferViewSet() {
        open lateinit var core: JiviXBase.BufferViewSet;

        constructor(obj: JiviXBase.BufferViewSet) { this.core = obj; }
        constructor(obj: JiviXCore.BufferViewSet) { this.core = JiviXBase.BufferViewSet(obj); }

        constructor(context: Context) { this.core = JiviXBase.BufferViewSet(context.core); }

        // Core Value
        open fun sharedPtr(): JiviXCore.BufferViewSet { return core.sharedPtr(); }
        open fun bufferCount(): ULong { return this.core.getBufferCount().toULong(); }

        //
        open operator fun get(index: Long): JiviXBase.UByteVector? {
            return core.get(index.toInt()); // TODO: Fix Index Type
        }

        //
        open fun pushBufferView(buf: JiviXBase.UByteVector?): Long {
            return core.pushBufferView(buf);
        }

        //
        open fun descriptorSet(): ULong{ return core.getDescriptorSet().toULong(); };
        open fun descriptorLayout(): ULong{ return core.getDescriptorLayout().toULong(); };


    }

    //
    open class MeshInput() {
        open lateinit var core: JiviXBase.MeshInput;

        constructor(obj: JiviXBase.MeshInput) {this.core = obj; }
        constructor(obj: JiviXCore.MeshInput) {this.core = JiviXBase.MeshInput(obj); }

        constructor(context: Context) { this.core = JiviXBase.MeshInput(context.core); }

        // Core Value
        open fun sharedPtr(): JiviXCore.MeshInput { return core.sharedPtr(); }
    }

    //
    open class MeshBinding() {
        open lateinit var core: JiviXBase.MeshBinding;

        constructor(obj: JiviXBase.MeshBinding) {this.core = obj; }
        constructor(obj: JiviXCore.MeshBinding) {this.core = JiviXBase.MeshBinding(obj); }

        // Core Value
        open fun sharedPtr(): JiviXCore.MeshBinding { return core.sharedPtr(); }

        // Unified Constructor
        constructor(context: Context, maxPrimitiveCount: ULong?, perGeometryCount: ULongArray?) {
            if (maxPrimitiveCount != null) {
                if (perGeometryCount != null) {
                    this.core = JiviXBase.MeshBinding(context.core, maxPrimitiveCount.toLong(), perGeometryCount.toLongArray())
                } else {
                    this.core = JiviXBase.MeshBinding(context.core, maxPrimitiveCount.toLong());
                }
            } else {
                this.core = JiviXBase.MeshBinding(context.core);
            };
        };

        //
        open fun bindingBuffer(idx: ULong = 0UL): UByteVector { return UByteVector(core.getBindingBuffer(idx.toLong())); };
        open fun bindingBufferGL(idx: ULong = 0UL): UInt { return core.getBindingBufferGL(idx.toLong()).toUInt() };

        // Instanced, but same material
        open fun addMeshInput(input: MeshInput, materialID: UInt, instances: ULong?): MeshBinding {
            if (instances != null) {
                return MeshBinding(this.core.addMeshInput(input.core, materialID.toInt(), instances.toLong()));
            } else {
                return MeshBinding(this.core.addMeshInput(input.core, materialID.toInt()));
            }
        }

        // Material Array
        open fun addMeshInput(input: MeshInput, materialID: UIntArray): MeshBinding {
            return MeshBinding(this.core.addMeshInput(input.core, materialID.toIntArray()));
        }
    }

    //
    open class Node() {
        open lateinit var core: JiviXBase.Node;

        constructor(obj: JiviXBase.Node) {this.core = obj; }
        constructor(obj: JiviXCore.Node) {this.core = JiviXBase.Node(obj); }

        constructor(context: Context) { this.core = JiviXBase.Node(context.core); }

        // Core Value
        open fun sharedPtr(): JiviXCore.Node { return core.sharedPtr(); }

        open fun pushInstance(instance: ULong): Node { return Node(core.pushInstance(instance.toLong())); };
        open fun pushInstance(instance: VsGeometryInstance): Node {return Node(core.pushInstance(instance.core)); };

        open fun pushMesh(binding: MeshBinding): ULong {
            return core.pushMesh(binding.core).toULong();
        }
    }

    //
    open class Renderer() {
        open lateinit var core: JiviXBase.Renderer;

        constructor(obj: JiviXBase.Renderer) {this.core = obj; }
        constructor(obj: JiviXCore.Renderer) {this.core = JiviXBase.Renderer(obj); }

        constructor(context: Context) { this.core = JiviXBase.Renderer(context.core); }

        // Core Value
        open fun sharedPtr(): JiviXCore.Renderer { return core.sharedPtr(); }
    }

    //
    open class Material() {
        open lateinit var core: JiviXBase.Material;

        constructor(obj: JiviXBase.Material) {this.core = obj; }
        constructor(obj: JiviXCore.Material) {this.core = JiviXBase.Material(obj); }

        constructor(context: Context) { this.core = JiviXBase.Material(context.core); }

        // Core Value
        open fun sharedPtr(): Material { return Material(core.sharedPtr()); }

        // 
        open fun pushMaterial(unit: MaterialUnit): Long {
            return core.pushMaterial(unit.core);
        }
    }


}
