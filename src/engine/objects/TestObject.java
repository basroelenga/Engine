package engine.objects;

import static org.lwjgl.opengl.GL11.*;

import camera.CameraManager;
import engine.EngineObjects;
import fbo.FrameBufferObjectManager;
import graphics.Texture;
import graphics.TextureManager;
import light.LightManager;
import light.LightObject;
import math.Vector4f;
import matrices.MatrixObjectManager;
import models.Model;
import shaders.ShaderManager;
import utils.DrawShapes;

public class TestObject extends EngineObjects{
	
	public TestObject(String name, Model model)
	{
		
		this.name = name;
		this.model = model;
		
		shader = ShaderManager.getShader("light");
		
		vaoID = model.getVaoID();
		amountOfTriangles = model.getVertices();
		
		tex = TextureManager.getTexture("testtex");
		depthTex = new Texture("depthtex", FrameBufferObjectManager.getFrameBuffer("directional_shadow").getDepthTexID());
		
		textureMap.put("mTexture", null);
		textureMap.put("dTexture", depthTex);
		
		viewMatrix = CameraManager.getCamera("cam").getViewMatrix();
		projectionMatrix = MatrixObjectManager.getMatrixObject("projectionMatrixDefault").getMatrix();
	}
	
	@Override
	public void update() 
	{
	
		modelMatrix.setIdentity();
		
		modelMatrix.translate(x, y, z);
		modelMatrix.scale(xs, ys, zs);
		modelMatrix.rotateEulerY(ya);
		modelMatrix.rotateEulerZ(za);
		modelMatrix.rotateEulerX(xa);
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
				
		DrawShapes.drawModel(this, shader, fbo, textureMap, vaoID, amountOfTriangles);
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