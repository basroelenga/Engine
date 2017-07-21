package graphics;

import java.util.ArrayList;

import utils.FileIO;

public class TextureManager {

	private static ArrayList<Texture> textureList = new ArrayList<Texture>();
	
	/**
	 * Load all the available textures.
	 */
	public static void loadTextures()
	{
		
		// Check all the files in the texture map
		ArrayList<String> fileNames = FileIO.getFilesInFolder("/textures");
		
		for(String names : fileNames)
		{

			textureList.add(new Texture(names));
		}
	}
	
	/**
	 * Get a texture.
	 * @param name The name of the texture.
	 * @return The texture.
	 */
	public static Texture getTexture(String name)
	{
		
		for(Texture tex : textureList)
		{

			if(tex.getTextureName().equals(name)) return tex;
		}
		
		throw new RuntimeException("Texture does not exist");
	}
}
