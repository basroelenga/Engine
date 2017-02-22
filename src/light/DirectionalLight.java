package light;

import cam.Camera;
import fbo.FrameBufferObjectManager;
import math.Vector3f;
import shaders.Shader;

public class DirectionalLight extends LightObject{

	public DirectionalLight(String name, Camera cam, float xDir, float yDir, float zDir, Vector3f lightColor)
	{
		
		this.name = name;
		
		this.xDir = xDir;
		this.yDir = yDir;
		this.zDir = zDir;
		
		this.cam = cam;
		this.lightColor = lightColor;
		
		ambIntensity = new Vector3f(0.2f, 0.2f, 0.2f);
		
		// Create the depth buffer for the light
		FrameBufferObjectManager.addShadowFrameBufferObject(name, 1024, 1024);
		depthBuffer = FrameBufferObjectManager.getFrameBuffer(name);
		
		// Calculate the initial direction of the light in polar coordinates
		getPolarDirection();
	}
	
	/**
	 * Get the direction of the light in polar coordinates
	 */
	private void getPolarDirection()
	{
		
		// Convert it to polar coordinates, this gives us the direction the light is looking at
		float radius = (float) Math.sqrt(Math.pow(xDir, 2) + Math.pow(yDir, 2) + Math.pow(zDir, 2));
		float theta = (float) (Math.acos(yDir / radius) * (180f / Math.PI));
		float phi = (float) (Math.atan2(xDir, zDir) * (180f / Math.PI));
		
		this.setRadius(radius);
		this.setTheta(theta);
		this.setPhi(phi);
	}

	@Override
	public void update() {
		
		// Calculate the 8 points which contain the camera view
		
		
		
		lightDir = new Vector3f(xDir, yDir, zDir);
	}
	
	private void calculateShadowBox()
	{
		
	}

	@Override
	public void uploadToShader(int light, Shader uShader) {
		
		uShader.uploadVector3f(lightDir, uShader.getDirectionalLightDirectionLocList().get(light));
		uShader.uploadVector3f(lightColor, uShader.getDirectionalLightColorLocList().get(light));
		uShader.uploadVector3f(ambIntensity, uShader.getDirectionalAmbIntensityLocList().get(light));
	}

	@Override
	public void render() {}
}