package shaders;

import java.util.ArrayList;

public class ShaderManager {

	private static String[] shaders = {"basic", "basictex", "ui", "light", "basicT"};
	
	private static ArrayList<Shader> shaderList = new ArrayList<Shader>();
	
	private ShaderManager() {}
	
	public static void loadBasicShaders()
	{
		
		for(int i = 0; i < shaders.length; i++)
		{
			
			if(!shaders[i].equals("light")) shaderList.add(new Shader(shaders[i], false));
		}
	}
	
	public static void loadLightShaders()
	{
		
		for(int i = 0; i < shaders.length; i++)
		{
			
			if(shaders[i].equals("light")) shaderList.add(new Shader(shaders[i], true));
		}
	}

	public static Shader getShader(String shader)
	{

		for(Shader sh : shaderList)
		{
			if(sh.getShaderName().equals(shader))
			{
				return sh;
			}
		}
		
		throw new RuntimeException("The shader: " + shader + " does not exist");
	}
	
	public static ArrayList<Shader> getShaderList()
	{
		return shaderList;
	}
}