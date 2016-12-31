package shapes;

import java.nio.FloatBuffer;
import java.util.ArrayList;

import org.lwjgl.BufferUtils;

import math.Vector3f;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

public class UVSphere {

	private float subdivision;
	private float amountOfTriangles;
	
	private int vaoID;
	
	public UVSphere(int subdivision)
	{
		
		this.subdivision = (float) subdivision;
		generateSphere();
	}
	
	private void generateSphere()
	{
	
		ArrayList<Vector3f> xyz = new ArrayList<Vector3f>();
		
		for(int i = 0; i < subdivision + 1; i++)
		{
			
			// Generating all the y values.
			float y = (float) Math.cos(Math.PI * (i / subdivision));
			
			// Top and bottom only have 1 point.
			if(i == 0 || i == subdivision)
			{
				
				xyz.add(new Vector3f(0, y, 0));
			}
			else
			{
				
				// Generating all the points in the middle.
				for(int j = 0; j < subdivision; j++)
				{
					
					float x = (float) Math.cos(2 * Math.PI * (j / subdivision)) * (float) Math.sin(Math.PI * (i / subdivision));
					float z = (float) Math.sin(2 * Math.PI * (j / subdivision)) * (float) Math.sin(Math.PI * (i / subdivision));
					
					xyz.add(new Vector3f(x, y, z));
				}
			}
		}
		
		// =========================================================
		// Generate normal coordinates for each vertices
		ArrayList<Vector3f> normalCoordinateList = new ArrayList<Vector3f>();
		
		for(int i = 0; i < xyz.size(); i++)
		{
			
			// Exception for the first and last point, where the normal direction is downward or upwards.
			if(i == 0)
			{
				
				normalCoordinateList.add(new Vector3f(0, 1, 0));
			}
			else if(i == xyz.size() - 1)
			{
				
				normalCoordinateList.add(new Vector3f(0, -1, 0));
			}
			else
			{
				
				// The other normal coordinates are calculated based on the coordinates beside them (start at 12 going clockwise).
				// Exception for the vertices in the first subdivision.
				if(i < subdivision || i == subdivision)
				{
					
					ArrayList<Vector3f> tempVList = new ArrayList<Vector3f>();
					
					if(i == 1)
					{
						
						tempVList.add(xyz.get(0));
						tempVList.add(xyz.get(i + 1));
						
						tempVList.add(xyz.get((int) (i + subdivision)));
						tempVList.add(xyz.get((int) (i + subdivision - 1)));
					}
					else
					{
						
						tempVList.add(xyz.get(0));
						tempVList.add(xyz.get(i + 1));
						
						tempVList.add(xyz.get((int) (i + subdivision)));
						tempVList.add(xyz.get(i - 1));
					}
					
					normalCoordinateList.add(generateNormal(tempVList, xyz.get(i)));
				}
				
				// Exception for the vertices in the last subdivision.
				else if(i > xyz.size() - subdivision - 1)
				{
					
					ArrayList<Vector3f> tempVList = new ArrayList<Vector3f>();
					
					tempVList.add(xyz.get((int) (i - subdivision)));
					tempVList.add(xyz.get(i + 1));
					
					tempVList.add(xyz.get(xyz.size() - 1));
					tempVList.add(xyz.get(i - 1));
					
					normalCoordinateList.add(generateNormal(tempVList, xyz.get(i)));
				}
				
				// Generate the normals for the rest of the vertices.
				else
				{
					
					ArrayList<Vector3f> tempVList = new ArrayList<Vector3f>();
					
					tempVList.add(xyz.get((int) (i - subdivision)));
					tempVList.add(xyz.get(i + 1));
					
					tempVList.add(xyz.get((int) (i + subdivision)));
					tempVList.add(xyz.get(i - 1));
					
					normalCoordinateList.add(generateNormal(tempVList, xyz.get(i)));
				}
			}
		}
		
		// Creating lists to store the data of triangles.
		ArrayList<Triangle> trianglesTopList =  new ArrayList<Triangle>();
		ArrayList<Triangle> trianglesBotList =  new ArrayList<Triangle>();
		
		ArrayList<Quad> quadMiddleList = new ArrayList<Quad>();
		
		// Putting the top into triangles.
		for(int i = 0; i < subdivision; i++)
		{
		
			Vector3f fPoint;
			Vector3f sPoint;
			Vector3f tPoint;
			
			Vector3f fnPoint;
			Vector3f snPoint;
			Vector3f tnPoint;
			
			// The last point of the the last triangle is also a part of the first triangle, thus there is an exception here.
			if(i == subdivision - 1)
			{
				
				fPoint = xyz.get(i + 1);
				sPoint = xyz.get(0);
				tPoint = xyz.get((int) (i + 2 - subdivision));
				
				fnPoint = normalCoordinateList.get(i + 1);
				snPoint = normalCoordinateList.get(0);
				tnPoint = normalCoordinateList.get((int) (i + 2 - subdivision));
			}
			else
			{
				
				fPoint = xyz.get(i + 1);
				sPoint = xyz.get(0);
				tPoint = xyz.get(i + 2);
				
				fnPoint = normalCoordinateList.get(i + 1);
				snPoint = normalCoordinateList.get(0);
				tnPoint = normalCoordinateList.get(i + 2);
			}
			
			Triangle topTri = new Triangle(fPoint, sPoint, tPoint, fnPoint, snPoint, tnPoint);
			trianglesTopList.add(topTri);
		}
		
		// Putting the bottom into triangles.
		for(int i = 0; i < subdivision; i++)
		{
			
			Vector3f fPoint;
			Vector3f sPoint;
			Vector3f tPoint;
			
			Vector3f fnPoint;
			Vector3f snPoint;
			Vector3f tnPoint;
			
			// Same exception as the top.
			if(i == subdivision - 1)
			{
				
				fPoint = xyz.get(xyz.size() - 2);
				sPoint = xyz.get(xyz.size() - 1);
				tPoint = xyz.get((int) (xyz.size() - 1 - subdivision));
				
				fnPoint = normalCoordinateList.get(xyz.size() - 2);
				snPoint = normalCoordinateList.get(xyz.size() - 1);
				tnPoint = normalCoordinateList.get((int) (xyz.size() - 1 - subdivision));
			}
			else
			{
				
				fPoint = xyz.get((int) ((xyz.size() - 1) - subdivision + i));
				sPoint = xyz.get(xyz.size() - 1);
				tPoint = xyz.get((int) ((xyz.size() - 1) - subdivision + i + 1));
				
				fnPoint = normalCoordinateList.get((int) ((xyz.size() - 1) - subdivision + i));
				snPoint = normalCoordinateList.get(xyz.size() - 1);
				tnPoint = normalCoordinateList.get((int) ((xyz.size() - 1) - subdivision + i + 1));
			}
			
			Triangle botTri = new Triangle(fPoint, sPoint, tPoint, fnPoint, snPoint, tnPoint);
			trianglesBotList.add(botTri);
		}
		
		// Putting the middle into quads
		for(int i = 0; i < subdivision - 2; i++)
		{
			for(int j = 0; j < subdivision; j++)
			{
			
				Vector3f fPoint;
				Vector3f sPoint;
				Vector3f tPoint;
				Vector3f ffPoint;
				
				Vector3f fnPoint;
				Vector3f snPoint;
				Vector3f tnPoint;
				Vector3f ffnPoint;
				
				ArrayList<Vector3f> quadVPointList = new ArrayList<Vector3f>();
				ArrayList<Vector3f> quadNPointList = new ArrayList<Vector3f>();
				
				// Exception for last quad
				if(j == subdivision - 1)
				{
					
					fPoint = xyz.get((int) (i * subdivision + (j + 1 + subdivision)));
					sPoint = xyz.get((int) (i * subdivision + subdivision + 1));
					
					ffPoint = xyz.get((int) (i * subdivision + (j + 1)));
					tPoint = xyz.get((int) (i * subdivision + 1));
					
					fnPoint = normalCoordinateList.get((int) (i * subdivision + (j + 1 + subdivision)));
					snPoint = normalCoordinateList.get((int) (i * subdivision + subdivision + 1));
					
					ffnPoint = normalCoordinateList.get((int) (i * subdivision + (j + 1)));
					tnPoint = normalCoordinateList.get((int) (i * subdivision + 1));
					
				}
				else
				{
					
					fPoint = xyz.get((int) (i * subdivision + (j + 1 + subdivision)));
					sPoint = xyz.get((int) (i * subdivision + (j + 2 + subdivision)));
							
					ffPoint = xyz.get((int) (i * subdivision + (j + 1)));
					tPoint = xyz.get((int) (i * subdivision + (j + 2)));
					
					fnPoint = normalCoordinateList.get((int) (i * subdivision + (j + 1 + subdivision)));
					snPoint = normalCoordinateList.get((int) (i * subdivision + (j + 2 + subdivision)));
							
					ffnPoint = normalCoordinateList.get((int) (i * subdivision + (j + 1)));
					tnPoint = normalCoordinateList.get((int) (i * subdivision + (j + 2)));
				}
				
				quadVPointList.add(fPoint);
				quadVPointList.add(sPoint);
				quadVPointList.add(tPoint);
				quadVPointList.add(ffPoint);
				
				quadNPointList.add(fnPoint);
				quadNPointList.add(snPoint);
				quadNPointList.add(tnPoint);
				quadNPointList.add(ffnPoint);

				Quad middleQuad = new Quad(quadVPointList, quadNPointList);
				quadMiddleList.add(middleQuad);
			}
		}
		
		amountOfTriangles = quadMiddleList.size() * 2 + trianglesBotList.size() + trianglesTopList.size();
		ArrayList<Triangle> triangleList = new ArrayList<Triangle>();
		
		// Add the top triangles
		triangleList.addAll(trianglesTopList);
		
		// Add the middle triangles
		for(int i = 0; i < quadMiddleList.size(); i++)
		{
			
			ArrayList<Triangle> tempTri = new ArrayList<Triangle>();
			tempTri = quadMiddleList.get(i).getTriangle();
			
			triangleList.add(tempTri.get(0));
			triangleList.add(tempTri.get(1));
		}
		
		// Add the bot triangles
		triangleList.addAll(trianglesBotList);
		
		// =========================================================
		// Generate texture coordinates for each triangle

		ArrayList<Float> UVCoordinateList = new ArrayList<Float>();
		System.out.println("Subdivision: " + subdivision);
		System.out.println("=============================");
		
		
		float uOffset = (getU(triangleList.get(1).getFirst().getZ(), triangleList.get(1).getFirst().getX()) - getU(triangleList.get(0).getFirst().getZ(), triangleList.get(0).getFirst().getX())) / 2f;
		uOffset = 0f;
		
		// Per triangle texture coordinates
		for(int i = 0; i < amountOfTriangles; i++)
		{
			
			float uF = getU(triangleList.get(i).getFirst().getZ(), triangleList.get(i).getFirst().getX()) + uOffset;
			float uS = getU(triangleList.get(i).getSecond().getZ(), triangleList.get(i).getSecond().getX()) + uOffset;
			float uT = getU(triangleList.get(i).getThird().getZ(), triangleList.get(i).getThird().getX()) + uOffset;
			
			float vF = getV(triangleList.get(i).getFirst().getY());
			float vS = getV(triangleList.get(i).getSecond().getY());
			float vT = getV(triangleList.get(i).getThird().getY());
			
			UVCoordinateList.add(uF);
			UVCoordinateList.add(vF);
			
			UVCoordinateList.add(uS);
			UVCoordinateList.add(vS);
			
			UVCoordinateList.add(uT);
			UVCoordinateList.add(vT);
		}
		
		// =========================================================
		// Put the data into the buffers
			
		int floatInVetTriangle = 9;
		int floatInTexTriangle = 6;
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
		
		// Store texture coordinates
		
		final float[] textureArrayData = new float[(int) (amountOfTriangles * floatInTexTriangle)];
		
		for(int i = 0; i < amountOfTriangles; i++)
		{
			
			for(int j = 0; j < floatInTexTriangle; j++)
			{
				
				textureArrayData[i * floatInTexTriangle + j] = UVCoordinateList.get(i * floatInTexTriangle + j);
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
		
		FloatBuffer textureData = BufferUtils.createFloatBuffer(textureArrayData.length);
		textureData.put(textureArrayData);
		textureData.flip();
		
		FloatBuffer normalData = BufferUtils.createFloatBuffer(normalArrayData.length);
		normalData.put(normalArrayData);
		normalData.flip();
		
		vaoID = glGenVertexArrays();
		glBindVertexArray(vaoID);
		
		int vboVID = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, vboVID);
		glBufferData(GL_ARRAY_BUFFER, vertexData, GL_STATIC_DRAW);
		glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		
		int vboTID = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, vboTID);
		glBufferData(GL_ARRAY_BUFFER, textureData, GL_STATIC_DRAW);
		glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, 0);
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		
		int vboNID = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, vboNID);
		glBufferData(GL_ARRAY_BUFFER, normalData, GL_STATIC_DRAW);
		glVertexAttribPointer(2, 3, GL_FLOAT, false, 0, 0);
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		
		glBindVertexArray(0);
	}
	
	private float getU(double dz, double dx)
	{
		
		float u = (float) (0.5 + Math.atan2(dz, dx) / (2 * Math.PI));
		return u;
	}
	
	private float getV(double dy)
	{
		
		float v = (float) (0.5 - Math.asin(dy) / (Math.PI));
		return v;
	}
	
	private Vector3f generateNormal(ArrayList<Vector3f> tempVList, Vector3f currentV)
	{
		
		Vector3f crossP = new Vector3f();
		
		for(int j = 0; j < tempVList.size(); j++)
		{
			
			Vector3f firstN = new Vector3f();
			
			firstN.setX(tempVList.get(j).getX() - currentV.getX());
			firstN.setY(tempVList.get(j).getY() - currentV.getY());
			firstN.setZ(tempVList.get(j).getZ() - currentV.getZ());
			
			Vector3f secondN = new Vector3f();
			
			if(j == tempVList.size() - 1)
			{
				
				secondN.setX(tempVList.get(j - 3).getX() - currentV.getX());
				secondN.setY(tempVList.get(j - 3).getY() - currentV.getY());
				secondN.setZ(tempVList.get(j - 3).getZ() - currentV.getZ());
			}
			else
			{
				
				secondN.setX(tempVList.get(j + 1).getX() - currentV.getX());
				secondN.setY(tempVList.get(j + 1).getY() - currentV.getY());
				secondN.setZ(tempVList.get(j + 1).getZ() - currentV.getZ());
			}
			
			crossP.add(firstN.crossProductR(secondN));
		}
		
		crossP.normalize();
		return crossP;
	}
	
	public int getAmountOfTriangles()
	{
		return (int) amountOfTriangles;
	}
	
	public int getVaoID()
	{
		return vaoID;
	}
	
	public int getSubdivision()
	{
		return (int) subdivision;
	}
}