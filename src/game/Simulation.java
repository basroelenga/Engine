package game;

import light.LightManager;
import cam.Camera;
import engine.EngineObjectManager;
import graphics.Texture;

public class Simulation {

	public static Camera cam;

	public Simulation()
	{
		
		cam = new Camera();
		// =========================
		EngineObjectManager.addSphere(20, 0, 0, 0, 50, new Texture("bg"), false);
		
		LightManager.addPointLight("light", 0, 5, 0, true);
		createParticles();
	}
	
	private void createParticles()
	{
		
		int particles = 10;
		
		for(int i = 0; i < particles; i++)
		{
			for(int j = 0; j < particles; j++)
			{
				EngineObjectManager.addParticle(i, 0, j, 0, 0, 0, 1);
			}
		}
	}
	
	public void update()
	{

		cam.update();

		EngineObjectManager.update();
		LightManager.update();
	}
	
	public void render()
	{

		EngineObjectManager.render();
		LightManager.render();
	}
}