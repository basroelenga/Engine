package game;

import cam.Camera;
import engine.EngineObjectManager;
import graphics.TextureManager;
import light.LightManager;
import math.Vector3f;
import shaders.ShaderManager;

public class Simulation {

	public static Camera cam;
	
	private float angle = 0f;
	
	public Simulation()
	{
		
		cam = new Camera();
		// =========================
		EngineObjectManager.addSphere(20, 0, 0, 0, 50, TextureManager.getTexture("bg"), false);
		
		LightManager.addPointLight("light1", 0f, 2f, 0f, new Vector3f(1, 1, 0), true);
		LightManager.addPointLight("light2", 0f, 2f, 0f, new Vector3f(0, 1, 0), true);
		
		LightManager.addPointLight("light3", 0f, -2f, 0f, new Vector3f(1, 0, 1), true);
		LightManager.addPointLight("light4", 0f, -2f, 0f, new Vector3f(1, 0, 0), true);
		
		LightManager.addPointLight("light5", 0f, 4f, 0f, new Vector3f(0, 0, 1), true);
		LightManager.addPointLight("light6", 0f, -4f, 0f, new Vector3f(1, 0, 1), true);
		
		// All shaders are construct after it is known how many lights there are
		ShaderManager.loadLightShaders();
		
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
		
		LightManager.getLight("light1").setX((float) (3f * Math.cos(angle)));
		LightManager.getLight("light1").setZ((float) (3f * Math.sin(angle)));
		
		LightManager.getLight("light2").setX((float) (3f * Math.cos(angle + 180f)));
		LightManager.getLight("light2").setZ((float) (3f * Math.sin(angle + 180f)));
		
		LightManager.getLight("light3").setX((float) (3f * Math.cos(angle)));
		LightManager.getLight("light3").setZ((float) (3f * Math.sin(angle)));
		
		LightManager.getLight("light4").setX((float) (3f * Math.cos(angle + 180f)));
		LightManager.getLight("light4").setZ((float) (3f * Math.sin(angle + 180f)));
		
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