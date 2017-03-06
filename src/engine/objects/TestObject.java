package engine.objects;

import java.util.ArrayList;

import camera.CameraManager;
import engine.EngineObjects;
import fbo.FrameBufferObjectManager;
import graphics.Texture;
import graphics.TextureManager;
import light.LightManager;
import light.LightObject;
import math.Matrix4f;
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
		
		vaoID = ModelManager.getModel("monkey").getVaoID();
		amountOfTriangles = ModelManager.getModel("monkey").getVertices();
		
		tex = TextureManager.getTexture("testtex");
		depthTex = new Texture("depthtex", FrameBufferObjectManager.getFrameBuffer("dir").getDepthTexID());
		
		viewMatrix = CameraManager.getCamera("cam").getViewMatrix();
		projectionMatrix = MatrixObjectManager.getMatrixObject("projectionMatrixDefault").getMatrix();
	}
	
	@Override
	public void update() 
	{
	
		modelMatrix.setIdentity();
		
		modelMatrix.translate(x, y, z);
		modelMatrix.scale(0.5f, 0.5f, 0.5f);
		modelMatrix.rotateEulerY(ya);
	}
	
	@Override
	public void render() 
	{
		
		shader.uploadMatrix4f(modelMatrix, shader.getModelMatrixLoc());
		shader.uploadMatrix4f(projectionMatrix, shader.getProjectionMatrixLoc());
		
		// Also upload the matrices for the depth texture
		shader.uploadMatrix4f(LightManager.getDirectionalLightList().get(0).getViewLightMatrix(), shader.getLightViewMatrixLoc());
		shader.uploadMatrix4f(LightManager.getDirectionalLightList().get(0).getProjectionLightMatrix(), shader.getLightProjectionMatrixLoc());
		shader.uploadMatrix4f(LightManager.getBiasMatrix(), shader.getBiasMatrixLoc());		
		
		shader.uploadVector4f(new Vector4f(1.0f, 1.0f, 1.0f, 1.0f), shader.getRgbaColorLoc());
		
		// Both textures need to be uploaded
		ArrayList<Texture> texList = new ArrayList<Texture>();
		
		texList.add(tex);
		texList.add(depthTex);
				
		DrawShapes.drawModel(shader, fbo, texList, vaoID, amountOfTriangles);
	}
	
	@Override
	public void prerender()
	{
		
		if(renderDepthMap)
		{
			
			// Update the FBO of every directional light
			for(LightObject dLight : LightManager.getDirectionalLightList())
			{
				
				if(dLight.isRenderShadows())
				{
					
					depthShader.uploadMatrix4f(modelMatrix, depthShader.getModelMatrixLoc());
					depthShader.uploadMatrix4f(dLight.getViewLightMatrix(), depthShader.getViewMatrixLoc());
					depthShader.uploadMatrix4f(dLight.getProjectionLightMatrix(), depthShader.getProjectionMatrixLoc());
					
					DrawShapes.drawModel(depthShader, dLight.getDepthBuffer(), vaoID, amountOfTriangles);
				}
			}
		}
	}
}