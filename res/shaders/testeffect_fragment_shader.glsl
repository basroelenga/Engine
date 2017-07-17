#version 300 es

precision mediump float;

uniform sampler2D textureSample;
uniform float offset;

in vec2 pass_TexCoord;

layout(location = 0) out vec4 fragColor;

void main() {

  vec2 texcoord = pass_TexCoord;
  texcoord.x += sin(texcoord.y * 4.0*2.0*3.14159 + offset) / 100.0;

  vec4 tex = texture(textureSample, texcoord);
  fragColor = tex;
}
