package math;

public class Vector3f {

	private float x;
	private float y;
	private float z;
	
	public Vector3f()
	{
		
		this.x = 0;
		this.y = 0;
		this.z = 0;
	}
	
	public Vector3f(float x, float y, float z)
	{
		
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public Vector3f(Vector3f vec)
	{
		
		this.x = vec.getX();
		this.y = vec.getY();
		this.z = vec.getZ();
	}
	
	public void add(Vector3f v)
	{
		
		x += v.getX();
		y += v.getY();
		z += v.getZ();
	}
	
	public void scale(float s)
	{
		
		x *= s;
		y *= s;
		z *= s;
	}
	
	public void dotProduct(Vector3f v)
	{
		
		x *= v.getX();
		y *= v.getY();
		z *= v.getZ();
	}
	
	public void crossProduct(Vector3f v)
	{
		
		float tempX = x;
		float tempY = y;
		float tempZ = z;
		
		x = tempY * v.getZ() - tempZ * v.getY();
		y = tempZ * v.getX() - tempX * v.getZ();
		z = tempX * v.getY() - tempY * v.getX();
	}
	
	public Vector3f crossProductR(Vector3f v)
	{
		
		Vector3f temp = new Vector3f();
		
		temp.x = y * v.getZ() - z * v.getY();
		temp.y = z * v.getX() - x * v.getZ();
		temp.z = x * v.getY() - y * v.getX();
		
		return temp;
	}
	
	public void normalize()
	{
		
		float length = (float) Math.sqrt(x * x + y * y + z * z);
		
		x = x / length;
		y = y / length;
		z = z / length;
	}
	
	public void print()
	{
		System.out.println(x + " , " + y + " , " + z);
	}

	// Some static methods
	
	public static Vector3f add(Vector3f vec1, Vector3f vec2)
	{
		
		Vector3f vect1 = new Vector3f(vec1);
		Vector3f vect2 = new Vector3f(vec2);
		
		vect1.add(vect2);
		
		return vect1;
	}
	
	public static Vector3f cross(Vector3f vec1, Vector3f vec2)
	{
		
		Vector3f vect1 = new Vector3f(vec1);
		Vector3f vect2 = new Vector3f(vec2);
		
		vect1.crossProduct(vect2);
		
		return vect1;
	}
	
	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	public float getZ() {
		return z;
	}

	public void setZ(float z) {
		this.z = z;
	}
}