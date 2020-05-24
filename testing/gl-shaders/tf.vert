#version 460 compatibility

layout (location = 0) in vec3 aPos;      // glVertexPointer   => glVertexAttribPointer(0)
layout (location = 1) in vec2 aTexcoord; // glTexcoordPointer => glVertexAttribPointer(1)


layout (location = 0) out vec3 oPos;


void main() {
    gl_Position = vec4(oPos = aPos, 1.f);
}
