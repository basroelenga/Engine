package shapes;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL33.*;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;

public class Point {

	private int vaoID;
	
	private int vboVID;
	private int vboMID;
	
	/**
	 * Create a point.
	 */
	public Point()
	{
		
		float[] vertexCoords = {
				
				0.0f, 0.0f, 0.0f
			};
		
		FloatBuffer vertexData = BufferUtils.createFloatBuffer(vertexCoords.length);
		vertexData.put(vertexCoords);
		vertexData.flip();
		
		vaoID = glGenVertexArrays();
		glBindVertexArray(vaoID);
		
		int vboVID = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, vboVID);
		glBufferData(GL_ARRAY_BUFFER, vertexData, GL_STATIC_DRAW);
		glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		
		glBindVertexArray(0);
	}
	
	/**
	 * Create a point which can be used for instanced rendering.
	 * @param numberOfPoints Number of maximum points.
	 */
	public Point(int numberOfPoints)
	{
				
		float[] vertexCoords = {
						
			0.0f, 0.0f, 0.0f
		};
		
		FloatBuffer vertexData = BufferUtils.createFloatBuffer(vertexCoords.length);
		vertexData.put(vertexCoords);
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
		glBufferData(GL_ARRAY_BUFFER, numberOfPoints * dataLength * 4, GL_STREAM_DRAW);
		
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
	 * Get the vaoID of a point.
	 * @return vaoID.
	 */
	public int getVaoID()
	{
		return vaoID;
	}
}