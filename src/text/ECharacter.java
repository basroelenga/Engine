package text;

import java.util.ArrayList;

import math.Vector3f;
import shapes.Quad;

public class ECharacter {

	private char ch;
	
	private float texcoordX;
	private float texcoordY;
	
	private Quad quad;
	
	private float spacing = 16f;
	
	public ECharacter(char ch)
	{
		
		this.ch = ch;

		getTexcoords();
		constructQuad();
	}
	
	private void getTexcoords()
	{
		
		int index = (int) ch;

		texcoordX = index % spacing;
		texcoordY = (int) (index * (1 / spacing));
	}
	
	private void constructQuad()
	{
		
		ArrayList<Vector3f> points = new ArrayList<Vector3f>();
		
		points.add(new Vector3f(-0.5f, -0.5f, 0.0f));
		points.add(new Vector3f(0.5f, -0.5f, 0.0f));
		points.add(new Vector3f(0.5f, 0.5f, 0.0f));
		points.add(new Vector3f(-0.5f, 0.5f, 0.0f));
		
		quad = new Quad(points, texcoordX, texcoordY, 1 / spacing, 1 / spacing);
	}
	
	public char getChar()
	{
		return ch;
	}
	
	public Quad getQuad()
	{
		return quad;
	}
}