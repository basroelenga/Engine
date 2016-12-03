package shapes;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

import java.nio.FloatBuffer;
import java.util.ArrayList;

import org.lwjgl.BufferUtils;

import math.Vector3f;

public class Quad {

	private int vaoID;
	
	private Triangle triangle1;
	private Triangle triangle2;
	
	private ArrayList<Vector3f> vPoints;
	private ArrayList<Vector3f> nPoints;
	
	/**
	 * Create a quad which will consist of 2 triangles which can be used for rendering purposes.
	 * Texture coordinates are generated over the whole quad.
	 * @param points The 4 points of the quad as a arraylist(vector3f).
	 */
	public Quad(ArrayList<Vector3f> vPoints, boolean store)
	{
		
		if(vPoints.size() == 4)
		{
			
			this.vPoints = vPoints;
			
			triangle1 = new Triangle(vPoints.get(0), vPoints.get(1), vPoints.get(2));
			triangle2 = new Triangle(vPoints.get(0), vPoints.get(3), vPoints.get(2));
		}
		else
		{
			
			System.err.println("Cannot construct a quad with more or less than four points");
		}
		
		if(store)
		{
			
			FloatBuffer vertexData = BufferUtils.createFloatBuffer(this.getVertexData().length);
			vertexData.put(this.getVertexData());
			vertexData.flip();

			float[] texCoords = {
					
					0f, 1f,
					1f, 1f,
					1f, 0f,
					
					1f, 0f,
					0f, 0f,
					0f, 1f
			};
			
			FloatBuffer textureData = BufferUtils.createFloatBuffer(texCoords.length);
			textureData.put(texCoords);
			textureData.flip();
			
			vaoID = glGenVertexArrays();
			glBindVertexArray(vaoID);
			
			int vboVID = glGenBuffers();
			glBindBuffer(GL_ARRAY_BUFFER, vboVID);
			glBufferData(GL_ARRAY_BUFFER, vertexData, GL_STATIC_DRAW);
			glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
			glBindBuffer(GL_ARRAY_BUFFER, 0);
			
			int vboTID = glGenBuffers();
			glBindBuffer(GL_ARRAY_BUFFER, vboTID);
			glBufferData(GL_ARRAY_BUFFER, textureData, GL_STATIC_DRAW);
			glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, 0);
			glBindBuffer(GL_ARRAY_BUFFER, 0);
			
			glBindVertexArray(0);
		}
	}
	
	/**
	 * Create a quad which will consist of 2 triangles which can be used for rendering text.
	 * Texture coordinates are generated for a given input (the position of the character in the font png).
	 * @param points The 4 points of the quad as a arraylist(vector3f).
	 */
	public Quad(ArrayList<Vector3f> vPoints, float texCoordX, float texCoordY, float offsetX, float offsetY)
	{
		
		if(vPoints.size() == 4)
		{
			
			this.vPoints = vPoints;
			
			triangle1 = new Triangle(vPoints.get(0), vPoints.get(1), vPoints.get(2));
			triangle2 = new Triangle(vPoints.get(0), vPoints.get(3), vPoints.get(2));
		}
		else
		{
			
			System.err.println("Cannot construct a quad with more or less than four points");
		}
		
		FloatBuffer vertexData = BufferUtils.createFloatBuffer(this.getVertexData().length);
		vertexData.put(this.getVertexData());
		vertexData.flip();

		float[] texCoords = {
				
				texCoordX, texCoordY,
				texCoordX + offsetX, texCoordY,
				texCoordX + offsetX, texCoordY + offsetY,
				
				texCoordX, texCoordY,
				texCoordX, texCoordY + offsetY,
				texCoordX + offsetX, texCoordY + offsetY
		};
		
		FloatBuffer textureData = BufferUtils.createFloatBuffer(texCoords.length);
		textureData.put(texCoords);
		textureData.flip();
		
		vaoID = glGenVertexArrays();
		glBindVertexArray(vaoID);
		
		int vboVID = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, vboVID);
		glBufferData(GL_ARRAY_BUFFER, vertexData, GL_STATIC_DRAW);
		glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		
		int vboTID = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, vboTID);
		glBufferData(GL_ARRAY_BUFFER, textureData, GL_STATIC_DRAW);
		glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, 0);
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		
		glBindVertexArray(0);
	}
	
	public Quad(ArrayList<Vector3f> vPoints, ArrayList<Vector3f> nPoints)
	{
		
		if(vPoints.size() == 4 && nPoints.size() == 4)
		{
			
			this.vPoints = vPoints;
			this.nPoints = nPoints;
			
			triangle1 = new Triangle(vPoints.get(0), vPoints.get(1), vPoints.get(2), nPoints.get(0), nPoints.get(1), nPoints.get(2));
			triangle2 = new Triangle(vPoints.get(0), vPoints.get(3), vPoints.get(2), nPoints.get(0), nPoints.get(3), nPoints.get(2));
		}
		else
		{
			
			System.err.println("Cannot construct a quad with more or less than four points");
		}
	}
	
	public ArrayList<Triangle> getTriangle()
	{
		
		ArrayList<Triangle> quadTri = new ArrayList<Triangle>();
		
		quadTri.add(triangle1);
		quadTri.add(triangle2);
		
		return quadTri;
	}
	
	public float[] getVertexData()
	{
		
		float[] vData = new float[9 * 2];
		
		for(int i = 0; i < 9; i++)
		{
			
			vData[i] = triangle1.getVertexData()[i];
			vData[i + 9] = triangle2.getVertexData()[i];
		}
		
		return vData;
	}
	
	public float[] getNormalData()
	{
		
		float[] nData = new float[9 * 2];
		
		for(int i = 0; i < 9; i++)
		{
			
			nData[i] = triangle1.getNormalData()[i];
			nData[i + 9] = triangle2.getNormalData()[i];
		}
		
		return nData;
	}

	public void print()
	{
		
		System.out.println("Triangle 1");
		triangle1.print();
		
		System.out.println("Triangle 2");
		triangle2.print();
	}
	
	public int getVaoID()
	{
		return vaoID;
	}
	
	public ArrayList<Vector3f> getQuadVertexPoints()
	{
		return vPoints;
	}
	
	public ArrayList<Vector3f> getQuadNormalPoints()
	{
		return nPoints;
	}
}