package engine;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;

import game.Simulation;
import input.KeyboardInput;
import math.Matrices;
import math.Matrix4f;
import shaders.ShaderManager;
import text.TextManager;

public class Engine {
	
	public static Matrix4f projMatrix;
	public static Matrix4f orthoMatrix;
	
	public static float FPS;
	
	private float nanoToSecond = 1000000000f;
	
	private long time1;
	private long time2;
	private long time3 = System.nanoTime();
	
	public static long window;
	
	private int width;
	private int height;
	
	private boolean isRunning = false;
	private boolean fullScreen = false;
	private boolean wireframe = false;
	
	private Simulation game;
	
	public Engine(int width, int height)
	{
		
		this.width = width;
		this.height = height;
		
		System.out.println("Engine started");
		System.out.println("Resolution: " + width + " x " + height);
		
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
		
		glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> 
		{
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
	}
	
	private void render()
	{
		
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		
		game.render();
		
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
}