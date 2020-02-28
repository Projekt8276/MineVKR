package net.fabricmc.minertx.jivix;

import org.bytedeco.javacpp.BytePointer;
import org.bytedeco.javacpp.Loader;
import org.bytedeco.javacpp.Pointer;
import org.bytedeco.javacpp.PointerPointer;
import org.bytedeco.javacpp.annotation.*;

//
@Platform(include={"../include/vkt2/vector.hpp"}, link="../lib/vulkan-1")
@Name("vkt::ByteVector")
public class ByteVector {
    static { Loader.load(); }

    //
    public native long size();
    public native long range();

    // vector-like operations
    @Name("operator[]")
    public native @ByRef BytePointer get(long n);
    public native @ByRef BytePointer at(long n);

    // map buffer data
    public native BytePointer mapped();
    public native BytePointer map();

    // Pukan API assign
    @Name("operator=")
    public native @ByRef ByteVector put(@ByRef ByteVector x);
}
