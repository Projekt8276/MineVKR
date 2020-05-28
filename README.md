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
- Support OS      : Windows 10 (2004 or Newer)
- Support HW      : RTX 2070 or familiar...
- Extensions      : WIP
- Project Type    : Experimental, Personal
- Portfolio       : Planned
- Ray-Tracing     : Planned
- Path-Tracing    : For Diffuse Only, (almost) noiseless
- Role/Target     : Simplify Vulkan API, alternate of Vookoo...
- License         : BSD-3 (Currently)
- Graphical API   : Vulkan, DirectX 12 (Vulkan-HPP over D3D12)
- DirectX Support : Planned
- Target Games    : Minecraft Java Edition
- Code Language   : C++20, Java, Visual Studio 2019 Support
- Commercial      : Under Consideration
- Execute Day     : [2021:Q2]
- Based On        : C++20, Java, STL, JavaCPP, lwjgl-3-vulkan
- General To Do   : Rewrite Vulkan and D3D12 library set...
- Testing On GPU  : NVIDIA GeForce RTX 2070 (Gigabyte)

Date:[21.02.2020]:[helix.d@IMN]
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


## Authors P.S.

- helix.d@IMN
- RTX@NVIDIA
- RDNA2@AMD
- [`qouteall`](https://github.com/qouteall)     (FOR MENTION)
- [`maierfelix`](https://github.com/maierfelix) (FOR MENTION)

It seems that only extremely experienced souls Frankenstein can survive in this 'real' (and virtual) world...
