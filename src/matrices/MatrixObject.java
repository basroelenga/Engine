package matrices;

import java.util.HashMap;

import math.Matrix4f;

public abstract class MatrixObject {

	protected Matrix4f matrix;
	
	protected String name;
	
	protected HashMap<String, Float> matrixMap;
	
	protected float fov;
	protected float aspect;
	
	protected float zNear;
	protected float zFar;
	
	protected float width;
	protected float height;

	protected float left;
	protected float right;
	
	protected float top;
	protected float bottom;
	
	protected void generateHashMap()
	{
		
		matrixMap = new HashMap<String, Float>();
		
		matrixMap.put("FOV", fov);
		matrixMap.put("aspect", aspect);
		
		matrixMap.put("zNear", zNear);
		matrixMap.put("zFar", zFar);
		
		matrixMap.put("width", width);
		matrixMap.put("height", height);
		
		matrixMap.put("left", left);
		matrixMap.put("right", right);
		
		matrixMap.put("top", top);
		matrixMap.put("bottom", bottom);
	}

	public void updateHashMap(String key, float value)
	{
		
		matrixMap.put(key, value);
	}
	
	public abstract void update();

	public Matrix4f getMatrix() {
		return matrix;
	}

	public String getName() {
		return name;
	}

	// The getter function return the values of the hashmap instead of the float instances.
	public float getFov() {
		return matrixMap.get("FOV");
	}

	public float getAspect() {
		return matrixMap.get("aspect");
	}

	public float getzNear() {
		return matrixMap.get("zNear");
	}

	public float getzFar() {
		return matrixMap.get("zFar");
	}

	public float getWidth() {
		return matrixMap.get("width");
	}

	public float getHeight() {
		return matrixMap.get("height");
	}

	public float getLeft() {
		return matrixMap.get("left");
	}

	public float getRight() {
		return matrixMap.get("right");
	}

	public float getTop() {
		return matrixMap.get("top");
	}

	public float getBottom() {
		return matrixMap.get("bottom");
	}
}