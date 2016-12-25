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

import org.lwjgl.BufferUtils;

public class Point {

	private int vaoID;
	
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
	 * Get the vaoID of a point.
	 * @return vaoID.
	 */
	public int getVaoID()
	{
		return vaoID;
	}
}