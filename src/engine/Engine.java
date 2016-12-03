package engine;

import static org.lwjgl.glfw.GLFW.GLFW_FALSE;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;
import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;
import static org.lwjgl.glfw.GLFW.GLFW_RESIZABLE;
import static org.lwjgl.glfw.GLFW.glfwCreateWindow;
import static org.lwjgl.glfw.GLFW.glfwDefaultWindowHints;
import static org.lwjgl.glfw.GLFW.glfwDestroyWindow;
import static org.lwjgl.glfw.GLFW.glfwGetPrimaryMonitor;
import static org.lwjgl.glfw.GLFW.glfwGetVideoMode;
import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwSetKeyCallback;
import static org.lwjgl.glfw.GLFW.glfwSetWindowPos;
import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;
import static org.lwjgl.glfw.GLFW.glfwShowWindow;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.glfw.GLFW.glfwSwapInterval;
import static org.lwjgl.glfw.GLFW.glfwTerminate;
import static org.lwjgl.glfw.GLFW.glfwWindowHint;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.GL_FILL;
import static org.lwjgl.opengl.GL11.GL_FRONT_AND_BACK;
import static org.lwjgl.opengl.GL11.GL_LINE;
import static org.lwjgl.opengl.GL11.GL_STENCIL_TEST;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glPolygonMode;
import static org.lwjgl.opengl.GL11.glViewport;
import static org.lwjgl.system.MemoryUtil.NULL;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;

import debug.Debugger;
import game.Simulation;
import input.KeyboardInput;
import math.Matrices;
import math.Matrix4f;
import shaders.ShaderManager;
import text.TextManager;

public class Engine {
	
	public static Matrix4f projMatrix;
	public static Matrix4f orthoMatrix;
	
	public static int keyInput;
	public static int keyAction;
	
	public static float FPS;
	
	private Debugger debugger;
	
	private float nanoToSecond = 1000000000f;
	
	private long time1;
	private long time2;
	private long time3 = System.nanoTime();
	
	public static long window;
	
	private static int width;
	private static int height;
	
	private boolean isRunning = false;
	private boolean fullScreen = false;
	private boolean wireframe = false;
	
	private Simulation game;
	
	public Engine(int width, int height)
	{
		
		Engine.width = width;
		Engine.height = height;
		
		System.out.println("Engine started");
		System.out.println("Resolution: " + Engine.width + " x " + Engine.height);
		
		setUpGLFWAndOpenGL();
		engineResourceLoader();
		engineLoop();
	}
	
	private void setUpGLFWAndOpenGL()
	{
		
		// Set up GLFW
		GLFWErrorCallback.createPrint(System.err).set();
		
		if(!glfwInit()) throw new IllegalStateException("Unable to initialize GLFW");
		
		glfwDefaultWindowHints();
		glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE);
		
		if(fullScreen) window = glfwCreateWindow(width, height, "MyEngine", glfwGetPrimaryMonitor(), NULL);
		else window = glfwCreateWindow(width, height, "MyEngine", NULL, NULL);
		
		if(window == NULL) throw new RuntimeException("Failed to create GLFW window");
		
		// Set a key callback function
		glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> 
		{
			
			Engine.keyInput = key;
			Engine.keyAction = action;
			
			if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE ) glfwSetWindowShouldClose(window, true); 
		});
		
		GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
		glfwSetWindowPos(window, (vidmode.width() - width) / 2,	(vidmode.height() - height) / 2);
		
		glfwMakeContextCurrent(window);
		glfwSwapInterval(1);
		
		glfwShowWindow(window);
		
		// Set up OpenGL
		GL.createCapabilities();

		float red = (1 / 255f) * 135;
		float green = (1 / 255f) * 206;
		float blue = (1 / 255f) * 250;
		
		glClearColor(red, green, blue, 0.0f);
		glViewport(0, 0, width, height);
		
		glEnable(GL_DEPTH_TEST);
		glEnable(GL_STENCIL_TEST);
		glEnable(GL_TEXTURE_2D);
		
		// Set up the projection matrices (these are configured at the width and height of the OpenGL window)
		projMatrix = Matrices.projectionMatrix(width, height);
		orthoMatrix = Matrices.orthographicMatrix(0f, width, height, 0f);
	}
	
	private void engineResourceLoader()
	{
		
		// Create the engine fundamental managers
		new TextManager();
		new ShaderManager();
		
		// Create the debugger
		debugger = new Debugger();
	}
	
	private void engineLoop()
	{
		
		isRunning = true;
		game = new Simulation();
		
		while(isRunning && !glfwWindowShouldClose(window))
		{
			
			update();
			render();
			
			getFPS();
		}
		
		glfwDestroyWindow(window);
		glfwTerminate();
	}
	
	private void update()
	{
		
		if(KeyboardInput.getState("Q") == GLFW_PRESS) wireframe = true;
		if(KeyboardInput.getState("E") == GLFW_PRESS) wireframe = false;
		
		if(wireframe) glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
		if(!wireframe) glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
		
		game.update();
		debugger.update();
	}
	
	private void render()
	{
		
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		
		game.render();
		debugger.render();
		
		glfwSwapBuffers(window);
		glfwPollEvents();
	}

	private void getFPS()
	{
		
		if(time2 == 0)
		{
			
			time2 = System.nanoTime();
		}
		else
		{
			
			time2 = System.nanoTime();
			
			float fps = 1 / ((time2 - time1) / nanoToSecond);
			
			if((time2 - time3) / nanoToSecond > 1)
			{
				
				System.out.println("MyEngine, FPS: " + (int) fps);
				FPS = fps;
				
				time3 = System.nanoTime();
			}
			
		}
		
		time1 = System.nanoTime();
	}
	
	/**
	 * Get the width of the GLFW window.
	 * @return Width of the window.
	 */
	public static int getWidth()
	{
		return Engine.width;
	}
	
	/**
	 * Get the height of the GLFW window.
	 * @return Height of the window.
	 */
	public static int getHeight()
	{
		return Engine.height;
	}
}