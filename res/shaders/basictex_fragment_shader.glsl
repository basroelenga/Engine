#version 300 es

precision mediump float;

uniform float cutoff;

uniform sampler2D textureSample;
uniform sampler2D depthTextureSample;

layout(location = 0) out vec4 fragColor;

in vec2 pass_TexCoord;

void main()
{
	vec4 tex = texture(textureSample, pass_TexCoord);
	vec4 tex2 = texture(depthTextureSample, pass_TexCoord);

	fragColor = tex;
}
