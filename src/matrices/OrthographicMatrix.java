package matrices;

public class OrthographicMatrix extends MatrixObject{
	
	private boolean scale = false;
	
	public OrthographicMatrix(String name, float zNear, float zFar, float left, float right, float top, float bottom)
	{
		
		this.name = name;
		
		this.zNear = zNear;
		this.zFar = zFar;
		
		this.left = left;
		this.right = right;
		
		this.top = top;
		this.bottom = bottom;
				
		generateHashMap();
		calculateOrthographicMatrix();
	}
	
	public OrthographicMatrix(String name, float lr, float tb, float fn)
	{
		
		this.name = name;
		
		this.zNear = - fn / 2;
		this.zFar = fn / 2;
		
		this.left = - lr / 2;
		this.right = lr / 2;
		
		this.top = tb / 2;
		this.bottom = -tb / 2;
		
		scale = true;
		
		generateHashMap();
		calculateScaleOrthographicMatrix();
	}
	
	private void calculateOrthographicMatrix()
	{
		
		matrix.setIdentity();
		
		matrix.getElements()[0] = 2 / (matrixMap.get("right") - matrixMap.get("left"));
		matrix.getElements()[5] = 2 / (matrixMap.get("top") - matrixMap.get("bottom"));
		matrix.getElements()[10] = -2 / (matrixMap.get("zFar") - matrixMap.get("zNear"));
		matrix.getElements()[15]= 1;
		
		matrix.getElements()[3] = -((matrixMap.get("right") + matrixMap.get("left")) / (matrixMap.get("right") - matrixMap.get("left")));
		matrix.getElements()[7] = -((matrixMap.get("top") + matrixMap.get("bottom")) / (matrixMap.get("top") - matrixMap.get("bottom")));
		matrix.getElements()[11] = (matrixMap.get("zFar") + matrixMap.get("zNear")) / (matrixMap.get("zFar") - matrixMap.get("zNear"));
	}
	
	private void calculateScaleOrthographicMatrix()
	{
		
		matrix.setIdentity();
		
		matrix.getElements()[0] = 2 / matrixMap.get("rl");
		matrix.getElements()[5] = 2 / matrixMap.get("tb");
		matrix.getElements()[10] = -2 / matrixMap.get("fn");
		matrix.getElements()[15]= 1;
	}

	@Override
	public void update() {
		
		if(scale) calculateScaleOrthographicMatrix();
		else calculateOrthographicMatrix();
	}
}