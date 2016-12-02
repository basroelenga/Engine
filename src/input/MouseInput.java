package input;

import static org.lwjgl.glfw.GLFW.*;

import engine.Engine;

public class MouseInput {

	private static double[] x = new double[1];
	private static double[] y = new double[1];
	
	private MouseInput() {}
	
	public static int getState(String mState)
	{
		
		int state = 2;
		
		switch(mState)
		{
		
		case "LB":
			
			state = glfwGetMouseButton(Engine.window, GLFW_MOUSE_BUTTON_LEFT);
		
			break;
			
		case "RB":
			
			state = glfwGetMouseButton(Engine.window, GLFW_MOUSE_BUTTON_RIGHT);
			
		}
	
		return state;
	}
	
	public static float[] getLocation()
	{
		
		glfwGetCursorPos(Engine.window, x, y);
		
		float[] mPos = {(float) x[0], (float) y[0]};
		return mPos;
	}
}