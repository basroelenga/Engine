package text;

import java.util.ArrayList;

import math.Matrix4f;
import matrices.MatrixObjectManager;
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
	
	private Shader shader;
	
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
			
			projMatrix = MatrixObjectManager.getMatrixObject("projectionMatrixDefault").getMatrix();
			shader = ShaderManager.getShader("basictex");
			
			scaling = 1f;
			
			break;
			
		case "HUD":
		
			projMatrix = MatrixObjectManager.getMatrixObject("orthographicMatrixDefault").getMatrix();
			shader = ShaderManager.getShader("ui");
			
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
			
			modelMatrix.translate(x + xTrans, y, z + (i * 0.01f));
			xTrans += charList.get(i).getXScaleCorrection() * scaling;
			
			modelMatrix.rotateQ(0f, 0f, 180f, false);
			modelMatrix.rotateQ(0f, 180f, 0f, false);
			
			modelMatrix.scale(scaling, scaling, 1f);
			
			shader.uploadMatrix4f(modelMatrix, shader.getModelMatrixLoc());
			shader.uploadMatrix4f(projMatrix, shader.getProjectionMatrixLoc());
			
			DrawShapes.drawQuad(shader, TextManager.getFontTexture(), null, charList.get(i).getQuad().getVaoID());
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