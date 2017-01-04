package utils;

import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_POINTS;
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

import fbo.FrameBufferObject;
import graphics.Texture;
import shaders.Shader;
import shapes.Point;
import shapes.Quad;
import shapes.UVSphere;

public class DrawShapes {

	private DrawShapes(){}
	
	public static void drawPoint(Shader shader, Point point, FrameBufferObject fbo)
	{
		
		shader.bind();
		
		if(fbo != null) fbo.bind();
		
		glBindVertexArray(point.getVaoID());
		
		glEnableVertexAttribArray(0);
		
		glDrawArrays(GL_POINTS, 0, 1);
		
		glDisableVertexAttribArray(0);
		
		glBindVertexArray(0);
		
		if(fbo != null) fbo.unbind();
		
		shader.unbind();
	}
	
	public static void drawTriangle(Shader shader, int vaoID, FrameBufferObject fbo){
		
		shader.bind();
		
		if(fbo != null) fbo.bind();
		
		glBindVertexArray(vaoID);
		
		glEnableVertexAttribArray(0);
		
		glDrawArrays(GL_TRIANGLES, 0, 3);
		
		glDisableVertexAttribArray(0);
		
		glBindVertexArray(0);
		
		if(fbo != null) fbo.unbind();

		shader.unbind();
	}
	
	public static void drawMultipleTriangles(Shader shader, int vaoID, int amountOfTriangles, FrameBufferObject fbo){
		
		shader.bind();
		
		if(fbo != null) fbo.bind();
		
		glBindVertexArray(vaoID);
		
		glEnableVertexAttribArray(0);
		
		glDrawArrays(GL_TRIANGLES, 0, amountOfTriangles * 3);
		
		glDisableVertexAttribArray(0);
		
		glBindVertexArray(0);
		
		if(fbo != null) fbo.unbind();

		shader.unbind();
	}
	
	public static void drawQuad(Shader shader, Quad quad, FrameBufferObject fbo){
		
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		
		shader.bind();
		
		if(fbo != null) fbo.bind();
		
		glBindVertexArray(quad.getVaoID());
		
		glEnableVertexAttribArray(0);
		
		glDrawArrays(GL_TRIANGLES, 0, 6);
		
		glDisableVertexAttribArray(0);
		
		glBindVertexArray(0);
		
		if(fbo != null) fbo.unbind();
		
		shader.unbind();
		
		glDisable(GL_BLEND);
	}
	
	public static void drawQuad(Shader shader, Quad quad, Texture tex, FrameBufferObject fbo){
		
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		
		shader.bind();
		
		if(fbo != null) fbo.bind();
		
		glActiveTexture(GL_TEXTURE0);
		glBindTexture(GL_TEXTURE_2D, tex.getTexID());
		
		glBindVertexArray(quad.getVaoID());
		
		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(1);
		
		glDrawArrays(GL_TRIANGLES, 0, 6);
		
		glDisableVertexAttribArray(0);
		glDisableVertexAttribArray(1);
		
		glBindVertexArray(0);
		glBindTexture(GL_TEXTURE_2D, 0);
		
		if(fbo != null) fbo.unbind();
		
		shader.unbind();
		
		glDisable(GL_BLEND);
	}

	public static void drawUVSphere(Shader shader, UVSphere sphere, FrameBufferObject fbo)
	{
		
		shader.bind();
		
		if(fbo != null) fbo.bind();
		
		glBindVertexArray(sphere.getVaoID());
		
		glEnableVertexAttribArray(0);
		
		glDrawArrays(GL_TRIANGLES, 0, 3 * (int) sphere.getAmountOfTriangles());
		
		glDisableVertexAttribArray(0);
		
		glBindVertexArray(0);
		
		if(fbo != null) fbo.unbind();
		
		shader.unbind();
	}
	
	public static void drawUVSphere(Shader shader, UVSphere sphere, FrameBufferObject fbo, Texture tex)
	{
		
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		
		if(fbo != null) fbo.bind();
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
		if(fbo != null) fbo.unbind();
		
		glDisable(GL_BLEND);
	}
}