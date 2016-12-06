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
	
	private ArrayList<ECharacter> charList = new ArrayList<ECharacter>();
	
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
			
			scaling = 32f;
			
			break;
		}
		
		modelMatrix = new Matrix4f();
	}
	
	private void constructCharList()
	{
		
		charList.clear();
		
		for(int i = 0; i < text.length(); i++)
		{
		
			charList.add(TextManager.getCharacter(text.charAt(i)));	
		}
	}
	
	public void updateAndRender()
	{
		
		float xTrans = 0;
		
		for(int i = 0; i < text.length(); i++)
		{

			modelMatrix.setIdentity();
			
			modelMatrix.transelate(x + xTrans, y, z + (i * 0.01f));
			xTrans += charList.get(i).getXScaleCorrection() * scaling;
			
			modelMatrix.rotateQ(0f, 0f, 180f, false);
			modelMatrix.rotateQ(0f, 180f, 0f, false);
			
			modelMatrix.scale(scaling, scaling, 1f);
			
			textShader.uploadMatrices(modelMatrix, projMatrix, Camera.getViewMatrix(), new Matrix4f());
			DrawShapes.drawQuad(textShader, TextManager.getFontTexture(), charList.get(i).getQuad().getVaoID());
		}
	}
	
	public void updateText(String newText)
	{
		
		text = newText;
		constructCharList();
	}
	
	public float getScaling()
	{
		return this.scaling;
	}
}