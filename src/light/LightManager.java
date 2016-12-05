package light;

import java.util.ArrayList;

public class LightManager {

	private static ArrayList<LightObject> lightList = new ArrayList<LightObject>();
	
	private LightManager(){}
	
	public static void update()	{for(LightObject light : lightList) light.update();}
	public static void render()	{for(LightObject light : lightList) light.render();}
	
	public static void addPointLight(String name, float x, float y, float z, boolean show)
	{
		
		lightList.add(new PointLight(name, x, y, z, show));
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