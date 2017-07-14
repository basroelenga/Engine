package text;

import java.util.ArrayList;

import graphics.Texture;
import utils.ArrayObject;
import utils.FileIO;

public class TextManager {

	public static float xMax;
	
	private static Texture fontTex;
	private static ArrayList<ECharacter> charList = new ArrayList<ECharacter>();
	
	/**
	 * Construct the text manager, this class manages text displayed on the screen
	 * @param useConf Type of text to use (Should be configuration file only in the future)
	 */
	public TextManager(boolean useConf)
	{
		
		// Construct a character list from a square bitmap
		if(!useConf) 
		{
			
			fontTex = new Texture("font1");
			constructCharListSquare();
		}
		
		// Construct a character list from a config file
		if(useConf) 
		{
			
			fontTex = new Texture("fontHD");
			constructCharListConfig();
		}
	}
	
	/**
	 * Construct a character list from a square bitmap
	 */
	private void constructCharListSquare()
	{
		
		// Construct the character list
		for(int i = 0; i < 256; i++)
		{
			charList.add(new ECharacter(Character.toChars(i)[0]));
		}
	}
	
	/**
	 * Construct a character list from config file
	 */
	private void constructCharListConfig()
	{
		
		// Load the configuration file in a ArrayObject
		ArrayObject conf = FileIO.loadtxt("textures/texdata/fontHD_converted.dat", ",", true, null);
		
		// Need to find the maximum x width
		xMax = 0f;
		
		for(int i = 0; i < conf.getDataArray()[0].length; i++)
		{
			
			int offX = Integer.parseInt(conf.get(4, i).split("=")[1]);
			if(offX > xMax) xMax = offX;
		}
		
		// Construct the character list (extract the data from the config file)
		for(int i = 0; i < conf.getDataArray()[0].length; i++)
		{

			int id = Integer.parseInt(conf.get(0, i).split("=")[1]);

			System.out.println(id);
			
			int posX = Integer.parseInt(conf.get(1, i).split("=")[1]);
			int posY = Integer.parseInt(conf.get(2, i).split("=")[1]);
			
			int offX = Integer.parseInt(conf.get(3, i).split("=")[1]);
			int offY = Integer.parseInt(conf.get(4, i).split("=")[1]);
			
			charList.add(new ECharacter(id, posX, posY, offX, offY));
		}
	}
	
	/**
	 * Get a character from the list
	 * @param ch Character
	 * @return The ECharacter that belongs to this file
	 */
	public static ECharacter getCharacter(char ch)
	{
		
		for(ECharacter character : charList)
		{
			
			if(character.getChar() == ch) return character;
		}
		
		throw new RuntimeException("This character is not present in the current character list");
	}
	
	/**
	 * Get the font texture that is used
	 * @return The current font texture
	 */
	public static Texture getFontTexture()
	{
		return fontTex;
	}
}