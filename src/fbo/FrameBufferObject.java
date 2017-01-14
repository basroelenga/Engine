package fbo;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL14.GL_DEPTH_COMPONENT32;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL32.glFramebufferTexture;

import java.nio.ByteBuffer;

import engine.Engine;
import engine.EngineObjectManager;
import graphics.Texture;
import math.Matrix4f;
import shaders.Shader;
import utils.DrawShapes;

public class FrameBufferObject {

	private int WIDTH;
	private int HEIGHT;
	
	private String bufferName;
	
	private int fboID;
	
	private int texID;
	
	private int depthTexID;
	private int depthBufferID;
	
	private Shader shader;
	
	private Matrix4f modelMatrix;
	
	private Texture tex;
	
	public FrameBufferObject(String name, Shader shader, int WIDTH, int HEIGHT)
	{
		
		this.bufferName = name;
		
		this.shader = shader;
		
		this.WIDTH = WIDTH;
		this.HEIGHT = HEIGHT;

		modelMatrix = new Matrix4f();
		
		modelMatrix.translate(0, Engine.getHeight(), 0);
		modelMatrix.scale(Engine.getWidth(), Engine.getHeight(), 0);
		modelMatrix.rotateQ(180, 0, 0, false);
		
		generateBuffer();
		checkBuffer();
	}
	
	private void generateBuffer()
	{
		// Create the frame buffer object
		fboID = glGenFramebuffers();
		
		glBindFramebuffer(GL_FRAMEBUFFER, fboID);
		glDrawBuffer(GL_COLOR_ATTACHMENT0);

		// Add the components to the frame buffer object
		// Add texture attachment
		texID = glGenTextures();
		
		glBindTexture(GL_TEXTURE_2D, texID);
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, WIDTH, HEIGHT, 0, GL_RGBA, GL_UNSIGNED_BYTE, (ByteBuffer) null);
		
		glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
		glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
		
		glFramebufferTexture(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, texID, 0);
		glBindTexture(GL_TEXTURE_2D, 0);
		
		// Add the depth texture attachment
		depthTexID = glGenTextures();
		
		glBindTexture(GL_TEXTURE_2D, depthTexID);
		glTexImage2D(GL_TEXTURE_2D, 0, GL_DEPTH_COMPONENT32, WIDTH, HEIGHT, 0, GL_DEPTH_COMPONENT, GL_FLOAT, (ByteBuffer) null);
		
		glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
		glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
		
		glFramebufferTexture(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, depthTexID, 0);
		glBindTexture(GL_TEXTURE_2D, 0);
		
		// Add the depth buffer attachment
		depthBufferID = glGenRenderbuffers();
		
		glBindRenderbuffer(GL_RENDERBUFFER, depthBufferID);
		
		glRenderbufferStorage(GL_RENDERBUFFER, GL_DEPTH_COMPONENT, WIDTH, HEIGHT);
		glFramebufferRenderbuffer(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, GL_RENDERBUFFER, depthBufferID);
		
		glBindRenderbuffer(GL_RENDERBUFFER, 0);
		
		unbind();
		
		tex = new Texture(bufferName, texID);
	}
	
	private void checkBuffer()
	{
		
		int framebuffer = glCheckFramebufferStatus(GL_FRAMEBUFFER); 
		
		switch (framebuffer)
		{
		
		    case GL_FRAMEBUFFER_COMPLETE:
		    	System.out.println("FrameBuffer generated: " + bufferName);
		        break;
		        
		    case GL_FRAMEBUFFER_INCOMPLETE_ATTACHMENT:
		        throw new RuntimeException( "FrameBuffer: " + bufferName
		                + ", has caused a GL_FRAMEBUFFER_INCOMPLETE_ATTACHMENT_EXT exception" );
		    case GL_FRAMEBUFFER_INCOMPLETE_MISSING_ATTACHMENT:
		        throw new RuntimeException( "FrameBuffer: " + bufferName
		                + ", has caused a GL_FRAMEBUFFER_INCOMPLETE_MISSING_ATTACHMENT_EXT exception" );
		    case GL_FRAMEBUFFER_INCOMPLETE_DRAW_BUFFER:
		        throw new RuntimeException( "FrameBuffer: " + bufferName
		                + ", has caused a GL_FRAMEBUFFER_INCOMPLETE_DRAW_BUFFER_EXT exception" );
		    case GL_FRAMEBUFFER_INCOMPLETE_READ_BUFFER:
		        throw new RuntimeException( "FrameBuffer: " + bufferName
		                + ", has caused a GL_FRAMEBUFFER_INCOMPLETE_READ_BUFFER_EXT exception" );
		    default:
		        throw new RuntimeException( "Unexpected reply from glCheckFramebufferStatus: " + bufferName + "(" + framebuffer + ")" );
		}
	}
	
	public void clearBuffer()
	{
		
		glBindFramebuffer(GL_FRAMEBUFFER, fboID);
		
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		
		glBindFramebuffer(GL_FRAMEBUFFER, 0);
	}
	
	public void bind()
	{
		
		glBindFramebuffer(GL_FRAMEBUFFER, fboID);
		glViewport(0, 0, WIDTH, HEIGHT);
	}
	
	public void unbind()
	{
		
		glBindFramebuffer(GL_FRAMEBUFFER, 0);
		glViewport(0, 0, Engine.getWidth(), Engine.getHeight());
	}
	
	public void render()
	{
				
		shader.uploadMatrix4f(modelMatrix, shader.getModelMatrixLoc());
		shader.uploadMatrix4f(new Matrix4f(), shader.getViewMatrixLoc());
		shader.uploadMatrix4f(Engine.orthoMatrix, shader.getProjectionMatrixLoc());
		
		//System.out.println(tex.getTexID());
		
		DrawShapes.drawQuad(shader, EngineObjectManager.getQuad(), tex, null);
	}
	
	public String getName()
	{
		return bufferName;
	}
	
	public int getFboID()
	{
		return fboID;
	}
}
