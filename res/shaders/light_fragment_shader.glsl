#version 330 core

precision mediump float;

uniform sampler2D tex;

uniform mat4 modelMatrix;
uniform vec3 cameraPos;

in vec2 pass_TexCoord;
in vec3 pass_Normal;
in vec3 pass_Vertices;

uniform struct PointLight {
	vec3 lightPos;
	vec3 lightColor;
	vec3 ambIntensity;
	
	float attFactor;
} pointLight;

uniform struct DirectionalLight {
	vec3 lightDir;
	vec3 lightColor;
	vec3 ambIntensity;
} dirLight;

uniform struct SpotLight {
	vec3 lightPos;
	vec3 lightColor;
	vec3 ambIntensity;

	vec3 lightDir;
	float attFactor;
	float coneAngle;
} spotLight;

uniform int number_of_point_lights;
uniform int number_of_directional_lights;
uniform int number_of_spot_lights;

// There is a maximum of 10 lights
uniform PointLight pointLights[10];
uniform DirectionalLight dirLights[10];
uniform SpotLight spotLights[10];

layout(location = 0) out vec4 fragColor;

vec3 calcPointLight(PointLight light, vec4 textureColor, vec3 calcNorm, vec3 pass_Vertices, vec3 surfaceC)
{
	
	// normalized distances between sources
	vec3 surfaceL = normalize(light.lightPos - pass_Vertices);
	
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

	return ambient + attenuation * (diffuse + specular);
}

vec3 calcDirLight(DirectionalLight light, vec4 textureColor, vec3 calcNorm, vec3 pass_Vertices, vec3 surfaceC)
{

	// Get the direction of the light
	vec3 lightDirection = light.lightDir;

	// normalized direction of directional light
	vec3 surfaceL = normalize(lightDirection);
	
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

	return ambient + diffuse + specular;
}

vec3 calcSpotLight(SpotLight light, vec4 textureColor, vec3 calcNorm, vec3 pass_Vertices, vec3 surfaceC)
{

	// Get the direction of the cone
	vec3 coneDirection = light.lightDir;	

	// normalized distances between sources
	vec3 surfaceL = normalize(light.lightPos - pass_Vertices);

	// Lighting is calculated in the same way as a point light, only now we take a piece of the point light via a cone
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
	
	// Calculate the angle
	float lightToSurfaceAngle = degrees(acos(dot(-surfaceL, normalize(coneDirection))));	

	// Attenuation
	float distanceToLight = length(light.lightPos - pass_Vertices);
	float attenuation = 1.0 / (1.0 + light.attFactor * pow(distanceToLight, 2));

	// Set the attenuation to zero if outside the cone
	if(lightToSurfaceAngle > light.coneAngle)
	{
		attenuation = 0.0;	
	}
	
	return ambient + attenuation * (diffuse + specular);
}

void main() 
{

	// Calculate normal
	mat4 normalMatrix = transpose(inverse(modelMatrix));
	mat3 modelM = mat3(normalMatrix);
	vec3 calcNorm = normalize(modelM * pass_Normal);

	// Texture light
	vec4 textureColor = texture(tex, pass_TexCoord);

	// Normalized distance between camera and vertex
	vec3 surfaceC = normalize(cameraPos - pass_Vertices);

	vec3 color = vec3(0, 0, 0);

	// Color before gamma correction (from every light)
	// First the point light contribution
	for(int i = 0; i < number_of_point_lights; i++)
	{
		color += calcPointLight(pointLights[i], textureColor, calcNorm, pass_Vertices, surfaceC);
	}
	
	// The directional light contribution
	for(int i = 0; i < number_of_directional_lights; i++)
	{
		color += calcDirLight(dirLights[i], textureColor, calcNorm, pass_Vertices, surfaceC);
	}

	// The spot light contribution
	for(int i = 0; i < number_of_spot_lights; i++)
	{
		color += calcSpotLight(spotLights[i], textureColor, calcNorm, pass_Vertices, surfaceC);
	}

	// Gamma correction
	vec3 gamma = vec3(1.0/1.0);
	vec4 finalColor = vec4(pow(color, gamma), textureColor.w);
	
	fragColor = finalColor;
}