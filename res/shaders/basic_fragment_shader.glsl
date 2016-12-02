#version 330 core

uniform vec3 rgbColor;

layout(location = 0) out vec4 fragColor;

void main()
{
	fragColor = vec4(rgbColor, 1.0);
}