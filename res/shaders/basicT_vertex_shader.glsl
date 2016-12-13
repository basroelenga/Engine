#version 300 es

precision mediump float;

uniform mat4 modelMatrix;
uniform mat4 viewMatrix;
uniform mat4 projectionMatrix;

layout(location = 0) in vec3 in_Position;
layout(location = 1) in vec3 in_Normals;

out vec3 pass_Vertices;
out vec3 pass_Normals;

void main(void)
{
	
	gl_Position = projectionMatrix * viewMatrix * modelMatrix * vec4(in_Position, 1.0);
	
	pass_Vertices = in_Position;
	pass_Normals = in_Normals;
}