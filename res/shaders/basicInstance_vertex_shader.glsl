#version 330 core

precision mediump float;

layout (location = 0) in vec3 in_Position;
layout (location = 1) in vec2 in_TexCoord;
layout (location = 3) in mat4 in_MVP;

void main(void)
{

	gl_Position = in_MVP * vec4(in_Position, 1.0);
}
