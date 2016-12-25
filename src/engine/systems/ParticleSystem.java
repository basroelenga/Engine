package engine.systems;

import java.util.ArrayList;

import engine.Engine;
import engine.objects.Particle;

public class ParticleSystem {

	private String name;
	
	private float x;
	private float y;
	private float z;
	
	private float xDir;
	private float yDir;
	private float zDir;
	
	private float directionVariation;
	
	private float particleSpeed;
	private float speedVariation;
	
	private float timeOut;
	private int particlesPerCycle;
	
	private ArrayList<Particle> pList = new ArrayList<Particle>();
	
	public ParticleSystem(String name, float x, float y, float z, float xDir, float yDir, float zDir, float timeOut)
	{
		
		this.name = name;
		
		this.x = x;
		this.y = y;
		this.z = z;
		
		this.xDir = xDir;
		this.yDir = yDir;
		this.zDir = zDir;
		
		this.timeOut = timeOut;
		
		this.particleSpeed = 0.02f;
		this.particlesPerCycle = 1;
		this.speedVariation = 0.1f;
	}
	
	public void update()
	{

		// Update the particles
		for(Particle particles : pList)
		{			
			particles.update();
		}
		
		// Create new particles
		for(int i = 0; i < particlesPerCycle; i++)
		{
			
			float vx = particleSpeed * xDir;
			float vy = particleSpeed * yDir;
			float vz = particleSpeed * zDir;
			
			pList.add(new Particle(x, y, z, vx, vy, vz, 0, null, "point"));
		}
	}

	public void render()
	{
		
		for(Particle particles : pList) particles.render();
	}
}