package game;

import cam.Camera;
import engine.Engine;
import engine.EngineObjectManager;
import fbo.FrameBufferObjectManager;
import light.LightManager;
import math.Vector3f;
import math.Vector4f;
import matrices.MatrixObjectManager;
import postprocessing.ShadowManager;

public class Simulation {
	
	public static Camera cam;
	
	private float angle = 0f;
	
	public Simulation()
	{
		
		cam = new Camera();
		// =========================
		
		LightManager.addDirectionalLight("dir", cam, 0, 1, -1, new Vector3f(1.0f, 0.0f, 0.0f));
		
		// Add a model
		EngineObjectManager.addBunny();
		EngineObjectManager.getEngineObject("bunny").setRenderDepthMap(true);
		
		// Add a surface
		EngineObjectManager.addRectangle("surface", null, -5f, -1f, 5f, 10f, 10f, 0f, MatrixObjectManager.getMatrixObject("projectionMatrixDefault").getMatrix(), new Vector4f(1.0f, 1.0f, 1.0f, 1.0f));
		EngineObjectManager.getEngineObject("surface").setRenderDepthMap(true);
		
		EngineObjectManager.getEngineObject("surface").setXa(90f);
		
		ShadowManager.setDepthFBO(FrameBufferObjectManager.getFrameBuffer("shadow"));
	}
	
	/**
	 * The game update function, changes to objects can be made here
	 */
	public void update()
	{
		
		angle += 0.025f;

		cam.update();
	}
}