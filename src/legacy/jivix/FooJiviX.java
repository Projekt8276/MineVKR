//package JiviX;

import org.bytedeco.javacpp.*;
import org.bytedeco.javacpp.annotation.*;

@Platform(include={"<algorithm>", "JiviX.h"})
@Namespace("std")
public class FooJiviX {
    static { Loader.load(); }

    public static class Callback extends FunctionPointer {
        // Loader.load() and allocate() are required only when explicitly creating an instance
        static { Loader.load(); }
        protected Callback() { allocate(); }
        private native void allocate();

        public @Name("jvx_foo") int call(int a, int b) { 
            return a * b;
        }
    }

    // We can also pass (or get) a FunctionPointer as argument to (or return value from) other functions
    public static native void stable_sort(IntPointer first, IntPointer last, Callback compare);

    // And to pass (or get) it as a C++ function object, annotate with @ByVal or @ByRef
    public static native void sort(IntPointer first, IntPointer last, @ByVal Callback compare);
}
