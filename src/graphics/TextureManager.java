package graphics;

import java.util.ArrayList;

import utils.FileIO;

public class TextureManager {

	private static ArrayList<Texture> textureList = new ArrayList<Texture>();
	
	public TextureManager()
	{
	
		loadTexture();
	}
	
	private void loadTexture()
	{
		
		// Check all the files in the texture map
		ArrayList<String> fileNames = FileIO.getFilesInFolder("/textures");
		
		for(String names : fileNames)
		{

			textureList.add(new Texture(names));
		}
	}
	
	public static Texture getTexture(String name)
	{
		
		for(Texture tex : textureList)
		{

			if(tex.getTexName().equals(name)) return tex;
		}
		
		return null;
	}
}
