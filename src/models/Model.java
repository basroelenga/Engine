package models;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

import java.nio.FloatBuffer;
import java.util.ArrayList;

import org.lwjgl.BufferUtils;

import utils.FileIO;

public class Model {

	private String name;
	
	private int vaoID;
	
	private int vertices;
	
	private boolean hasTexture = false;
	private boolean hasNormals = false;
	
	private String modelData;
	
	private ArrayList<Float> vertexList = new ArrayList<Float>();
	private ArrayList<Float> textureList = new ArrayList<Float>();
	private ArrayList<Float> normalList = new ArrayList<Float>();
	
	private ArrayList<Float> vertexOrderList = new ArrayList<Float>();
	private ArrayList<Float> textureOrderList = new ArrayList<Float>();
	private ArrayList<Float> normalOrderList = new ArrayList<Float>();
	
	public Model(String name)
	{
		
		this.name = name;
		
		modelData = FileIO.loadModel(name);
		addDataToLists();
	}
	
	/**
	 * Load all the different types of data into lists.
	 */
	private void addDataToLists()
	{
		
		String[] lineData = modelData.split("\n");
				
		for(int i = 0; i < lineData.length; i++)
		{
			
			String[] splitLineData = lineData[i].split(" ");
			
			switch(splitLineData[0])
			{
			
			// Add the vertex data.
			case "v":
				
				vertexList.add(Float.parseFloat(splitLineData[1]));
				vertexList.add(Float.parseFloat(splitLineData[2]));
				vertexList.add(Float.parseFloat(splitLineData[3]));
				
				break;
				
			// Add the texture data.
			case "vt":
				
				textureList.add(Float.parseFloat(splitLineData[1]));
				textureList.add(Float.parseFloat(splitLineData[2]));
				
				hasTexture = true;
				
				break;
				
			// Add the normal data.
			case "vn":
				
				normalList.add(Float.parseFloat(splitLineData[1]));
				normalList.add(Float.parseFloat(splitLineData[2]));
				normalList.add(Float.parseFloat(splitLineData[3]));

				hasNormals = true;
				
				break;
			
			// The last case, the faces, will order all the data from the lists in the correct order. 
			case "f":
				
				for(int j = 1; j < splitLineData.length; j++)
				{
					
					String[] lineSplitFaceData = splitLineData[j].split("//");
					System.out.println(lineSplitFaceData.length);
					
					// Should handle all kind of types of data.
					if(lineSplitFaceData.length == 1)
					{
						
						int v = Integer.parseInt(lineSplitFaceData[0]);
						
						vertexOrderList.add(vertexList.get(3 * v + 0 - 3));
						vertexOrderList.add(vertexList.get(3 * v + 1 - 3));
						vertexOrderList.add(vertexList.get(3 * v + 2 - 3));
					}
					else if(lineSplitFaceData.length == 2 & normalList.size() == 0)
					{
						
						int v = Integer.parseInt(lineSplitFaceData[0]);
						int vt = Integer.parseInt(lineSplitFaceData[1]);
						
						vertexOrderList.add(vertexList.get(3 * v + 0 - 3));
						vertexOrderList.add(vertexList.get(3 * v + 1 - 3));
						vertexOrderList.add(vertexList.get(3 * v + 2 - 3));
						
						textureOrderList.add(textureList.get(2 * vt + 0 - 2));
						textureOrderList.add(textureList.get(2 * vt + 1 - 2));
					}
					else if(lineSplitFaceData.length == 2 & textureList.size() == 0)
					{
						
						int v = Integer.parseInt(lineSplitFaceData[0]);
						int vn = Integer.parseInt(lineSplitFaceData[1]);
						
						vertexOrderList.add(vertexList.get(3 * v + 0 - 3));
						vertexOrderList.add(vertexList.get(3 * v + 1 - 3));
						vertexOrderList.add(vertexList.get(3 * v + 2 - 3));
						
						normalOrderList.add(normalList.get(3 * vn + 0 - 3));
						normalOrderList.add(normalList.get(3 * vn + 1 - 3));
						normalOrderList.add(normalList.get(3 * vn + 2 - 3));
					}
					else
					{
						
						int v = Integer.parseInt(lineSplitFaceData[0]);
						int vt = Integer.parseInt(lineSplitFaceData[1]);
						int vn = Integer.parseInt(lineSplitFaceData[2]);
						
						vertexOrderList.add(vertexList.get(3 * v + 0 - 3));
						vertexOrderList.add(vertexList.get(3 * v + 1 - 3));
						vertexOrderList.add(vertexList.get(3 * v + 2 - 3));
						
						textureOrderList.add(textureList.get(2 * vt + 0 - 2));
						textureOrderList.add(textureList.get(2 * vt + 1 - 2));
						
						normalOrderList.add(normalList.get(3 * vn + 0 - 3));
						normalOrderList.add(normalList.get(3 * vn + 1 - 3));
						normalOrderList.add(normalList.get(3 * vn + 2 - 3));
					}
				}
				
				break;
			}
		}
		
		// Put all the data into a VAO.
		vaoID = glGenVertexArrays();
		glBindVertexArray(vaoID);
		
		// Put in all the vertex data.
		float[] vertexData = new float[vertexOrderList.size()];
		vertices = vertexOrderList.size();
		
		for(int i = 0; i < vertexOrderList.size(); i++)
		{
			
			vertexData[i] = vertexOrderList.get(i);
		}
		
		FloatBuffer vBuffer = BufferUtils.createFloatBuffer(vertexData.length);
		vBuffer.put(vertexData);
		vBuffer.flip();
		
		int vboVID = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, vboVID);
		
		glBufferData(GL_ARRAY_BUFFER, vBuffer, GL_STATIC_DRAW);
		glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
		
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		
		// Clear all vertex lists
		vertexList.clear();
		vertexOrderList.clear();
		
		// Put in all the texture data.
		if(hasTexture)
		{
			
			float[] textureData = new float[textureOrderList.size()];
			
			for(int i = 0; i < textureOrderList.size(); i++)
			{
				
				textureData[i] = textureOrderList.get(i);
			}
			
			FloatBuffer tBuffer = BufferUtils.createFloatBuffer(textureData.length);
			tBuffer.put(textureData);
			tBuffer.flip();
			
			int vboTID = glGenBuffers();
			glBindBuffer(GL_ARRAY_BUFFER, vboTID);
			
			glBufferData(GL_ARRAY_BUFFER, tBuffer, GL_STATIC_DRAW);
			glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, 0);
			
			glBindBuffer(GL_ARRAY_BUFFER, 0);
			// Clear all texture lists
			textureList.clear();
			textureOrderList.clear();
		}
		
		// Put in all the normal data.
		if(hasNormals)
		{
			
			float[] normalData = new float[normalOrderList.size()];
			
			for(int i = 0; i < normalOrderList.size(); i++)
			{
				
				normalData[i] = normalOrderList.get(i);
			}
			
			FloatBuffer nBuffer = BufferUtils.createFloatBuffer(normalData.length);
			nBuffer.put(normalData);
			nBuffer.flip();
			
			int vboNID = glGenBuffers();
			glBindBuffer(GL_ARRAY_BUFFER, vboNID);
			
			glBufferData(GL_ARRAY_BUFFER, nBuffer, GL_STATIC_DRAW);
			glVertexAttribPointer(2, 3, GL_FLOAT, false, 0, 0);
			
			glBindBuffer(GL_ARRAY_BUFFER, 0);
			
			// Clear all normal lists
			normalList.clear();
			normalOrderList.clear();
		}
		
		glBindVertexArray(0);
	}

	public int getVaoID() {
		return vaoID;
	}
	
	public int getVertices() {
		return vertices;
	}
	
	public String getModelName() {
		return name;
	}
}