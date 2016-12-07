package game;

import cam.Camera;
import engine.EngineObjectManager;
import graphics.TextureManager;
import light.LightManager;

public class Simulation {

	public static Camera cam;
	
	private float angle = 0f;
	
	public Simulation()
	{
		
		cam = new Camera();
		// =========================
		EngineObjectManager.addSphere(20, 0, 0, 0, 50, TextureManager.getTexture("bg"), false);
		
		LightManager.addPointLight("light", 0f, 2f, 0f, true);
		createParticles();
	}
	
	private void createParticles()
	{
		
		int particles = 16;
		
		for(int i = -particles / 2; i < particles / 2; i++)
		{
			for(int j = -particles / 2; j < particles / 2; j++)
			{
				EngineObjectManager.addParticle(i, 0, j, 0, 0, 0, 1, TextureManager.getTexture("testtex"));
			}
		}
	}
	
	public void update()
	{
		angle += 0.1f;
		
		LightManager.getLight("light").setX((float) (3f * Math.cos(angle)));
		LightManager.getLight("light").setZ((float) (3f * Math.sin(angle)));
		
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