package matrices;

import math.Matrix4f;

public class OrthographicMatrix extends MatrixObject{
	
	public OrthographicMatrix(String name, float zNear, float zFar, float left, float right, float top, float bottom)
	{
		
		this.name = name;
		
		this.zNear = zNear;
		this.zFar = zFar;
		
		this.left = left;
		this.right = right;
		
		this.top = top;
		this.bottom = bottom;
		
		matrix = new Matrix4f();
		
		generateHashMap();
		calculateOrthographicMatrix();
	}
	
	private void calculateOrthographicMatrix()
	{
		
		matrix.getElements()[0] = 2 / (matrixMap.get("right") - matrixMap.get("left"));
		matrix.getElements()[5] = 2 / (matrixMap.get("top") - matrixMap.get("bottom"));
		matrix.getElements()[10] = -2 / (matrixMap.get("zFar") - matrixMap.get("zNear"));
		matrix.getElements()[15]= 1;
		
		matrix.getElements()[3] = -((matrixMap.get("right") + matrixMap.get("left")) / (matrixMap.get("right") - matrixMap.get("left")));
		matrix.getElements()[7] = -((matrixMap.get("top") + matrixMap.get("bottom")) / (matrixMap.get("top") - matrixMap.get("bottom")));
		matrix.getElements()[11] = (matrixMap.get("zFar") + matrixMap.get("zNear")) / (matrixMap.get("zFar") - matrixMap.get("zNear"));
	}

	@Override
	public void update() {
		
		calculateOrthographicMatrix();
	}
}