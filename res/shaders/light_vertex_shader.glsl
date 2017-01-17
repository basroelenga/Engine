#version 330 core

precision mediump float;

uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform mat4 modelMatrix;

layout(location = 0) in vec3 in_Position;
layout(location = 1) in vec2 in_TexCoord;
layout(location = 2) in vec3 in_Normals;

out vec2 pass_TexCoord;
out vec3 pass_Normal;
out vec3 pass_Vertices;
out mat4 v_mv;

void main() 
{

	vec4 worldPosition = modelMatrix * vec4(in_Position, 1.0);
	gl_Position = projectionMatrix * viewMatrix * worldPosition;
	
	v_mv = viewMatrix * modelMatrix;
	pass_TexCoord = in_TexCoord;

	pass_Normal = in_Normals;
	pass_Vertices = (viewMatrix * worldPosition).xyz;
}
