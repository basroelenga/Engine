package engine.objects;

import camera.CameraManager;
import engine.EngineObjectManager;
import engine.EngineObjects;
import fbo.FrameBufferObjectManager;
import graphics.Texture;
import light.LightManager;
import light.LightObject;
import matrices.MatrixObjectManager;
import shaders.ShaderManager;
import utils.DrawShapes;

public class Rectangle extends EngineObjects{
	
	// 3D space
	public Rectangle(String name, Texture tex, float x, float y, float z, float xs, float ys, float zs)
	{
		
		this.name = name;
		
		this.x = x;
		this.y = y;
		this.z = z;
		
		this.xs = xs;
		this.ys = ys;
		this.zs = zs;
		
		this.tex = tex;
		depthTex = new Texture("depthtex", FrameBufferObjectManager.getFrameBuffer("dir").getDepthTexID());
		
		textureMap.put("mTexture", tex);
		textureMap.put("dTexture", depthTex);
		
		projectionMatrix = MatrixObjectManager.getMatrixObject("projectionMatrixDefault").getMatrix();
		vaoID = EngineObjectManager.getQuad().getVaoID();
		
		viewMatrix = CameraManager.getCamera("cam").getViewMatrix();
		shader = ShaderManager.getShader("light");
	}
	
	// GUI/debug
	public Rectangle(String name, Texture tex, float x, float y, float xs, float ys)
	{
		
		this.name = name;
		
		this.x = x;
		this.y = y;
		
		this.xs = xs;
		this.ys = ys;
		
		this.tex = tex;
		
		projectionMatrix = MatrixObjectManager.getMatrixObject("orthographicMatrixDefault").getMatrix();
		vaoID = EngineObjectManager.getQuad().getVaoID();
				
		if(tex == null)	shader = ShaderManager.getShader("basic");
		else shader = ShaderManager.getShader("basictex");
	}
	
	public void update()
	{
		
		modelMatrix.setIdentity();
		
		modelMatrix.translate(x, y, z);
		modelMatrix.rotateQ(xa, ya, za, false);
		modelMatrix.scale(xs, ys, zs);
	}
	
	@Override
	public void prerender() {
		
		if(renderDepthMap)
		{
			
			// Update the FBO depth map of every directional light
			for(LightObject dLight : LightManager.getDirectionalLightList())
			{
				
				if(dLight.isRenderShadows())
				{
					
					depthShader.uploadMatrix4f(modelMatrix, depthShader.getModelMatrixLoc());
					depthShader.uploadMatrix4f(dLight.getViewLightMatrix(), depthShader.getViewMatrixLoc());
					depthShader.uploadMatrix4f(dLight.getProjectionLightMatrix(), depthShader.getProjectionMatrixLoc());

					DrawShapes.drawQuad(depthShader, dLight.getDepthBuffer(), vaoID);
				}
			}
		}
	}
	
	public void render()
	{

		shader.uploadMatrix4f(modelMatrix, shader.getModelMatrixLoc());
		shader.uploadMatrix4f(viewMatrix, shader.getViewMatrixLoc());
		shader.uploadMatrix4f(projectionMatrix, shader.getProjectionMatrixLoc());
		
		// Also upload the matrices for the depth texture
		shader.uploadMatrix4f(LightManager.getDirectionalLightList().get(0).getViewLightMatrix(), shader.getLightViewMatrixLoc());
		shader.uploadMatrix4f(LightManager.getDirectionalLightList().get(0).getProjectionLightMatrix(), shader.getLightProjectionMatrixLoc());
		shader.uploadMatrix4f(LightManager.getBiasMatrix(), shader.getBiasMatrixLoc());	
		
		shader.uploadVector4f(RGBAcolor, shader.getRgbaColorLoc());
		
		if(tex == null) DrawShapes.drawQuad(shader, fbo, vaoID);
		else if(textureMap.size() == 0) DrawShapes.drawQuad(shader, tex, fbo, vaoID);
		//else DrawShapes.drawQuadNormal(shader, textureMap, fbo, vaoID);
	}
}