package engine.objects;

import camera.CameraManager;
import engine.EngineObjects;
import math.Vector4f;
import matrices.MatrixObjectManager;
import models.ModelManager;
import shaders.ShaderManager;
import utils.DrawShapes;

public class TestObject extends EngineObjects{
		
	public TestObject()
	{
		
		name = "bunny";
		
		shader = ShaderManager.getShader("light");
		model = ModelManager.getModel("monkey");
		
		viewMatrix = CameraManager.getCamera("cam").getViewMatrix();
		projectionMatrix = MatrixObjectManager.getMatrixObject("projectionMatrixDefault").getMatrix();
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
		
		// Render the object to the depth buffer
		if(renderDepthMap)
		{
			
			depthShader.uploadMatrix4f(modelMatrix, depthShader.getModelMatrixLoc());
			depthShader.uploadMatrix4f(projectionMatrix, depthShader.getProjectionMatrixLoc());
			
			DrawShapes.drawModel(depthShader, model, depthBuffer);
		}
	}
}