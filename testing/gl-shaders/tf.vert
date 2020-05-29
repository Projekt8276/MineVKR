#version 460 compatibility

layout (location = 0) in vec3 aPos;      // glVertexPointer   => glVertexAttribPointer(0)
layout (location = 1) in vec2 aTexcoord; // glTexcoordPointer => glVertexAttribPointer(1)
layout (location = 2) in vec3 aNormal;
layout (location = 3) in vec4 aColor;

layout (location = 0) out vec3 oPos;
layout (location = 1) out vec2 oTexcoord;
layout (location = 2) out vec4 oNormal;
//layout (location = 3) out vec3 oColor;

layout (location = 0) uniform mat4 modelView;
layout (location = 1) uniform mat4 texmatric;
layout (location = 2) uniform mat3 normalmat;

// 
void main() {
    gl_Position = vec4(oPos = (vec4(aPos,1.f)*modelView).xyz, 1.f);
    oTexcoord = (vec4(aTexcoord,0.f,1.f) * texmatric).xy;
    oNormal = vec4(aNormal,uintBitsToFloat(packUnorm4x8(aColor))); // Do NOT interpolate W for Fragment Shader, because needs `unpackUnorm4x8(floatBitsToUint())`
}
