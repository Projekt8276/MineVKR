#version 460 compatibility // #

layout ( location = 0 ) out vec4 uFragColor;
layout ( location = 0 ) in vec2 vcoord;
layout ( binding = 0 ) uniform sampler2D texture0;

// 
void main() { // TODO: explicit sampling 
    uFragColor = texture(texture0, vcoord);
};
