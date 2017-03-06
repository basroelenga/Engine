package game;

import camera.CameraManager;
import engine.EngineObjectManager;
import light.LightManager;
import math.Vector3f;

public class Simulation {
		
	private float angle = 0f;
	
	public Simulation()
	{
		
		CameraManager.addCamera("cam", new Vector3f(0, 0, -2), new Vector3f());
		// =========================
		
		LightManager.addDirectionalLight("dir", CameraManager.getCamera("cam"), 0, 1, -1, new Vector3f(1.0f, 0.0f, 0.0f));
		LightManager.setBiasMatrix();
		
		// Add a model
		EngineObjectManager.addBunny();
		EngineObjectManager.getEngineObject("bunny").setRenderDepthMap(true);
		
		// Add a surface
		EngineObjectManager.addRectangle("surface", null, -5f, -1f, 5f, 10f, 10f, 0f);
		EngineObjectManager.getEngineObject("surface").setRenderDepthMap(true);
		
		EngineObjectManager.getEngineObject("surface").setXa(90f);
	}
	
	/**
	 * The game update function, changes to objects can be made here
	 */
	public void update()
	{
		
		angle += 0.25f;
		EngineObjectManager.getEngineObject("bunny").setYa(angle);
	}
}