package shapes;

import math.Vector3f;

public class Triangle {

	private Vector3f first;
	private Vector3f second;
	private Vector3f third;
	
	private Vector3f firstN;
	private Vector3f secondN;
	private Vector3f thirdN;
	
	private float[] triangleData;
	private float[] normalData;
	
	/**
	 * Create a triangle for which the values are stored as floats.
	 * @param first The first point.
	 * @param second The second point.
	 * @param third The third point.
	 */
	public Triangle(Vector3f first, Vector3f second, Vector3f third)
	{
		
		this.first = first;
		this.second = second;
		this.third = third;
		
		setDataV();
	}
	
	/**
	 * Create a triangle for which the values are stored as floats. This constructor
	 * should be used if normal coordinates are available
	 * @param first The first point (vertices).
	 * @param second The second point (vertices).
	 * @param third The third point (vertices).
	 * @param firstN The first point (normals).
	 * @param secondN The second point (normals).
	 * @param thirdN The third point (normals).
	 */
	public Triangle(Vector3f first, Vector3f second, Vector3f third, Vector3f firstN, Vector3f secondN, Vector3f thirdN)
	{
		
		this.first = first;
		this.second = second;
		this.third = third;
		
		this.firstN = firstN;
		this.secondN = secondN;
		this.thirdN = thirdN;
		
		setDataV();
		setDataN();
	}
	
	private void setDataV()
	{
		
		triangleData = new float[9];
		
		triangleData[0] = first.getX();
		triangleData[1] = first.getY();
		triangleData[2] = first.getZ();
		
		triangleData[3] = second.getX();
		triangleData[4] = second.getY();
		triangleData[5] = second.getZ();
		
		triangleData[6] = third.getX();
		triangleData[7] = third.getY();
		triangleData[8] = third.getZ();
	}
	
	private void setDataN()
	{
		
		normalData = new float[9];
		
		normalData[0] = firstN.getX();
		normalData[1] = firstN.getY();
		normalData[2] = firstN.getZ();
		
		normalData[3] = secondN.getX();
		normalData[4] = secondN.getY();
		normalData[5] = secondN.getZ();
		
		normalData[6] = thirdN.getX();
		normalData[7] = thirdN.getY();
		normalData[8] = thirdN.getZ();
	}
	
	public void print()
	{
		System.out.println("TRIANGLE");
		
		System.out.println(first.getX() + ", " + first.getY() + ", " + first.getZ());
		System.out.println(second.getX() + ", " + second.getY() + ", " + second.getZ());
		System.out.println(third.getX() + ", " + third.getY() + ", " + third.getZ());
	}
	
	public float[] getVertexData()
	{
		return triangleData;
	}
	
	public float[] getNormalData()
	{
		return normalData;
	}

	public Vector3f getFirst() {
		return first;
	}

	public Vector3f getSecond() {
		return second;
	}

	public Vector3f getThird() {
		return third;
	}

	public Vector3f getFirstN() {
		return firstN;
	}

	public Vector3f getSecondN() {
		return secondN;
	}

	public Vector3f getThirdN() {
		return thirdN;
	}
}