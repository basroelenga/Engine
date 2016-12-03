package debug;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_TAB;

import engine.Engine;
import game.obj.Rectangle;
import input.KeyboardInput;
import math.Vector4f;

public class Debugger {

	private boolean debugState;
	private boolean blinkShow;
	
	// Time in milliseconds
	private float debugTimeOut = 1000f;
	private long debugTimer;
	
	// Debug window
	private Rectangle windowInput;
	private Rectangle windowOutput;
	
	// Debug blinker
	private Rectangle windowBlinker;
	
	// Blink timer
	private float blinkTimeOut = 500f;
	private long blinkTimer;
	
	// Input string
	private StringBuilder input = new StringBuilder();
	
	public Debugger()
	{
		
		// Set the debug state
		debugState = false;
		debugTimer = System.currentTimeMillis();
		
		// Set up the debug window (2 windows: input and output)
		windowInput = new Rectangle(Engine.getWidth() / 2, Engine.getHeight() - 90f, 0f, Engine.getWidth(), 30f, 0f, 0f, Engine.orthoMatrix, new Vector4f(0f, 0f, 0f, 0.6f));
		windowOutput = new Rectangle(Engine.getWidth() / 2, Engine.getHeight() - 45f, 0f, Engine.getWidth(), 60f, 0f, 0f, Engine.orthoMatrix, new Vector4f(0f, 1f, 0f, 0.6f));
		
		// Blinker
		windowBlinker = new Rectangle(5, Engine.getHeight() - 90f, 0f, 5f, 30f, 0f, 0f, Engine.orthoMatrix, new Vector4f(1f, 1f, 1f, 1f));
		
		blinkShow = true;
		blinkTimer = System.currentTimeMillis();
	}
	
	public void update()
	{
		
		// This part functions as a listener for the debug init command (tab)
		float timeD = (float) (System.currentTimeMillis() - debugTimer);
		
		if(KeyboardInput.isPressed(GLFW_KEY_TAB) && !debugState && timeD >= debugTimeOut) 
		{
			
			System.out.println("Debugger on");
			debugTimer = System.currentTimeMillis();
			debugState = true; 
		}	
		else if(KeyboardInput.isPressed(GLFW_KEY_TAB) && debugState && timeD >= debugTimeOut) {
			
			System.out.println("Debugger off");
			debugTimer = System.currentTimeMillis();
			debugState = false;
		}

		// Blinker is independent of whether debugger is active
		float timeB = (float) (System.currentTimeMillis() - blinkTimer);
		
		if(blinkShow && timeB >= blinkTimeOut)
		{
			
			blinkTimer = System.currentTimeMillis();
			blinkShow = false;
		}
		else if(!blinkShow && timeB >= blinkTimeOut)
		{
			
			blinkTimer = System.currentTimeMillis();
			blinkShow = true;
		}
		
		// Process input typed in the terminal
		if(debugState)
		{
			
			char currentChar = KeyboardInput.getCurrentKey();
			
			if(currentChar != 0 && currentChar != 258)
			{
				
				input.append(currentChar);
			}
			
			//if(input.length() != 0) System.out.println(input.toString());
			
			System.out.println(Engine.keyAction);
			
			windowBlinker.update();
			
			windowInput.update();
			windowOutput.update();
		}
	}
	
	public void render()
	{
		
		// Render the terminal
		if(debugState)
		{
			
			if(blinkShow) windowBlinker.render();
			
			windowInput.render();
			windowOutput.render();
		}
	}
}