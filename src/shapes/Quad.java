package shapes;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.GL_STREAM_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glBufferSubData;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;
import static org.lwjgl.opengl.GL33.glVertexAttribDivisor;

import java.nio.FloatBuffer;
import java.util.ArrayList;

import org.lwjgl.BufferUtils;

import math.Vector3f;

public class Quad {

	private int vaoID;
	
	private int vboVID;
	private int vboMID;
	
	private Triangle triangle1;
	private Triangle triangle2;
	
	private Vector3f normal;
	
	private ArrayList<Vector3f> vPoints;
	private ArrayList<Vector3f> nPoints;
	
	/**
	 * Create a quad which will consist of 2 triangles which can be used for rendering purposes or just as a place holder.
	 * Texture coordinates are generated over the whole quad.
	 * @param points The 4 points of the quad as a arraylist(vector3f).
	 * @param store Should the quad data be stored in a VAO for rendering.
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
					
					0f, 1f,
					0f, 0f,
					1f, 0f
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
	
	public Quad(ArrayList<Vector3f> vPoints, Vector3f normal)
	{
		
		if(vPoints.size() == 4)
		{
			
			this.vPoints = vPoints;
			this.normal = normal;
			
			triangle1 = new Triangle(vPoints.get(0), vPoints.get(1), vPoints.get(2), normal);
			triangle2 = new Triangle(vPoints.get(0), vPoints.get(3), vPoints.get(2), normal);
		}
		else
		{
			throw new RuntimeException("Cannot construct a quad with more or less than four points");
		}
		
		FloatBuffer vertexData = BufferUtils.createFloatBuffer(getVertexData().length);
		vertexData.put(this.getVertexData());
		vertexData.flip();

		float[] texCoords = {
				
				0f, 1f,
				1f, 1f,
				1f, 0f,
				
				0f, 1f,
				0f, 0f,
				1f, 0f
		};
		
		FloatBuffer textureData = BufferUtils.createFloatBuffer(texCoords.length);
		textureData.put(texCoords);
		textureData.flip();
		
		FloatBuffer normalData = BufferUtils.createFloatBuffer(this.getNormalData().length);
		normalData.put(this.getNormalData());
		normalData.flip();
		
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
		
		int vboNID = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, vboNID);
		glBufferData(GL_ARRAY_BUFFER, normalData, GL_STATIC_DRAW);
		glVertexAttribPointer(2, 3, GL_FLOAT, false, 0, 0);
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		
		glBindVertexArray(0);
	}
	
	/**
	 * Create a quad which consists out of 2 triangles (contains vertices and normals).
	 * @param vPoints Vertices.
	 * @param nPoints Normals.
	 */
	public Quad(ArrayList<Vector3f> vPoints, ArrayList<Vector3f> nPoints, boolean store)
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
		
		if(store)
		{
			
			FloatBuffer vertexData = BufferUtils.createFloatBuffer(getVertexData().length);
			vertexData.put(this.getVertexData());
			vertexData.flip();

			float[] texCoords = {
					
					0f, 1f,
					1f, 1f,
					1f, 0f,
					
					0f, 1f,
					0f, 0f,
					1f, 0f
			};
			
			FloatBuffer textureData = BufferUtils.createFloatBuffer(texCoords.length);
			textureData.put(texCoords);
			textureData.flip();
			
			FloatBuffer normalData = BufferUtils.createFloatBuffer(this.getNormalData().length);
			normalData.put(this.getNormalData());
			normalData.flip();
			
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
			
			int vboNID = glGenBuffers();
			glBindBuffer(GL_ARRAY_BUFFER, vboNID);
			glBufferData(GL_ARRAY_BUFFER, normalData, GL_STATIC_DRAW);
			glVertexAttribPointer(2, 3, GL_FLOAT, false, 0, 0);
			glBindBuffer(GL_ARRAY_BUFFER, 0);
			
			glBindVertexArray(0);
		}
	}
	
	/**
	 * Creates a quad which consists out of 2 triangles and can be used for instanced rendering.
	 * @param numberOfQuads
	 */
	public Quad(int numberOfQuads)
	{
		
		triangle1 = new Triangle(new Vector3f(0f, 0f, 0f), new Vector3f(1f, 0f, 0f), new Vector3f(1f, 1f, 0f));
		triangle2 = new Triangle(new Vector3f(0f, 0f, 0f), new Vector3f(0f, 1f, 0f), new Vector3f(1f, 1f, 0f));
		
		FloatBuffer vertexData = BufferUtils.createFloatBuffer(this.getVertexData().length);
		vertexData.put(this.getVertexData());
		vertexData.flip();
		
		vaoID = glGenVertexArrays();
		glBindVertexArray(vaoID);
		
		vboVID = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, vboVID);
		glBufferData(GL_ARRAY_BUFFER, vertexData, GL_STATIC_DRAW);
		glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		
		// This VBO will contain the MVP
		vboMID = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, vboMID);
		
		int dataLength = 16;
		glBufferData(GL_ARRAY_BUFFER, numberOfQuads * dataLength * 4, GL_STREAM_DRAW);
		
		glEnableVertexAttribArray(3);
		glEnableVertexAttribArray(4);
		glEnableVertexAttribArray(5);
		glEnableVertexAttribArray(6);
		
		glVertexAttribPointer(3, 4, GL_FLOAT, false, dataLength * 4, 0 * 4);
		glVertexAttribDivisor(3, 1);
		
		glVertexAttribPointer(4, 4, GL_FLOAT, false, dataLength * 4, 4 * 4);
		glVertexAttribDivisor(4, 1);
		
		glVertexAttribPointer(5, 4, GL_FLOAT, false, dataLength * 4, 8 * 4);
		glVertexAttribDivisor(5, 1);
		
		glVertexAttribPointer(6, 4, GL_FLOAT, false, dataLength * 4, 12 * 4);
		glVertexAttribDivisor(6, 1);
		
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		
		glBindVertexArray(0);
	}
	
	/**
	 * Update the MVP VBO.
	 * @param data The data to be stored in the VBO.
	 * @param buffer The buffer in which the data is stored.
	 */
	public void updateVBO(float[] data, FloatBuffer buffer)
	{
		
		buffer.clear();
		buffer.put(data);
		buffer.flip();
		
		glBindBuffer(GL_ARRAY_BUFFER, vboMID);
		glBufferData(GL_ARRAY_BUFFER, buffer.capacity() * 4, GL_STREAM_DRAW);
		glBufferSubData(GL_ARRAY_BUFFER, 0, buffer);
		glBindBuffer(GL_ARRAY_BUFFER, 0);
	}
	
	/**
	 * Get the triangles which are contained in the quad.
	 * @return Triangles in quad.
	 */
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
		
		int length = 2 * triangle1.getNormalData().length;
		float[] nData = new float[length];
		
		for(int i = 0; i < length / 2; i++)
		{
			
			nData[i] = triangle1.getNormalData()[i];
			nData[i + length / 2] = triangle2.getNormalData()[i];
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