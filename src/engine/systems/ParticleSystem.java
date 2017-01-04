package engine.systems;

import java.util.ArrayList;
import java.util.Random;

import engine.EngineSystem;
import engine.objects.Particle;

public class ParticleSystem extends EngineSystem{

	private float xDir;
	private float yDir;
	private float zDir;
	
	private float directionVariation;
	
	private float particleSpeed;
	private float speedVariation;
	
	private float timeOut;
	private int particlesPerCycle;

	private Random rand = new Random();
	
	private String type = "";
	
	private ArrayList<Particle> pList = new ArrayList<Particle>();
	private ArrayList<Integer> removeIndx = new ArrayList<Integer>();
	
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
		
		this.particleSpeed = 0.002f;
		this.particlesPerCycle = 1;
		this.speedVariation = 0.1f;
		this.directionVariation = 2f;
		
		this.type = "Directional";
	}
	
	public ParticleSystem(String name, float x, float y, float z, float timeOut)
	{
		
		this.name = name;
		
		this.x = x;
		this.y = y;
		this.z = z;
		
		this.timeOut = timeOut;
		
		this.particleSpeed = 0.002f;
		this.particlesPerCycle = 1;
		
		this.type = "Uniform";
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
			
			float vx = 0;
			float vy = 0;
			float vz = 0;
			
			// Select which type
			switch(type)
			{
			
			case "Directional":
				
				vx = particleSpeed * xDir * (1 - (directionVariation / 2) + rand.nextFloat() * directionVariation);
				vy = particleSpeed * yDir * (1 - (directionVariation / 2) + rand.nextFloat() * directionVariation);
				vz = particleSpeed * zDir * (1 - (directionVariation / 2) + rand.nextFloat() * directionVariation);
				
				pList.add(new Particle(x, y, z, vx, vy, vz, 0.02f, 0, null, "sphere"));
				break;
				
			case "Uniform":
				
				vx = (float) (particleSpeed * Math.sin(Math.PI * rand.nextFloat()) * Math.cos(2 * Math.PI * rand.nextFloat()));
				vy = (float) (particleSpeed * Math.sin(Math.PI * rand.nextFloat()) * Math.sin(2 * Math.PI * rand.nextFloat()));
				vz = (float) (particleSpeed * Math.sin(Math.PI * rand.nextFloat()));
				
				pList.add(new Particle(x, y, z, vx, vy, vz, 0.02f, 0, null, "point"));
				break;
			}
		}
		
		// Remove particles
		remove();
	}
	
	private void remove()
	{
		
		removeIndx.clear();
		
		// Add all the indices
		for(int i = 0; i < pList.size(); i++)
		{
			if(pList.get(i).getLifeTime() >= timeOut)
			{
				removeIndx.add(i);
			}
		}
		
		// Remove particles from list
		for(int i = 0; i < removeIndx.size(); i++)
		{
			pList.remove((int) removeIndx.get(removeIndx.size() - i - 1));
		}
	}

	public void render()
	{
		
		for(Particle particles : pList) particles.render();
	}
}