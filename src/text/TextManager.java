package text;

import java.util.ArrayList;

import graphics.Texture;

public class TextManager {

	private static Texture fontTex;
	
	private static ArrayList<ECharacter> charList = new ArrayList<ECharacter>();
	
	public TextManager()
	{
		
		fontTex = new Texture("font");
		constructCharList();
	}
	
	private void constructCharList()
	{
		
		for(int i = 0; i < 256; i++)
		{
		
			charList.add(new ECharacter(Character.toChars(i)[0]));
		}
	}
	
	public static ECharacter getCharacter(char ch)
	{
		return charList.get((int) ch);
	}
	
	public static Texture getFontTexture()
	{
		return fontTex;
	}
}