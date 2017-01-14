#version 330 core

precision mediump float;

uniform vec4 rgbaColor;

layout(location = 0) out vec4 fragColor;

void main()
{
	fragColor = vec4(1.0, 0.0, 0.0, 1.0);
}
