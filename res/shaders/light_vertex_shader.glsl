#version 330 core

uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform mat4 modelMatrix;

layout(location = 0) in vec3 in_Position;
layout(location = 1) in vec2 in_TexCoord;
layout(location = 2) in vec3 in_Normals;

out vec2 pass_TexCoord;
out vec3 pass_Normal;
out vec3 pass_Vertices;

void main() 
{
	gl_Position = projectionMatrix * viewMatrix * modelMatrix * vec4(in_Position, 1.0);
	
	pass_TexCoord = in_TexCoord;
	pass_Normal = in_Normals;
	pass_Vertices = vec3(modelMatrix * vec4(in_Position, 1));
}