package game.obj;

import cam.Camera;
import game.Simulation;
import generation.TerrainGenerator;
import math.Matrix4f;
import math.Vector4f;
import shaders.ShaderManager;
import utils.DrawShapes;

public class TerrainObj {

	private Matrix4f modelMatrix;

	private float x;
	private float y;
	private float z;
	
	private int[] vaoID;
	
	public TerrainObj(float x, float y, float z)
	{
		
		this.x = x;
		this.y = y;
		this.z = z;
		
		modelMatrix = new Matrix4f();
		
		vaoID = TerrainGenerator.generateTerrainV2(256, 256);
	}
	
	public void update()
	{
		
		modelMatrix.setIdentity();
		
		modelMatrix.transelate(x, y, z);
		modelMatrix.scale(1f, 1f, 1f);
	}
	
	public void render()
	{
		
		ShaderManager.getShader("basic").uploadMatrices(modelMatrix, Simulation.projMatrix, Camera.getViewMatrix());
		ShaderManager.getShader("basic").uploadColor(new Vector4f(1, 1, 1, 1));
		
		DrawShapes.drawQuad(ShaderManager.getShader("basic") ,vaoID[0], vaoID[1]);
	}
}