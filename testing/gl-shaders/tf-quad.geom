#version 460 compatibility

layout (location = 0) in vec3 oPos[];
layout (location = 1) in vec2 oTexcoord[];
layout (location = 2) in vec4 oNormal[];

layout (location = 0, xfb_buffer = 0, xfb_stride = 80, xfb_offset = 0 ) out vec4 fPosition;
layout (location = 1, xfb_buffer = 0, xfb_stride = 80, xfb_offset = 16) out vec4 fTexcoord;
layout (location = 2, xfb_buffer = 0, xfb_stride = 80, xfb_offset = 32) out vec4 fNormal;
layout (location = 3, xfb_buffer = 0, xfb_stride = 80, xfb_offset = 48) out vec4 fTangent;
layout (location = 4, xfb_buffer = 0, xfb_stride = 80, xfb_offset = 64) out vec4 fBinormal;

layout (lines_adjacency) in;
layout (triangle_strip, max_vertices = 4) out;

void main() {
    const vec3 normal = normalize(cross(oPos[1] - oPos[0], oPos[2] - oPos[0]));
    for (int i=0;i<4;i++) {
       gl_Position = vec4(oPos[i].xyz, 1.f);
       fNormal = vec4(normal, oNormal[i].w);
       fPosition = vec4(oPos[i].xyz, 1.0f);
       fTexcoord = vec4(oTexcoord[i], 0.f.xx);
       EmitVertex();
    }
    EndPrimitive();
}
