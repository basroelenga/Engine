package game.simobj;

import game.obj.Sphere;
import math.Vector3f;

public class Particle {

	private Sphere sphere;
	
	private Vector3f pos;
	private Vector3f vel;
	
	private float mass;
	
	public Particle(Vector3f pos, Vector3f vel, float mass)
	{
		
		this.pos = pos;
		this.vel = vel;
		
		this.mass = mass;
		
		sphere = new Sphere(10, pos.getX(), pos.getY(), pos.getZ(), 1, null, false);
	}
	
	public void update()
	{
		
		sphere.setX(pos.getX());
		sphere.setY(pos.getY());
		sphere.setZ(pos.getZ());
		
		sphere.update();
	}
	
	public void render()
	{
		
		sphere.render();
	}
}