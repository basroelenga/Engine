package engine.primitives;

import engine.EngineObjects;
import math.Vector3f;

public class Particle extends EngineObjects{

	private Sphere sphere;
	
	private Vector3f pos;
	private Vector3f vel;
	
	private float mass;
	
	public Particle(Vector3f pos, Vector3f vel, float mass)
	{
		
		this.pos = pos;
		this.vel = vel;
		
		this.mass = mass;
		
		sphere = new Sphere(10, pos.getX(), pos.getY(), pos.getZ(), 1, null, true);
	}
	
	public void update()
	{
		
		sphere.setX(pos.getX());
		sphere.setY(pos.getY());
		sphere.setZ(pos.getZ());
		
		sphere.setXs(0.2f);
		sphere.setYs(0.2f);
		sphere.setZs(0.2f);
		
		sphere.update();
	}
	
	public void render()
	{
		sphere.render();
	}
}