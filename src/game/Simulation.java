package game;

import java.util.ArrayList;

import cam.Camera;
import game.obj.Sphere;
import game.simobj.Particle;
import graphics.Texture;
import light.LightManager;
import math.Vector3f;
import terrain.Terrain;

public class Simulation {
	
	public static Camera cam;
	
	private ArrayList<Particle> particleList = new ArrayList<Particle>();
	
	private Terrain tObj;
	
	private Sphere bgS;
	
	public Simulation()
	{
		
		cam = new Camera();
		// =========================

		bgS = new Sphere(20, 0, 0, 0, 50, new Texture("bg"), false);
		tObj = new Terrain();
		
		LightManager.addPointLight(new Vector3f(0, 0, 0), "light");
		//createParticles();
	}
	
	private void createParticles()
	{
		
		int particles = 10;
		
		for(int i = 0; i < particles; i++)
		{
			for(int j = 0; j < particles; j++)
			{
				particleList.add(new Particle(new Vector3f(i, 0, j), new Vector3f(), 1));
			}
		}
	}
	
	public void update()
	{

		cam.update();
		bgS.update();
		
		tObj.update();
		
		for(Particle part : particleList)
		{
			
			part.update();
		}
		
		LightManager.update();
	}
	
	public void render()
	{
		
		bgS.render();
		tObj.render();
		
		for(Particle part : particleList)
		{
			
			part.render();
		}
		
		LightManager.render();
	}
}