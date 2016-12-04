package game;

import java.util.ArrayList;

import cam.Camera;
import engine.EngineObjects;
import engine.primitives.Particle;
import engine.primitives.Sphere;
import graphics.Texture;
import light.LightManager;
import math.Vector3f;

public class Simulation {
	
	public static Camera cam;
	private ArrayList<EngineObjects> simulationObjectsList = new ArrayList<EngineObjects>();
	
	public Simulation()
	{
		
		cam = new Camera();
		// =========================
		simulationObjectsList.add(new Sphere(20, 0, 0, 0, 50, new Texture("bg"), false));
		
		LightManager.addPointLight(new Vector3f(0, 0, 0), "light");
		createParticles();
	}
	
	private void createParticles()
	{
		
		int particles = 10;
		
		for(int i = 0; i < particles; i++)
		{
			for(int j = 0; j < particles; j++)
			{
				simulationObjectsList.add(new Particle(new Vector3f(i, 0, j), new Vector3f(), 1));
			}
		}
	}
	
	public void update()
	{

		cam.update();

		for(EngineObjects obj : simulationObjectsList) obj.update();
		
		LightManager.update();
	}
	
	public void render()
	{
		
		for(EngineObjects obj : simulationObjectsList) obj.render();
		
		LightManager.render();
	}
}