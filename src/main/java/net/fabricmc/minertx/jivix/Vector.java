package net.fabricmc.minertx.jivix;

import org.bytedeco.javacpp.BytePointer;
import org.bytedeco.javacpp.Loader;
import org.bytedeco.javacpp.Pointer;
import org.bytedeco.javacpp.PointerPointer;
import org.bytedeco.javacpp.annotation.*;

@Platform(include={"./include/vkt2/vector.hpp"}, link="./lib/vulkan-1", define={"ENABLE_OPENGL_INTEROP","WIN32","OS_WIN","VK_ENABLE_BETA_EXTENSIONS","VK_USE_PLATFORM_WIN32_KHR"})
public class Vector {

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

};
