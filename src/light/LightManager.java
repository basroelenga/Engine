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
		
		// Tell the shaders how many lights there are going to be
		for(Shader shader : ShaderManager.getShaderList())
		{
		
			shader.uploadInt(lightList.size(), shader.getNumberOfLightsLoc());
		}
		
		// Update all the light sources
		for(LightObject light : lightList) light.update();
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
	
	public static int size()
	{
		return lightList.size();
	}
}