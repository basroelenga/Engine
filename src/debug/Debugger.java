package debug;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_TAB;
import input.KeyboardInput;

import java.util.ArrayList;

import math.Vector4f;
import text.Text;
import text.TextManager;
import engine.Engine;
import engine.objects.Rectangle;

public class Debugger {

	public static boolean debugState;
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
	
	// List of all commands
	private ArrayList<String> commandList = new ArrayList<String>();
	private int listIndex = 0;
	
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
				// Handle the arrow keys
				else if((int) currentChar == 262 || (int) currentChar == 263 || (int) currentChar == 264 || (int) currentChar == 265)
				{
					
					switch(currentChar)
					{
					
					// The left right keys
					case 262:
						
						
						
						break;
						
					case 263:
						
						
						break;
					
					// The down key
					case 264:

						if(listIndex < commandList.size())
						{
							
							// Down the listindex
							listIndex += 1;
							
							// Update the text
							String temp = commandList.get(listIndex - 1);
							input = new StringBuilder();
							
							for(int i = 0; i < temp.length(); i++) input.append(temp.charAt(i));
							
							// Set the blinker to the correct position
							float tempBlink = 20f;
							
							for(int i = 0; i < temp.length(); i++)
							{
								
								tempBlink += TextManager.getCharacter(temp.charAt(i)).getXScaleCorrection() * text.getScaling();
								
							}
							
							blinkerPosition = tempBlink;
							windowBlinker.setX(blinkerPosition);
						}
						
						break;
					
					// The up key
					case 265:

						if(listIndex > 1)
						{
							
							// Update the text
							String temp = commandList.get(listIndex - 1);
							input = new StringBuilder();
							
							for(int i = 0; i < temp.length(); i++) input.append(temp.charAt(i));
							
							// Set the blinker to the correct position
							float tempBlink = 20f;
							
							for(int i = 0; i < temp.length(); i++)
							{
								
								tempBlink += TextManager.getCharacter(temp.charAt(i)).getXScaleCorrection() * text.getScaling();
								
							}
							
							blinkerPosition = tempBlink;
							windowBlinker.setX(blinkerPosition);
							
							// Down the listindex
							listIndex -= 1;
						}
						else if(listIndex == 1)
						{
							
							// Update the text
							String temp = commandList.get(0);
							input = new StringBuilder();
							
							for(int i = 0; i < temp.length(); i++) input.append(temp.charAt(i));
							
							// Set the blinker to the correct position
							float tempBlink = 20f;
							
							for(int i = 0; i < temp.length(); i++)
							{
								
								tempBlink += TextManager.getCharacter(temp.charAt(i)).getXScaleCorrection() * text.getScaling();
								
							}
							
							blinkerPosition = tempBlink;
							windowBlinker.setX(blinkerPosition);
						}
						
						break;
					}
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
		String[] commandParts = commandS.split(" ");
		
		// Add the command to the list 
		commandList.add(commandS);
		listIndex = commandList.size();
		
		if(commandParts.length != 2)
		{
		
			reply = new Text("Command has to few arguments", "HUD", 20f, Engine.getHeight() - 30f, 0.1f);
			showReply = true;
			
			return;
		}
		
		switch(commandParts[0])
		{
		
		case "show-fps":
			
			if(commandParts[1].equals("0")) Engine.showFPS = false;
			else if(commandParts[1].equals("1")) Engine.showFPS = true;
			else
			{
			
				reply = new Text("Not a valid command", "HUD", 20f, Engine.getHeight() - 30f, 0.1f);
				break;
			}
			
			reply = new Text("Show FPS: " + Engine.showFPS, "HUD", 20f, Engine.getHeight() - 30f, 0.1f);
			break;
		
		case "show-wireframe":
			
			if(commandParts[1].equals("0")) Engine.wireframe = false;
			else if(commandParts[1].equals("1")) Engine.wireframe = true;
			else
			{
			
				reply = new Text("Not a valid command", "HUD", 20f, Engine.getHeight() - 30f, 0.1f);
				break;
			}
			
			reply = new Text("Show wireframe: " + Engine.wireframe, "HUD", 20f, Engine.getHeight() - 30f, 0.1f);
			break;
			
		case "exit":
			
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