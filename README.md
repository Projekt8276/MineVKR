# CURRENTLY, IN DEVELOPMENT!!!

[![License](https://img.shields.io/badge/License-BSD%203--Clause-blue.svg)](https://opensource.org/licenses/BSD-3-Clause)


## Minecraft VKR Mod 1.16...

Planned REAL ray-tracing for Minecraft 1.16 and higher...


## Additional Links

- [JiviX](https://github.com/hyperearth/JiviX), based-on project
- [hE-ART](https://github.com/hyperearth/hE-ART), terminal of projects, central...
- [VKt/VKh](https://github.com/world8th/vkt), simpler and unified helpers and mini-framework for Vulkan API...
- [RadX](https://github.com/world8th/RadX), radix-sorting for RTX graphic cards...
- [My Discord](https://discord.gg/NqjBJsG), but currently we have no focus for them... 
- [My Puzzle](https://vk.cc/afiR3v) for psychologists, psychoanalysts, psychotherapists and students ...


## Tech Spec

```MD
- RHI             : Yes, Vulkan-HPP
- Support HW      : RTX 2070 or familiar...
- Extensions      : WIP
- Project Type    : Experimental, Personal
- Portfolio       : Planned
- Ray-Tracing     : Planned
- Path-Tracing    : For Diffuse Only, (almost) noiseless
- Role/Target     : Simplify Vulkan API, alternate of Vookoo...
- License         : Apache-2.0 (Currently)
- Graphical API   : Vulkan, DirectX 12 (Vulkan-HPP over D3D12)
- DirectX Support : Planned
- Target Games    : Minecraft Java Edition
- Code Language   : C++20, Java, Visual Studio 2019 Support
- Commercial      : Under Consideration
- Execute Day     : [2021:Q2]
- Based On        : C++20, Java, STL, JavaCPP, lwjgl-3-vulkan
- General To Do   : Rewrite Vulkan and D3D12 library set...
- Testing On GPU  : NVIDIA GeForce RTX 2070 (Gigabyte)

Date:[22.06.2020]:[helix.d@IMN]
```


## License

Currently, BSD-3 Clause...


## Other Developers

Any developer with knowledge of C, C ++, Java, as well as GLSL and EVEN HLSL (the most important thing is knowledge of the Vulkan API and OpenGL), can develop this mod with me. At the moment, we need additional strength and consultants in this matter ...


### Ending Fight?

In June or July announces final development on the Minecraft. Also, I waiting release of `Minecraft: Java Edition, 1.16.x`
In least May I should to complete preparing stage (initialization, libraries, engine, kotlin)...


### About Minecraft Mod Idea... 

- Vulkan API can import OpenGL buffer data only using copy, OpenGL can import memory directly from Vulkan API. 
- Minecraft can use Vulkan API using JNI, JavaCPP, LWJGL-3... 
- Always communication using LONG type in Java and uint64_t in C++ 
- Java may have conflicts with `VkDeviceOrHostAddressKHR` or `VkDeviceOrHostAddressConstKHR` due same LONG type... 
- Any native pointers in Java are LONG type... 


### Intellij IDEA Problem AGAIN?!

I again tip to use `gradlew idea` for that IDE... 

I afraid that my mod may become to a second [Immersive Portals](https://github.com/qouteall/ImmersivePortalsMod/tree/1.16), but I want base from Improved First Person... 

I afraid that for me needs [`qouteall`](https://github.com/qouteall), [`maierfelix`](https://github.com/maierfelix) for development... 



### Used Symbolic Links!

Clone with `git clone -c core.symlinks=true https://github.com/hyperearth/MineVKR.git --recursive`


### Project is so HARD!

Even my memory can forget how I did it... 
It will take me a lot of time to do the assembly instructions for this item...


### Work in progress...

It should be understood that this is not only a rewriting of history, but also a leap towards development to a similar level with `daxnitro`... If everything goes more or less smoothly, then perhaps in July we will be able to see the first results... BUT! Normally, you need to rewrite the project, finally implement DirectX 12 Ultimate (because the Vulkan API can only work normally on NVIDIA and Linux, NOT "or")... it's clear that such a fuss will take me not only time, but also moral strength. In addition, I’m really hovering with a possible problem with Optifine (and there’s simply no compatibility), since tying it is quite complicated... and what will be released in July will most likely turn out shit, since mods were so relevant only in the the early era of Minecraft shading... but after all kinds of umsoea, PTGI, Realistic Mats, etc., more than half of my arsenal just fades away from my headache... just writing on Kotlin is not enough (which, however, is still the author of Immersive Portals didn’t, and I would advise the author of Nova Renderer)... this normal path is so long and marathon that I obviously could not cope alone... I'm not talking about such phenomena as normal multithreading, RHI, HAL... I really do not want to turn into another vapor-ware or nova...

- [x] Basic texture managment (`registerTexture` with Vulkan API version one)
- [x] Fix aligment issues (such as `MaterialUnit`)
- [x] Transform Feedback with correct `modelView`, `projection`, `texcoord` matrices... 
- [x] Descriptor getters from `ImageRegion` or `Vector`... 
- [ ] Opaque, translucent and cutout specifier
- [ ] Correct instanced managment
- [ ] Lightmaps support
- [ ] Materials support
- [ ] Entities support
- [ ] Using semaphore
- [ ] Final rendering
- [ ] Block identifier (needs for water or emission)
- [ ] Unified render code
- [ ] Correct skybox rendering
- [ ] Correct weather rendering
- [ ] Clouds rendering
- [ ] HARD! Optifine support


## Authors P.S.

- helix.d@IMN
- RTX@NVIDIA
- RDNA2@AMD
- [`qouteall`](https://github.com/qouteall), Immersive Portals mod author...
- [`maierfelix`](https://github.com/maierfelix), who interesting about Vulkan API ray-tracing...
- [`daxnitro`](https://daxnitro.fandom.com/wiki/Shaders), former author of shaders mod...
- [`dethraid`](https://github.com/dethraid), author of limbed Nova Renderer... 

It seems that only extremely experienced souls Frankenstein can survive in this 'real' (and virtual) world...
