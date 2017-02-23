package matrices;

import math.Matrix4f;

public class ProjectionMatrix extends MatrixObject{
	
	/**
	 * Create a projection matrix object, this object contains the various properties of the projection matrix
	 * and also contains the matrix itself.
	 * @param name The name of the object.
	 * @param fov The field of view.
	 * @param zNear Distance zNear.
	 * @param zFar Distance zFar. 
	 * @param width Width of the window.
	 * @param height Height of the window.
	 */
	public ProjectionMatrix(String name, float fov, float zNear, float zFar, float width, float height)
	{
				
		this.name = name;
		this.fov = fov;
		
		this.zNear = zNear;
		this.zFar = zFar;
		
		this.width = width;
		this.height = height;
		
		aspect = width / height;
		
		matrix = new Matrix4f();
		
		generateHashMap();
		calculateProjectionMatrix();
	}
	
	/**
	 * Calculate the projection matrix.
	 */
	private void calculateProjectionMatrix()
	{
	
		float f = 1.0f / (float) Math.tan(Math.toRadians(matrixMap.get("FOV") / 2));
		
		float fLenght = matrixMap.get("zFar") - matrixMap.get("zNear");
		
		matrix.getElements()[0] = f / matrixMap.get("aspect");
		matrix.getElements()[5] = f;
		matrix.getElements()[10] = - (matrixMap.get("zFar") + matrixMap.get("zNear")) / fLenght;
		matrix.getElements()[11] = - (2 * matrixMap.get("zFar") * matrixMap.get("zNear")) / fLenght;
		matrix.getElements()[14] = -1f;
		matrix.getElements()[15] = 0f;
	}

	@Override
	public void update() {
		
		calculateProjectionMatrix();
	}
}