package text;

import java.util.ArrayList;

import engine.Engine;
import math.Vector3f;
import shapes.Quad;

public class ECharacter {

	private char ch;
	
	private float texcoordX;
	private float texcoordY;
	
	private Quad quad;
	
	private float spacing = 16f;
	
	private float spacingX;
	private float spacingY;
	
	private float correctionXFactorTrans;
	private float correctionXFactorScale;
	
	/**
	 * The character to be used in a square bitmap
	 * @param ch Character id
	 */
	public ECharacter(char ch)
	{
		
		this.ch = ch;
		int index = (int) ch;
		
		texcoordX = (index % spacing) / spacing;
		texcoordY = ((int) (index * (1 / spacing))) / spacing;
		
		spacingX = 1 / spacing;
		spacingY = 1 / spacing;
		
		constructQuad(1);
	}
	
	/**
	 * The character to be used with config file
	 * @param ID The ASCII id of the character
	 * @param posX X position of the character
	 * @param posY Y position of the character
	 * @param width Width of the character
	 * @param height Height of the character
	 */
	public ECharacter(int ID, int posX, int posY, int width, int height)
	{
		
		this.ch = (char) ID;
		
		// Convert the position to a [0, 1] system
		texcoordX = posX / (float) TextManager.getFontTexture().getWidth();
		texcoordY = posY / (float) TextManager.getFontTexture().getHeight();
		
		spacingX = width / (float) TextManager.getFontTexture().getWidth();
		spacingY = height / (float) TextManager.getFontTexture().getHeight();
		
		correctionXFactorTrans = width * (((float) Engine.getWidth()) / TextManager.getFontTexture().getWidth());
		
		System.out.println(correctionXFactorTrans + " , " + width);
		
		correctionXFactorScale = width / TextManager.xMax;

		
		constructQuad(correctionXFactorScale);
	}
	
	private void constructQuad(float correctionXFactor)
	{
		
		ArrayList<Vector3f> points = new ArrayList<Vector3f>();
		
		points.add(new Vector3f(-0.5f * correctionXFactor, -0.5f, 0.0f));
		points.add(new Vector3f(0.5f * correctionXFactor, -0.5f, 0.0f));
		points.add(new Vector3f(0.5f * correctionXFactor, 0.5f, 0.0f));
		points.add(new Vector3f(-0.5f * correctionXFactor, 0.5f, 0.0f));
		
		quad = new Quad(points, texcoordX, texcoordY, spacingX, spacingY);
	}
	
	public char getChar()
	{
		return ch;
	}
	
	public Quad getQuad()
	{
		return quad;
	}
	
	public float getXScaleCorrection()
	{
		return correctionXFactorScale;
	}
	
	public float getXTransCorrection()
	{
		return correctionXFactorTrans;
	}
}