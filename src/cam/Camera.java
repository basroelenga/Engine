package cam;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;

import debug.Debugger;
import input.KeyboardInput;
import input.MouseInput;
import math.Matrix4f;
import math.Vector3f;
import shaders.Shader;
import shaders.ShaderManager;

public class Camera {

	private float mouseX = 0f;
	private float mouseY = 0f;
	
	private float prevMouseX = 0f;
	private float prevMouseY = 0f;
	
	private float mouseDX = 0f;
	private float mouseDY = 0f;
	
	private static float x = 0f;
	private static float y = 0f;
	private static float z = 0f;
	
	private static float theta = 0f;
	private static float phi = 0f;
	//private static float Rz = 0f;
	
	private static Matrix4f viewMatrix;
	
	public Camera()
	{
		
		viewMatrix = new Matrix4f();
	}
	
	public void update()
	{
		viewMatrix.setIdentity();
		
		updatePositionAndRotation();
		
		uRotationX();
		uRotationY();
		
		uPosition();
	}
	
	private void updatePositionAndRotation()
	{
		
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
			
			phi += -mouseDX;
			theta += mouseDY;
		}
		else
		{
			
			prevMouseX = 0f;
			prevMouseY = 0f;
		}
		
		if(KeyboardInput.getState("W") == GLFW_PRESS && !Debugger.debugState) 
		{
			
			moveX(0.1f);
			moveY(-0.1f);
		}
		
		if(KeyboardInput.getState("S") == GLFW_PRESS && !Debugger.debugState) 
		{
				
			moveX(-0.1f);
			moveY(0.1f);
		}

		if(KeyboardInput.getState("A") == GLFW_PRESS && !Debugger.debugState) 
		{	
			
			moveZ(0.1f);
		}
		
		if(KeyboardInput.getState("D") == GLFW_PRESS && !Debugger.debugState) 
		{
				
			moveZ(-0.1f);
		}
	}
	
	private void uPosition()
	{
	
		Matrix4f posMatrix = new Matrix4f();
		posMatrix.translate(x, y, z);
		
		viewMatrix.multiply(posMatrix);
		
		// Upload the view matrix and the position of the camera to all shaders
		for(Shader shader : ShaderManager.getShaderList())
		{
			
			shader.uploadVector3f(new Vector3f(x, y, z), shader.getCameraPocLoc());
			shader.uploadMatrix4f(viewMatrix, shader.getViewMatrixLoc());
		}
	}
	
	private void moveX(float dir)
	{
		
		x -= dir * Math.sin(Math.toRadians(phi));
		z += dir * Math.cos(Math.toRadians(phi));
	}
	
	private void moveY(float dir)
	{
		
		y += dir * Math.sin(Math.toRadians(theta));
	}
	
	private void moveZ(float dir)
	{
		
		x -= dir * Math.sin(Math.toRadians(phi - 90));
		z += dir * Math.cos(Math.toRadians(phi - 90));
	}
	
	private void uRotationX()
	{
		
		Matrix4f rotMatrix = new Matrix4f();
		rotMatrix.rotateQ(theta, 0, 0, true);
	
		viewMatrix.multiply(rotMatrix);
	}
	
	private void uRotationY()
	{
		
		Matrix4f rotMatrix = new Matrix4f();
		rotMatrix.rotateQ(0, phi, 0, true);
	
		viewMatrix.multiply(rotMatrix);
	}
	
	public static Matrix4f getViewMatrix()
	{
		return viewMatrix;
	}
	
	public static float getX()
	{
		return x;
	}
	
	public static float getY()
	{
		return y;
	}
	
	public static float getZ()
	{
		return z;
	}
	
	public static float getPhi()
	{
		return phi;
	}
	
	public static float getTheta()
	{
		return theta;
	}
}