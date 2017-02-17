package engine;

import java.util.ArrayList;

import engine.objects.TestObject;
import engine.objects.Particle;
import engine.objects.Rectangle;
import engine.objects.Sphere;
import graphics.Texture;
import math.Matrix4f;
import math.Vector3f;
import math.Vector4f;
import shapes.Point;
import shapes.Quad;
import shapes.UVSphere;

public class EngineObjectManager {

	private static ArrayList<EngineObjects> engineObjectList = new ArrayList<EngineObjects>();
	private static ArrayList<UVSphere> uvSphereList = new ArrayList<UVSphere>();
	
	private static Point point;
	private static Quad quad;
		
	private static boolean isInit = false;
	
	private EngineObjectManager() {}
	
	/**
	 * Create all the primitive objects (vaoID) for rendering.
	 */
	public static void createPrimitives()
	{
		
		// Create point primitive
		point = new Point();
		
		// Create quad primitive
		// Create the points in the rectangle.
		ArrayList<Vector3f> points = new ArrayList<Vector3f>();
		
		points.add(new Vector3f(0f, 0f, 0.0f));
		points.add(new Vector3f(1f, 0f, 0.0f));
		points.add(new Vector3f(1f, 1f, 0.0f));
		points.add(new Vector3f(0f, 1f, 0.0f));
		
		// Create the quad which represents the rectangle as a cube/box.
		quad = new Quad(points, true);
		
		// Set the init state to true
		isInit = true;
	}
	
	public static void update()	{for(EngineObjects obj : engineObjectList) obj.update();}
	public static void render()	{for(EngineObjects obj : engineObjectList) obj.render();}
	
	/**
	 * Create a quad based rectangle, this rectangle is automatically added to the engine object update and render list.
	 * @param x The x-position of the rectangle.
	 * @param y The y-position of the rectangle.
	 * @param z The z-position of the rectangle.
	 * @param xs The x-scaling of the rectangle.
	 * @param ys The y-scaling of the rectangle.
	 * @param zs The z-scaling of the rectangle.
	 * @param ya The y-rotation of the rectangle.
	 * @param projection The projection matrix to be used, can be orthographic or perspective.
	 * @param RGBAcolor The color of the rectangle.
	 */
	public static void addRectangle(String name, Texture tex, float x, float y, float z, float xs, float ys, float zs, Matrix4f projection, Vector4f RGBAcolor)
	{
		engineObjectList.add(new Rectangle(name, tex, x, y, z, xs, ys, zs, projection, RGBAcolor));
	}
	
	/**
	 * Add a sphere to the engine object list (only a UV sphere for now).
	 * @param subdivision The amount of subdivisions of the sphere.
	 * @param x The x-position of the sphere.
	 * @param y The y-position of the sphere.
	 * @param z The z-position of the sphere.
	 * @param scaling The scaling of the sphere (same in all directions).
	 * @param tex The texture to be used on the sphere.
	 * @param useLighting Use the lighting system.
	 */
	public static void addSphere(String name, int subdivision, float x, float y, float z, float scaling, Texture tex, boolean useLighting)
	{
		engineObjectList.add(new Sphere(name, subdivision, x, y, z, scaling, tex, useLighting));
	}
	
	/**
	 * Add a particle to the engine object list
	 * @param x The x-position of the particle.
	 * @param y The y-position of the particle.
	 * @param z The z-position of the particle.
	 * @param vx The x-velocity of the particle.
	 * @param vy The y-velocity of the particle.
	 * @param vz The z-velocity of the particle.
	 * @param scaling The scaling of the particle (same in all directions).
	 * @param mass The mass of the particle (for physics purposes).
	 * @param type The type of particle, either a point or a UV sphere.
	 * @param tex The texture of the particle.
	 */
	public static void addParticle(float x, float y, float z, float vx, float vy, float vz, float scaling, float mass, String type, Texture tex)
	{
		engineObjectList.add(new Particle(x, y, z, vx, vy, vz, scaling, mass, tex, type));
	}
	
	public static void addBunny()
	{
		engineObjectList.add(new TestObject());
	}
	
	/**
	 * Get an object which is currently in the engine.
	 * @param id The name/"id" of the object.
	 * @return The object.
	 */
	public static EngineObjects getEngineObject(String id)
	{
		
		for(EngineObjects obj : engineObjectList)
		{
			System.out.println(obj.getName());
			if(obj.getName().equals(id)) return obj;
		}
		
		throw new RuntimeException("Object does not exist: " + id);
	}
	
	/**
	 * Get the point.
	 * @return The point.
	 */
	public static Point getPoint()
	{
		
		if(isInit) return point;
		throw new RuntimeException("Primitives have not yet been created");
	}
	
	/**
	 * Get the quad.
	 * @return The quad.
	 */
	public static Quad getQuad()
	{
		
		if(isInit) return quad;
		throw new RuntimeException("Primitives have not yet been created");
	}
	
	/**
	 * Get the UV sphere, if the UV sphere does not exist one will be created.
	 * For higher subdivision the engine can drop fps. This function is mostly used by the Sphere object or the Particle object.
	 * @param subdivision The amount of subdivision.
	 * @return The requested UV sphere.
	 */
	public static UVSphere getUVSphere(int subdivision)
	{
		
		for(UVSphere uvs : uvSphereList)
		{
			
			if(uvs.getSubdivision() == subdivision) return uvs;
		}
		
		UVSphere uvs = new UVSphere(subdivision);
		uvSphereList.add(uvs);
		
		return uvs;
	}
}