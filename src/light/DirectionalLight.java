package light;

import math.Vector3f;
import shaders.Shader;

public class DirectionalLight extends LightObject{

	public DirectionalLight(String name, float xDir, float yDir, float zDir, Vector3f lightColor)
	{
		
		this.name = name;
		
		this.x = xDir;
		this.y = yDir;
		this.z = zDir;
		
		this.lightColor = lightColor;
		
		lightDir = new Vector3f(x, y, z);
		ambIntensity = new Vector3f(0.2f, 0.2f, 0.2f);
		
		// There is no rendering of the light for now, so no shaders/spheres needed
	}

	@Override
	public void update() {
		
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