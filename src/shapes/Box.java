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
import java.util.List;

import org.lwjgl.BufferUtils;

import math.Vector3f;

public class Box {

	private int vaoID;
	private int vboVID;
	
	public Box()
	{
		
		// A box consists out of 6 quads
		ArrayList<Quad> quadList = getBoxData();
		
		List<Float> verticesList = new ArrayList<Float>();
		List<Float> normalList = new ArrayList<Float>();
		
		for(Quad quad : quadList)
		{
			
			float[] verticesQuad = quad.getVertexData();
			float[] normalsQuad = quad.getNormalData();
			
			for(int i = 0; i < verticesQuad.length; i++)
			{
				
				verticesList.add(verticesQuad[i]);
			}
			
			for(int i = 0; i < normalsQuad.length; i++)
			{
				
				normalList.add(normalsQuad[i]);
			}
		}

		
		float[] vertices = new float[verticesList.size()];
		int i = 0;
		
		for(Float f : verticesList)
		{
			vertices[i++] = f;
		}
		
		float[] normals = new float[normalList.size()];
		int j = 0;
		
		for(Float n : normalList)
		{
			normals[j++] = n;
		}
		
		System.out.println(normals.length);
		
		FloatBuffer vertexData = BufferUtils.createFloatBuffer(vertices.length);
		vertexData.put(vertices);
		vertexData.flip();
		
		FloatBuffer normalData = BufferUtils.createFloatBuffer(normals.length);
		normalData.put(normals);
		normalData.flip();
		
		vaoID = glGenVertexArrays();
		glBindVertexArray(vaoID);
		
		vboVID = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, vboVID);
		glBufferData(GL_ARRAY_BUFFER, vertexData, GL_STATIC_DRAW);
		glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		
		int vboNID = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, vboNID);
		glBufferData(GL_ARRAY_BUFFER, normalData, GL_STATIC_DRAW);
		glVertexAttribPointer(2, 3, GL_FLOAT, false, 0, 0);
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		
		glBindVertexArray(0);
	}
	
	private ArrayList<Quad> getBoxData()
	{
		
		// List containing all the quads
		ArrayList<Quad> quadList = new ArrayList<Quad>();
		ArrayList<Vector3f> points = new ArrayList<Vector3f>();
		
		// Create the front quad
		points.clear();
		
		points.add(new Vector3f(-1.0f, -1.0f, 1.0f));
		points.add(new Vector3f(1.0f, -1.0f, 1.0f));
		points.add(new Vector3f(1.0f, 1.0f, 1.0f));
		points.add(new Vector3f(-1.0f, 1.0f, 1.0f));
		
		Vector3f normal = new Vector3f(0f, 0f, 1f);

		Quad front = new Quad(points, normal, false);
		quadList.add(front);
		
		// Create the back quad
		points.clear();
		
		points.add(new Vector3f(-1.0f, -1.0f, -1.0f));
		points.add(new Vector3f(1.0f, -1.0f, -1.0f));
		points.add(new Vector3f(1.0f, 1.0f, -1.0f));
		points.add(new Vector3f(-1.0f, 1.0f, -1.0f));
		
		normal = new Vector3f(0f, 0f, -1f);

		Quad back = new Quad(points, normal, false);
		quadList.add(back);
		
		// Create the left quad
		points.clear();
		
		points.add(new Vector3f(1.0f, -1.0f, -1.0f));
		points.add(new Vector3f(1.0f, -1.0f, 1.0f));
		points.add(new Vector3f(1.0f, 1.0f, 1.0f));
		points.add(new Vector3f(1.0f, 1.0f, -1.0f));
		
		normal = new Vector3f(1f, 0f, 0f);

		Quad left = new Quad(points, normal, false);
		quadList.add(left);
		
		// Create the right quad
		points.clear();
		
		points.add(new Vector3f(-1.0f, -1.0f, -1.0f));
		points.add(new Vector3f(-1.0f, -1.0f, 1.0f));
		points.add(new Vector3f(-1.0f, 1.0f, 1.0f));
		points.add(new Vector3f(-1.0f, 1.0f, -1.0f));
		
		normal = new Vector3f(-1f, 0f, 0f);

		Quad right = new Quad(points, normal, false);
		quadList.add(right);
		
		// Create the top quad
		points.clear();
		
		points.add(new Vector3f(-1.0f, 1.0f, -1.0f));
		points.add(new Vector3f(-1.0f, 1.0f, 1.0f));
		points.add(new Vector3f(1.0f, 1.0f, 1.0f));
		points.add(new Vector3f(1.0f, 1.0f, -1.0f));
		
		normal = new Vector3f(0f, 1f, 0f);

		Quad top = new Quad(points, normal, false);
		quadList.add(top);
		
		// Create the top quad
		points.clear();
		
		points.add(new Vector3f(-1.0f, -1.0f, -1.0f));
		points.add(new Vector3f(-1.0f, -1.0f, 1.0f));
		points.add(new Vector3f(1.0f, -1.0f, 1.0f));
		points.add(new Vector3f(1.0f, -1.0f, -1.0f));
		
		normal = new Vector3f(0f, -1f, 0f);

		Quad bottom = new Quad(points, normal, false);
		quadList.add(bottom);
		
		return quadList;
	}
	
	public int getVaoID()
	{
		return vaoID;
	}
}