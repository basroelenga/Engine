package camera;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;

import debug.Debugger;
import input.KeyboardInput;
import input.MouseInput;
import math.Matrix4f;
import math.Quaternion;
import math.Vector3f;
import shaders.Shader;
import shaders.ShaderManager;

public class PlayerCamera extends CameraObject{
	
	private float mouseX = 0f;
	private float mouseY = 0f;
	
	private float prevMouseX = 0f;
	private float prevMouseY = 0f;
	
	private float mouseDX = 0f;
	private float mouseDY = 0f;

	/**
	 * Create a FPS camera at the specified location and orientation.
	 * @param name The name of the camera object.
	 * @param x The x-position of the camera object.
	 * @param y The y-position of the camera object.
	 * @param z The z-position of the camera object.
	 * @param yaw The yaw of the camera object.
	 * @param pitch The pitch of the camera object.
	 * @param roll The roll of the camera object.
	 */
	public PlayerCamera(String name, float x, float y, float z, float yaw, float pitch, float roll)
	{
	
		this.name = name;
		
		this.x = x;
		this.y = y;
		this.z = z;
		
		this.yaw = yaw;
		this.pitch = pitch;
		this.roll = roll;
		
		this.position = new Vector3f(x, y, z);
		this.orientation = new Vector3f(yaw, pitch, roll);
	}
	
	/**
	 * Update the viewmatrix of the camera object.
	 */
	@Override
	public void update()
	{
		
		getInputData();
		
		updateOrientationAndPosition();
		uploadToShader();
	}
	
	/**
	 * Get input from the keyboard and mouse and process this information to update the right parameters.
	 */
	private void getInputData()
	{
		
		// Get the rotation input data
		if(MouseInput.getState("LB") == GLFW_PRESS)
		{
			
			mouseX = MouseInput.getLocation()[0];
			mouseY = MouseInput.getLocation()[1];
			
			if(prevMouseX == 0 && prevMouseY == 0) 
			{
				
				prevMouseX = mouseX;
				prevMouseY = mouseY;
			}
			else
			{
				
				mouseDX = (prevMouseX - mouseX);
				mouseDY = (prevMouseY - mouseY);
				
				prevMouseX = mouseX;
				prevMouseY = mouseY;
			}
			
			yaw += mouseDX;
			pitch += mouseDY;
		}
		else
		{
			
			prevMouseX = 0f;
			prevMouseY = 0f;
		}
		
		// Get the position input data
		if(KeyboardInput.getState("W") == GLFW_PRESS && !Debugger.debugState) 
		{
			
			float tx = 0.1f * (float) Math.sin(Math.toRadians(yaw)) * (float) Math.cos(Math.toRadians(-pitch));
			float ty = 0.1f * (float) Math.sin(-Math.toRadians(pitch));
			float tz = 0.1f * (float) Math.cos(Math.toRadians(yaw)) * (float) Math.cos(Math.toRadians(-pitch));
			
			x += tx;
			y += ty;
			z += tz;
		}
		
		if(KeyboardInput.getState("S") == GLFW_PRESS && !Debugger.debugState) 
		{
				
			float tx = -0.1f * (float) Math.sin(Math.toRadians(yaw)) * (float) Math.cos(Math.toRadians(-pitch));
			float ty = -0.1f * (float) Math.sin(-Math.toRadians(pitch));
			float tz = -0.1f * (float) Math.cos(Math.toRadians(yaw)) * (float) Math.cos(Math.toRadians(-pitch));
			
			x += tx;
			y += ty;
			z += tz;
		}

		// The strave keys
		if(KeyboardInput.getState("A") == GLFW_PRESS && !Debugger.debugState) 
		{	
			
			float tx = 0.1f * (float) Math.cos(Math.toRadians(yaw));
			float tz = 0.1f * (float) Math.sin(Math.toRadians(-yaw));
			
			x += tx;
			z += tz;
		}
		
		if(KeyboardInput.getState("D") == GLFW_PRESS && !Debugger.debugState) 
		{
				
			float tx = -0.1f * (float) Math.cos(Math.toRadians(yaw));
			float tz = -0.1f * (float) Math.sin(Math.toRadians(-yaw));
			
			x += tx;
			z += tz;
		}
		
	}
	
	/**
	 * Update the orientation of the camera object to the view matrix using quaternions.
	 */
	private void updateOrientationAndPosition()
	{
		
		viewMatrix.setIdentity();
		
		// Update position and orientation
		position = new Vector3f(x, y, z);
		orientation = new Vector3f(yaw, pitch, roll);
		
		// Update quaternions and multiply them
		rotateTheta.updateQuaternion(orientation.getX(), new Vector3f(0, -1, 0));
		rotatePhi.updateQuaternion(orientation.getY(), new Vector3f(-1, 0, 0));
		
		Quaternion result = Quaternion.multiply(rotateTheta, rotatePhi);
		result.normalize();
		
		// Obtain the rotation matrix from the final quaternion
		viewRotationMatrix = result.toMatrix4f();
		viewMatrix.multiply(viewRotationMatrix);
		
		// Update the position of the view matrix
		viewPositionMatrix = new Matrix4f();
		viewPositionMatrix.translate(position);

		viewMatrix.multiply(viewPositionMatrix);
	}
	
	/**
	 * Upload the position and the view matrix of the camera to every shader
	 */
	private void uploadToShader()
	{
		
		// Upload the view matrix and the position of the camera to all shaders
		for(Shader shader : ShaderManager.getShaderList())
		{
			
			shader.uploadVector3f(position, shader.getCameraPocLoc());
			shader.uploadMatrix4f(viewMatrix, shader.getViewMatrixLoc());
		}
	}
}