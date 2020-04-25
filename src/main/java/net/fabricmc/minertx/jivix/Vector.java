package net.fabricmc.minertx.jivix;

import org.bytedeco.javacpp.BytePointer;
import org.bytedeco.javacpp.Loader;
import org.bytedeco.javacpp.Pointer;
import org.bytedeco.javacpp.PointerPointer;
import org.bytedeco.javacpp.annotation.*;

@Platform(include={"./include/vkt2/vector.hpp"}, link="./lib/vulkan-1")
public class Vector {

    @Name("vkt::Vector<uint8_t>")
    public static class ByteVector extends Pointer {
        static { Loader.load(); }
        public ByteVector()       { allocate();  }
        public ByteVector(long n) { allocate(n); }
        public ByteVector(Pointer p) { super(p); } // this = (vector<vector<void*> >*)p
        private native void allocate();                  // this = new vector<vector<void*> >()
        private native void allocate(long n);            // this = new vector<vector<void*> >(n)
        @Name("operator=")
        public native @ByRef ByteVector put(@ByRef ByteVector x);

        //@Name("operator[]")
        //public native @StdVector PointerPointer get(long n);
        //public native @StdVector PointerPointer at(long n);

        @Name("operator[]")
        public native @ByRef BytePointer get(long n);
        public native @ByRef BytePointer at(long n);

        public native long size();
        public native long range();

        // map buffer data
        public native BytePointer mapped();
        public native BytePointer map();

        //
        public native @Cast("bool") boolean empty();
        public native void resize(long n);
        public native @Index long size(long i);                   // return (*this)[i].size()
        public native @Index @Cast("bool") boolean empty(long i); // return (*this)[i].empty()
        public native @Index void resize(long i, long n);         // (*this)[i].resize(n)

        public native @Index Pointer get(long i, long j);  // return (*this)[i][j]
        public native void put(long i, long j, Pointer p); // (*this)[i][j] = p
    }
};
