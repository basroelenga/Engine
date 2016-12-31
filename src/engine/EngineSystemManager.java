package engine;

import java.util.ArrayList;

import engine.systems.ParticleSystem;

public class EngineSystemManager {

	private static ArrayList<EngineSystem> engineSystemList = new ArrayList<EngineSystem>();
	
	private EngineSystemManager() {}
	
	public static void update() {for(EngineSystem sys : engineSystemList) sys.update();}
	public static void render() {for(EngineSystem sys : engineSystemList) sys.render();}
	
	public static void addDirectionalParticleSystem(String name, float x, float y, float z, float xDir, float yDir, float zDir, float timeOut)
	{
		engineSystemList.add(new ParticleSystem(name, x, y, z, xDir, yDir, zDir, timeOut));
	}
	
	public static void addUniformParticleSystem(String name ,float x, float y, float z, float timeOut)
	{
		engineSystemList.add(new ParticleSystem(name, x, y, z, timeOut));
	}
	
	public static EngineSystem getEngineSystem(String id)
	{
		
		for(EngineSystem sys : engineSystemList)
		{
			
			if(sys.getName().equals(id)) return sys;
		}
		
		throw new RuntimeException("Object does not exist: " + id);
	}
}