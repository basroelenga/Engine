package light;

import camera.CameraManager;
import engine.Engine;
import engine.objects.Rectangle;
import fbo.FrameBufferObjectManager;
import graphics.Texture;
import math.Vector3f;
import math.Vector4f;
import shaders.Shader;
import shaders.ShaderManager;
import shapes.UVSphere;
import utils.DrawShapes;

public class PointLight extends LightObject{

	private UVSphere sphere;
	
	private Rectangle rect;
	
	private float pitch;
	private float yaw;
	
	public PointLight(String name, float x, float y, float z, Vector3f lightColor, boolean show) 
	{
		
		this.name = name;
		
		this.x = x;
		this.y = y;
		this.z = z;
		
		this.xs = 0.2f;
		this.ys = 0.2f;
		this.zs = 0.2f;
		
		this.show = show;
		
		// The sphere that show the position
		sphere = new UVSphere(8);
		vaoID = sphere.getVaoID();
		
		// Create the cubemap buffer for shadow rendering
		FrameBufferObjectManager.addFrameBufferObject(name, "cubemap", 512, 512);
		depthBuffer = FrameBufferObjectManager.getFrameBuffer(name);
		
		// Create a camera to change the direction of the view matrix for depth rendering
		CameraManager.addFreeCamera("pointCamera" + name, x, y, z, 0, 0, 0);
		
		// Rendering the cube map
		rect = new Rectangle("cube", new Texture("cube", depthBuffer.getTextureID()), Engine.getWidth() - Engine.getWidth() / 3, Engine.getHeight() - Engine.getHeight() / 3, Engine.getWidth() / 4, Engine.getHeight() / 4, new Vector4f());
		
		// The sphere should not be influenced by other lighting, thus use basic shader
		shader = ShaderManager.getShader("basic");
		
		// Set light properties
		this.lightColor = lightColor;
		ambIntensity = new Vector3f(0.02f, 0.02f, 0.02f);
		
		attenuationFactor = 0.01f;
	}
	
	public void prepare(int face)
	{

		changeDirection(face);
		viewLightMatrix = CameraManager.getFreeCamera("pointCamera" + name).getViewMatrix();
		
		depthBuffer.bindCube(face);
	}
	
	public void reset()
	{
		
		depthBuffer.unbindCube();
	}
	
	/**
	 * Change the direction of the camera of the point light to update the view matrix.
	 * @param direction The direction of the camera.
	 */
	public void changeDirection(int direction)
	{
		
		switch(direction)
		{
		
		case 0:
			
			pitch = 0f;
			yaw = 90f;
			
			break;
		
		case 1:
			
			pitch = 0f;
			yaw = -90f;
			
			break;
		
		case 2:
			
			pitch = 90f;
			yaw = 0f;
			
			break;
		
		case 3:
			
			pitch = -90f;
			yaw = 0f;
			
			break;
			
		case 4:
			
			pitch = 0f;
			yaw = 180f;
			
			break;
		
		case 5:
			
			pitch = 0f;
			yaw = 0f;
			
			break;
			
		default:
			
			System.err.println("Invalid direction number (0 - 5)");
		}
		
		CameraManager.getFreeCamera("pointCamera" + name).setPitch(pitch);
		CameraManager.getFreeCamera("pointCamera" + name).setYaw(yaw);
	}
	
	public void update()
	{
				
		// Update the position of the light (for rendering)
		modelMatrix.setIdentity();
		
		modelMatrix.translate(x, y, z);
		modelMatrix.scale(xs, ys, zs);
		
		lightPos = new Vector3f(x, y, z);
	}
	
	public void uploadToShader(int light, Shader uShader)
	{
		
		uShader.uploadFloat(attenuationFactor, uShader.getPointAttenuationFactorLocList().get(light));

		uShader.uploadVector3f(lightPos, uShader.getPointLightPosLocList().get(light));
		uShader.uploadVector3f(lightColor, uShader.getPointLightColorLocList().get(light));
		uShader.uploadVector3f(ambIntensity, uShader.getPointAmbIntensityLocList().get(light));
	}
	
	public void render()
	{
		
		// Renders the point light location
		if(show)
		{
			
			shader.uploadMatrix4f(modelMatrix, shader.getModelMatrixLoc());
			shader.uploadMatrix4f(projectionMatrix, shader.getProjectionMatrixLoc());
			
			shader.uploadVector4f(new Vector4f(lightColor.getX(), lightColor.getY(), lightColor.getZ(), 1), shader.getRgbaColorLoc());
			
			DrawShapes.drawUVSphere(shader, null, sphere.getVaoID(), sphere.getAmountOfTriangles());
		}
		
		// Renders the cube map
		if(renderShadowMap)
		{
			
			rect.update();
			rect.render();
		}
	}
}