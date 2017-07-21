#version 300 es

in vec3 modelSpaceNormal;

uniform samplerCube cubeTexture;

out vec4 outputColor;

void main()
{
    outputColor = texture(cubeTexture, modelSpaceNormal);
}
