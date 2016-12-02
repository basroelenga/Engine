package shaders;

import java.util.ArrayList;

public class ShaderManager {

	private String[] shaders = {"basic", "basictex", "ui", "light", "basicT"};
	
	private static ArrayList<Shader> shaderList = new ArrayList<Shader>();
	
	public ShaderManager()
	{
		
		loadShaders();
	}
	
	private void loadShaders()
	{
		
		for(int i = 0; i < shaders.length; i++)
		{
			
			shaderList.add(new Shader(shaders[i]));
		}
	}
	
	public static Shader getShader(String shader)
	{

		Shader rSh = null;
		
		for(Shader sh : shaderList)
		{
			if(sh.getShaderName().equals(shader))
			{
				rSh = sh;
			}
		}
		
		return rSh;
	}
}