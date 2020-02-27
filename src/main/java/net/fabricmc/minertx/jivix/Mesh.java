package net.fabricmc.minertx.jivix;

import org.bytedeco.javacpp.Loader;
import org.bytedeco.javacpp.annotation.Name;
import org.bytedeco.javacpp.annotation.Namespace;
import org.bytedeco.javacpp.annotation.Platform;

@Platform(include={"../include/JiviX/mesh.hpp"}, link="../lib/vulkan-1")
@Namespace("jvx")
@Name("Mesh")
public class Mesh {
    static { Loader.load(); }


}
