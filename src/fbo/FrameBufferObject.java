package fbo;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_TEXTURE_CUBE_MAP_POSITIVE_X;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL32.glFramebufferTexture;

import engine.Engine;
import graphics.Texture;

public class FrameBufferObject {

	private int WIDTH;
	private int HEIGHT;
	
	private String bufferName;
	
	private int fboID;
	private int depthBufferID;
	
	private Texture texture;
	
	/**
	 * Initialize a default frame buffer object.
	 * @param name Name of the frame buffer.
	 * @param shader Shader which to draw to.
	 * @param WIDTH Width of the frame buffer.
	 * @param HEIGHT Height of the frame buffer.
	 */
	public FrameBufferObject(String name, String type, int WIDTH, int HEIGHT)
	{
		
		this.bufferName = name;
		
		this.WIDTH = WIDTH;
		this.HEIGHT = HEIGHT;
		
		// Create the frame buffer object
		fboID = glGenFramebuffers();
		
		switch(type)
		{
		
		case "RGBA":
			
			generateDefaultBuffer();
			break;
			
		case "DEPTH":
			
			generateShadowBuffer();
			break;
			
		case "CUBE_RGBA":
			
			generateCubeMapRGBABuffer();
			break;
			
		case "CUBE_DEPTH":	
			
			generateCubeMapDepthBuffer();
			break;
		
		default:
			
			System.err.println("Buffer type does not exist");
			break;
		}
		
		checkBuffer();
	}
	
	/**
	 * Create a default frame buffer which contains texture and depth information.
	 * Can be used for rendering to texture.
	 */
	private void generateDefaultBuffer()
	{

		bind();
		glDrawBuffer(GL_COLOR_ATTACHMENT0);

		// Add the components to the frame buffer object, first the texture component
		texture = new Texture(bufferName, "RGBA", WIDTH, HEIGHT);
		glFramebufferTexture(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, texture.getTextureID(), 0);

		// Add the depth buffer attachment
		depthBufferID = glGenRenderbuffers();
		
		glBindRenderbuffer(GL_RENDERBUFFER, depthBufferID);
		
		glRenderbufferStorage(GL_RENDERBUFFER, GL_DEPTH_COMPONENT, WIDTH, HEIGHT);
		glFramebufferRenderbuffer(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, GL_RENDERBUFFER, depthBufferID);
		
		glBindRenderbuffer(GL_RENDERBUFFER, 0);
		
		unbind();
	}
	
	/**
	 * Generate a shadow buffer, this essentially generates a depth map.
	 */
	private void generateShadowBuffer()
	{
		
		bind();
		
		// The draw and read buffers are none because only the depth buffer is rendered.
		glDrawBuffer(GL_NONE);
		glReadBuffer(GL_NONE);
		
		// Create the depth texture
		texture = new Texture(bufferName, "DEPTH", WIDTH, HEIGHT);
        glFramebufferTexture(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, texture.getTextureID(), 0);
        
        unbind();
	}
	
	/**
	 * Generate a cube map buffer of the type RGBA, thus it also contains a depth buffer.
	 */
	private void generateCubeMapRGBABuffer()
	{
		
		bind();
		
        // Create the cube map texture
		texture = new Texture(bufferName, "CUBE_RGBA", WIDTH, HEIGHT);
		
		// Attach the cube map texture to all faces, maybe only attach 1?
		for(int i = 0; i < 6; i++)
		{
			
			glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_CUBE_MAP_POSITIVE_X + i, texture.getTextureID(), 0);
		}		
		
		// Also attach a depth buffer
		depthBufferID = glGenRenderbuffers();
		glBindRenderbuffer(GL_RENDERBUFFER, depthBufferID);
		
		glRenderbufferStorage(GL_RENDERBUFFER, GL_DEPTH_COMPONENT, WIDTH, HEIGHT);
		glFramebufferRenderbuffer(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, GL_RENDERBUFFER, depthBufferID);
		
		glBindRenderbuffer(GL_RENDERBUFFER, 0);
		unbind();
	}
	
	private void generateCubeMapDepthBuffer()
	{
		
		bind();
		
		// Create the cube map depth texture
		texture = new Texture(bufferName, "CUBE_DEPTH", WIDTH, HEIGHT);
		
		// Attach the cube map texture to all faces, maybe only attach 1?
		for(int i = 0; i < 6; i++)
		{
			
			glFramebufferTexture2D(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, GL_TEXTURE_CUBE_MAP_POSITIVE_X + i, texture.getTextureID(), 0);
		}
		
		unbind();
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
	
	public void deleteBuffer()
	{
		
		glDeleteRenderbuffers(depthBufferID);
		glDeleteFramebuffers(fboID);
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
	
	public void bindCube(int face)
	{
		
		glBindFramebuffer(GL_DRAW_FRAMEBUFFER, fboID);
		glViewport(0, 0, WIDTH, HEIGHT);
		
		//glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_CUBE_MAP_POSITIVE_X + face, depthTexID, 0);
	}
	
	public void unbindCube()
	{
		
		glBindFramebuffer(GL_DRAW_FRAMEBUFFER, 0);
		glViewport(0, 0, Engine.getWidth(), Engine.getHeight());
	}
	
	public String getName()
	{
		return bufferName;
	}
	
	public int getFboID()
	{
		return fboID;
	}
	
	public int getTextureID()
	{
		return texture.getTextureID();
	}
}
