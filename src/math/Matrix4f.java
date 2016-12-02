package math;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class Matrix4f {

	private float m00, m01, m02, m03 = 0f;
	private float m10, m11, m12, m13 = 0f;
	private float m20, m21, m22, m23 = 0f;
	private float m30, m31, m32, m33 = 0f;
	
	float[] matrixArray = new float[16];
	
	public Matrix4f()
	{
		
		setIdentity();
	}
	
	private void updateElements()
	{
		
		m00 = matrixArray[0];
		m01 = matrixArray[1];
		m02 = matrixArray[2];
		m03 = matrixArray[3];
		
		m10 = matrixArray[4];
		m11 = matrixArray[5];
		m12 = matrixArray[6];
		m13 = matrixArray[7];
		
		m20 = matrixArray[8];
		m21 = matrixArray[9];
		m22 = matrixArray[10];
		m23 = matrixArray[11];
		
		m30 = matrixArray[12];
		m31 = matrixArray[13];
		m32 = matrixArray[14];
		m33 = matrixArray[15];
	}

	public void setIdentity()
	{
		
		for(int i = 0; i < matrixArray.length; i++)	matrixArray[i] = 0f;

		matrixArray[0 * 4 + 0] = 1f;
		matrixArray[1 * 4 + 1] = 1f;
		matrixArray[2 * 4 + 2] = 1f;
		matrixArray[3 * 4 + 3] = 1f;
	}
	
	public void multiply(Matrix4f m2)
	{
		
		float[] temp = new float[16];
		
		for(int i = 0; i < 4; i++)
		{
			
			for(int j = 0; j < 4; j++)
			{
				
				temp[i * 4 + j] = 0f;
				for(int r = 0; r < 4; r++)
				{
					
					temp[i * 4 + j] += matrixArray[i * 4 + r] * m2.getElements()[r * 4 + j];
				}
			}
		}
		
		matrixArray = temp;
	}
	
	// NOT WORKING PROP
	public void transpose()
	{
		
		float[] temp = new float[16];
		
		for(int i = 0; i < 4; i++)
		{
			
			for(int j = 0; j < 4; j++)
			{
				
				temp[i * 4 + j] = matrixArray[j * 4 + i];
			}
		}
		
		matrixArray = temp;
	}
	
	public void inverse()
	{
		
		float det = determinant();
		
		Matrix4f adj = new Matrix4f();
		updateElements();
		
		adj.m00 = m11 * m22 * m33 + m12 * m23 * m31 + m13 * m21 * m32 - m11 * m23 * m32 - m12 * m21 * m33 - m13 * m22 * m31;
		adj.m01 = m01 * m23 * m32 + m02 * m21 * m33 + m03 * m22 * m31 - m01 * m22 * m33 - m02 * m23 * m31 - m03 * m21 * m32;
		adj.m02 = m01 * m12 * m33 + m02 * m13 * m31 + m03 * m11 * m32 - m01 * m13 * m32 - m02 * m11 * m33 - m03 * m12 * m31;
		adj.m03 = m01 * m13 * m22 + m02 * m11 * m23 + m03 * m12 * m32 - m01 * m12 * m23 - m02 * m13 * m21 - m03 * m11 * m22;
		
		adj.m10 = m10 * m23 * m32 + m12 * m20 * m33 + m13 * m22 * m30 - m10 * m22 * m33 - m12 * m23 * m30 - m13 * m20 * m32;
		//adj.m11 = m01 * m23 * m32 + m02 * m21 * m33 + m03 * m22 * m31 - m01 * m22 * m33 - m02 * m23 * m31 - m03 * m21 * m32;
		adj.m12 = m01 * m12 * m33 + m02 * m13 * m31 + m03 * m11 * m32 - m01 * m13 * m32 - m02 * m11 * m33 - m03 * m12 * m31;
		adj.m13 = m01 * m13 * m22 + m02 * m11 * m23 + m03 * m12 * m32 - m01 * m12 * m23 - m02 * m13 * m21 - m03 * m11 * m22;
		
		adj.m20 = m11 * m22 * m33 + m12 * m23 * m31 + m13 * m21 * m32 - m11 * m23 * m32 - m12 * m21 * m33 - m13 * m22 * m31;
		adj.m21 = m01 * m23 * m32 + m02 * m21 * m33 + m03 * m22 * m31 - m01 * m22 * m33 - m02 * m23 * m31 - m03 * m21 * m32;
		adj.m22 = m01 * m12 * m33 + m02 * m13 * m31 + m03 * m11 * m32 - m01 * m13 * m32 - m02 * m11 * m33 - m03 * m12 * m31;
		adj.m23 = m01 * m13 * m22 + m02 * m11 * m23 + m03 * m12 * m32 - m01 * m12 * m23 - m02 * m13 * m21 - m03 * m11 * m22;
		
		adj.m30 = m11 * m22 * m33 + m12 * m23 * m31 + m13 * m21 * m32 - m11 * m23 * m32 - m12 * m21 * m33 - m13 * m22 * m31;
		adj.m31 = m01 * m23 * m32 + m02 * m21 * m33 + m03 * m22 * m31 - m01 * m22 * m33 - m02 * m23 * m31 - m03 * m21 * m32;
		adj.m32 = m01 * m12 * m33 + m02 * m13 * m31 + m03 * m11 * m32 - m01 * m13 * m32 - m02 * m11 * m33 - m03 * m12 * m31;
		adj.m33 = m01 * m13 * m22 + m02 * m11 * m23 + m03 * m12 * m32 - m01 * m12 * m23 - m02 * m13 * m21 - m03 * m11 * m22;
		
	}
	
	public float determinant()
	{
		
		float[] m = matrixArray;
		
		float det = (m[0] * m[5] - m[1] * m[4]) * (m[10] * m[15] - m[11] * m[14])
				  + (m[2] * m[4] - m[0] * m[6]) * (m[9] * m[15] - m[11] * m[13])
				  + (m[0] * m[7] - m[3] * m[4]) * (m[9] * m[14] - m[10] * m[13])
				  + (m[1] * m[6] - m[2] * m[5]) * (m[8] * m[15] - m[11] * m[12])
				  + (m[3] * m[5] - m[1] * m[7]) * (m[8] * m[14] - m[10] * m[12])
				  + (m[2] * m[7] - m[3] * m[6]) * (m[8] * m[13] - m[9] * m[12]);

		return det;
	}
	
	public void transelate(float x, float y, float z)
	{
		
		Matrix4f transMatrix = new Matrix4f();
		
		transMatrix.getElements()[3] = x;
		transMatrix.getElements()[7] = y;
		transMatrix.getElements()[11] = z;
		
		this.multiply(transMatrix);
	}
	
	public void scale(float x, float y, float z)
	{
	
		Matrix4f scaleMatrix = new Matrix4f();
		
		scaleMatrix.getElements()[0] = x;
		scaleMatrix.getElements()[5] = y;
		scaleMatrix.getElements()[10] = z;
		
		this.multiply(scaleMatrix);
	}
	
	public void rotate(String axis, float angle)
	{
		
		Matrix4f rotation = new Matrix4f();
		angle = (float) Math.toRadians(angle);
		
		switch(axis)
		{
		
			case "x":
		
				rotation.getElements()[5] = (float) Math.cos(angle);
				rotation.getElements()[6] = (float) -Math.sin(angle);
				
				rotation.getElements()[9] = (float) Math.sin(angle);
				rotation.getElements()[10] = (float) Math.cos(angle);
				
				this.multiply(rotation);
				
				break;
			
			case "y":
				
				rotation.getElements()[0] = (float) Math.cos(angle);
				rotation.getElements()[2] = (float) Math.sin(angle);
				
				rotation.getElements()[8] = (float) -Math.sin(angle);
				rotation.getElements()[10] = (float) Math.cos(angle);
				
				this.multiply(rotation);
				
				break;
				
			case "z":
				
				rotation.getElements()[0] = (float) Math.cos(angle);
				rotation.getElements()[1] = (float) -Math.sin(angle);
				
				rotation.getElements()[4] = (float) Math.sin(angle);
				rotation.getElements()[5] = (float) Math.cos(angle);
				
				this.multiply(rotation);
				
				break;
		}
	}
	
	public void rotateQ(float Rz, float Rx, float Ry, boolean show)
	{
		
		Matrix4f rotationMatrixQ = new Matrix4f();
		
		float theta = (float) Math.sqrt(Math.pow(Rx, 2) + Math.pow(Ry, 2) + Math.pow(Rz, 2));
		
		if(theta == 0) return;
		
		Rx = (float) Math.toRadians(Rx);
		Ry = (float) Math.toRadians(Ry);
		Rz = (float) Math.toRadians(Rz);
		
		float c2 = (float) (Math.cos(Rx / 2));
		float c3 = (float) (Math.cos(Ry / 2));
		float c1 = (float) (Math.cos(Rz / 2));
		
		float s2 = (float) Math.sin(Rx / 2);
		float s3 = (float) Math.sin(Ry / 2);
		float s1 = (float) Math.sin(Rz / 2);
		
		float w = c2 * c3 * c1 - s2 * s3 * s1;
		float x = s2 * s3 * c1 + c2 * c3 * s1;
		float y = s2 * c3 * c1 + c2 * s3 * s1;
		float z = c2 * s3 * c1 - s2 * c3 * s1;
		
		//if(show) System.out.println(w + " , " + x + " , " + y + " , " + z + " , " + Math.toDegrees(Rx) + " , " + Math.toDegrees(Ry));
		
		rotationMatrixQ.getElements()[0] = (float) (1f - 2f * Math.pow(y, 2) - 2f * Math.pow(z, 2));
		rotationMatrixQ.getElements()[1] = 2f * x * y - 2f * w * z;
		rotationMatrixQ.getElements()[2] = 2f * x * z + 2f * w * y ;
		rotationMatrixQ.getElements()[3] = 0f;
		
		rotationMatrixQ.getElements()[4] = 2f * x * y + 2f * w * z;
		rotationMatrixQ.getElements()[5] = (float) (1f - 2f * Math.pow(x, 2) - 2f * Math.pow(z, 2));
		rotationMatrixQ.getElements()[6] = 2f * y * z + 2f * w * x;
		rotationMatrixQ.getElements()[7] = 0f;
		
		rotationMatrixQ.getElements()[8] = 2f * x * z - 2f * w * y;
		rotationMatrixQ.getElements()[9] = 2f * y * z - 2f * w * x;
		rotationMatrixQ.getElements()[10] = (float) (1 - 2f * Math.pow(x, 2) - 2f * Math.pow(y, 2));
		rotationMatrixQ.getElements()[11] = 0f;
		
		rotationMatrixQ.getElements()[12] = 0f;
		rotationMatrixQ.getElements()[13] = 0f;
		rotationMatrixQ.getElements()[14] = 0f;
		rotationMatrixQ.getElements()[15] = 1f;
		
		this.multiply(rotationMatrixQ);
	}
	
	public void print()
	{
		
		for(int i = 0; i < 4; i++)
		{
			
			System.out.print(matrixArray[i * 4 + 0] + " ");
			System.out.print(matrixArray[i * 4 + 1] + " ");
			System.out.print(matrixArray[i * 4 + 2] + " ");
			
			System.out.println(matrixArray[i * 4 + 3]);
		}
	}
	
	public FloatBuffer toFloatBuffer()
	{
		
		FloatBuffer result = ByteBuffer.allocateDirect(matrixArray.length << 2).order(ByteOrder.nativeOrder()).asFloatBuffer();
		result.put(matrixArray).flip();
		
		return result;
	}
	
	public float[] getElements()
	{
		return matrixArray;
	}
}