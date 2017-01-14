package game;

import cam.Camera;
import engine.Engine;
import engine.EngineObjectManager;
import engine.EngineSystemManager;
import graphics.TextureManager;
import light.LightManager;
import math.Vector3f;
import math.Vector4f;

public class Simulation {
	
	public static Camera cam;
	
	private float angle = 0f;
	
	public Simulation()
	{
		
		cam = new Camera();
		// =========================
		EngineObjectManager.addSphere("sphere1", 5, 0, 0, 0, 50, TextureManager.getTexture("bg"), false);
		
		LightManager.addPointLight("light1", 0f, 2f, 0f, new Vector3f(1, 0, 0), true);
		LightManager.addPointLight("light2", 0f, 2f, 0f, new Vector3f(0, 1, 0), true);
		
		LightManager.addPointLight("light3", 0f, -2f, 0f, new Vector3f(1, 0, 1), true);
		LightManager.addPointLight("light4", 0f, -2f, 0f, new Vector3f(0, 1, 1), true);
		
		LightManager.addDirectionalLight("dir", 0, 1, 0, new Vector3f(1, 1, 1));
		//LightManager.addSpotLight("spot", 0, 5, 0, 0, -1, 0, 45, new Vector3f(1, 1, 1));
		
		EngineObjectManager.addRectangle("rect1", 0, 2, 0, 1, 1, 1, 0, Engine.projMatrix, new Vector4f(1, 1, 1, 1));
		//EngineSystemManager.addUniformParticleSystem("p1", "quad", 0, 1, 0, 180);
		
		createParticles();
	}
	
	private void createParticles()
	{
		
		int particles = 16;
		
		for(int i = -particles / 2; i < particles / 2; i++)
		{
			for(int j = -particles / 2; j < particles / 2; j++)
			{
				EngineObjectManager.addParticle(i, 0, j, 0, 0, 0, 0.2f, 1, "sphere", TextureManager.getTexture("testtex"));
			}
		}
	}
	
	public void update()
	{
		angle += 0.05f;
		
		LightManager.getLight("light1").setX((float) (3f * Math.cos(angle)));
		LightManager.getLight("light1").setZ((float) (3f * Math.sin(angle)));
		
		LightManager.getLight("light2").setX((float) (6f * Math.cos(angle + 180f)));
		LightManager.getLight("light2").setZ((float) (3f * Math.sin(angle + 180f)));
		
		LightManager.getLight("light3").setX((float) (3f * Math.cos(angle)));
		LightManager.getLight("light3").setZ((float) (6f * Math.sin(angle)));
		
		LightManager.getLight("light4").setX((float) (3f * Math.cos(angle + 180f)));
		LightManager.getLight("light4").setZ((float) (3f * Math.sin(angle + 180f)));
		
		EngineObjectManager.getEngineObject("rect1").setYa(angle);;
		
		cam.update();
		
		EngineObjectManager.update();
		EngineSystemManager.update();
		
		LightManager.update();
	}
	
	public void render()
	{
		
		EngineObjectManager.render();
		EngineSystemManager.render();
				
		LightManager.render();
	}
}