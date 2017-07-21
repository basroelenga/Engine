package engine.objects;

import camera.CameraManager;
import engine.EngineObjectManager;
import engine.EngineObjects;
import fbo.FrameBufferObjectManager;
import graphics.Texture;
import light.LightManager;
import math.Vector4f;
import matrices.MatrixObjectManager;
import shaders.ShaderManager;
import utils.DrawShapes;

public class Rectangle extends EngineObjects{
	
	/**
	 * Create a rectangle in 3 dimensional space
	 * @param name Identifier of the rectangle
	 * @param tex Texture applied to the rectangle (can be null)
	 * @param x x-coordinate of the rectangle
	 * @param y y-coordinate of the rectangle
	 * @param z z-coordinate of the rectangle
	 * @param xs scaling in x direction
	 * @param ys scaling in y direction
	 * @param zs scaling in z direction
	 */
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
		
		// Obtain the depth texture ID
		depthTex = new Texture("depthtex", FrameBufferObjectManager.getFrameBuffer("dir").getTextureID());
		
		// Put the different texture in the map
		textureMap.put("mTexture", tex);
		textureMap.put("dTexture", depthTex);
		
		// Use the pre-render phase
		renderDepthMap = true;
		
		// Use the perspective projection matrix for the 3 dimensional rectangle
		projectionMatrix = MatrixObjectManager.getMatrixObject("projectionMatrixDefault").getMatrix();
		vaoID = EngineObjectManager.getQuad().getVaoID();
		
		// Set up shaders and other matrices
		viewMatrix = CameraManager.getPlayerCamera("playercam").getViewMatrix();
		shader = ShaderManager.getShader("light");
	}
	
	/**
	 * Create a rectangle in 2 dimensional space, this rectangle should be used for GUI or the debug menu
	 * This means that a shadow framebuffer is not required
	 * @param name Identifier of the rectangle
	 * @param tex Texture applied to the rectangle (can be null)
	 * @param x x-coordinate of the rectangle
	 * @param y y-coordinate of the rectangle
	 * @param xs scaling in x direction
	 * @param ys scaling in y direction
	 */
	public Rectangle(String name, Texture tex, float x, float y, float xs, float ys, Vector4f color)
	{
		
		this.name = name;
		
		this.x = x;
		this.y = y;
		
		this.xs = xs;
		this.ys = ys;
		
		this.tex = tex;
		this.RGBAcolor = color;
		
		// No pre-render phase
		renderDepthMap = false;
		
		// Use the orthographic matrix for the 2 dimensional rectangle
		projectionMatrix = MatrixObjectManager.getMatrixObject("orthographicMatrixDefault").getMatrix();
		vaoID = EngineObjectManager.getQuad().getVaoID();
		
		// Set up which shader to use
		if(tex == null)	shader = ShaderManager.getShader("basic");
		else shader = ShaderManager.getShader("basictex");
	}
	
	@Override
	public void update()
	{
		
		modelMatrix.setIdentity();
		
		modelMatrix.translate(x, y, z);
		modelMatrix.rotateQ(xa, ya, za, false);
		modelMatrix.scale(xs, ys, zs);
	}
	
	/**
	 * The render phase depends if the rectangle is 2 or 3 dimensional. In the 2 dimensional case the lights are not uploaded to the shaders
	 * but they are in the 3 dimensional case. This is the same for color and the bias matrix.
	 */
	@Override
	public void render()
	{
		
		// Always upload the model, view and projection matrix
		shader.uploadMatrix4f(modelMatrix, shader.getModelMatrixLoc());
		shader.uploadMatrix4f(viewMatrix, shader.getViewMatrixLoc());
		shader.uploadMatrix4f(projectionMatrix, shader.getProjectionMatrixLoc());
		
		// Also upload the color
		shader.uploadVector4f(RGBAcolor, shader.getRgbaColorLoc());
		
		// Upload the light matrices if the a shadow needs to be rendered
		if(renderDepthMap)
		{
			
			shader.uploadMatrix4f(LightManager.getDirectionalLightList().get(0).getViewLightMatrix(), shader.getLightViewMatrixLoc());
			shader.uploadMatrix4f(LightManager.getDirectionalLightList().get(0).getProjectionLightMatrix(), shader.getLightProjectionMatrixLoc());
			shader.uploadMatrix4f(LightManager.getBiasMatrix(), shader.getBiasMatrixLoc());	
		}
		
		if(tex == null) DrawShapes.drawQuad(shader, fbo, vaoID);
		else if(textureMap.size() == 0) DrawShapes.drawQuad(shader, tex, fbo, vaoID);
		//else DrawShapes.drawQuadNormal(shader, textureMap, fbo, vaoID);
	}
}