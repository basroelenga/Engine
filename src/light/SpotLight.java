package light;

import math.Vector3f;
import shaders.Shader;

public class SpotLight extends LightObject{

	public SpotLight(String name, float x, float y, float z, float xDir, float yDir, float zDir, float coneAngle, Vector3f lightColor)
	{
		
		this.name = name;
		
		this.x = x;
		this.y = y;
		this.z = z;
		
		this.xDir = xDir;
		this.yDir = yDir;
		this.zDir = zDir;
		
		this.coneAngle = coneAngle;
		this.lightColor = lightColor;
		
		ambIntensity = new Vector3f(0.1f, 0.1f, 0.1f);
	}

	@Override
	public void update() {
		
		lightPos = new Vector3f(x, y, z);
		lightDir = new Vector3f(xDir, yDir, zDir);
	}

	@Override
	public void render() {
		
		// No rendering of the spotlight yet
	}

	@Override
	public void uploadToShader(int light, Shader uShader) {
		
		uShader.uploadFloat(attenuationFactor, uShader.getSpotAttenuationFactorLocList().get(light));
		uShader.uploadFloat(coneAngle, uShader.getSpotConeAngleLocList().get(light));
		
		uShader.uploadVector3f(ambIntensity, uShader.getSpotAmbIntensityLocList().get(light));
		uShader.uploadVector3f(lightColor, uShader.getSpotLightColorLocList().get(light));
		uShader.uploadVector3f(lightDir, uShader.getSpotLightDirLocList().get(light));
		uShader.uploadVector3f(lightPos, uShader.getSpotLightPosLocList().get(light));
	}	
}