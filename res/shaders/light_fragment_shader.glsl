#version 330

precision mediump float;

uniform sampler2D tex;

uniform mat4 modelMatrix;
uniform vec3 cameraPos;

in vec2 pass_TexCoord;
in vec3 pass_Normal;
in vec3 pass_Vertices;

uniform struct Light {
	vec3 lightPos;
	vec3 lightColor;
	vec3 ambIntensity;
	float attFactor;
} light;

layout(location = 0) out vec4 fragColor;

void main() 
{

	// Calculate normal
	mat4 normalMatrix = transpose(inverse(modelMatrix));
	mat3 modelM = mat3(normalMatrix);
	vec3 calcNorm = normalize(modelM * pass_Normal);

	// Texture light
	vec4 textureColor = texture(tex, pass_TexCoord);

	// normalized distances between sources
	vec3 surfaceL = normalize(light.lightPos - pass_Vertices);
	vec3 surfaceC = normalize(cameraPos - pass_Vertices);
	
	// Ambient light
	vec3 ambient = light.ambIntensity * textureColor.xyz * light.lightColor;
	
	// Diffuse light
	float diffuseC = max(0.0, dot(calcNorm, surfaceL));
	vec3 diffuse = diffuseC * textureColor.xyz * light.lightColor;
	
	// Specular light
	float specularC = 0.0;
	
	if(diffuseC > 0.0)
	{
		specularC = pow(max(0.0, dot(surfaceC, reflect(-surfaceL, calcNorm))), 256); 
	}
	
	vec3 specular = specularC * light.lightColor;
	
	// Attenuation
	float distanceToLight = length(light.lightPos - pass_Vertices);
	float attenuation = 1.0 / (1.0 + light.attFactor * pow(distanceToLight, 2));
	
	// Color before gamma correction
	vec3 color = ambient + attenuation * (diffuse + specular);
	
	// Gamma correction
	vec3 gamma = vec3(1.0/0.8);
	vec4 finalColor = vec4(pow(color, gamma), textureColor.w);
	
	fragColor = finalColor;
}