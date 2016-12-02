package input;

import static org.lwjgl.glfw.GLFW.glfwGetKey;
import static org.lwjgl.glfw.GLFW.GLFW_PRESS;

import engine.Engine;

public class KeyboardInput {
	
	private KeyboardInput() {}
	
	public static int getState(String key) {
		
		return glfwGetKey(Engine.window, (int) key.charAt(0));
	}
	
	/**
	 * Returns boolean state of key, only work with ASCII symbols.
	 * @param key Key to check.
	 * @return State of key.
	 */
	public static boolean isPressed(String key)
	{
		
		// Return false if not pressed
		boolean isPressed = false;
		
		if(glfwGetKey(Engine.window, (int) key.charAt(0)) == GLFW_PRESS && key.length() == 1) isPressed = true;
		return isPressed;
	}
	
	/**
	 * Returns boolean state of key, this should be used for non-ASCII symbols.
	 * Example for input key is "GLFW_KEY_TAB" for TAB.
	 * @param key Key to check.
	 * @return State of key.
	 */
	public static boolean isPressed(int key)
	{
		
		// Return false if not pressed
		boolean isPressed = false;
		
		if(glfwGetKey(Engine.window, key) == GLFW_PRESS) isPressed = true;
		return isPressed;
	}
}