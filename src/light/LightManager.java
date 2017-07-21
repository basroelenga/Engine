package light;

import java.util.ArrayList;

import camera.PlayerCamera;
import math.Matrix4f;
import math.Vector3f;
import shaders.Shader;
import shaders.ShaderManager;

public class LightManager {

	private static boolean shadows = true;
	
	private static Matrix4f biasMatrix;
	
	private static ArrayList<PointLight> pointLightList = new ArrayList<PointLight>();
	private static ArrayList<DirectionalLight> directionalLightList = new ArrayList<DirectionalLight>();
	private static ArrayList<SpotLight> spotLightList = new ArrayList<SpotLight>();
	
	private static ArrayList<LightObject> lightList = new ArrayList<LightObject>();
	
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
	
	/**
	 * Enable the rendering of shadows for all lights.
	 */
	public static void enableShadows()
	{
		
		for(LightObject obj : getLightList()) obj.renderShadows = true;
		shadows = true;
	}
	
	/**
	 * Disable the rendering of shadows for all lights.
	 */
	public static void disableShadows()
	{
		
		for(LightObject obj : getLightList()) obj.renderShadows = false;
		shadows = false;
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
	
	public static void addDirectionalLight(String name, PlayerCamera cam, float xDir, float yDir, float zDir, Vector3f lightColor)
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
	
	public static DirectionalLight getDirectionalLight(String name)
	{
		
		for(DirectionalLight light : directionalLightList)
		{
			
			if(light.getName().equals(name)) return light;
		}
		
		throw new RuntimeException("Light does not exist: " + name);
	}
	
	public static PointLight getPointLight(String name)
	{
		
		for(PointLight light : pointLightList)
		{
			
			if(light.getName().equals(name)) return light;
		}
		
		throw new RuntimeException("Light does not exist: " + name);
	}
	
	public static SpotLight getSpotLight(String name)
	{
		
		for(SpotLight light : spotLightList)
		{
			
			if(light.getName().equals(name)) return light;
		}
		
		throw new RuntimeException("Light does not exist: " + name);
	}
	
	public static ArrayList<PointLight> getPointLightList()
	{
		return pointLightList;
	}
	
	public static int getNumberOfPointLights()
	{
		return pointLightList.size();
	}
	
	public static ArrayList<DirectionalLight> getDirectionalLightList()
	{
		return directionalLightList;
	}
	
	public static int getNumberOfDirectionalLights()
	{
		return directionalLightList.size();
	}
	
	public static ArrayList<SpotLight> getSpotLightList()
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
		
		lightList.clear();
		
		lightList.addAll(pointLightList);
		lightList.addAll(directionalLightList);
		lightList.addAll(spotLightList);
		
		return lightList;
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
	
	public static boolean getRenderShadows()
	{
		return shadows;
	}
}