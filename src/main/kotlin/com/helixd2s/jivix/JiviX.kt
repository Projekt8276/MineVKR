package com.helixd2s.jivix

import com.helixd2s.jivix.JiviXBase.*
import com.helixd2s.jivix.JiviXCore.*
import com.helixd2s.jivix.JiviXBase
import com.helixd2s.jivix.JiviXCore
import org.lwjgl.vulkan.VkBufferCreateInfo
import org.lwjgl.vulkan.VkImageCreateInfo

//
open class Device() {
    public lateinit var core: JiviXCore.Device;
    constructor(core: JiviXCore.Device) { this.core = core; };
}

//
open class Instance() {
    public lateinit var core: JiviXCore.Instance;
    constructor(core: JiviXCore.Instance) { this.core = core; };
}

// WARNING! ANY STRUCT SHOULD TO BE ALREADY INITIALIZED!
open class VmaMemoryInfo() {
    public var core: JiviXCore.VmaMemoryInfo = JiviXCore.VmaMemoryInfo();

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
    public lateinit var core: JiviXCore.VmaBufferAllocation;

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
    public lateinit var core: JiviXCore.VmaImageAllocation;

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
    public lateinit var core: JiviXBase.ImageRegion;

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
open class ByteVector {
    public lateinit var core: JiviXBase.ByteVector;

    constructor(vector: JiviXBase.ByteVector) {
        this.core = vector;
    }

    // JavaCPP Have NO ULong, so NEEDS BI-DIRECTIONAL PER-BITS Conversion Between LONG and ULONG! (i.e. ULONG -> LONG -> ULONG WITHOUT any data loss)
    constructor(allocation: VmaBufferAllocation, offset: ULong, range: ULong) {
        this.core = JiviXBase.ByteVector(allocation.core, offset.toLong(), range.toLong());
    }

    // TODO: FULL REFERENCE SUPPORT
    operator fun get(index: Long): Byte {
        return core.at(index).get(0L);
    }

    operator fun set(index: Long, value: Byte) {
        core.at(index).put(0L, value);
    }

}

//
open class UByteVector {
    public lateinit var core: JiviXBase.UByteVector;


    constructor(vector: JiviXBase.UByteVector) {
        this.core = vector;
    }

    // JavaCPP Have NO ULong, so NEEDS BI-DIRECTIONAL PER-BITS Conversion Between LONG and ULONG! (i.e. ULONG -> LONG -> ULONG WITHOUT any data loss)
    constructor(allocation: VmaBufferAllocation, offset: ULong, range: ULong) {
        this.core = JiviXBase.UByteVector(allocation.core, offset.toLong(), range.toLong());
    }

    // TODO: FULL REFERENCE SUPPORT
    operator fun get(index: Long): UByte { // SHOULD TO BE LOSSLESS (Bit In Bit!)
        return core.at(index).get(0L).toUByte();
    }

    operator fun set(index: Long, value: UByte) { // SHOULD TO BE LOSSLESS (Bit In Bit!)
        core.at(index).put(0L, value.toByte());
    }

}


class JiviX {



}
