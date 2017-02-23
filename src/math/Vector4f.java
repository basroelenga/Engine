package math;

public class Vector4f {

	private float x;
	private float y;
	private float z;
	private float w;
	
	public Vector4f()
	{
		
		this.x = 0;
		this.y = 0;
		this.z = 0;
		this.w = 0;
	}
	
	public Vector4f(float x, float y, float z, float w)
	{
		
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
	}
	
	public Vector3f toVector3f()
	{
		
		Vector3f temp = new Vector3f();
		
		temp.setX(x);
		temp.setY(y);
		temp.setZ(z);
		
		return temp;
	}

	public void print()
	{
		System.out.println(x + " , " + y + " , " + z + " , " + w);
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

	public float getW() {
		return w;
	}

	public void setW(float w) {
		this.w = w;
	}
}