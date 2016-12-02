package input;

import static org.lwjgl.glfw.GLFW.glfwGetKey;
import static org.lwjgl.glfw.GLFW.GLFW_PRESS;

import engine.Engine;

public class KeyboardInput {
	
	private KeyboardInput() {}
	
	public static int getState(String key) {
		
		return glfwGetKey(Engine.window, (int) key.charAt(0));
	}
	
	public static boolean isPressed(String key)
	{
		
		// Return false if not pressed
		boolean isPressed = false;
		
		if(glfwGetKey(Engine.window, (int) key.charAt(0)) == GLFW_PRESS) isPressed = true;
		return isPressed;
	}
}