//package JiviX;

import org.bytedeco.javacpp.*;
import org.bytedeco.javacpp.annotation.*;

@Platform(include={"JiviX.h", "jniFooJiviX.h"}, link="jniFooJiviX")
@Namespace("JiviX")
@Name("JiviX")
public class JiviX {
    static { Loader.load(); }

    public static native void process(java.nio.Buffer buffer, int size);

    public static void main(String[] args) {
        process(null, 0);
    }
}
