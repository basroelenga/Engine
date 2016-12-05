package engine.primitives;

import engine.EngineObjects;

public class Particle extends EngineObjects{

	private Sphere sphere;
	
	private float mass;
	
	public Particle(float x, float y, float z, float vx, float vy, float vz, float mass)
	{
		
		this.x = x;
		this.y = y;
		this.z = z;
		
		this.vx = vx;
		this.vy = vy;
		this.vz = vz;
		
		this.mass = mass;
		
		sphere = new Sphere(10, x, y, z, 1, null, true);
	}
	
	public void update()
	{
		
		sphere.setX(x);
		sphere.setY(y);
		sphere.setZ(z);
		
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