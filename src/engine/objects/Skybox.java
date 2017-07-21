package engine.objects;

import camera.CameraManager;
import engine.EngineObjects;
import fbo.FrameBufferObjectManager;
import math.Vector4f;
import matrices.MatrixObjectManager;
import shaders.ShaderManager;
import shapes.Box;
import utils.DrawShapes;

public class Skybox extends EngineObjects{

	public Skybox()
	{
		
		shader = ShaderManager.getShader("light");
		fbo = FrameBufferObjectManager.getFrameBuffer("testeffect");
		
		Box box = new Box();
		
		vaoID = box.getVaoID();
		amountOfTriangles = 36;
		
		viewMatrix = CameraManager.getPlayerCamera("playercam").getViewMatrix();
		projectionMatrix = MatrixObjectManager.getMatrixObject("projectionMatrixDefault").getMatrix();

		RGBAcolor = new Vector4f(1.0f, 0.0f, 0.0f, 1.0f);
		renderDepthMap = true;
	}

	@Override
	public void update() 
	{
	
		modelMatrix.setIdentity();
		
		modelMatrix.translate(2f, 0f, 0f);
		modelMatrix.scale(1f, 1f, 1f);
	}

	@Override
	public void render() 
	{
		
		// Always upload the model, view and projection matrix
		shader.uploadMatrix4f(modelMatrix, shader.getModelMatrixLoc());
		shader.uploadMatrix4f(viewMatrix, shader.getViewMatrixLoc());
		shader.uploadMatrix4f(projectionMatrix, shader.getProjectionMatrixLoc());
		
		shader.uploadVector4f(RGBAcolor, shader.getRgbaColorLoc());
		
		DrawShapes.drawModel(shader, fbo, vaoID, amountOfTriangles);
	}	
}