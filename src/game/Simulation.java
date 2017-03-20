package game;

import camera.CameraManager;
import engine.EngineObjectManager;
import light.LightManager;
import math.Vector3f;
import models.ModelManager;

public class Simulation {
		
	private float angle = 0f;
	
	public Simulation()
	{
		
		CameraManager.addCamera("cam", new Vector3f(0, 0, -2), new Vector3f());
		// =========================
		
		LightManager.addDirectionalLight("dir", CameraManager.getCamera("cam"), 0, 1, -1, new Vector3f(1.0f, 0.0f, 0.0f));
		LightManager.setBiasMatrix();
		
		// Add a model
		EngineObjectManager.addTO("bunny", ModelManager.getModel("bunny"));
		EngineObjectManager.getEngineObject("bunny").setRenderDepthMap(true);
		EngineObjectManager.getEngineObject("bunny").setY(-1f);
		EngineObjectManager.getEngineObject("bunny").setXs(0.5f);
		EngineObjectManager.getEngineObject("bunny").setYs(0.5f);
		EngineObjectManager.getEngineObject("bunny").setZs(0.5f);
		
		EngineObjectManager.addTO("surface", ModelManager.getModel("surface"));
		EngineObjectManager.getEngineObject("surface").setRenderDepthMap(true);
		EngineObjectManager.getEngineObject("surface").setY(-1.3f);
		EngineObjectManager.getEngineObject("surface").setXs(5f);
		EngineObjectManager.getEngineObject("surface").setZs(5f);
		
		EngineObjectManager.addTO("plane", ModelManager.getModel("surface"));
		EngineObjectManager.getEngineObject("plane").setRenderDepthMap(true);
		EngineObjectManager.getEngineObject("plane").setXa(90f);
		EngineObjectManager.getEngineObject("plane").setZ(-2f);
		EngineObjectManager.getEngineObject("plane").setXs(0.2f);
		EngineObjectManager.getEngineObject("plane").setZs(0.2f);
		
		//EngineObjectManager.getEngineObject("surface").getModel().printInfo();
		
		// Add a surface
		/**
		EngineObjectManager.addRectangle("surface", null, -5f, -1f, 5f, 10f, 10f, 0f);
		EngineObjectManager.getEngineObject("surface").setRenderDepthMap(true);
		
		EngineObjectManager.getEngineObject("surface").setXa(90f);
		
		EngineObjectManager.addRectangle("test", null, -2, 0, 0, 1, 1, 0);
		EngineObjectManager.getEngineObject("test").setRenderDepthMap(true);
		*/
	}
	
	/**
	 * The game update function, changes to objects can be made here
	 */
	public void update()
	{
		
		angle += 0.25f;
		EngineObjectManager.getEngineObject("bunny").setYa(angle);
		
		EngineObjectManager.getEngineObject("plane").setXa(90 + angle);

	}
}