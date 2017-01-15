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
import static org.lwjgl.opengl.GL31.glDrawArraysInstanced;

import java.util.ArrayList;

import fbo.FrameBufferObject;
import graphics.Texture;
import models.Model;
import shaders.Shader;
import shapes.Point;
import shapes.Quad;
import shapes.Triangle;
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
	
	public static void drawPointInstanced(Shader shader, Point point, int points, FrameBufferObject fbo)
	{
		
		shader.bind();
		
		if(fbo != null) fbo.bind();
		
		glBindVertexArray(point.getVaoID());
		
		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(3);
		glEnableVertexAttribArray(4);
		glEnableVertexAttribArray(5);
		glEnableVertexAttribArray(6);
		
		glDrawArraysInstanced(GL_POINTS, 0, 1, points);
		
		glDisableVertexAttribArray(0);
		glDisableVertexAttribArray(3);
		glDisableVertexAttribArray(4);
		glDisableVertexAttribArray(5);
		glDisableVertexAttribArray(6);
		
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
	
	public static void drawTrianglesInstanced(Shader shader, Triangle triangle, int triangles)
	{
		
		shader.bind();
		
		glBindVertexArray(triangle.getVaoID());
		
		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(3);
		glEnableVertexAttribArray(4);
		glEnableVertexAttribArray(5);
		glEnableVertexAttribArray(6);
		
		glDrawArraysInstanced(GL_TRIANGLES, 0, 3, triangles);
		
		glDisableVertexAttribArray(0);
		glDisableVertexAttribArray(3);
		glDisableVertexAttribArray(4);
		glDisableVertexAttribArray(5);
		glDisableVertexAttribArray(6);
		
		glBindVertexArray(0);
		
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
	
	public static void drawQuad(Shader shader, Quad quad, ArrayList<Texture> texList, FrameBufferObject fbo){
		
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		
		shader.bind();
		
		if(fbo != null) fbo.bind();
		
		for(int i = 0; i < texList.size(); i++)
		{
			
			// This indicates which texture to use, 33984 corresponds to GL_TEXTURE0.
			glActiveTexture(33984 + i);
			glBindTexture(GL_TEXTURE_2D, texList.get(i).getTexID());
		}
		
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
	
	public static void drawQuadInstanced(Shader shader, Quad quad, int quads, FrameBufferObject fbo)
	{
		
		shader.bind();
		
		if(fbo != null) fbo.bind();
		
		glBindVertexArray(quad.getVaoID());
		
		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(3);
		glEnableVertexAttribArray(4);
		glEnableVertexAttribArray(5);
		glEnableVertexAttribArray(6);
		
		glDrawArraysInstanced(GL_TRIANGLES, 0, 6, quads);
		
		glDisableVertexAttribArray(0);
		glDisableVertexAttribArray(3);
		glDisableVertexAttribArray(4);
		glDisableVertexAttribArray(5);
		glDisableVertexAttribArray(6);
		
		glBindVertexArray(0);
		
		if(fbo != null) fbo.unbind();
		
		shader.unbind();
	}
	
	public static void drawModel(Shader shader, Model model, FrameBufferObject fbo)
	{
		
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		
		shader.bind();
		
		if(fbo != null) fbo.bind();
		
		glBindVertexArray(model.getVaoID());
		
		glEnableVertexAttribArray(0);
		
		glDrawArrays(GL_TRIANGLES, 0, model.getVertices());
		
		glDisableVertexAttribArray(0);
		
		glBindVertexArray(0);
		
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