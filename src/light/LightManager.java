package light;

import java.util.ArrayList;

import math.Vector3f;

public class LightManager {

	private static ArrayList<PointLight> pointLightList = new ArrayList<PointLight>();
	
	private LightManager(){}
	
	public static void update()
	{
		
		for(PointLight light : pointLightList) light.update();
	}
	
	public static void render()
	{
		
		for(PointLight light : pointLightList) light.render();
	}
	
	public static void addPointLight(Vector3f position, String id)
	{
		
		pointLightList.add(new PointLight(position, id, true));
	}
	
	public static PointLight getPointLight(String id)
	{
		
		for(PointLight light : pointLightList)
		{
			
			if(light.getID().equals(id)) return light;
		}
		
		System.err.println("Light does not exist: " + id);
		return null;
	}
	
	public static int size()
	{
		return pointLightList.size();
	}
}