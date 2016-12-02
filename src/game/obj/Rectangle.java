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

	private Matrix4f modelMatrix;

	private float x;
	private float y;
	private float z;
	
	private Vector4f color;
	
	private float angle = 0f;
	
	public int vaoID;
	
	public Rectangle(float x, float y, float z, float Ry, Vector4f color)
	{
		
		this.x = x;
		this.y = y;
		this.z = z;
		
		this.angle = Ry;
		this.color = color;
		
		modelMatrix = new Matrix4f();
		modelMatrix.transelate(0f, 0f, -2f);
		modelMatrix.rotateQ(0f, 0f, 0f, false);
		modelMatrix.scale(1f, 1f, 1f);
		
		ArrayList<Vector3f> points = new ArrayList<Vector3f>();
		
		points.add(new Vector3f(-0.5f, -0.5f, 0.0f));
		points.add(new Vector3f(0.5f, -0.5f, 0.0f));
		points.add(new Vector3f(0.5f, 0.5f, 0.0f));
		points.add(new Vector3f(-0.5f, 0.5f, 0.0f));
		
		Quad quad = new Quad(points, true);
		vaoID = quad.getVaoID();
	}
	
	public void update()
	{
		
		modelMatrix.setIdentity();
		
		modelMatrix.transelate(x, y, z);
		modelMatrix.rotateQ(0f, angle, 0f, false);
		modelMatrix.scale(1f, 1f, 1f);
	}
	
	public void render()
	{
		
		ShaderManager.getShader("basic").uploadMatrices(modelMatrix, Engine.projMatrix, Camera.getViewMatrix());
		ShaderManager.getShader("basic").uploadColor(color);
		
		DrawShapes.drawQuad(ShaderManager.getShader("basic") ,vaoID, 2);
	}
}