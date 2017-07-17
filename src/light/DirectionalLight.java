package light;

import camera.Camera;
import engine.Engine;
import engine.objects.Rectangle;
import fbo.FrameBufferObjectManager;
import graphics.Texture;
import math.Matrix4f;
import math.Quaternion;
import math.Vector3f;
import math.Vector4f;
import matrices.MatrixObjectManager;
import shaders.Shader;

public class DirectionalLight extends LightObject{

	private Rectangle rect;
	
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
		FrameBufferObjectManager.addFrameBufferObject(name, "shadow", 4096, 4096);
		depthBuffer = FrameBufferObjectManager.getFrameBuffer(name);
		
		// Rendering the depth map
		rect = new Rectangle("depth", new Texture("depth", depthBuffer.getDepthTexID()), Engine.getWidth() - Engine.getWidth() / 3, Engine.getHeight() - Engine.getHeight() / 3, Engine.getWidth() / 4, Engine.getHeight() / 4, new Vector4f());

		// Calculate the initial direction of the light in polar coordinates
		getPolarDirection();
		
		// Generate a projection matrix object 
		MatrixObjectManager.generateOrthographicMatrix("lightdirmatrix", 0, 0, 0);
	}

	@Override
	public void update() {
		
		// Update direction of light
		lightDir = new Vector3f(xDir, yDir, zDir);
		getPolarDirection();
				
		// First calculate the rotation of the camera from the lights point of view
		Quaternion firstRotation = new Quaternion(getPhi(), new Vector3f(0, -1, 0));
		Quaternion secondRotation = new Quaternion(getTheta(), new Vector3f(1, 0, 0));
		
		// Assume that only the orientation of the light is important
		// This means that shadows will only be casted around the zero point and will not be dynamic
		Quaternion result = Quaternion.multiply(firstRotation, secondRotation);
		Matrix4f lightRotationMatrix = result.toMatrix4f();
		
		// Create a default box
		float lr = 10;
		float tb = 10;
		float fn = 10;
		
		// The orthographic projection matrix as a default box
		MatrixObjectManager.getMatrixObject("lightdirmatrix").updateHashMap("rl", lr);
		MatrixObjectManager.getMatrixObject("lightdirmatrix").updateHashMap("tb", tb);
		MatrixObjectManager.getMatrixObject("lightdirmatrix").updateHashMap("fn", fn);
				
		MatrixObjectManager.getMatrixObject("lightdirmatrix").update();
		projectionLightMatrix = MatrixObjectManager.getMatrixObject("lightdirmatrix").getMatrix();
		
		// The center in world space is the camera position
		Vector3f center = cam.getPosition();

		// Calculate the light view matrix
		viewLightMatrix.setIdentity();
		
		viewLightMatrix.multiply(lightRotationMatrix);
		viewLightMatrix.translate(center);
	}
	
	@Override
	public void uploadToShader(int light, Shader uShader) {
		
		uShader.uploadVector3f(lightDir, uShader.getDirectionalLightDirectionLocList().get(light));
		uShader.uploadVector3f(lightColor, uShader.getDirectionalLightColorLocList().get(light));
		uShader.uploadVector3f(ambIntensity, uShader.getDirectionalAmbIntensityLocList().get(light));
	}

	@Override
	public void render() {
		
		if(renderShadowMap)
		{
			
			rect.update();
			rect.render();
		}
	}
	
	/**
	 * Get the direction of the light in polar coordinates
	 */
	private void getPolarDirection()
	{
		
		// Convert it to polar coordinates, this gives us the direction the light is looking at
		float radius = (float) Math.sqrt(Math.pow(xDir, 2) + Math.pow(yDir, 2) + Math.pow(zDir, 2));
		float theta = 90f - (float) (Math.acos(yDir / radius) * (180f / Math.PI));
		float phi = (float) (Math.atan2(xDir, zDir) * (180f / Math.PI));
		
		this.setRadius(radius);
		this.setTheta(theta);
		this.setPhi(phi);
	}
}