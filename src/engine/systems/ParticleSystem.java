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

	private Random rand;
	
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
		
		rand = new Random();
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
			
			float vx = particleSpeed * xDir * (1 - (directionVariation / 2) + rand.nextFloat() * directionVariation);
			float vy = particleSpeed * yDir * (1 - (directionVariation / 2) + rand.nextFloat() * directionVariation);
			float vz = particleSpeed * zDir * (1 - (directionVariation / 2) + rand.nextFloat() * directionVariation);
			
			pList.add(new Particle(x, y, z, vx, vy, vz, 0.02f, 0, null, "sphere"));
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