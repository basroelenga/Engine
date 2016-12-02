package utils;

import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

import graphics.Texture;
import shaders.Shader;
import shapes.UVSphere;

public class DrawShapes {

	private DrawShapes(){}
	
	public static void drawTriangle(Shader shader, int vaoID){
		
		shader.bind();
		
		glBindVertexArray(vaoID);
		
		glEnableVertexAttribArray(0);
		
		glDrawArrays(GL_TRIANGLES, 0, 3);
		
		glDisableVertexAttribArray(0);
		
		glBindVertexArray(0);
		
		shader.unbind();
	}
	
	public static void drawQuad(Shader shader, int vaoID, int amountOfTriangles){
			
		shader.bind();
		
		glBindVertexArray(vaoID);
		
		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(1);
		
		glDrawArrays(GL_TRIANGLES, 0, amountOfTriangles * 3);
		
		glDisableVertexAttribArray(0);
		glDisableVertexAttribArray(1);
		
		glBindVertexArray(0);
		
		shader.unbind();
	}
	
	public static void drawQuad(Shader shader, Texture tex, int vaoID){
		
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		
		shader.bind();
		
		glActiveTexture(GL_TEXTURE0);
		glBindTexture(GL_TEXTURE_2D, tex.getTexID());
		
		glBindVertexArray(vaoID);
		
		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(1);
		
		glDrawArrays(GL_TRIANGLES, 0, 6);
		
		glDisableVertexAttribArray(0);
		glDisableVertexAttribArray(1);
		
		glBindVertexArray(0);
		glBindTexture(GL_TEXTURE_2D, 0);
		
		shader.unbind();
		
		glDisable(GL_BLEND);
	}

	public static void drawUVSphere(Shader shader, UVSphere sphere)
	{
		
		shader.bind();
		
		glBindVertexArray(sphere.getVaoID());
		
		glEnableVertexAttribArray(0);
		
		glDrawArrays(GL_TRIANGLES, 0, 3 * (int) sphere.getAmountOfTriangles());
		
		glDisableVertexAttribArray(0);
		
		glBindVertexArray(0);
		
		shader.unbind();
	}
	
	public static void drawUVSphere(Shader shader, UVSphere sphere, Texture tex)
	{
		
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		
		shader.bind();
		
		glActiveTexture(GL_TEXTURE0);
		glBindTexture(GL_TEXTURE_2D, tex.getTexID());
		
		glBindVertexArray(sphere.getVaoID());
		
		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(1);
		glEnableVertexAttribArray(2);
		
		glDrawArrays(GL_TRIANGLES, 0, 3 * (int) sphere.getAmountOfTriangles());
		
		glDisableVertexAttribArray(0);
		glDisableVertexAttribArray(1);
		glDisableVertexAttribArray(2);
		
		glBindVertexArray(0);
		glBindTexture(GL_TEXTURE_2D, 0);
		
		shader.unbind();
		
		glDisable(GL_BLEND);
	}
}