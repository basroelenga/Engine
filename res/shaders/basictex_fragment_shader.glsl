#version 330 core

uniform float cutoff;

uniform sampler2D sampler;

layout(location = 0) out vec4 fragColor;

in vec2 pass_TexCoord;

void main()
{
	vec4 tex = texture(sampler, pass_TexCoord);

	fragColor = tex;
}