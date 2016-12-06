package engine.primitives;

import java.util.ArrayList;

import cam.Camera;
import engine.Engine;
import engine.EngineObjects;
import math.Matrix4f;
import math.Vector3f;
import math.Vector4f;
import shaders.ShaderManager;
import shapes.Quad;
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
	public Rectangle(float x, float y, float z, float xs, float ys, float zs, float ya, Matrix4f projection, Vector4f RGBAcolor)
	{
		
		this.x = x;
		this.y = y;
		this.z = z;
		
		this.xs = xs;
		this.ys = ys;
		this.zs = zs;
		
		this.projectionMatrix = projection;
		
		this.ya = ya;
		this.RGBAcolor = RGBAcolor;

		viewMatrix = new Matrix4f(); 
		
		// Define the shader to be used
		shader = ShaderManager.getShader("basic");
		
		// If the projection matrix is the perspective matrix the view matrix should also be set.
		if(projectionMatrix == Engine.projMatrix) viewMatrix = Camera.getViewMatrix();
		
		// Create the points in the rectangle.
		ArrayList<Vector3f> points = new ArrayList<Vector3f>();
		
		points.add(new Vector3f(0f, 0f, 0.0f));
		points.add(new Vector3f(1f, 0f, 0.0f));
		points.add(new Vector3f(1f, 1f, 0.0f));
		points.add(new Vector3f(0f, 1f, 0.0f));
		
		// Create the quad which represents the rectangle as a cube/box.
		Quad quad = new Quad(points, true);
		vaoID = quad.getVaoID();
	}
	
	public void update()
	{
		
		modelMatrix.setIdentity();
		
		modelMatrix.transelate(x, y, z);
		modelMatrix.rotateQ(xa, ya, za, false);
		modelMatrix.scale(xs, ys, zs);
	}
	
	public void render()
	{
		
		shader.uploadMatrices(modelMatrix, projectionMatrix, viewMatrix, normalMatrix);
		shader.uploadColor(RGBAcolor);
		
		DrawShapes.drawQuad(shader ,vaoID, 2);
	}
}