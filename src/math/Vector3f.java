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
		
		this.x += v.getX();
		this.y += v.getY();
		this.z += v.getZ();
	}
	
	public void dotProduct(Vector3f v)
	{
		
		this.x = this.x * v.getX();
		this.y = this.y * v.getY();
		this.z = this.z * v.getZ();
	}
	
	public void crossProduct(Vector3f v)
	{
		
		float tempX = x;
		float tempY = y;
		float tempZ = z;
		
		this.x = tempY * v.getZ() - tempZ * v.getY();
		this.y = tempZ * v.getX() - tempX * v.getZ();
		this.z = tempX * v.getY() - tempY * v.getX();
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
		
		float length = (float) Math.sqrt(this.x * this.x + this.y * this.y + this.z * this.z);
		
		this.x = this.x / length;
		this.y = this.y / length;
		this.z = this.z / length;
	}
	
	public void print()
	{
		System.out.println(x + " , " + y + " , " + z);
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