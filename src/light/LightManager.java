package light;

import java.util.ArrayList;

import camera.Camera;
import math.Matrix4f;
import math.Vector3f;
import shaders.Shader;
import shaders.ShaderManager;

public class LightManager {

	private static Matrix4f biasMatrix;
	
	private static ArrayList<LightObject> pointLightList = new ArrayList<LightObject>();
	private static ArrayList<LightObject> directionalLightList = new ArrayList<LightObject>();
	private static ArrayList<LightObject> spotLightList = new ArrayList<LightObject>();
	
	private LightManager(){}
	
	/**
	 * Initialize the light manager, this includes creating the bias matrix for shadow rendering
	 */
	public static void initialize()
	{
		setBiasMatrix();
	}
	
	public static void update()	
	{
		
		// Update all the light sources (position)
		for(LightObject light : pointLightList) light.update();
		for(LightObject light : directionalLightList) light.update();
		for(LightObject light : spotLightList) light.update();
		
		// Tell the shaders how many lights there are going to be
		for(Shader shader : ShaderManager.getShaderList())
		{
		
			if(shader.getUseLighting())
			{
				
				// Upload number of current lights
				shader.uploadInt(getNumberOfPointLights(), shader.getNumberOfPointLightsLoc());
				shader.uploadInt(getNumberOfDirectionalLights(), shader.getNumberOfDirectionalLightsLoc());
				shader.uploadInt(getNumberOfSpotLights(), shader.getNumberOfSpotLightsLoc());
				
				// Upload all the light to the shaders that use lighting
				for(int i = 0; i < getNumberOfPointLights(); i++)
				{
					
					pointLightList.get(i).uploadToShader(i, shader);
				}
				
				for(int i = 0; i < getNumberOfDirectionalLights(); i++)
				{
					
					directionalLightList.get(i).uploadToShader(i, shader);
				}
				
				for(int i = 0; i < getNumberOfSpotLights(); i++)
				{
					
					spotLightList.get(i).uploadToShader(i, shader);
				}
			}
		}
	}
	
	public static void render()	{
		
		for(LightObject light : pointLightList) light.render();
		for(LightObject light : directionalLightList) light.render();
	}
	
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
	
	public static void addDirectionalLight(String name, Camera cam, float xDir, float yDir, float zDir, Vector3f lightColor)
	{
		
		directionalLightList.add(new DirectionalLight(name, cam, xDir, yDir, zDir, lightColor));
		
		// When a directional light is added, the shaders should also know this
		for(Shader shader : ShaderManager.getShaderList())
		{
			
			if(shader.getUseLighting())
			{
				
				shader.addDirectionalLight();
			}
		}
	}
	
	public static void addSpotLight(String name, float x, float y, float z, float xDir, float yDir, float zDir, float coneAngle, Vector3f lightColor)
	{
		
		spotLightList.add(new SpotLight(name, x, y, z, xDir, yDir, zDir, coneAngle, lightColor));
		
		// When a directional light is added, the shaders should also know this
		for(Shader shader : ShaderManager.getShaderList())
		{
			
			if(shader.getUseLighting())
			{
				
				shader.addSpotLight();
			}
		}
	}
	
	public static LightObject getLight(String id)
	{
		
		for(LightObject light : pointLightList)
		{
			
			if(light.getName().equals(id)) return light;
		}
		
		for(LightObject light : directionalLightList)
		{
			
			if(light.getName().equals(id)) return light;
		}
		
		for(LightObject light : spotLightList)
		{
			
			if(light.getName().equals(id)) return light;
		}
		
		throw new RuntimeException("Light does not exist: " + id);
	}
	
	public static ArrayList<LightObject> getPointLightList()
	{
		return pointLightList;
	}
	
	public static int getNumberOfPointLights()
	{
		return pointLightList.size();
	}
	
	public static ArrayList<LightObject> getDirectionalLightList()
	{
		return directionalLightList;
	}
	
	public static int getNumberOfDirectionalLights()
	{
		return directionalLightList.size();
	}
	
	public static ArrayList<LightObject> getSpotLightList()
	{
		return spotLightList;
	}
	
	public static int getNumberOfSpotLights()
	{
		return spotLightList.size();
	}
	
	/**
	 * Return a list of all lights, order is point to directional to spot
	 * @return The list containing all lightobjects
	 */
	public static ArrayList<LightObject> getLightList()
	{
		
		ArrayList<LightObject> tempLightList = new ArrayList<LightObject>();
		
		tempLightList.addAll(pointLightList);
		tempLightList.addAll(directionalLightList);
		tempLightList.addAll(spotLightList);
		
		return tempLightList;
	}
	
	public static int getNumberOfLights()
	{
		return pointLightList.size() + directionalLightList.size() + spotLightList.size();
	}
	
	public static void setBiasMatrix()
	{
		
		biasMatrix = new Matrix4f();
		
		biasMatrix.translate(0.5f, 0.5f, 0.5f);
		biasMatrix.scale(0.5f, 0.5f, 0.5f);
	}
	
	public static Matrix4f getBiasMatrix()
	{
		return biasMatrix;
	}
}