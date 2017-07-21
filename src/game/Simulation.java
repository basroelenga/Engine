package game;

import camera.CameraManager;
import engine.EngineObjectManager;
import fbo.FrameBufferObjectManager;
import light.LightManager;
import math.Vector3f;
import models.ModelManager;
import postprocess.PostProcessEffectManager;

public class Simulation {
		
	private float angle = 0f;
	
	public Simulation()
	{
		
		CameraManager.addPlayerCamera("playercam", 0f, 0f, -2f, 0f, 0f, 0f);
		// =========================
		
		// Set up post process effect
		PostProcessEffectManager.addPostProcessEffect("testeffect");
		
		LightManager.addDirectionalLight("directional_shadow", CameraManager.getPlayerCamera("playercam"), 0, 1, 0, new Vector3f(1.0f, 0.0f, 0.0f));
		//LightManager.addPointLight("point", 0, 0, -5, new Vector3f(0.0f, 1.0f, 0.0f), true);
		PostProcessEffectManager.disable();
		addScene();

		LightManager.getDirectionalLight("directional_shadow").setRenderShadowMap(false);
	}
	
	private void addScene()
	{
		
		// Add a model
		EngineObjectManager.addTO("bunny", ModelManager.getModel("bunny"));
		EngineObjectManager.getEngineObject("bunny").setRenderDepthMap(true);
		EngineObjectManager.getEngineObject("bunny").setY(-1f);
		EngineObjectManager.getEngineObject("bunny").setXs(0.5f);
		EngineObjectManager.getEngineObject("bunny").setYs(0.5f);
		EngineObjectManager.getEngineObject("bunny").setZs(0.5f);
		
		// Add the surface
		EngineObjectManager.addTO("surface", ModelManager.getModel("surface"));
		EngineObjectManager.getEngineObject("surface").setRenderDepthMap(true);
		EngineObjectManager.getEngineObject("surface").setY(-1.3f);
		EngineObjectManager.getEngineObject("surface").setXs(5f);
		EngineObjectManager.getEngineObject("surface").setZs(5f);
		
		// Add the rotating plane
		EngineObjectManager.addTO("plane", ModelManager.getModel("surface"));
		EngineObjectManager.getEngineObject("plane").setRenderDepthMap(true);
		EngineObjectManager.getEngineObject("plane").setXa(90f);
		EngineObjectManager.getEngineObject("plane").setZ(-2f);
		EngineObjectManager.getEngineObject("plane").setXs(0.2f);
		EngineObjectManager.getEngineObject("plane").setZs(0.2f);
		
		EngineObjectManager.addSkybox();
		
		EngineObjectManager.getEngineObject("bunny").setFbo(FrameBufferObjectManager.getFrameBuffer("testeffect"));
		EngineObjectManager.getEngineObject("surface").setFbo(FrameBufferObjectManager.getFrameBuffer("testeffect"));
		EngineObjectManager.getEngineObject("plane").setFbo(FrameBufferObjectManager.getFrameBuffer("testeffect"));
	}
	
	/**
	 * The game update function, changes to objects can be made here
	 */
	public void update()
	{
		
		angle += 0.25f;
		EngineObjectManager.getEngineObject("bunny").setYa(angle);
		
		EngineObjectManager.getEngineObject("plane").setXa(90 + angle);
		LightManager.getDirectionalLight("directional_shadow").setxDir(-angle / 1000);
		
		//MatrixObjectManager.getMatrixObject("projectionMatrixDefault").getMatrix().print();
	}
}