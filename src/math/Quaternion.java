package math;

public class Quaternion {

	private float w;
	private float x;
	private float y;
	private float z;
	
	public Quaternion()
	{
		
	}
	
	public Quaternion(float w, float x, float y, float z)
	{
		
		this.w = w;
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public float getW() {
		return w;
	}

	public void setW(float w) {
		this.w = w;
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