#version 330 core

uniform sampler2D tex;
uniform vec4 rgbaColor;

uniform mat4 modelMatrix;

uniform vec3 ambColor;
uniform vec3 ambIntensity;

uniform vec3 lightPos;

in vec2 pass_TexCoord;
in vec3 pass_Normal;
in vec3 pass_Vertices;

layout(location = 0) out vec4 fragColor;

void main() 
{

	mat4 normalMatrix = transpose(inverse(modelMatrix));

	vec4 textureColor = texture(tex, pass_TexCoord);
	vec4 totalLight = vec4(ambIntensity, 1);
	vec4 color = vec4(ambColor, 1);

	mat3 modelM = mat3(normalMatrix);
	vec3 calcNorm = normalize(modelM * pass_Normal);

	vec3 surfaceL = normalize(lightPos - pass_Vertices);
	vec3 surfaceC = normalize(vec3(0,0,0) - pass_Vertices);
	
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