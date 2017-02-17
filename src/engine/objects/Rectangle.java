package engine.objects;

import cam.Camera;
import engine.Engine;
import engine.EngineObjectManager;
import engine.EngineObjects;
import graphics.Texture;
import graphics.TextureManager;
import math.Matrix4f;
import math.Vector4f;
import shaders.ShaderManager;
import utils.DrawShapes;

public class Rectangle extends EngineObjects{
	
	/**
	 * This function creates a rectangle. This can be specified in both perspective and orthogonal space.
	 * The position and scaling should be based on the projection matrix which is used.
	 * Call update and render function of the object to get it to function.
	 * @param x Position in x-space.
	 * @param y Position in y-space.
	 * @param z Position in z-space.
	 * @param sx Scaling in x-space.
	 * @param sy Scaling in y-space.
	 * @param sz Scaling in z-space.
	 * @param angleY Rotation around the y-axis.
	 * @param projection The projection matrix to use.
	 * @param color The color of the rectangle (RGBA).
	 */
	public Rectangle(String name, Texture tex, float x, float y, float z, float xs, float ys, float zs, Matrix4f projection, Vector4f RGBAcolor)
	{
		
		this.name = name;
		
		this.x = x;
		this.y = y;
		this.z = z;
		
		this.xs = xs;
		this.ys = ys;
		this.zs = zs;
		
		this.projectionMatrix = projection;
		
		this.RGBAcolor = RGBAcolor;
		
		// Define the shader to be used
		shader = ShaderManager.getShader("basic");
		
		// If the projection matrix is the perspective matrix the view matrix should also be set.
		if(projectionMatrix == Engine.projMatrix) viewMatrix = Camera.getViewMatrix();
		else viewMatrix = new Matrix4f();

		this.tex = tex;
		
		quad = EngineObjectManager.getQuad();
	}
	
	public void update()
	{
		
		modelMatrix.setIdentity();
		
		modelMatrix.translate(x, y, z);
		modelMatrix.rotateQ(xa, ya, za, false);
		modelMatrix.scale(xs, ys, zs);
	}
	
	public void render()
	{

		shader.uploadMatrix4f(modelMatrix, shader.getModelMatrixLoc());
		shader.uploadMatrix4f(projectionMatrix, shader.getProjectionMatrixLoc());
		shader.uploadMatrix4f(viewMatrix, shader.getViewMatrixLoc());
		
		shader.uploadVector4f(RGBAcolor, shader.getRgbaColorLoc());
		
		if(tex == null) DrawShapes.drawQuad(shader, quad, fbo);
		else DrawShapes.drawQuad(shader, quad, tex, fbo);
	}
}