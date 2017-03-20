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

public class Camera {

	private String name;
	
	private float mouseX = 0f;
	private float mouseY = 0f;
	
	private float prevMouseX = 0f;
	private float prevMouseY = 0f;
	
	private float mouseDX = 0f;
	private float mouseDY = 0f;
	
	// Ordering: xyz
	private Vector3f position;
	
	// Ordering: yaw pitch roll
	private Vector3f angles;
	
	private Quaternion rotateTheta;
	private Quaternion rotatePhi;
	
	private Matrix4f viewRotationMatrix;
	private Matrix4f viewPositionMatrix;
	
	private Matrix4f viewMatrix;

	/**
	 * Create a FPS camera at the default location (position = 0, 0, 0 and orientation = 0, 0, 0).
	 * @param name The name of the camera object.
	 */
	public Camera(String name)
	{
		
		this.name = name;
		
		this.position = new Vector3f();
		this.angles = new Vector3f();
		
		viewMatrix = new Matrix4f();
		
		rotateTheta = new Quaternion(angles.getY(), new Vector3f(0, -1, 0));
		rotatePhi = new Quaternion(angles.getX(), new Vector3f(-1, 0, 0));
	}
	
	/**
	 * Create a FPS camera at the specified position and angle.
	 * @param name The name of the camera object.
	 * @param position The position of the camera object.
	 * @param angles The orientation of the camera object.
	 */
	public Camera(String name, Vector3f position, Vector3f angles)
	{
	
		this.name = name;
		
		this.position = position;
		this.angles = angles;
		
		viewMatrix = new Matrix4f();
		
		rotateTheta = new Quaternion(angles.getY(), new Vector3f(0, -1, 0));
		rotatePhi = new Quaternion(angles.getX(), new Vector3f(-1, 0, 0));
	}
	
	/**
	 * Update the viewmatrix of the camera object.
	 */
	public void update()
	{
				
		viewMatrix.setIdentity();
		getInputData();
		
		updateOrientation();
		updatePosition();
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
			
			angles.setX(angles.getX() + mouseDX);
			angles.setY(angles.getY() + mouseDY);
		}
		else
		{
			
			prevMouseX = 0f;
			prevMouseY = 0f;
		}
		
		// Get the position input data
		if(KeyboardInput.getState("W") == GLFW_PRESS && !Debugger.debugState) 
		{
			
			float tx = 0.1f * (float) Math.sin(Math.toRadians(angles.getX())) * (float) Math.cos(Math.toRadians(-angles.getY()));
			float ty = 0.1f * (float) Math.sin(-Math.toRadians(angles.getY()));
			float tz = 0.1f * (float) Math.cos(Math.toRadians(angles.getX())) * (float) Math.cos(Math.toRadians(-angles.getY()));
			
			position.setX(position.getX() + tx);
			position.setY(position.getY() + ty);
			position.setZ(position.getZ() + tz);
		}
		
		if(KeyboardInput.getState("S") == GLFW_PRESS && !Debugger.debugState) 
		{
				
			float tx = -0.1f * (float) Math.sin(Math.toRadians(angles.getX())) * (float) Math.cos(Math.toRadians(-angles.getY()));
			float ty = -0.1f * (float) Math.sin(-Math.toRadians(angles.getY()));
			float tz = -0.1f * (float) Math.cos(Math.toRadians(angles.getX())) * (float) Math.cos(Math.toRadians(-angles.getY()));
			
			position.setX(position.getX() + tx);
			position.setY(position.getY() + ty);
			position.setZ(position.getZ() + tz);
		}

		// The strave keys
		if(KeyboardInput.getState("A") == GLFW_PRESS && !Debugger.debugState) 
		{	
			
			float tx = 0.1f * (float) Math.cos(Math.toRadians(angles.getX()));
			float tz = 0.1f * (float) Math.sin(Math.toRadians(-angles.getX()));
			
			position.setX(position.getX() + tx);
			position.setZ(position.getZ() + tz);
		}
		
		if(KeyboardInput.getState("D") == GLFW_PRESS && !Debugger.debugState) 
		{
				
			float tx = -0.1f * (float) Math.cos(Math.toRadians(angles.getX()));
			float tz = -0.1f * (float) Math.sin(Math.toRadians(-angles.getX()));
			
			position.setX(position.getX() + tx);
			position.setZ(position.getZ() + tz);
		}
		
	}

	/**
	 * Update the position of the camera object to the view matrix.
	 */
	private void updatePosition()
	{
	
		viewPositionMatrix = new Matrix4f();
		viewPositionMatrix.translate(position);

		viewMatrix.multiply(viewPositionMatrix);
		
		// Upload the view matrix and the position of the camera to all shaders
		for(Shader shader : ShaderManager.getShaderList())
		{
			
			shader.uploadVector3f(position, shader.getCameraPocLoc());
			shader.uploadMatrix4f(viewMatrix, shader.getViewMatrixLoc());
		}
	}
	
	/**
	 * Update the orientation of the camera object to the view matrix using quaternions.
	 */
	private void updateOrientation()
	{
		
		// Update quaternions and multiply them
		rotatePhi.updateQuaternion(angles.getX(), new Vector3f(0, -1, 0));
		rotateTheta.updateQuaternion(angles.getY(), new Vector3f(-1, 0, 0));
		
		Quaternion result = Quaternion.multiply(rotatePhi, rotateTheta);
		result.normalize();
		
		// Obtain the rotation matrix from the final quaternion
		viewRotationMatrix = result.toMatrix4f();
		viewMatrix.multiply(viewRotationMatrix);
	}
	
	/**
	 * Print the current orientation of the camera object.
	 */
	public void printOrientation()
	{
		System.out.println("theta: " + angles.getY() + " ," + "phi: " + angles.getX());
	}
	
	/**
	 * Print the current position of the camera object.
	 */
	public void printPosition()
	{
		System.out.println("x: " + position.getX() + " ," + "y: " + position.getY() + " ," + "z:" + position.getZ());
	}
	
	/**
	 * Get the name of the camera object.
	 * @return The name of the camera object.
	 */
	public String getName()
	{
		return name;
	}
	
	/**
	 * Get the view matrix of the camera object.
	 * @return The view matrix of the camera object.
	 */
	public Matrix4f getViewMatrix()
	{
		return viewMatrix;
	}
	
	/**
	 * Get the position of the camera object.
	 * @return The position of the camera object.
	 */
	public Vector3f getPosition()
	{
		return position;
	}
	
	/**
	 * Get the orientation of the camera object.
	 * @return The orientation of the camera object.
	 */
	public Vector3f getOrientation()
	{
		return angles;
	}
	
	/**
	 * Get the rotation matrix of the camera.
	 * @return The view rotation matrix.
	 */
	public Matrix4f getRotationMatrix()
	{
		return viewRotationMatrix;
	}
	
	/**
	 * Get the position matrix of the camera.
	 * @return the view position matrix.
	 */
	public Matrix4f getPositionMatrix()
	{
		return viewPositionMatrix;
	}
}