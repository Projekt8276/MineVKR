package net.fabricmc.minertx.jivix;

import org.bytedeco.javacpp.BytePointer;
import org.bytedeco.javacpp.Loader;
import org.bytedeco.javacpp.LongPointer;
import org.bytedeco.javacpp.Pointer;
import org.bytedeco.javacpp.annotation.*;

// TODO: Engine Wrappers for std::shared_ptr, like "vkt::Vector<uint8_t>"
@Platform(include={
        "./include/JiviX/context.hpp",
        "./include/JiviX/driver.hpp",
        "./include/JiviX/renderer.hpp"
}, link="./lib/vulkan-1")
@Namespace("jvx")

public class JiviXBase extends Pointer {
    static { Loader.load(); }

    @Name("jvx::Context")
    public static class Context extends Pointer {
        static { Loader.load(); }
        public Context() { allocate(); }
        private native void allocate();
        public native void createDescriptorSet();
    }

    @Name("jvx::Thread")
    public static class Thread extends Pointer {
        static { Loader.load(); }
        public Thread() { allocate(); }
        private native void allocate();
        public native void createDescriptorSet();
    }

    @Name("jvx::Driver")
    public static class Driver extends Pointer {
        static { Loader.load(); }
        public Driver() { allocate(); }
        private native void allocate();
        public native void createDescriptorSet();
    }

}
