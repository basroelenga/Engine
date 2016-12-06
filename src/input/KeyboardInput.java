package input;

import static org.lwjgl.glfw.GLFW.glfwGetKey;
import static org.lwjgl.glfw.GLFW.GLFW_PRESS;

import engine.Engine;

public class KeyboardInput {
	
	public static boolean shiftPressed = false;
	
	private static int actionNow = Engine.keyAction;
	private static int actionPrev = Engine.keyAction;
	
	private static int keyNow = Engine.keyInput;
	private static int keyPrev = Engine.keyInput;
	
	private KeyboardInput() {}
	
	/**
	 * This function get the current key char that is pressed
	 * @return Returns the current key pressed
	 */
	public static char getCurrentKey()
	{

		char key = 0;
		
		actionNow = Engine.keyAction;
		keyNow = Engine.keyInput;
		
		// This handles a new key press
		if(actionPrev == 0 && actionNow == 1) key = (char) keyNow;
	
		// This line should fix the rapid key press
		else if(actionNow == 1 && keyNow != keyPrev) key = (char) keyNow;
		
		// The key gets reset to zero if none of this action take place (this should be ignored)
		else key = 0;
		
		// Check if the shift key is pressed
		if(actionNow == 1 && keyNow == 340) shiftPressed = true;
		if(actionNow == 0 && keyNow == 340) shiftPressed = false;

		actionPrev = actionNow;
		keyPrev = keyNow;

		return key;
	}
	
	/**
	 * Get the current state of the key.
	 * @param key Key to check.
	 * @return State of the key.
	 */
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