package engine;

import static org.lwjgl.glfw.GLFW.GLFW_FALSE;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;
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
import static org.lwjgl.glfw.GLFW.glfwSetWindowIcon;
import static org.lwjgl.glfw.GLFW.glfwSetWindowPos;
import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;
import static org.lwjgl.glfw.GLFW.glfwShowWindow;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.glfw.GLFW.glfwSwapInterval;
import static org.lwjgl.glfw.GLFW.glfwTerminate;
import static org.lwjgl.glfw.GLFW.glfwWindowHint;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWImage;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;

import camera.CameraManager;
import debug.Debugger;
import fbo.FrameBufferObject;
import fbo.FrameBufferObjectManager;
import game.Simulation;
import graphics.Texture;
import graphics.TextureManager;
import light.LightManager;
import matrices.MatrixObjectManager;
import models.ModelManager;
import postprocess.PostProcessEffectManager;
import shaders.ShaderManager;
import text.Text;
import text.TextManager;

public class Engine {
	
	public static int keyInput;
	public static int keyAction;
	public static int scancode;
	
	public static boolean isRunning = false;
	public static boolean wireframe = false;
	
	public static boolean showFPS = false;
	
	private Debugger debugger;
	
	private float nanoToSecond = 1000000000f;
	
	private static float FPS;
	
	private long time1;
	private long time2;
	private long time3 = System.nanoTime();
	
	public static long window;
	
	private static int width;
	private static int height;

	private boolean fullScreen = false;
	
	private Text engineFPS;
	
	private Simulation game;
	
	public Engine(int width, int height)
	{
		
		Engine.width = width;
		Engine.height = height;
		
		System.out.println("Engine started");
		System.out.println("Resolution: " + Engine.getWidth() + " x " + Engine.getHeight());
		
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
			Engine.scancode = scancode;
			
			if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE ) glfwSetWindowShouldClose(window, true); 
		});
		
		GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
		glfwSetWindowPos(window, (vidmode.width() - width) / 2,	(vidmode.height() - height) / 2);
		
		//glfwSetWindowIcon(window, NULL);
		
		glfwMakeContextCurrent(window);
		glfwSwapInterval(1);
		
		glfwShowWindow(window);
		
		// Set up OpenGL
		GL.createCapabilities();

		// Set the icon
		setIcon();
		
		// Construct the clear color
		float red = (1 / 255f) * 135;
		float green = (1 / 255f) * 206;
		float blue = (1 / 255f) * 250;
		
		glClearColor(red, green, blue, 0.0f);
		glViewport(0, 0, width, height);
		
		glEnable(GL_DEPTH_TEST);
		glEnable(GL_STENCIL_TEST);
		glEnable(GL_TEXTURE_2D);
		
		// Set up the projection matrices (these are configured at the width and height of the OpenGL window)
		// These are the default projection matrices and used for camera and GUI rendering and can be changed
		MatrixObjectManager.generateProjectionMatrix("projectionMatrixDefault", 70, 0.1f, 10f, Engine.getWidth(), Engine.getHeight());
		MatrixObjectManager.generateOrthographicMatrix("orthographicMatrixDefault", -0.2f, 0.2f, 0f, Engine.getWidth(), Engine.getHeight(), 0f);
	}
	
	private void setIcon()
	{
		
		// Load the icon
		Texture tex = new Texture("icon");
		
		// Create the image
		GLFWImage icon = GLFWImage.malloc();
		GLFWImage.Buffer iconBuffer = GLFWImage.malloc(1);
		
		icon.set(tex.getWidth(), tex.getHeight(), tex.getImage());
		iconBuffer.put(0, icon);
		
		glfwSetWindowIcon(window, iconBuffer);
	}
	
	private void engineResourceLoader()
	{
		
		// Create the engine fundamental managers
		new TextureManager();
		new TextManager(true);
		new ShaderManager();
		new ModelManager();
		
		PostProcessEffectManager.loadPostProcessShaders();
		
		// Create primitives, these shapes can be used for anything
		EngineObjectManager.createPrimitives();
		
		// Create the debugger
		debugger = new Debugger();
		
		// Intialize the light manager
		LightManager.initialize();
		
		// Create the engine FPS timer
		engineFPS = new Text("FPS", "HUD", Engine.getWidth() - 105f, Engine.getHeight() - 7f, 0.1f, 32);
	}
	
	private void engineLoop()
	{
		
		isRunning = true;
		game = new Simulation();
		
		while(isRunning && !glfwWindowShouldClose(window))
		{
			
			update();
			render();
			
			setFPS();
		}
		
		glfwDestroyWindow(window);
		glfwTerminate();
		
		System.exit(0);
	}
	
	private void update()
	{
		
		// This function will update changes to objects
		game.update();
		
		CameraManager.update();
		
		// Update all matrices etc
		EngineObjectManager.update();
		EngineSystemManager.update();
		
		LightManager.update();
		
		debugger.update();
		engineFPS.updateText("FPS:" + (int) FPS);
	}
	
	private void render()
	{
		
		// Clear buffers
		clearBuffers();
		
		// Prerender phase (rendering to depth maps)
		prerenderPass();
		
		// Render the scene
		firstPass();
		
		// Render postprocessing effects
		secondPass();
		
		glfwSwapBuffers(window);
		glfwPollEvents();
	}
	
	/**
	 * Function that clears all frame buffers
	 */
	private void clearBuffers()
	{
		
		// Clear the default buffer
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		
		// Clear all framebuffer objects
		for(FrameBufferObject fbo : FrameBufferObjectManager.getFBOList())
		{
			fbo.clearBuffer();
		}
	}
	
	/**
	 * The prerender phase will render the depth maps for every directional light in the scene
	 */
	private void prerenderPass()
	{
		
		EngineObjectManager.prerender();
	}
	
	/**
	 * This first render pass will render everything to either a FBO or the default FBO
	 */
	private void firstPass()
	{
		
		if(wireframe) glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
		if(!wireframe) glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
		
		// Render all the objects
		EngineObjectManager.render();
		EngineSystemManager.render();
				
		LightManager.render();
		
		// The debugger will never be shown as a wireframe
		glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
		
		debugger.render();
		if(showFPS) engineFPS.updateAndRender();
	}
	
	/**
	 * Objects that are rendered to a FBO are rendered here back to the default FBO
	 */
	private void secondPass()
	{
		
		PostProcessEffectManager.render();
		debugger.renderFBO();
	}

	private void setFPS()
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
	
	/**
	 * Get the FPS of the current OpenGL window.
	 * @return FPS.
	 */
	public static float getFPS()
	{
		return FPS;
	}
}