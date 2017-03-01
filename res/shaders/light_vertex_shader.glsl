#version 330 core

precision mediump float;

uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform mat4 modelMatrix;

uniform mat4 lightProjectionMatrix;
uniform mat4 lightViewMatrix;
uniform mat4 biasMatrix;

layout(location = 0) in vec3 in_Position;
layout(location = 1) in vec2 in_TexCoord;
layout(location = 2) in vec3 in_Normals;

out vec2 pass_TexCoord;
out vec3 pass_Normal;
out vec3 pass_Vertices;

out vec4 depth_Coords;

void main() 
{

	vec4 worldPosition = modelMatrix * vec4(in_Position, 1.0);
	gl_Position = projectionMatrix * viewMatrix * worldPosition;
	
	mat3 normalMatrix = transpose(inverse(mat3(modelMatrix))) ;
	pass_TexCoord = in_TexCoord;

	pass_Normal = normalMatrix * in_Normals;
	pass_Vertices = (worldPosition).xyz;

	depth_Coords = lightProjectionMatrix * lightViewMatrix * modelMatrix * vec4(in_Position, 1.0);
	depth_Coords = biasMatrix * depth_Coords;
}
