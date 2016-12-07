package engine.primitives;

import engine.EngineObjects;
import graphics.Texture;

public class Particle extends EngineObjects{

	private Sphere sphere;
	
	private float mass;
	
	public Particle(float x, float y, float z, float vx, float vy, float vz, float mass, Texture tex)
	{
		
		this.x = x;
		this.y = y;
		this.z = z;
		
		this.vx = vx;
		this.vy = vy;
		this.vz = vz;
		
		this.mass = mass;
		
		this.tex = tex;
		
		sphere = new Sphere(20, x, y, z, 1, tex, true);
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