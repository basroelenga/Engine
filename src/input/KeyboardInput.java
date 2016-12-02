package input;

import static org.lwjgl.glfw.GLFW.glfwGetKey;

import engine.Engine;

public class KeyboardInput {
	
	private KeyboardInput() {}
	
	public static int getState(String key) {
		
		return glfwGetKey(Engine.window, (int) key.charAt(0));
	}
}