package light;

import java.util.ArrayList;

import math.Vector3f;
import shaders.Shader;
import shaders.ShaderManager;

public class LightManager {

	private static ArrayList<LightObject> pointLightList = new ArrayList<LightObject>();
	private static ArrayList<LightObject> directionalLightList = new ArrayList<LightObject>();
	
	private LightManager(){}
	
	public static void update()	
	{
		
		// Update all the light sources (position)
		for(LightObject light : pointLightList) light.update();
		
		// Tell the shaders how many lights there are going to be
		for(Shader shader : ShaderManager.getShaderList())
		{
		
			if(shader.getUseLighting())
			{
				
				// Upload number of current lights
				shader.uploadInt(getNumberOfPointLights(), shader.getNumberOfPointLightsLoc());
				shader.uploadInt(getNumberOfDirectionalLights(), shader.getNumberOfDirectionalLightsLoc());
				
				// Upload all the light to the shaders that use lighting
				for(int i = 0; i < getNumberOfPointLights(); i++)
				{
					
					pointLightList.get(i).uploadToShader(i, shader);
				}
				
				for(int i = 0; i < getNumberOfDirectionalLights(); i++)
				{
					
					directionalLightList.get(i).uploadToShader(i, shader);
				}
			}
		}
	}
	public static void render()	{for(LightObject light : pointLightList) light.render();}
	
	public static void addPointLight(String name, float x, float y, float z, Vector3f lightColor, boolean show)
	{
		
		pointLightList.add(new PointLight(name, x, y, z, lightColor, show));
		
		// When a point light is added, the shaders should also know this
		for(Shader shader : ShaderManager.getShaderList())
		{
			
			if(shader.getUseLighting())
			{
				
				shader.addPointLight();
			}
		}
	}
	
	public static void addDirectionalLight(String name, float xDir, float yDir, float zDir, Vector3f lightColor)
	{
		
		directionalLightList.add(new DirectionalLight(name, xDir, yDir, zDir, lightColor));
		
		// When a directional light is added, the shaders should also know this
		for(Shader shader : ShaderManager.getShaderList())
		{
			
			if(shader.getUseLighting())
			{
				
				shader.addDirectionalLight();
			}
		}
	}
	
	public static LightObject getLight(String id)
	{
		
		for(LightObject light : pointLightList)
		{
			
			if(light.getName().equals(id)) return light;
		}
		
		throw new RuntimeException("Light does not exist: " + id);
	}
	
	public static int getNumberOfPointLights()
	{
		return pointLightList.size();
	}
	
	public static int getNumberOfDirectionalLights()
	{
		return directionalLightList.size();
	}
	
	public static int getNumberOfLights()
	{
		return pointLightList.size() + directionalLightList.size();
	}
}