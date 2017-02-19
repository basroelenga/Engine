package light;

import fbo.FrameBufferObjectManager;
import math.Matrices;
import math.Matrix4f;
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
		
		// Create the depth buffer for the light
		FrameBufferObjectManager.addShadowFrameBufferObject(name, 1024, 1024);
		depthBuffer = FrameBufferObjectManager.getFrameBuffer(name);
		
		// The view matrix for the light needs to be calculated, this can be done by using the direction of the light
		viewLightMatrix = getLightViewMatrix();
		
		// The projection matrix determines how large of an area the light sees, an orthographic matrix is used for this
		// This orthographic matrix is constructed by using the properties of the projection matrix from the camera
		projectionLightMatrix = Matrices.getOrthographicMatrix(10, 10, 10, 10);
		
		// There is no rendering of the light for now, so no shaders/spheres needed
	}
	
	private Matrix4f getLightViewMatrix()
	{
		
		float radius = (float) Math.sqrt(Math.pow(xDir, 2) + Math.pow(yDir, 2) + Math.pow(zDir, 2));
		float theta = (float) (Math.acos(zDir / radius) * (180f / Math.PI));
		float phi = (float) (Math.atan2(xDir, yDir) * (180f / Math.PI));
		
		Matrix4f tempMatrix = new Matrix4f();
		tempMatrix.rotateQ(theta, 0, phi, false);
		
		return tempMatrix;
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
		
		uShader.uploadVector3f(lightDir, uShader.getDirectionalLightDirectionLocList().get(light));
		uShader.uploadVector3f(lightColor, uShader.getDirectionalLightColorLocList().get(light));
		uShader.uploadVector3f(ambIntensity, uShader.getDirectionalAmbIntensityLocList().get(light));
	}
}