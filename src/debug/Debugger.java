package debug;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_TAB;

import engine.Engine;
import engine.primitives.Rectangle;
import input.KeyboardInput;
import math.Vector4f;
import text.Text;
import text.TextManager;

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
	
	// Position of the blinker
	private float blinkerPosition = 0f;
	
	// Blink timer
	private float blinkTimeOut = 500f;
	private long blinkTimer;
	
	// Start text
	private Text startText;
	
	// Input string
	private StringBuilder input = new StringBuilder();
	
	// Dynamic text object
	private Text text;
	
	// Text command & reply
	private Text command;
	private Text reply;
	
	// Show reply
	private boolean showReply = false;
	
	public Debugger()
	{
		
		// Set the debug state
		debugState = false;
		debugTimer = System.currentTimeMillis();
		
		// Set up the debug window (2 windows: input and output)
		windowInput = new Rectangle(0f, Engine.getHeight() - 90f, 0f, Engine.getWidth(), 30f, 0f, 0f, Engine.orthoMatrix, new Vector4f(0f, 0f, 0f, 0.6f));
		windowOutput = new Rectangle(0f, Engine.getHeight() - 60f, 0f, Engine.getWidth(), 60f, 0f, 0f, Engine.orthoMatrix, new Vector4f(0.8f, 0.8f, 0.8f, 0.6f));
		
		// Blinker
		windowBlinker = new Rectangle(20, Engine.getHeight() - 90f, 0f, 5f, 30f, 0f, 0f, Engine.orthoMatrix, new Vector4f(1f, 1f, 1f, 1f));
		
		blinkShow = true;
		blinkTimer = System.currentTimeMillis();
		
		// Initial text
		startText = new Text(">", "HUD", 0, Engine.getHeight() - 60f, 0f);
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
			
			// This get the current key
			char currentChar = (char) (KeyboardInput.getCurrentKey());
			
			// Fix for capital to normal letters
			if((int) currentChar >= 65 && (int) currentChar <= 90 && !KeyboardInput.shiftPressed) currentChar += 32;

			// The 0 key char should be ignore
			if(currentChar != 0 && currentChar != 258)
			{	
				
				// Handle a backspace event
				if((int) currentChar == 259)
				{
					
					if(input.length() != 0)
					{
						
						// This makes the blinker move with the text
						blinkerPosition = TextManager.getCharacter(input.charAt(input.length() - 1)).getXScaleCorrection() * text.getScaling();
						windowBlinker.setX(windowBlinker.getX() - blinkerPosition);
						
						input.deleteCharAt(input.length() - 1);
					}
				}
				// Handle a enter key
				else if((int) currentChar == 257)
				{
					
					// Send the command for processing
					processCommand(input.toString());
					
					// Reset command line
					input = new StringBuilder();
					windowBlinker.setX(20f);
				}
				else
				{
					
					// Don't add a shift key
					if((int) currentChar != 340)
					{
						
						// This makes the blinker move with the text
						blinkerPosition = TextManager.getCharacter(currentChar).getXScaleCorrection() * text.getScaling();
						windowBlinker.setX(windowBlinker.getX() + blinkerPosition);
						
						input.append(currentChar);
					}
				}
			}
			
			//if(input.length() != 0) System.out.println(input.toString());
			
			//System.out.println(Engine.keyAction);
			text = new Text(input.toString(), "HUD", 20, Engine.getHeight() - 60f, 0.1f);
			
			windowBlinker.update();
			
			windowInput.update();
			windowOutput.update();
		}
	}
	
	/**
	 * Process the input command
	 * @param commandS Input command
	 */
	private void processCommand(String commandS)
	{
		
		command = new Text("Command: " + commandS, "HUD", 20f, Engine.getHeight(), 0f);
		
		switch(commandS)
		{
		
		case "Show FPS":
			
			if(Engine.showFPS) Engine.showFPS = false;
			else Engine.showFPS = true;
			
			reply = new Text("Show FPS: " + Engine.showFPS, "HUD", 20f, Engine.getHeight() - 30f, 0.1f);
			break;
		
		case "Exit":
			
			Engine.isRunning = false;
			
			reply = new Text("Exiting", "HUD", 20f, Engine.getHeight() - 30f, 0.1f);
			break;
			
		default:
				
			reply = new Text("Command not recognized", "HUD", 20f, Engine.getHeight() - 30f, 0.1f);
			break;
		}
		
		showReply = true;
	}
	
	public void render()
	{
		
		// Render the terminal
		if(debugState)
		{
			
			if(blinkShow) windowBlinker.render();
			
			windowInput.render();
			windowOutput.render();
			
			startText.updateAndRender();
			text.updateAndRender();
			
			if(showReply)
			{
				
				command.updateAndRender();
				reply.updateAndRender();
			}
		}
	}
}