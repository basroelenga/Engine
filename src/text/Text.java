package text;

import java.util.ArrayList;

import cam.Camera;
import engine.Engine;
import math.Matrix4f;
import shaders.Shader;
import shaders.ShaderManager;
import utils.DrawShapes;

public class Text {
	
	private float x;
	private float y;
	private float z;
	
	private float scaling;
	
	private Matrix4f modelMatrix;
	private Matrix4f projMatrix;
	
	private Shader textShader;
	
	private String text;
	
	private ArrayList<Integer> charIDList = new ArrayList<Integer>();
	
	public Text(String text, String type, float x, float y, float z)
	{
		
		this.x = x;
		this.y = y;
		this.z = z;
		
		this.text = text;
		constructCharList();
		
		switch(type)
		{
		
		case "3D":
			
			projMatrix = Engine.projMatrix;
			textShader = ShaderManager.getShader("basictex");
			
			scaling = 1f;
			
			break;
			
		case "HUD":
		
			projMatrix = Engine.orthoMatrix;
			textShader = ShaderManager.getShader("ui");
			
			scaling = 50f;
			
			break;
		}
		
		modelMatrix = new Matrix4f();
	}
	
	private void constructCharList()
	{
		
		charIDList.clear();
		
		for(int i = 0; i < text.length(); i++)
		{
		
			charIDList.add(TextManager.getCharacter(text.charAt(i)).getQuad().getVaoID());	
		}
	}
	
	public void updateAndRender()
	{
		
		for(int i = 0; i < text.length(); i++)
		{

			modelMatrix.setIdentity();
			
			modelMatrix.transelate(x + (i * scaling * 0.6f), y, z + (i * 0.01f));
			
			modelMatrix.rotateQ(0f, 0f, 180f, false);
			modelMatrix.rotateQ(0f, 180f, 0f, false);
			
			modelMatrix.scale(scaling, scaling, 1f);
			
			textShader.uploadMatrices(modelMatrix, projMatrix, Camera.getViewMatrix());
			DrawShapes.drawQuad(textShader, TextManager.getFontTexture(), charIDList.get(i));
		}
	}
	
	public void updateText(String newText)
	{
		
		text = newText;
		constructCharList();
	}
}