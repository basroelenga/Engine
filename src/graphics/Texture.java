package graphics;

import java.io.IOException;
import java.nio.ByteBuffer;

import de.matthiasmann.twl.utils.PNGDecoder;
import de.matthiasmann.twl.utils.PNGDecoder.Format;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL14.*;
import static org.lwjgl.opengl.GL30.*;

import utils.FileIO;

public class Texture {

	private int texID;
	private String texName;
	
	/**
	 * Constructs an OpenGL texture from a PNG file.
	 * @param path Texture to be loaded.
	 */
	public Texture(String path)
	{

		texName = path;
		
		try {
			
			PNGDecoder decoder = new PNGDecoder(FileIO.loadTexture(path));
			
			ByteBuffer buffer = ByteBuffer.allocateDirect(4 * decoder.getHeight() * decoder.getWidth());
			
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
	
	public Texture(String name, int textureID)
	{
		
		texID = textureID;
		texName = name;
	}
	
	public void destroy()
	{
		
		glDeleteTextures(texID);
	}
	
	/**
	 * Returns the OpenGL texture ID.
	 * @return The texture ID.
	 */
	public int getTexID()
	{
		return texID;
	}
	
	public String getTexName()
	{
		
		return texName;
	}
}