package engine.objects;

import cam.Camera;
import engine.Engine;
import engine.EngineObjects;
import math.Vector4f;
import models.Model;
import models.ModelManager;
import shaders.ShaderManager;
import utils.DrawShapes;

public class TestObject extends EngineObjects{
	
	private Model model;
	
	public TestObject()
	{
		
		shader = ShaderManager.getShader("light");
		model = ModelManager.getModel("monkey");
		
		viewMatrix = Camera.getViewMatrix();
		projectionMatrix = Engine.projMatrix;
	}

	@Override
	public void update() 
	{
	
		modelMatrix.setIdentity();
		
		modelMatrix.scale(0.5f, 0.5f, 0.5f);
	}

	@Override
	public void render() 
	{
		
		shader.uploadMatrix4f(modelMatrix, shader.getModelMatrixLoc());
		shader.uploadMatrix4f(projectionMatrix, shader.getProjectionMatrixLoc());
		
		shader.uploadVector4f(new Vector4f(1.0f, 1.0f, 1.0f, 1.0f), shader.getRgbaColorLoc());
		
		DrawShapes.drawModel(shader, model, fbo);
	}
}