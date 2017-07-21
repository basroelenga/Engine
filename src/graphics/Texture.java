package graphics;

import java.io.IOException;
import java.nio.ByteBuffer;

import de.matthiasmann.twl.utils.PNGDecoder;
import de.matthiasmann.twl.utils.PNGDecoder.Format;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE;
import static org.lwjgl.opengl.GL12.GL_TEXTURE_WRAP_R;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL14.*;
import static org.lwjgl.opengl.GL30.*;

import utils.FileIO;

public class Texture {

	private int texID;
	private String texName;
	
	private int height;
	private int width;
	
	private ByteBuffer buffer;
	
	/**
	 * Constructs an OpenGL texture from a PNG file.
	 * @param path Texture to be loaded.
	 */
	public Texture(String path)
	{

		texName = path;
		
		try {
			
			PNGDecoder decoder = new PNGDecoder(FileIO.loadTexture(path));
			
			height = decoder.getHeight();
			width = decoder.getWidth();
			
			buffer = ByteBuffer.allocateDirect(4 * decoder.getHeight() * decoder.getWidth());
			
			decoder.decode(buffer, 4 * decoder.getWidth(), Format.RGBA);
			buffer.flip();
			
			texID = glGenTextures();
			glBindTexture(GL_TEXTURE_2D, texID);
			
			glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA16, decoder.getWidth(), decoder.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);
			
			glGenerateMipmap(GL_TEXTURE_2D);
			
			glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
			glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
		
			glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
			glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
			
			glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_LOD_BIAS, -0.4f);
			
			glBindTexture(GL_TEXTURE_2D, 0);

		} catch (IOException e) {
			
			System.err.println("Could not decode buffer: " + path);
			e.printStackTrace();
		}
	}
	
	/**
	 * Construct a texture from a OpenGL texture ID
	 * @param name Name of the texture
	 * @param textureID The OpenGL texture id
	 */
	public Texture(String name, int textureID)
	{
		
		texID = textureID;
		texName = name;
	}
	
	/**
	 * Create an OpenGL texture.
	 * @param name The name of the texture.
	 * @param type The type of the texture.
	 * @param dimensions The dimensions of the texture.
	 */
	public Texture(String name, String type, int width, int height)
	{
		
		this.texName = name;
		
		this.width = width;
		this.height = height;
		
		switch(type)
		{
		
		case "RGBA":
			
			createRGBATexture();
			break;
			
		case "DEPTH":
			
			createDepthTexture();
			break;
			
		case "CUBE_RGBA":
		
			createCubeMapTexture();
			break;
			
		case "CUBE_DEPTH":
			
			createCubeMapDepthTexture();
			break;
			
		default:
			
			System.err.println("Texture type not supported: " + type);
			break;
		}
	}
	
	/**
	 * Create an OpenGL texture with a RGBA format.
	 */
	private void createRGBATexture()
	{
		
		texID = glGenTextures();
		glBindTexture(GL_TEXTURE_2D, texID);
		
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA16, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, (ByteBuffer) null);
		
		glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
		glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);

		glBindTexture(GL_TEXTURE_2D, 0);
	}
	
	/**
	 * Create an OpenGL texture with a depth format.
	 */
	private void createDepthTexture()
	{
		
		texID = glGenTextures();
		glBindTexture(GL_TEXTURE_2D, texID);
		
        glTexImage2D(GL_TEXTURE_2D, 0, GL_DEPTH_COMPONENT32, width, height, 0, GL_DEPTH_COMPONENT, GL_FLOAT, (ByteBuffer) null);
        
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
        
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_COMPARE_FUNC, GL_LEQUAL);
        glTexParameteri(GL_TEXTURE_2D, GL_DEPTH_TEXTURE_MODE, GL_LUMINANCE);  
        
        glBindTexture(GL_TEXTURE_2D, 0);
	}
	
	/**
	 * Create an OpenGL cube map texture with a RGBA format.
	 */
	private void createCubeMapTexture()
	{
		
		texID = glGenTextures();
		glBindTexture(GL_TEXTURE_CUBE_MAP, texID);
		
		for(int i = 0; i < 6; i++)
		{
			
			glTexImage2D(GL_TEXTURE_CUBE_MAP_POSITIVE_X + i, 0, GL_RGBA16, width, height, 0, GL_RGBA, GL_FLOAT, (ByteBuffer) null);
		}
		
		glTexParameterf(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
	    glTexParameterf(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
	    
	    glTexParameterf(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
	    glTexParameterf(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
	    glTexParameterf(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_WRAP_R, GL_CLAMP_TO_EDGE);
	    
	    glBindTexture(GL_TEXTURE_CUBE_MAP, 0);
	}
	
	/**
	 * Create an OpenGl cube map texture with a depth format.
	 */
	private void createCubeMapDepthTexture()
	{
		
		texID = glGenTextures();
		glBindTexture(GL_TEXTURE_CUBE_MAP, texID);
		
		for(int i = 0; i < 6; i++)
		{
			
			glTexImage2D(GL_TEXTURE_CUBE_MAP_POSITIVE_X + i, 0, GL_DEPTH_COMPONENT32, width, height, 0, GL_DEPTH_COMPONENT, GL_FLOAT, (ByteBuffer) null);
		}
		
		glTexParameterf(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
	    glTexParameterf(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
	    
	    glTexParameterf(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
	    glTexParameterf(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
	    glTexParameterf(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_WRAP_R, GL_CLAMP_TO_EDGE);
	    
	    glBindTexture(GL_TEXTURE_CUBE_MAP, 0);
	}
	
	/**
	 * Destroy the current texture object
	 */
	public void destroy()
	{
		glDeleteTextures(texID);
	}
	
	/**
	 * Returns the OpenGL texture ID.
	 * @return The texture ID.
	 */
	public int getTextureID()
	{
		return texID;
	}
	
	/**
	 * Returns the name of the texture object
	 * @return The name
	 */
	public String getTextureName()
	{
		return texName;
	}
	
	/**
	 * Returns the width in pixels of the texture object
	 * @return Width in pixels
	 */
	public int getWidth()
	{
		return width;
	}
	
	/**
	 * Returns the height in pixels of the texture object
	 * @return Height in pixels
	 */
	public int getHeight()
	{
		return height;
	}
	
	/**
	 * Returns the image as a buffer
	 * @return Image bytebuffer
	 */
	public ByteBuffer getImage()
	{
		return buffer;
	}
}