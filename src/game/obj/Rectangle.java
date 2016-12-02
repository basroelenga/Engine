package game.obj;

import java.util.ArrayList;

import cam.Camera;
import engine.Engine;
import math.Matrix4f;
import math.Vector3f;
import math.Vector4f;
import shaders.ShaderManager;
import shapes.Quad;
import utils.DrawShapes;

public class Rectangle {

	private float x;
	private float y;
	private float z;
	
	private float sx;
	private float sy;
	private float sz;
	
	private Matrix4f modelMatrix;
	
	private Matrix4f viewMatrix;
	private Matrix4f projectionMatrix;
	
	private Vector4f color;
	
	private float angleY = 0f;
	
	public int vaoID;
	
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
	public Rectangle(float x, float y, float z, float sx, float sy, float sz, float angleY, Matrix4f projection, Vector4f color)
	{
		
		this.x = x;
		this.y = y;
		this.z = z;
		
		this.sx = sx;
		this.sy = sy;
		this.sz = sz;
		
		this.projectionMatrix = projection;
		
		this.angleY = angleY;
		this.color = color;
		
		modelMatrix = new Matrix4f();
		viewMatrix = new Matrix4f(); 
		
		// If the projection matrix is the perspective matrix the view matrix should also be set.
		if(projectionMatrix == Engine.projMatrix) viewMatrix = Camera.getViewMatrix();
		
		// Create the points in the rectangle.
		ArrayList<Vector3f> points = new ArrayList<Vector3f>();
		
		points.add(new Vector3f(-0.5f, -0.5f, 0.0f));
		points.add(new Vector3f(0.5f, -0.5f, 0.0f));
		points.add(new Vector3f(0.5f, 0.5f, 0.0f));
		points.add(new Vector3f(-0.5f, 0.5f, 0.0f));
		
		// Create the quad which represents the rectangle as a cube/box.
		Quad quad = new Quad(points, true);
		vaoID = quad.getVaoID();
	}
	
	public void update()
	{
		
		modelMatrix.setIdentity();
		
		modelMatrix.transelate(x, y, z);
		modelMatrix.rotateQ(0f, angleY, 0f, false);
		modelMatrix.scale(sx, sy, sz);
	}
	
	public void render()
	{
		
		ShaderManager.getShader("basic").uploadMatrices(modelMatrix, projectionMatrix, viewMatrix);
		ShaderManager.getShader("basic").uploadColor(color);
		
		DrawShapes.drawQuad(ShaderManager.getShader("basic") ,vaoID, 2);
	}
}