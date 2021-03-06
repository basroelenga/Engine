#version 330 core

precision mediump float;

uniform sampler2D textureSample;
uniform sampler2D depthTextureSample;

uniform vec4 rgbaColor;

uniform mat4 modelMatrix;
uniform vec3 cameraPos;

in vec2 pass_TexCoord;
in vec3 pass_Normal;
in vec3 pass_Vertices;

in vec4 depth_Coords;

vec2 poissonDisk[4] = vec2[](
  vec2( -0.94201624, -0.39906216 ),
  vec2( 0.94558609, -0.76890725 ),
  vec2( -0.094184101, -0.92938870 ),
  vec2( 0.34495938, 0.29387760 )
);

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
	float diffuseC = max(dot(calcNorm, surfaceL), 0.0);
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

vec3 calcDirLight(DirectionalLight light, vec4 textureColor, vec3 calcNorm, vec3 pass_Vertices, vec3 surfaceC, float visibility)
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

	return ambient + visibility * (diffuse);
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

const int pcfCount = 2;
const float totalT = (pcfCount * 2.0 + 1.0) * (pcfCount * 2.0 + 1.0);

void main() 
{

	// Texture light
	vec4 textureColor = vec4(1.0, 1.0, 1.0, 1.0);
	vec4 depthTexture = texture(depthTextureSample, depth_Coords.xy);

	// Initial values for the shadows
	float visibility = 1.0;
	float bias = 0.008;

	float mapSize = 4096.0;
	float Tsize = 1.0 / mapSize;
	float total = 0.0;

	// Use PCF the obtain better shadows
	for(int x = -pcfCount; x <= pcfCount; x++)
	{
		for(int y = -pcfCount; y <= pcfCount; y++)
		{

			float objNL = texture(depthTextureSample, depth_Coords.xy + vec2(x, y) * Tsize).z;
			if(depth_Coords.z-bias > objNL)
			{
				total += 1.0;
			}
		}
	}

	// Change the visibility
	total /= totalT;
	visibility = 1.0 - (total * depth_Coords.w);

	// Normalized distance between camera and vertex
	vec3 surfaceC = normalize(cameraPos - pass_Vertices);

	vec3 color = vec3(0, 0, 0);

	// Color before gamma correction (from every light)
	// First the point light contribution
	for(int i = 0; i < number_of_point_lights; i++)
	{
		color += calcPointLight(pointLights[i], textureColor, pass_Normal, pass_Vertices, surfaceC);
	}
	
	// The directional light contribution
	for(int i = 0; i < number_of_directional_lights; i++)
	{
		color += calcDirLight(dirLights[i], textureColor, pass_Normal, pass_Vertices, surfaceC, visibility);
	}

	// The spot light contribution
	for(int i = 0; i < number_of_spot_lights; i++)
	{
		color += calcSpotLight(spotLights[i], textureColor, pass_Normal, pass_Vertices, surfaceC);
	}

	// Gamma correction
	vec3 gamma = vec3(1.0/1.0);
	vec4 finalColor = vec4(pow(color, gamma), textureColor.w);
	
	fragColor = finalColor;
}
