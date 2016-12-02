package cam;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;

import input.KeyboardInput;
import input.MouseInput;
import math.Matrix4f;

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
	
	private static float Rx = 0f;
	private static float Ry = 0f;
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
			
			Ry += -mouseDX;
			Rx += mouseDY;
		}
		else
		{
			
			prevMouseX = 0f;
			prevMouseY = 0f;
		}
		
		if(KeyboardInput.getState("W") == GLFW_PRESS) 
		{
			
			moveX(0.1f);
			moveY(-0.1f);
		}
		
		if(KeyboardInput.getState("S") == GLFW_PRESS) 
		{
				
			moveX(-0.1f);
			moveY(0.1f);
		}

		if(KeyboardInput.getState("A") == GLFW_PRESS) 
		{	
			
			moveZ(0.1f);
		}
		
		if(KeyboardInput.getState("D") == GLFW_PRESS) 
		{
				
			moveZ(-0.1f);
		}
	}
	
	private void uPosition()
	{
	
		Matrix4f posMatrix = new Matrix4f();
		posMatrix.transelate(x, y, z);
		
		viewMatrix.multiply(posMatrix);
	}
	
	private void moveX(float dir)
	{
		
		x -= dir * Math.sin(Math.toRadians(Ry));
		z += dir * Math.cos(Math.toRadians(Ry));
	}
	
	private void moveY(float dir)
	{
		
		y += dir * Math.sin(Math.toRadians(Rx));
	}
	
	private void moveZ(float dir)
	{
		
		x -= dir * Math.sin(Math.toRadians(Ry - 90));
		z += dir * Math.cos(Math.toRadians(Ry - 90));
	}
	
	private void uRotationX()
	{
		
		Matrix4f rotMatrix = new Matrix4f();
		rotMatrix.rotateQ(Rx, 0, 0, true);
	
		viewMatrix.multiply(rotMatrix);
	}
	
	private void uRotationY()
	{
		
		Matrix4f rotMatrix = new Matrix4f();
		rotMatrix.rotateQ(0, Ry, 0, true);
	
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
}