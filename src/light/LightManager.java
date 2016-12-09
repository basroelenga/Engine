package light;

import java.util.ArrayList;

import math.Vector3f;
import shaders.Shader;
import shaders.ShaderManager;

public class LightManager {

	private static ArrayList<LightObject> lightList = new ArrayList<LightObject>();
	
	private LightManager(){}
	
	public static void update()	
	{
		
		// Update all the light sources (position)
		for(LightObject light : lightList) light.update();
		
		// Tell the shaders how many lights there are going to be
		for(Shader shader : ShaderManager.getShaderList())
		{
		
			if(shader.getUseLighting())
			{
				
				shader.uploadInt(lightList.size(), shader.getNumberOfLightsLoc());
				
				// Upload all the light to the shaders that use lighting
				for(int i = 0; i < getNumberOfLights(); i++)
				{
					
					lightList.get(i).uploadToShader(i, shader);
				}
			}
		}
	}
	public static void render()	{for(LightObject light : lightList) light.render();}
	
	public static void addPointLight(String name, float x, float y, float z, Vector3f lightColor, boolean show)
	{
		
		lightList.add(new PointLight(name, x, y, z, lightColor, show));
	}
	
	public static LightObject getLight(String id)
	{
		
		for(LightObject light : lightList)
		{
			
			if(light.getName().equals(id)) return light;
		}
		
		throw new RuntimeException("Light does not exist: " + id);
	}
	
	public static int getNumberOfLights()
	{
		return lightList.size();
	}
}