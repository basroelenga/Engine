package matrices;

import java.util.ArrayList;

public class MatrixObjectManager {

	private static ArrayList<MatrixObject> matrixList = new ArrayList<MatrixObject>();
	
	private MatrixObjectManager() {}
	
	public static void generateProjectionMatrix(String name, float fov, float zNear, float zFar, float width, float height)
	{
		matrixList.add(new PerspectiveMatrix(name, fov, zNear, zFar, width, height));
	}
	
	public static void generateOrthographicMatrix(String name, float zNear, float zFar, float left, float right, float top, float bottom)
	{
		matrixList.add(new OrthographicMatrix(name, zNear, zFar, left, right, top, bottom));
	}
	
	public static void generateOrthographicMatrix(String name, float lr, float tb, float nf)
	{
		matrixList.add(new OrthographicMatrix(name, lr, tb, nf));
	}
	
	public static void generateCubeMapMatrix(String name, float zNear, float zFar)
	{
		matrixList.add(new PerspectiveMatrix(name, 90, zNear, zFar, 1, 1));
	}
	
	/**
	 * Update an element of the selected matrix.
	 * @param name Name of the matrix.
	 * @param key The key value to be updated.
	 * @param value The updated value.
	 */
	public static void updateMatrixObject(String name, String key, float value)
	{
		
		MatrixObject mObj = getMatrixObject(name);
		
		mObj.updateHashMap(key, value);
		mObj.update();
	}
	
	/**
	 * Get a matrix object.
	 * @param name Name of the matrix object.
	 * @return The matrix object.
	 */
	public static MatrixObject getMatrixObject(String name)
	{
		
		for(MatrixObject obj : matrixList)
		{
			
			if(obj.getName().equals(name))
			{
				return obj;
			}
		}
		
		throw new RuntimeException("Matrix object does not exist");
	}
}
