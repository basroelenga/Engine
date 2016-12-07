package engine;

import java.util.ArrayList;

import engine.primitives.Particle;
import engine.primitives.Rectangle;
import engine.primitives.Sphere;
import graphics.Texture;
import math.Matrix4f;
import math.Vector4f;

public class EngineObjectManager {

	private static ArrayList<EngineObjects> engineObjectList = new ArrayList<EngineObjects>();
	
	private EngineObjectManager() {}
	
	public static void update()	{for(EngineObjects obj : engineObjectList) obj.update();}
	public static void render()	{for(EngineObjects obj : engineObjectList) obj.render();}
	
	public static void addRectangle(float x, float y, float z, float xs, float ys, float zs, float ya, Matrix4f projection, Vector4f RGBAcolor)
	{
		engineObjectList.add(new Rectangle(x, y, z, xs, ys, zs, ya, projection, RGBAcolor));
	}
	
	public static void addSphere(float subdivision, float x, float y, float z, float scaling, Texture tex, boolean useLighting)
	{
		engineObjectList.add(new Sphere(subdivision, x, y, z, scaling, tex, useLighting));
	}
	
	public static void addParticle(float x, float y, float z, float vx, float vy, float vz, float mass, Texture tex)
	{
		engineObjectList.add(new Particle(x, y, z, vx, vy, vz, mass, tex));
	}
	
	public static EngineObjects getEngineObject(String id)
	{
		
		for(EngineObjects obj : engineObjectList)
		{
			
			if(obj.getName().equals(id)) return obj;
		}
		
		throw new RuntimeException("Object does not exist: " + id);
	}
}