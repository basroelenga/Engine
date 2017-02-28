package math;

public class Quaternion {

	private float a;
	private Vector3f axis;
	
	private float x;
	private float y;
	private float z;
	private float w;
	
	public Quaternion(float w, float x, float y, float z)
	{
		
		this.w = w;
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public Quaternion(float a, Vector3f axis)
	{
		
		this.a = (float) Math.toRadians(a);
		this.axis = axis;
		
		setQuaternion();
	}
	
	private void setQuaternion()
	{
		
		w = (float) Math.cos(a / 2);
		
		x = axis.getX() * (float) Math.sin(a / 2);
		y = axis.getY() * (float) Math.sin(a / 2);
		z = axis.getZ() * (float) Math.sin(a / 2);
	}
	
	public void updateQuaternion(float a, Vector3f axis)
	{
		
		this.a = (float) Math.toRadians(a);
		this.axis = axis;
		
		setQuaternion();
	}
	
	public Quaternion inverse()
	{
		
		Quaternion q = new Quaternion(w, -x, -y, -z);
		return q;
	}
	
	public void normalize()
	{
		float length = (float) Math.sqrt(Math.pow(w, 2) + Math.pow(x, 2) + Math.pow(y, 2) + Math.pow(z, 2));
		
		w = w / length;
		
		x = x / length;
		y = y / length;
		z = z / length;
	}
	
	public Matrix4f toMatrix4f()
	{
		
		Matrix4f rotationMatrix = new Matrix4f();
		
		rotationMatrix.getElements()[0] = (float) (1f - 2f * Math.pow(y, 2) - 2f * Math.pow(z, 2));
		rotationMatrix.getElements()[1] = 2f * x * y - 2f * w * z;
		rotationMatrix.getElements()[2] = 2f * x * z + 2f * w * y ;
		rotationMatrix.getElements()[3] = 0f;
		
		rotationMatrix.getElements()[4] = 2f * x * y + 2f * w * z;
		rotationMatrix.getElements()[5] = (float) (1f - 2f * Math.pow(x, 2) - 2f * Math.pow(z, 2));
		rotationMatrix.getElements()[6] = 2f * y * z - 2f * w * x;
		rotationMatrix.getElements()[7] = 0f;
		
		rotationMatrix.getElements()[8] = 2f * x * z - 2f * w * y;
		rotationMatrix.getElements()[9] = 2f * y * z + 2f * w * x;
		rotationMatrix.getElements()[10] = (float) (1 - 2f * Math.pow(x, 2) - 2f * Math.pow(y, 2));
		rotationMatrix.getElements()[11] = 0f;
		
		rotationMatrix.getElements()[12] = 0f;
		rotationMatrix.getElements()[13] = 0f;
		rotationMatrix.getElements()[14] = 0f;
		rotationMatrix.getElements()[15] = 1f;
				
		return rotationMatrix;
	}
	
	public void print()
	{
		
		System.out.println("Quaternion");
		System.out.println("w: " + w + " x: " + x + " y: " + y + " z: " + z);
	}
	
	// Static methods
	public static Quaternion multiply(Quaternion q1, Quaternion q2)
	{
		
		float tempW = (q1.getW() * q2.getW() - q1.getX() * q2.getX() - q1.getY() * q2.getY() - q1.getZ() * q2.getZ());
		float tempX = (q1.getW() * q2.getX() + q1.getX() * q2.getW() - q1.getY() * q2.getZ() + q1.getZ() * q2.getY());
		float tempY = (q1.getW() * q2.getY() + q1.getX() * q2.getZ() + q1.getY() * q2.getW() - q1.getZ() * q2.getX());
		float tempZ = (q1.getW() * q2.getZ() - q1.getX() * q2.getY() + q1.getY() * q2.getX() + q1.getZ() * q2.getW());
		
		Quaternion q = new Quaternion(tempW, tempX, tempY, tempZ);
		return q;
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