package game;

import java.util.ArrayList;

import cam.Camera;
import engine.Engine;
import game.obj.Sphere;
import game.simobj.Particle;
import graphics.Texture;
import light.LightManager;
import math.Vector3f;
import text.Text;

public class Simulation {
	
	public static Camera cam;
	
	private ArrayList<Particle> particleList = new ArrayList<Particle>();
	
	private Sphere bgS;
	
	private Text test;
	
	public Simulation()
	{
		
		cam = new Camera();
		// =========================
		
		test = new Text("FPS", "HUD", 30f, Engine.getHeight() - 30f, 0f);
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
		
		test.updateText("FPS:" + (int) Engine.FPS);
		
		for(Particle part : particleList)
		{
			
			part.update();
		}
		
		LightManager.update();
	}
	
	public void render()
	{
		
		bgS.render();
		
		test.updateAndRender();
		
		for(Particle part : particleList)
		{
			
			part.render();
		}
		
		LightManager.render();
	}
}