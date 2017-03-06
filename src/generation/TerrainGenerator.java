package generation;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.Random;

import org.lwjgl.BufferUtils;

import math.Vector3f;
import shapes.Quad;
import shapes.Triangle;

public class TerrainGenerator {

	public static int[] terrainGenerator(int iterations)
	{
		
		Random rand = new Random();
		
		float H = 0.8f;
		
		int gridSize = 2;
		float[][] gridData = new float[gridSize][gridSize];
		
		for(int i = 0; i < gridSize; i++){
			for(int j = 0; j < gridSize; j++)
			{
				
				gridData[i][j] = 1;
			}
		}
		
		for(int i = 0; i < iterations; i++)
		{
			
			float[][] newGridData = new float[(gridSize * 2) - 1][(gridSize * 2) - 1];
			
			float offset = (float) Math.pow(2, -i * 2 * H);
			System.out.println(offset);
			
			// The diamond step
			for(int j = 0; j < gridSize - 1; j++){
				for(int k = 0; k < gridSize - 1; k++)
				{
					
					float average = (gridData[j][k] + gridData[j + 1][k] + gridData[j][k + 1] + gridData[j + 1][k + 1]) / 4f;
					float off = average + (rand.nextFloat() * 2 * offset) - offset;
					
					newGridData[j * 2][k * 2] = gridData[j][k];
				    newGridData[j * 2 + 2][k * 2] = gridData[j + 1][k];
		            newGridData[j * 2 + 2][k * 2 + 2] = gridData[j + 1][k + 1];
		            newGridData[j * 2][k * 2 + 2] = gridData[j][k + 1];
				             
		            newGridData[j * 2 + 1][k * 2 + 1] = off;
				}
			}
			
			// The square step
			for(int j = 0; j < (gridSize * 2) - 1; j++)
			{
				
				if(j % 2 == 0){
		            
		            for (int k = 0; k < (gridSize - 1); k++)
		            {
		                
		                if(j == 0)
		                {
		                    
		                    float average = (newGridData[j][2 * k] + newGridData[j][2 * k + 2] + newGridData[j + 1][2 * k + 1]) / 3f;
		                    float off = average + (rand.nextFloat() * 2 * offset) - offset;
		                    
		                    newGridData[j][k * 2 + 1] = off;
		                }
		                else if(j == (gridSize * 2) - 2)
		                {
		                    
		                    float average = (newGridData[j][2 * k] + newGridData[j][2 * k + 2] + newGridData[j - 1][2 * k + 1]) / 3f;
		                    float off =  average + (rand.nextFloat() * 2 * offset) - offset;
		                    
		                    newGridData[j][k * 2 + 1] = off;
		                }
		                else
		                {
		                    
		                    float average = (newGridData[j][2 * k] + newGridData[j][2 * k + 2] + newGridData[j - 1][2 * k + 1] + newGridData[j + 1][2 * k + 1]) / 4f;
		                    float off =  average + (rand.nextFloat() * 2 * offset) - offset;
		                    
		                    newGridData[j][k * 2 + 1] = off;
		                }
		            }
				}
		        else
		        {
		         
		        	for (int k = 0; k < gridSize; k++)
		        	{
		        		
		                if(k == 0){
		                    
		                    float average = (newGridData[j + 1][2 * k] + newGridData[j][2 * k + 2] + newGridData[j - 1][2 * k]) / 3f;
		                    float off =  average + (rand.nextFloat() * 2 * offset) - offset;
		                    
		                    newGridData[j][k * 2] = off;
		                }
		                else if(k == gridSize - 1)
		                {

		                    float average = (newGridData[j + 1][2 * k] + newGridData[j][2 * k - 2] + newGridData[j - 1][2 * k]) / 3f;
		                    float off =  average + (rand.nextFloat() * 2 * offset) - offset;
		                    
		                    newGridData[j][k * 2] = off;
		                }
		                else
		                {

		                    float average = (newGridData[j + 1][2 * k] + newGridData[j][2 * k + 2] + newGridData[j - 1][2 * k] + newGridData[j][2 * k - 2]) / 4f;
		                    float off =  average + (rand.nextFloat() * 2 * offset) - offset;
		                    
		                    newGridData[j][k * 2] = off;
		                }
		        	}
		        }
			}
			
			gridData = newGridData;    	    
			gridSize = gridData[0].length;
		}
		
		// Putting all the data into quads
		ArrayList<Quad> quadList = new ArrayList<Quad>();
		
		float norm = 10f / gridData[0].length;
		
		for(int i = 0; i < gridSize - 1; i++){
			for(int j = 0; j < gridSize - 1; j++)
			{
				
				ArrayList<Vector3f> points = new ArrayList<Vector3f>();
				
				points.add(new Vector3f(j * norm, gridData[i + 1][j], (i + 1) * norm));
				points.add(new Vector3f((j + 1) * norm, gridData[i + 1][j + 1], (i + 1) * norm));
				points.add(new Vector3f((j + 1) * norm, gridData[i][j + 1], i * norm));
				points.add(new Vector3f(j * norm, gridData[i][j], i * norm));
				
				quadList.add(new Quad(points, false));
			}
		}
		
		// Putting all the data into triangles
		ArrayList<Triangle> triangleList = new ArrayList<Triangle>();
		
		for(int i = 0; i < quadList.size(); i++)
		{
			
			ArrayList<Triangle> tempTri = new ArrayList<Triangle>();
			tempTri = quadList.get(i).getTriangle();
			
			triangleList.add(tempTri.get(0));
			triangleList.add(tempTri.get(1));
		}
		
		int amountOfTriangles = triangleList.size();
		int floatInVetTriangle = 9;
		
		// Store vertices coordinates
		final float[] vertexArrayData = new float[(int) (amountOfTriangles * floatInVetTriangle)];

		for(int i = 0; i < amountOfTriangles; i++)
		{
			
			for(int j = 0; j < floatInVetTriangle; j++)
			{
				
				vertexArrayData[i * floatInVetTriangle + j] = triangleList.get(i).getVertexData()[j];
			}
		}
		
		FloatBuffer vertexData = BufferUtils.createFloatBuffer(vertexArrayData.length);
		vertexData.put(vertexArrayData);
		vertexData.flip();
		
		int vaoID = glGenVertexArrays();
		glBindVertexArray(vaoID);
		
		int vboVID = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, vboVID);
		glBufferData(GL_ARRAY_BUFFER, vertexData, GL_STATIC_DRAW);
		glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		
		glBindVertexArray(0);
		
		int[] r = {vaoID, amountOfTriangles};
		
		return r;
	}
	
	// Generating terrain using perlin noise
	// ===============================================
	
	private static float[][] baseNoise(int width, int height)
	{
		
		float[][] bNoise = new float[width][height];
		Random rand = new Random();
		
		for(int i = 0; i < width; i++){
			for(int j = 0; j < height; j++)
			{
				
				bNoise[i][j] = rand.nextFloat();
			}
		}
		
		return bNoise;
	}
	
	private static float interpolate(float x0, float x1, float alpha)
	{
		
		float val = x0 * (1 - alpha) + alpha * x1;
		return val;
	}
	
	private static float[][] smoothNoise(float[][] baseNoise, int octave)
	{
		
		int width = baseNoise.length;
		int height = baseNoise[0].length;
		
		float[][] sNoise = new float[width][height];
		
		int samplePeriod = 1 << octave;
		float sampleFrequency = 1.0f / samplePeriod;
		
		for(int i = 0; i < width; i++)
		{
			
			int sample_i0 = (i / samplePeriod) * samplePeriod;
			int sample_i1 = (sample_i0 + samplePeriod) % width;
			
			float hori_blend = (i - sample_i0) * sampleFrequency;
			
			for(int j = 0; j < height; j++)
			{
				
				int sample_j0 = (j / samplePeriod) * samplePeriod;
				int sample_j1 = (sample_j0 + samplePeriod) % height;
				
				float vert_blend = (j - sample_j0) * sampleFrequency;
				
				float top = interpolate(baseNoise[sample_i0][sample_j0], baseNoise[sample_i1][sample_j0], hori_blend);
				float bot = interpolate(baseNoise[sample_i0][sample_j1], baseNoise[sample_i1][sample_j1], hori_blend);
				
				sNoise[i][j] = interpolate(top, bot, vert_blend);
			}
		}
		
		return sNoise;
	}
	
	private static float[][] perlinNoise(float[][] baseNoise, int octaves)
	{
		
		int width = baseNoise.length;
		int height = baseNoise[0].length;
		
		float[][] pNoise = new float[width][height];
		float[][][] nArray = new float[octaves][width][height];
		
		for(int i = 0; i < octaves; i++)
		{
			
			nArray[i] = smoothNoise(baseNoise, i);
		}
		
		// Variables used in determining the terrain
		float persistance = 0.4f;
		float amplitude = 1.0f;
		
		float totalAmplitude = 0.0f;
		float max = 0f;
		
		for(int i = octaves - 1; i >= 0; i--)
		{

			amplitude *= persistance;
			totalAmplitude += amplitude;
			
			for(int j = 0; j < width; j++){
				for(int k = 0; k < height; k++)
				{
					
					pNoise[j][k] += nArray[i][j][k] * amplitude;
					
					if(pNoise[j][k] > max)
					{
						
						max = pNoise[j][k];
					}
				}
			}
		}
		
		for(int i = 0; i < width; i++){
			for(int j = 0; j < height; j++)
			{
				
				pNoise[i][j] /= totalAmplitude;
			}
		}
		
		System.out.println("MaxVal " + max);
		
		return pNoise;
	}
	
	public static int[] generateTerrainV2(int width, int height)
	{
		
		int octaves = 7;
		
		float[][] bNoise = baseNoise(width, height);
		float[][] pNoise = perlinNoise(bNoise, octaves);
		
		float[][] gridData = pNoise;
		
		// Putting all the data into quads
		ArrayList<Quad> quadList = new ArrayList<Quad>();
		
		System.out.println(gridData[0].length);
		float norm = 5f / gridData[0].length;
		
		for(int i = 0; i < width - 1; i++){
			for(int j = 0; j < height - 1; j++)
			{
				
				ArrayList<Vector3f> points = new ArrayList<Vector3f>();
				
				points.add(new Vector3f(j * norm, gridData[i + 1][j], (i + 1) * norm));
				points.add(new Vector3f((j + 1) * norm, gridData[i + 1][j + 1], (i + 1) * norm));
				points.add(new Vector3f((j + 1) * norm, gridData[i][j + 1], i * norm));
				points.add(new Vector3f(j * norm, gridData[i][j], i * norm));
				
				quadList.add(new Quad(points, false));
			}
		}
		
		// Putting all the data into triangles
		ArrayList<Triangle> triangleList = new ArrayList<Triangle>();
		
		for(int i = 0; i < quadList.size(); i++)
		{
			
			ArrayList<Triangle> tempTri = new ArrayList<Triangle>();
			tempTri = quadList.get(i).getTriangle();
			
			triangleList.add(tempTri.get(0));
			triangleList.add(tempTri.get(1));
		}
		
		int amountOfTriangles = triangleList.size();
		int floatInVetTriangle = 9;
		
		// Store vertices coordinates
		final float[] vertexArrayData = new float[(int) (amountOfTriangles * floatInVetTriangle)];

		for(int i = 0; i < amountOfTriangles; i++)
		{
			
			for(int j = 0; j < floatInVetTriangle; j++)
			{
				
				vertexArrayData[i * floatInVetTriangle + j] = triangleList.get(i).getVertexData()[j];
			}
		}
		
		FloatBuffer vertexData = BufferUtils.createFloatBuffer(vertexArrayData.length);
		vertexData.put(vertexArrayData);
		vertexData.flip();
		
		int vaoID = glGenVertexArrays();
		glBindVertexArray(vaoID);
		
		int vboVID = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, vboVID);
		glBufferData(GL_ARRAY_BUFFER, vertexData, GL_STATIC_DRAW);
		glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		
		glBindVertexArray(0);
		
		int[] r = {vaoID, amountOfTriangles};
		
		return r;
		
	}
	
	// End perlin generation
	// ===============================================
	
	// ===============================================
	// Method for storing terrain triangles in vao
	
	public static int[] getTileVaoID(float[][] gridData, ArrayList<Vector3f> normalList)
	{
		
		// Putting all the data into quads
		ArrayList<Quad> quadList = new ArrayList<Quad>();
		
		System.out.println(gridData[0].length);
		float norm = 5f / (gridData[0].length);
		
		int width = gridData.length;
		int height = gridData[0].length;
		
		for(int i = 0; i < width - 1; i++){
			for(int j = 0; j < height - 1; j++)
			{
				
				ArrayList<Vector3f> pointsV = new ArrayList<Vector3f>();
				ArrayList<Vector3f> pointsN = new ArrayList<Vector3f>();
				
				pointsV.add(new Vector3f(j * norm, gridData[i + 1][j], (i + 1) * norm));
				pointsV.add(new Vector3f((j + 1) * norm, gridData[i + 1][j + 1], (i + 1) * norm));
				pointsV.add(new Vector3f((j + 1) * norm, gridData[i][j + 1], i * norm));
				pointsV.add(new Vector3f(j * norm, gridData[i][j], i * norm));
				
				pointsN.add(normalList.get((i + 1) * width + j));
				pointsN.add(normalList.get((i + 1) * width + (j + 1)));
				pointsN.add(normalList.get((i) * width + (j + 1)));
				pointsN.add(normalList.get((i) * width + j));
				
				quadList.add(new Quad(pointsV, pointsN, false));
			}
		}
		
		// Putting all the data into triangles
		ArrayList<Triangle> triangleList = new ArrayList<Triangle>();
		
		for(int i = 0; i < quadList.size(); i++)
		{
			
			ArrayList<Triangle> tempTri = new ArrayList<Triangle>();
			tempTri = quadList.get(i).getTriangle();
			
			triangleList.add(tempTri.get(0));
			triangleList.add(tempTri.get(1));
		}
		
		int amountOfTriangles = triangleList.size();
		
		int floatInVetTriangle = 9;
		int floatInNorTriangle = 9;
		
		// Store vertices coordinates
		final float[] vertexArrayData = new float[(int) (amountOfTriangles * floatInVetTriangle)];

		for(int i = 0; i < amountOfTriangles; i++)
		{
			
			for(int j = 0; j < floatInVetTriangle; j++)
			{
				
				vertexArrayData[i * floatInVetTriangle + j] = triangleList.get(i).getVertexData()[j];
			}
		}
		
		// Store normal coordinates
		final float[] normalArrayData = new float[(int) (amountOfTriangles * floatInNorTriangle)];
		
		for(int i = 0; i < amountOfTriangles; i++)
		{
			
			for(int j = 0; j < floatInNorTriangle; j++)
			{
				
				normalArrayData[i * floatInNorTriangle + j] = triangleList.get(i).getNormalData()[j];
			}
		}
		
		FloatBuffer vertexData = BufferUtils.createFloatBuffer(vertexArrayData.length);
		vertexData.put(vertexArrayData);
		vertexData.flip();
		
		FloatBuffer normalData = BufferUtils.createFloatBuffer(normalArrayData.length);
		normalData.put(normalArrayData);
		normalData.flip();
		
		int vaoID = glGenVertexArrays();
		glBindVertexArray(vaoID);
		
		int vboVID = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, vboVID);
		glBufferData(GL_ARRAY_BUFFER, vertexData, GL_STATIC_DRAW);
		glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		
		int vboNID = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, vboNID);
		glBufferData(GL_ARRAY_BUFFER, normalData, GL_STATIC_DRAW);
		glVertexAttribPointer(1, 3, GL_FLOAT, false, 0, 0);
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		
		glBindVertexArray(0);
		
		int[] r = {vaoID, amountOfTriangles};
		
		return r;
	}
	
	// End
	// ===============================================
	
	public static int[] generateWaveTerrain(int size)
	{
		
		
		float[][] gridData = new float[size][size];
		
		for(int i = 0; i < gridData.length; i++){
			for(int j = 0; j < gridData[i].length; j++)
			{
				
				gridData[i][j] = (float) Math.sin(Math.toRadians(j));
			}
		}
		
		// Putting all the data into quads
		ArrayList<Quad> quadList = new ArrayList<Quad>();
		
		System.out.println(gridData[0].length);
		float norm = 10f / gridData[0].length;
		
		for(int i = 0; i < size - 1; i++){
			for(int j = 0; j < size - 1; j++)
			{
				
				ArrayList<Vector3f> points = new ArrayList<Vector3f>();
				
				points.add(new Vector3f(j * norm, gridData[i + 1][j], (i + 1) * norm));
				points.add(new Vector3f((j + 1) * norm, gridData[i + 1][j + 1], (i + 1) * norm));
				points.add(new Vector3f((j + 1) * norm, gridData[i][j + 1], i * norm));
				points.add(new Vector3f(j * norm, gridData[i][j], i * norm));
				
				quadList.add(new Quad(points, false));
			}
		}
		
		// Putting all the data into triangles
		ArrayList<Triangle> triangleList = new ArrayList<Triangle>();
		
		for(int i = 0; i < quadList.size(); i++)
		{
			
			ArrayList<Triangle> tempTri = new ArrayList<Triangle>();
			tempTri = quadList.get(i).getTriangle();
			
			triangleList.add(tempTri.get(0));
			triangleList.add(tempTri.get(1));
		}
		
		int amountOfTriangles = triangleList.size();
		int floatInVetTriangle = 9;
		
		// Store vertices coordinates
		final float[] vertexArrayData = new float[(int) (amountOfTriangles * floatInVetTriangle)];

		for(int i = 0; i < amountOfTriangles; i++)
		{
			
			for(int j = 0; j < floatInVetTriangle; j++)
			{
				
				vertexArrayData[i * floatInVetTriangle + j] = triangleList.get(i).getVertexData()[j];
			}
		}
		
		FloatBuffer vertexData = BufferUtils.createFloatBuffer(vertexArrayData.length);
		vertexData.put(vertexArrayData);
		vertexData.flip();
		
		int vaoID = glGenVertexArrays();
		glBindVertexArray(vaoID);
		
		int vboVID = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, vboVID);
		glBufferData(GL_ARRAY_BUFFER, vertexData, GL_STATIC_DRAW);
		glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		
		glBindVertexArray(0);
		
		int[] r = {vaoID, amountOfTriangles};
		
		return r;
	}
	
	private static void printA(float[][] a)
	{
		
		System.out.println("Grid Array");
		
		for(int i = 0; i < a[0].length; i++){
			for(int j = 0; j < a[0].length; j++)
			{
				
				float aa = Math.round(a[i][j] * 100) / 100f;
				System.out.print(aa + " , ");
				
			}
			
			System.out.println("");
		}
		
		System.out.println("====================");
	}
}