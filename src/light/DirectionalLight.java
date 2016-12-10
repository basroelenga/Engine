package light;

import math.Vector3f;
import shaders.Shader;

public class DirectionalLight extends LightObject{

	public DirectionalLight(String name, float xDir, float yDir, float zDir, Vector3f lightColor)
	{
		
		this.name = name;
		
		this.xDir = xDir;
		this.yDir = yDir;
		this.zDir = zDir;
		
		this.lightColor = lightColor;
		
		ambIntensity = new Vector3f(0.2f, 0.2f, 0.2f);
		
		// There is no rendering of the light for now, so no shaders/spheres needed
	}

	@Override
	public void update() {
		
		lightDir = new Vector3f(xDir, yDir, zDir);
	}

	@Override
	public void render() {
		
		// A direction light is not shown
	}

	@Override
	public void uploadToShader(int light, Shader uShader) {

		System.out.println(uShader.getDirectionalLightDirectionLocList().get(light));
		
		uShader.uploadVector3f(lightDir, uShader.getDirectionalLightDirectionLocList().get(light));
		uShader.uploadVector3f(lightColor, uShader.getDirectionalLightColorLocList().get(light));
		uShader.uploadVector3f(ambIntensity, uShader.getDirectionalAmbIntensityLocList().get(light));
	}
}