#version 300 es

precision mediump float;

uniform vec4 rgbaColor;

layout(location = 0) out vec4 fragColor;

void main()
{
	fragColor = rgbaColor;
}
