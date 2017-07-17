package debug;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_TAB;

import java.util.ArrayList;

import engine.Engine;
import engine.EngineObjectManager;
import engine.objects.Rectangle;
import fbo.FrameBufferObject;
import fbo.FrameBufferObjectManager;
import graphics.Texture;
import graphics.TextureManager;
import input.KeyboardInput;
import light.LightManager;
import math.Matrix4f;
import math.Vector4f;
import matrices.MatrixObjectManager;
import shaders.Shader;
import shaders.ShaderManager;
import text.Text;
import text.TextManager;
import utils.DrawShapes;

public class Debugger {

	// The debug framebuffer object
	FrameBufferObject debugFBO;
	
	// States of the debug window
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
	
	// Number of possible out lines
	private int outLines = 5;
	private ArrayList<Text> outList = new ArrayList<Text>();
	
	// Size of debug text
	private int DEBUGTEXTSIZE = 24;
	
	// Start text
	private Text startText;
	
	// Input string
	private StringBuilder input = new StringBuilder();
	
	// Dynamic text object
	private Text inputText;
	
	// Text reply
	private Text reply;
	
	// List of all commands
	private ArrayList<String> commandList = new ArrayList<String>();
	private int listIndex = 0;
	
	// Matrices for FBO rendering
	private Matrix4f modelMatrix;
	
	private Texture fboTexture;
	private Shader fboShader;
	
	public Debugger()
	{
		
		// Set the debug state
		debugState = false;
		debugTimer = System.currentTimeMillis();
		
		// Create the debug FBO
		FrameBufferObjectManager.addFrameBufferObject("debug", "default", 2048, 2048);
		debugFBO = FrameBufferObjectManager.getFrameBuffer("debug");
		
		// Set up the debug window (2 windows: input and output)
		// The input window:
		float x_in = 0f;
		float y_in = Engine.getHeight() - (outLines + 1) * DEBUGTEXTSIZE;
		
		float xScale_in = Engine.getWidth();
		float yScale_in = DEBUGTEXTSIZE;
		
		Vector4f color_in = new Vector4f(0.0f, 0.0f, 0.0f, 0.8f);
		windowInput = new Rectangle("windowIn", null, x_in, y_in, xScale_in, yScale_in, color_in);
		
		// The output window:
		float x_out = 0f;
		float y_out = Engine.getHeight() - outLines * DEBUGTEXTSIZE;
		
		float xScale_out = Engine.getWidth();
		float yScale_out = outLines * DEBUGTEXTSIZE;
		
		Vector4f color_out = new Vector4f(0.8f, 0.8f, 0.8f, 0.8f);
		windowOutput = new Rectangle("windowOut", null, x_out, y_out, xScale_out, yScale_out, color_out);
		
		// Blinker
		windowBlinker = new Rectangle("blinker", null, 20, Engine.getHeight() - (outLines + 1) * DEBUGTEXTSIZE, 5f, DEBUGTEXTSIZE, new Vector4f(1f, 1f, 1f, 1f));
		
		blinkShow = true;
		blinkTimer = System.currentTimeMillis();
		
		// Initial text
		startText = new Text(">", "HUD", 0, Engine.getHeight() - outLines * DEBUGTEXTSIZE, 0f, DEBUGTEXTSIZE);
		
		// Requirments for rendering the debug FBO
		modelMatrix = new Matrix4f();
		
		modelMatrix.translate(0, Engine.getHeight(), 0.1f);
		modelMatrix.scale(Engine.getWidth(), Engine.getHeight(), 0);
		modelMatrix.rotateQ(180, 0, 0, false);
		
		fboTexture = new Texture("debug_texture", debugFBO.getTexID());
		fboShader = ShaderManager.getShader("basictex");
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
						blinkerPosition = TextManager.getCharacter(input.charAt(input.length() - 1)).getXScaleCorrection() * inputText.getScaling();
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
								
								tempBlink += TextManager.getCharacter(temp.charAt(i)).getXScaleCorrection() * inputText.getScaling();
								
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
								
								tempBlink += TextManager.getCharacter(temp.charAt(i)).getXScaleCorrection() * inputText.getScaling();
								
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
								
								tempBlink += TextManager.getCharacter(temp.charAt(i)).getXScaleCorrection() * inputText.getScaling();
								
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
						blinkerPosition = TextManager.getCharacter(currentChar).getXScaleCorrection() * inputText.getScaling();
						windowBlinker.setX(windowBlinker.getX() + blinkerPosition);
						
						input.append(currentChar);
					}
				}
			}
						
			// Text that is displayed as the input
			inputText = new Text(input.toString(), "HUD", 20, Engine.getHeight() - outLines * DEBUGTEXTSIZE, 0.1f, DEBUGTEXTSIZE);
			
			// Update the different components
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
		
		String[] commandParts = commandS.split(" ");
		
		// Add the command to the list 
		commandList.add(commandS);
		listIndex = commandList.size();
		
		switch(commandParts[0])
		{
		
		// Show different properties of the engine
		case "show":
			
			// Check if there are enough parameters
			if(commandParts.length == 1)
			{
				
				reply = new Text("Following show commands available: fps, wireframe", "HUD", 20f, 0f, 0.1f, DEBUGTEXTSIZE);
				break;
			}
			
			switch(commandParts[1])
			{
			
			case "fps":
				
				if(commandParts.length == 2)
				{
					
					reply = new Text("Missing command parameter (0 or 1)", "HUD", 20f, 0f, 0.1f, DEBUGTEXTSIZE);
					break;
				}
				else
				{
					
					if(commandParts[2].equals("0")) Engine.showFPS = false;
					else if(commandParts[2].equals("1")) Engine.showFPS = true;
					else 
					{
						
						reply = new Text("Not a valid command parameter", "HUD", 20f, 0f, 0.1f, DEBUGTEXTSIZE);
						break;
					}
					
					reply = new Text("Show FPS: " + Engine.showFPS, "HUD", 20f, 0f, 0.1f, DEBUGTEXTSIZE);
					break;
				}
				
			case "wireframe":
				
				if(commandParts.length == 2)
				{
					
					reply = new Text("Missing command parameter (0 or 1)", "HUD", 20f, 0f, 0.1f, DEBUGTEXTSIZE);
					break;
				}
				else
				{
					
					if(commandParts[2].equals("0")) Engine.wireframe = false;
					else if(commandParts[2].equals("1")) Engine.wireframe = true;
					else
					{
					
						reply = new Text("Not a valid command parameter", "HUD", 20f, 0f, 0.1f, DEBUGTEXTSIZE);
						break;
					}
					
					reply = new Text("Show wireframe: " + Engine.wireframe, "HUD", 20f, 0f, 0.1f, DEBUGTEXTSIZE);
				}
				
				break;
			
			default:
				
				reply = new Text("Show command not recognized, type show for more information", "HUD", 20f, 0f, 0.1f, DEBUGTEXTSIZE);
				break;
			}
			
			break;
		
		// Spawn an object in the engine
		case "spawn":
			
			// Check if there are enough parameters
			if(commandParts.length == 1)
			{
				
				reply = new Text("Following spawn commands available: rectangle", "HUD", 20f, 0f, 0.1f, DEBUGTEXTSIZE);
				break;
			}
			
			switch(commandParts[1])
			{
			
			case "rectangle":
				
				if(commandParts.length == 2)
				{
					
					reply = new Text("Missing command parameter", "HUD", 20f, 0f, 0.1f, DEBUGTEXTSIZE);
					break;
				}
				else
				{
					
					String name = commandParts[2];
					Texture tex = TextureManager.getTexture(commandParts[3]);
					
					float x = Float.parseFloat(commandParts[4]);
					float y = Float.parseFloat(commandParts[5]);
					float z = Float.parseFloat(commandParts[6]);
					
					float xs = Float.parseFloat(commandParts[7]);
					float ys = Float.parseFloat(commandParts[8]);
					float zs = Float.parseFloat(commandParts[9]);
					
					EngineObjectManager.addRectangle(name, tex, x, y, z, xs, ys, zs);
				}
				
				break;
			}
			
			break;
		
		// Switch different effects on and off
		case "toggle":
			
			// Check if there are enough parameters
			if(commandParts.length == 1)
			{
				
				reply = new Text("Following toggle commands available: shadow", "HUD", 20f, 0f, 0.1f, DEBUGTEXTSIZE);
				break;
			}
			
			switch(commandParts[1])
			{
			
			case "shadow":
				
				if(commandParts.length == 2)
				{
					
					reply = new Text("Missing command parameter (0 or 1)", "HUD", 20f, 0f, 0.1f, DEBUGTEXTSIZE);
					break;
				}
				else
				{
					
					if(commandParts[2].equals("0")) LightManager.toggleShadow(false);
					else if(commandParts[2].equals("1")) LightManager.toggleShadow(true);
					else
					{
					
						reply = new Text("Not a valid command parameter", "HUD", 20f, 0f, 0.1f, DEBUGTEXTSIZE);
						break;
					}
					
					reply = new Text("Toggle shadows: " + LightManager.getRenderShadows(), "HUD", 20f, 0f, 0.1f, DEBUGTEXTSIZE);
					break;
				}
				
			default:
				
				reply = new Text("Toggle command not recognized, type toggle for more information", "HUD", 20f, 0f, 0.1f, DEBUGTEXTSIZE);
				break;
			}
			
			break;
			
		// Grab an object to move it in 3 dimensional space
		// Select in which plane to object needs to be moved
		case "grab":
			
			reply = new Text("Need to implement", "HUD", 20f, 0f, 0.1f, DEBUGTEXTSIZE);
			break;
			
		// Exit the engine
		case "exit":
			
			Engine.isRunning = false;
			
			reply = new Text("Exiting", "HUD", 20f, 0f, 0.1f, DEBUGTEXTSIZE);
			break;
		
		// Clear the terminal
		case "clear":
			
			outList.clear();
			return;
		
		// The default response if the command is unknown
		default:
				
			reply = new Text("Command not recognized: " + commandS, "HUD", 20f, 0f, 0.1f, DEBUGTEXTSIZE);
			break;
		}
		
		addToOutQueue();
	}
	
	private void addToOutQueue()
	{
		
		if(outList.size() >= outLines)
		{
			
			outList.remove(0);
			outList.add(reply);
		}
		else
		{
			outList.add(reply);
		}
	}
	
	public void render()
	{
		// Select the debug FBO
		debugFBO.bind();
		
		// Render the terminal
		if(debugState)
		{
			
			if(blinkShow) windowBlinker.render();
			
			windowInput.render();
			windowOutput.render();
			
			startText.updateAndRender();
			inputText.updateAndRender();
			
			for(int i = 0; i < outList.size(); i++)
			{
				
				outList.get(i).setY(Engine.getHeight() - i * DEBUGTEXTSIZE);
				outList.get(i).updateAndRender();
			}
		}
		
		// Unbind the debug FBO
		debugFBO.unbind();
	}
	
	public void renderFBO()
	{
		
		fboShader.uploadMatrix4f(modelMatrix, fboShader.getModelMatrixLoc());
		fboShader.uploadMatrix4f(new Matrix4f(), fboShader.getViewMatrixLoc());
		fboShader.uploadMatrix4f(MatrixObjectManager.getMatrixObject("orthographicMatrixDefault").getMatrix(), fboShader.getProjectionMatrixLoc());
		
		DrawShapes.drawQuadToScreen(fboShader, fboTexture, null, EngineObjectManager.getQuad().getVaoID());
	}
}