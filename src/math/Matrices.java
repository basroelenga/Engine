package math;

public class Matrices {

	private static float zNear = 0.1f;
	private static float zFar = 1000f;
	
	private static float fov = 60f;
	
	private Matrices(){}
	
	public static Matrix4f getProjectionMatrix(float width, float height)
	{
		
		Matrix4f projMatrix = new Matrix4f();
		
		float aspect = width / height;
		float f = 1.0f / (float) Math.tan(Math.toRadians(fov / 2));
		
		float fLenght = zFar - zNear;
		
		projMatrix.getElements()[0] = f / aspect;
		projMatrix.getElements()[5] = f;
		projMatrix.getElements()[10] = - (zFar + zNear) / fLenght;
		projMatrix.getElements()[11] = - (2 * zFar * zNear) / fLenght;
		projMatrix.getElements()[14] = -1f;
		projMatrix.getElements()[15] = 0f;
		
		return projMatrix;
	}
	
	public static Matrix4f getOrthographicMatrix(float left, float right, float top, float bottom)
	{
		
		Matrix4f orthoMatrix = new Matrix4f();

		zFar = 1f;
		zNear = -1f;
	
		orthoMatrix.getElements()[0] = 2 / (right - left);
		orthoMatrix.getElements()[5] = 2 / (top - bottom);
		orthoMatrix.getElements()[10] = -2 / (zFar - zNear);
		orthoMatrix.getElements()[15]= 1;
		
		orthoMatrix.getElements()[3] = -((right + left) / (right - left));
		orthoMatrix.getElements()[7] = -((top + bottom) / (top - bottom));
		orthoMatrix.getElements()[11] = (zFar + zNear) / (zFar - zNear);

		return orthoMatrix;
	}
}