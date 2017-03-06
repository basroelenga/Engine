package engine.systems;

import java.util.ArrayList;
import org.lwjgl.BufferUtils;

import engine.EngineSystem;
import engine.objects.Particle;
import shaders.ShaderManager;
import shapes.Point;
import shapes.Quad;
import utils.DrawShapes;

public class ParticleSystem extends EngineSystem{
	
	private float directionVariation;
	private float particleSpeed;
	
	private float timeOut;
	private int particlesPerCycle;

	private int maxParticles = 10000;
			
	private ArrayList<Particle> pList = new ArrayList<Particle>();
	private ArrayList<Integer> removeIndx = new ArrayList<Integer>();
	
	/**
	 * Construct a directional particle system.
	 * @param name Name of the particle system.
	 * @param pType The particle render type ("UV Sphere", "Quad", "Point").
	 * @param x The x-position of the system.
	 * @param y The y-position of the system.
	 * @param z The z-position of the system.
	 * @param xDir The x-direction of the system.
	 * @param yDir The y-direction of the system.
	 * @param zDir The z-direction of the system.
	 * @param timeOut The particle time out (life time).
	 */
	public ParticleSystem(String name, String pType, float x, float y, float z, float xDir, float yDir, float zDir, float timeOut)
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
		this.directionVariation = 2f;
		
		this.psType = "Directional";
		
		checkPType(pType);
	}
	
	/**
	 * Construct a uniform particle system.
	 * @param name Name of the particle system.
	 * @param pType The particle render type ("UV Sphere", "Quad", "Point").
	 * @param x The x-position of the system.
	 * @param y The y-position of the system.
	 * @param z The z-position of the system.
	 * @param timeOut The particle time out (life time).
	 */
	public ParticleSystem(String name, String pType, float x, float y, float z, float timeOut)
	{
		
		this.name = name;
		
		this.x = x;
		this.y = y;
		this.z = z;
		
		this.timeOut = timeOut;
		
		this.particleSpeed = 0.002f;
		this.particlesPerCycle = 4;
		
		this.psType = "Uniform";
		
		shader = ShaderManager.getShader("basicInstance");
		buffer = BufferUtils.createFloatBuffer(16 * maxParticles);
		
		checkPType(pType);
		createType();
	}
	
	/**
	 * This method checks if the given particle render type is valid.
	 * @param pType The particle render type.
	 */
	private void checkPType(String pType)
	{
		
		for(int i = 0; i < allowedPType.length; i++) 
		{
			
			if(allowedPType[i].equals(pType)) 
			{
				
				this.pType = pType;
				return;
			}
		}
		
		throw new RuntimeException("Particle system with pType: " + pType + " is invalid for particle system " + name);
	}
	
	/**
	 * Create the particle system with the ptype.
	 */
	private void createType()
	{
		
		switch(pType)
		{
		
		case "point":
			
			point = new Point(maxParticles);
			break;
			
		case "quad":
			
			quad = new Quad(maxParticles);
			break;
		}
	}
	
	/**
	 * The particle system update function, this function mostly takes care of the creation of particles.
	 */
	public void update()
	{

		// Instance particle update
		float[] vboData = new float[16 * pList.size()];
		int pointer = 0;
		
		for(Particle particles : pList)
		{			
			
			// First update the particle (position, velocity etc).
			particles.update();
							
			// Get the MVP matrix from this particle as a float array.
			float[] mvpData = particles.getMVP().getElements();

			// Store it in the VBO float array.
			for(int i = 0; i < mvpData.length; i++)
			{
				
				vboData[pointer++] = mvpData[i];
			}
		}
		
		// Depending on the particle type, update the VBO.
		switch(pType)
		{
		
		case "point":
			
			point.updateVBO(vboData, buffer);
			break;
			
		case "quad":
			
			quad.updateVBO(vboData, buffer);
			break;
		}
		
		// Create new particles
		createParticles();
		
		// Remove particles
		remove();
	}
	
	/**
	 * Create the particles in the particle system and add them to the particle list.
	 */
	private void createParticles()
	{
		
		for(int i = 0; i < particlesPerCycle; i++)
		{
			
			float vx = 0;
			float vy = 0;
			float vz = 0;
			
			// Select which type
			switch(psType)
			{
			
			case "Directional":
				
				vx = particleSpeed * xDir * (1 - (directionVariation / 2) + rand.nextFloat() * directionVariation);
				vy = particleSpeed * yDir * (1 - (directionVariation / 2) + rand.nextFloat() * directionVariation);
				vz = particleSpeed * zDir * (1 - (directionVariation / 2) + rand.nextFloat() * directionVariation);
				
				pList.add(new Particle(x, y, z, vx, vy, vz, 0.02f, 0, null, pType));
				break;
				
			case "Uniform":
				
				vx = (float) (particleSpeed * Math.sin(Math.PI * rand.nextFloat()) * Math.cos(2 * Math.PI * rand.nextFloat()));
				vy = (float) (particleSpeed * Math.sin(Math.PI * rand.nextFloat()) * Math.sin(2 * Math.PI * rand.nextFloat()));
				vz = (float) (particleSpeed * Math.cos(Math.PI * rand.nextFloat()));
				
				pList.add(new Particle(x, y, z, vx, vy, vz, 0.02f, 0, null, pType));
				break;
			}
		}
	}
	
	/**
	 * Function that removes particles which are out of life time.
	 */
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

	/**
	 * Render all particles in the particle system.
	 */
	public void render()
	{
		
		switch(pType)
		{
		
		case "point":

			DrawShapes.drawPointInstanced(shader, fbo, point.getVaoID(), pList.size());
			break;
			
		case "quad":
			
			DrawShapes.drawQuadInstanced(shader, fbo, quad.getVaoID(),pList.size());
			break;
			
		case "sphere":
			
			break;
		}
	}
}