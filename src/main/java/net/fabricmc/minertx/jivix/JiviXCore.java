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
        "./include/JiviX/JVI/JiviX.hpp"
}, link="./lib/vulkan-1", define={"ENABLE_OPENGL_INTEROP","WIN32","OS_WIN","VK_ENABLE_BETA_EXTENSIONS","VK_USE_PLATFORM_WIN32_KHR","VMA_IMPLEMENTATION"})

@Name("")
public class JiviXCore extends Pointer {
    static { Loader.load(); }

    @Name("jvi::Context")
    public static class Context extends Pointer {
        static { Loader.load(); }

        public Context() { allocate(); }
        private native void allocate();
    };

    @Name("jvi::Thread")
    public static class Thread extends Pointer {
        static { Loader.load(); }

        public Thread() { allocate(); }
        private native void allocate();
    };

    @Name("jvi::Driver")
    public static class Driver extends Pointer {
        static { Loader.load(); }

        public Driver() { allocate(); }
        private native void allocate();
    };

    @Name("jvi::BufferViewSet")
    public static class BufferViewSet extends Pointer {
        static { Loader.load(); }

        public BufferViewSet() { allocate(); }
        private native void allocate();

        public BufferViewSet(Context context) { allocate(context); }
        private native void allocate(Context context);

    };

    @Name("jvi::MeshInput")
    public static class MeshInput extends Pointer {
        static { Loader.load(); }

        public MeshInput(Context context) { allocate(context); }
        private native void allocate(Context context);

        public MeshInput() { allocate(); }
        private native void allocate();

    };

    @Name("jvi::MeshBinding")
    public static class MeshBinding extends Pointer {
        static { Loader.load(); }

        public MeshBinding() { allocate(); }
        private native void allocate();

        public MeshBinding(Context context) { allocate(context); }
        private native void allocate(Context context);

    };

    @Name("jvi::Node")
    public static class Node extends Pointer {
        static { Loader.load(); }

        public Node() { allocate(); }
        private native void allocate();

        public Node(Context context) { allocate(context); }
        private native void allocate(Context context);

    };

    @Name("jvi::Material")
    public static class Material extends Pointer {
        static { Loader.load(); }

        public Material() { allocate(); }
        private native void allocate();

        public Material(Context context) { allocate(context); }
        private native void allocate(Context context);

    };

    @Name("jvi::Renderer")
    public static class Renderer extends Pointer {
        static { Loader.load(); }

        public Renderer() { allocate(); }
        private native void allocate();

        public Renderer(Context context) { allocate(context); }
        private native void allocate(Context context);

    };

};
