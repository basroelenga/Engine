package game;

import java.util.ArrayList;

import cam.Camera;
import game.obj.Sphere;
import game.simobj.Particle;
import graphics.Texture;
import light.LightManager;
import math.Vector3f;

public class Simulation {
	
	public static Camera cam;
	
	private ArrayList<Particle> particleList = new ArrayList<Particle>();
	
	private Sphere bgS;
	
	public Simulation()
	{
		
		cam = new Camera();
		// =========================

		bgS = new Sphere(20, 0, 0, 0, 50, new Texture("bg"), false);
		
		LightManager.addPointLight(new Vector3f(0, 0, 0), "light");
		//createParticles();
	}
	
	private void createParticles()
	{
		
		int particles = 100;
		
		for(int i = 0; i < particles; i++)
		{
			
			particleList.add(new Particle(new Vector3f(i, 0, 0), new Vector3f(), 1));
		}
	}
	
	public void update()
	{

		cam.update();
		bgS.update();
		
		for(Particle part : particleList)
		{
			
			part.update();
		}
		
		LightManager.update();
	}
	
	public void render()
	{
		
		bgS.render();
		
		for(Particle part : particleList)
		{
			
			part.render();
		}
		
		LightManager.render();
	}
}