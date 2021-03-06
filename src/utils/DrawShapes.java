package utils;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL14.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL31.glDrawArraysInstanced;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import engine.EngineObjects;
import fbo.FrameBufferObject;
import graphics.Texture;
import shaders.Shader;

public class DrawShapes {

	private DrawShapes(){}
	
	public static void drawPoint(Shader shader, FrameBufferObject fbo, int vaoID)
	{
		
		shader.bind();
		
		if(fbo != null) fbo.bind();
		
		glBindVertexArray(vaoID);
		
		glEnableVertexAttribArray(0);
		
		glDrawArrays(GL_POINTS, 0, 1);
		
		glDisableVertexAttribArray(0);
		
		glBindVertexArray(0);
		
		if(fbo != null) fbo.unbind();
		
		shader.unbind();
	}
	
	public static void drawPointInstanced(Shader shader, FrameBufferObject fbo, int vaoID, int points)
	{
		
		shader.bind();
		
		if(fbo != null) fbo.bind();
		
		glBindVertexArray(vaoID);
		
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
	
	public static void drawTriangle(Shader shader, FrameBufferObject fbo, int vaoID){
		
		shader.bind();
		
		if(fbo != null) fbo.bind();
		
		glBindVertexArray(vaoID);
		
		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(2);
		
		glDrawArrays(GL_TRIANGLES, 0, 3);
		
		glDisableVertexAttribArray(0);
		glDisableVertexAttribArray(2);
		
		glBindVertexArray(0);
		
		if(fbo != null) fbo.unbind();

		shader.unbind();
	}
	
	public static void drawMultipleTriangles(Shader shader, FrameBufferObject fbo, int vaoID, int amountOfTriangles){
		
		shader.bind();
		
		if(fbo != null) fbo.bind();
		
		glBindVertexArray(vaoID);
		
		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(2);
		
		glDrawArrays(GL_TRIANGLES, 0, amountOfTriangles * 3);
		
		glDisableVertexAttribArray(0);
		glDisableVertexAttribArray(2);
		
		glBindVertexArray(0);
		
		if(fbo != null) fbo.unbind();

		shader.unbind();
	}
	
	public static void drawTrianglesInstanced(Shader shader, int vaoID, int triangles)
	{
		
		shader.bind();
		
		glBindVertexArray(vaoID);
		
		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(2);
		glEnableVertexAttribArray(3);
		glEnableVertexAttribArray(4);
		glEnableVertexAttribArray(5);
		glEnableVertexAttribArray(6);
		
		glDrawArraysInstanced(GL_TRIANGLES, 0, 3, triangles);
		
		glDisableVertexAttribArray(0);
		glDisableVertexAttribArray(2);
		glDisableVertexAttribArray(3);
		glDisableVertexAttribArray(4);
		glDisableVertexAttribArray(5);
		glDisableVertexAttribArray(6);
		
		glBindVertexArray(0);
		
		shader.unbind();
	}
	
	public static void drawQuad(Shader shader, FrameBufferObject fbo, int vaoID){
		
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		
		shader.bind();
		if(fbo != null) fbo.bind();
		
		glBindVertexArray(vaoID);
		
		glEnableVertexAttribArray(0);
				
		glDrawArrays(GL_TRIANGLES, 0, 6);
		
		glDisableVertexAttribArray(0);
		
		glBindVertexArray(0);
		
		if(fbo != null) fbo.unbind();
		shader.unbind();
		
		glDisable(GL_BLEND);
	}
	
	public static void drawQuadToScreen(Shader shader, Texture tex, FrameBufferObject fbo, int vaoID){
		
		glEnable(GL_BLEND);
		glBlendEquation(GL_FUNC_ADD);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		
		shader.bind();
		
		if(fbo != null) fbo.bind();
		
		glActiveTexture(GL_TEXTURE0);
		glBindTexture(GL_TEXTURE_2D, tex.getTextureID());
		
		glBindVertexArray(vaoID);
		
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
	
	public static void drawQuad(Shader shader, Texture tex, FrameBufferObject fbo, int vaoID){
		
		glEnable(GL_BLEND);
		glBlendFuncSeparate(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA, GL_ONE, GL_ONE);
		
		shader.bind();
		
		if(fbo != null) fbo.bind();
		
		glActiveTexture(GL_TEXTURE0);
		glBindTexture(GL_TEXTURE_2D, tex.getTextureID());
		
		glBindVertexArray(vaoID);
		
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
	
	public static void drawQuadNormal(Shader shader, Texture tex, FrameBufferObject fbo, int vaoID)
	{
		
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		
		shader.bind();
		
		if(fbo != null) fbo.bind();
		
		glActiveTexture(GL_TEXTURE0);
		glBindTexture(GL_TEXTURE_2D, tex.getTextureID());
		
		glBindVertexArray(vaoID);
		
		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(1);
		glEnableVertexAttribArray(2);
		
		glDrawArrays(GL_TRIANGLES, 0, 6);
		
		glDisableVertexAttribArray(0);
		glDisableVertexAttribArray(1);
		glDisableVertexAttribArray(2);
		
		glBindVertexArray(0);
		glBindTexture(GL_TEXTURE_2D, 0);
		
		if(fbo != null) fbo.unbind();
		
		shader.unbind();
		
		glDisable(GL_BLEND);
	}
	
	public static void drawQuadNormal(Shader shader, ArrayList<Texture> texList, FrameBufferObject fbo, int vaoID)
	{
		
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		
		shader.bind();
		
		if(fbo != null) fbo.bind();
		
		for(int i = 0; i < texList.size(); i++)
		{
			
			// This indicates which texture to use, 33984 corresponds to GL_TEXTURE0.
			glActiveTexture(GL_TEXTURE0 + i);
			glBindTexture(GL_TEXTURE_2D, texList.get(i).getTextureID());
			
			glUniform1i(shader.getTextureLocList().get(i), i);
		}
		
		glBindVertexArray(vaoID);
		
		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(1);
		glEnableVertexAttribArray(2);
		
		glDrawArrays(GL_TRIANGLES, 0, 6);
		
		glDisableVertexAttribArray(0);
		glDisableVertexAttribArray(1);
		glDisableVertexAttribArray(2);
		
		glBindVertexArray(0);
		
		for(int i = 0; i <texList.size(); i++)
		{
			glBindTexture(GL_TEXTURE_2D, 0);
		}
		
		if(fbo != null) fbo.unbind();
		
		shader.unbind();
		
		glDisable(GL_BLEND);
	}
	
	public static void drawQuad(Shader shader, ArrayList<Texture> texList, FrameBufferObject fbo, int vaoID){
		
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		
		shader.bind();
		
		if(fbo != null) fbo.bind();
		
		for(int i = 0; i < texList.size(); i++)
		{
			
			// This indicates which texture to use, 33984 corresponds to GL_TEXTURE0.
			glActiveTexture(GL_TEXTURE0 + i);
			glBindTexture(GL_TEXTURE_2D, texList.get(i).getTextureID());
			
			glUniform1i(shader.getTextureLocList().get(i), i);
		}
		
		glBindVertexArray(vaoID);
		
		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(1);
		glEnableVertexAttribArray(2);
		
		glDrawArrays(GL_TRIANGLES, 0, 6);
		
		glDisableVertexAttribArray(0);
		glDisableVertexAttribArray(1);
		glDisableVertexAttribArray(2);
		
		glBindVertexArray(0);
		
		for(int i = 0; i <texList.size(); i++)
		{
			glBindTexture(GL_TEXTURE_2D, 0);
		}
		
		if(fbo != null) fbo.unbind();
		
		shader.unbind();
		
		glDisable(GL_BLEND);
	}
	
	public static void drawQuadInstanced(Shader shader, FrameBufferObject fbo, int vaoID, int quads)
	{
		
		shader.bind();
		if(fbo != null) fbo.bind();
		
		glBindVertexArray(vaoID);
		
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
	
	public static void drawModel(Shader shader, FrameBufferObject fbo, int vaoID, int amountOfTriangles)
	{
		
		glEnable(GL_BLEND);
		glBlendFuncSeparate(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA, GL_ONE, GL_ONE);
		
		shader.bind();
		if(fbo != null) fbo.bind();
		
		glBindVertexArray(vaoID);
		
		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(2);
		
		glDrawArrays(GL_TRIANGLES, 0, amountOfTriangles);
		
		glDisableVertexAttribArray(0);
		glDisableVertexAttribArray(2);
		
		glBindVertexArray(0);
		
		if(fbo != null) fbo.unbind();
		shader.unbind();
		
		glDisable(GL_BLEND);
	}
	
	public static void drawSkyBox(Shader shader, FrameBufferObject fbo, int vaoID, int amountOfTriangles)
	{
		
		glEnable(GL_BLEND);
		glBlendFuncSeparate(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA, GL_ONE, GL_ONE);
		
		shader.bind();
		if(fbo != null) fbo.bind();
		
		glBindVertexArray(vaoID);
		
		glEnableVertexAttribArray(0);
		
		glDrawArrays(GL_TRIANGLES, 0, amountOfTriangles);
		
		glDisableVertexAttribArray(0);
		
		glBindVertexArray(0);
		
		if(fbo != null) fbo.unbind();
		shader.unbind();
		
		glDisable(GL_BLEND);
	}
	
	public static void drawModel(Shader shader, FrameBufferObject fbo, Texture tex, int vaoID, int amountOfTriangles)
	{
		
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		
		if(fbo != null) fbo.bind();
		shader.bind();
		
		glActiveTexture(GL_TEXTURE0);
		glBindTexture(GL_TEXTURE_2D, tex.getTextureID());
		
		glBindVertexArray(vaoID);
		
		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(1);
		glEnableVertexAttribArray(2);
		
		glDrawArrays(GL_TRIANGLES, 0, amountOfTriangles);
		
		glDisableVertexAttribArray(0);
		glDisableVertexAttribArray(1);
		glDisableVertexAttribArray(2);
		
		glBindVertexArray(0);
		glBindTexture(GL_TEXTURE_2D, 0);
		
		shader.unbind();
		if(fbo != null) fbo.unbind();
		
		glDisable(GL_BLEND);
	}
	
	public static void drawModel(EngineObjects obj, Shader shader, FrameBufferObject fbo, LinkedHashMap<String, Texture> textureMap, int vaoID, int amountOfTriangles)
	{
		
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		
		if(fbo != null) fbo.bind();
		shader.bind();
		
		int j = 0;
		
		for(Map.Entry<String, Texture> entry : textureMap.entrySet())
		{
			
			if(entry.getValue() == null) {j+= 1; continue;}
			
			// This indicates which texture to use
			glActiveTexture(GL_TEXTURE0 + j);
			glBindTexture(GL_TEXTURE_2D, entry.getValue().getTextureID());
			
			shader.setLocation(entry.getKey(), j);
			j += 1;
		}
		
		glBindVertexArray(vaoID);
		
		glEnableVertexAttribArray(0);
		if(textureMap.get("mTexture") != null && obj.getModel().hasTextureCoordinates()) glEnableVertexAttribArray(1);
		glEnableVertexAttribArray(2);
		
		glDrawArrays(GL_TRIANGLES, 0, amountOfTriangles);
		
		glDisableVertexAttribArray(0);
		if(textureMap.get("mTexture") != null && obj.getModel().hasTextureCoordinates()) glDisableVertexAttribArray(1);
		glDisableVertexAttribArray(2);
		
		glBindVertexArray(0);
		
		for(Map.Entry<String, Texture> entry : textureMap.entrySet())
		{
			
			if(entry.getValue() == null) continue;
			glBindTexture(GL_TEXTURE_2D, 0);
		}
		
		shader.unbind();
		if(fbo != null) fbo.unbind();
		
		glDisable(GL_BLEND);
	}

	public static void drawUVSphere(Shader shader, FrameBufferObject fbo, int vaoID, int amountOfTriangles)
	{
		
		shader.bind();
		
		if(fbo != null) fbo.bind();
		
		glBindVertexArray(vaoID);
		
		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(2);
		
		glDrawArrays(GL_TRIANGLES, 0, 3 * amountOfTriangles);
		
		glDisableVertexAttribArray(0);
		glDisableVertexAttribArray(2);
		
		glBindVertexArray(0);
		
		if(fbo != null) fbo.unbind();
		
		shader.unbind();
	}
	
	public static void drawUVSphere(Shader shader, FrameBufferObject fbo, Texture tex, int vaoID, int amountOfTriangles)
	{
		
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		
		if(fbo != null) fbo.bind();
		shader.bind();
		
		glActiveTexture(GL_TEXTURE0);
		glBindTexture(GL_TEXTURE_2D, tex.getTextureID());
		
		glBindVertexArray(vaoID);
		
		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(1);
		glEnableVertexAttribArray(2);
		
		glDrawArrays(GL_TRIANGLES, 0, 3 * amountOfTriangles);
		
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