#version 300 es

precision mediump float;

in vec3 pass_Vertices;
in vec3 pass_Normals;

uniform mat4 modelMatrix;

uniform vec3 ambColor;
uniform vec3 ambIntensity;

uniform vec3 lightPos;

layout(location = 0) out vec4 fragColor;

void main()
{

	float r = pass_Vertices.y / 2.0 + pass_Normals.y / 6.0;
	float g = 0.80;
	float b = pass_Vertices.y / 2.0 + pass_Normals.y / 6.0;
	
	vec3 baseColor = vec3(r, g, b);
	vec3 pass_VerticesT = vec3(modelMatrix * vec4(pass_Vertices, 1)); 
	
	mat4 normalMatrix = transpose(inverse(modelMatrix));

	vec4 textureColor = vec4(baseColor, 1);
	vec4 totalLight = vec4(ambIntensity, 1);
	vec4 color = vec4(ambColor, 1);

	mat3 modelM = mat3(normalMatrix);
	vec3 calcNorm = normalize(modelM * pass_Normals);

	vec3 surfaceL = normalize(lightPos - pass_VerticesT);
	vec3 surfaceC = normalize(vec3(0,0,0) - pass_VerticesT);
	
	float diffuse = max(0.0, dot(calcNorm, surfaceL));
	
	float specularC = 0.0;
	
	if(diffuse > 0.0)
	{
		specularC = pow(max(0.0, dot(surfaceC, reflect(-surfaceL, calcNorm))), 256); 
	}
	
	color *= textureColor;

	totalLight += (diffuse + specularC);;

	vec4 colorF = color * totalLight;

	fragColor = colorF;
}