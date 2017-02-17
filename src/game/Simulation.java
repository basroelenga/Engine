package game;

import cam.Camera;
import engine.Engine;
import engine.EngineObjectManager;
import fbo.FrameBufferObjectManager;
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
		
		LightManager.addDirectionalLight("dir", 0, 1, -1, new Vector3f(1.0f, 0.0f, 0.0f));
		
		// Add a model
		EngineObjectManager.addBunny();
		
		// Add a surface
		EngineObjectManager.addRectangle("surface", null, -5f, -1f, 5f, 10f, 10f, 0f, Engine.projMatrix, new Vector4f(1.0f, 1.0f, 1.0f, 1.0f));
		
		EngineObjectManager.getEngineObject("surface").setXa(90f);
		
		// Add a shadow buffer
		FrameBufferObjectManager.addShadowFrameBufferObject("shadow", 1024, 1024);
	}
	
	
	public void update()
	{
		
		angle += 0.025f;
		
		cam.update();
	}
}