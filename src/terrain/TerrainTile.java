package terrain;

import java.util.ArrayList;

import engine.Engine;
import generation.TerrainGenerator;
import math.MathUtils;
import math.Matrix4f;
import math.SimplexNoise;
import math.Vector3f;
import shaders.ShaderManager;
import utils.DrawShapes;

public class TerrainTile {

	private int x;
	private int z;
	
	private float[] xPos;
	private float[] zPos;
	
	private float positionNorm = 5f;
	
	private int size;
	
	private int octaves = 8;
	
	private float frequency = 0.005f;
	
	private float amplitude = 1.0f;
	private float persistence = 0.5f;
	
	private float e = 0.9f;
	
	private float[][] yData;
	private ArrayList<Vector3f> normalList;
	
	private int[] vaoID;
	
	private Matrix4f modelMatrix;
	
	public TerrainTile(int x, int z, int size)
	{
		
		this.x = x;
		this.z = z;
		
		this.size = size;
		
		modelMatrix = new Matrix4f();
		
		constructTile();
	}
	
	private void constructTile()
	{
		
		generateHeightMap();
		generateNormals();
	}
	
	private void generateHeightMap()
	{
		
		xPos = new float[size];
		zPos = new float[size];
		
		yData = new float[size][size];
		
		for(int i = 0; i < octaves; i++)
		{
			
			float tempFreq = (float) (frequency * Math.pow(2, i));
			
			for(int j = 0; j < size; j++){
				for(int k = 0; k < size; k++)
				{
					
					yData[j][k] += amplitude * ((SimplexNoise.noise2d((k + x * (size - 1)) * tempFreq, (j + z * (size - 1)) * tempFreq) + 1) * 0.5); 
				}
			}
			
			amplitude *= persistence;
		}
		
		for(int j = 0; j < size; j++){
			for(int k = 0; k < size; k++)
			{
				
				yData[j][k] = (float) Math.pow(yData[j][k], e);
			}
			
			xPos[j] = (x * (size - 1)) + j;
			zPos[j] = (z * (size - 1)) + j;
		}
	}
	
	public void generateNormals()
	{
		
		normalList = new ArrayList<Vector3f>();
		
		for(int i = 0; i < size; i++){
			for(int j = 0; j < size; j++)
			{
				
				// There are 4 exception cases, namely the four borders of every tile. For now the normal will point upwards at these edges.
				if(i == 0)
				{
					
					normalList.add(new Vector3f(0, 1, 0));
				}
				
				if(i == size - 1)
				{
					
					normalList.add(new Vector3f(0, 1, 0));
				}
				
				if(j == 0 && i != 0 && i != size - 1)
				{
					
					normalList.add(new Vector3f(0, 1, 0));
				}
				
				if(j == size - 1 && i != 0 && i != size - 1)
				{
					
					normalList.add(new Vector3f(0, 1, 0));
				}
				
				// The rest of the normals can be calculated as follows
				if(i != 0 && i != size - 1 && j != 0 && j != size - 1)
				{
					
					ArrayList<Vector3f> tempVList = new ArrayList<Vector3f>();
					
					tempVList.add(new Vector3f(xPos[i - 1], yData[i - 1][j], zPos[j]));
					tempVList.add(new Vector3f(xPos[i], yData[i][j + 1], zPos[j + 1]));
					tempVList.add(new Vector3f(xPos[i + 1], yData[i + 1][j], zPos[j]));
					tempVList.add(new Vector3f(xPos[i], yData[i][j - 1], zPos[j - 1]));
					
					Vector3f currentV = new Vector3f(xPos[i], yData[i][j], zPos[j]);
					
					Vector3f normal = MathUtils.generateNormal(tempVList, currentV);

					normalList.add(normal);
				}
			}
		}
	}
	
	public void setVaoID()
	{
		vaoID = TerrainGenerator.getTileVaoID(yData, normalList);
	}
	
	public void update()
	{
		
		modelMatrix.setIdentity();

		modelMatrix.transelate(x * 5f - ((1f / size) * x * 5), -5, z * 5f - ((1f / size) * z * 5));
		modelMatrix.scale(1f, 1f, 1f);
	}
	
	public void render()
	{
		
		ShaderManager.getShader("basicT").uploadMatrix4f(modelMatrix, ShaderManager.getShader("basicT").getModelMatrixLoc());
		ShaderManager.getShader("basicT").uploadMatrix4f(Engine.projMatrix, ShaderManager.getShader("basicT").getProjectionMatrixLoc());
		
		DrawShapes.drawQuad(ShaderManager.getShader("basicT") ,vaoID[0], vaoID[1]);
	}
	
	public int getX()
	{
		return x;
	}
	
	public int getZ()
	{
		return z;
	}
}