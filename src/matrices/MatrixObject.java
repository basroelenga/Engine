package matrices;

import java.util.HashMap;

import math.Matrix4f;

public abstract class MatrixObject {

	protected Matrix4f matrix = new Matrix4f();;
	
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
	
	// This is only applicable for a projection matrix, since the width and height for an orthographic matrix are the same in the near and far plane
	// which means that they are already described by the width and height variables in this interface. These values can be used to construct a box
	// around the projection matrix and so construct an orthographic matrix which can be used to render the depth maps of light.
	protected float farHeight;
	protected float farWidth;
	
	protected float nearHeight;
	protected float nearWidth;
		
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
		
		matrixMap.put("rl", Math.abs(right - left));
		matrixMap.put("tb", Math.abs(top - bottom));
		matrixMap.put("fn", Math.abs(zFar - zNear));
	}

	public void updateHashMap(String key, float value)
	{
		
		matrixMap.put(key, value);
	}
	
	/**
	 * Calculate the width and heights of the projection matrix.
	 * @param shadowDistance The shadow distance, this is taken as zFar.
	 */
	public void calculateWidthAndHeights(float shadowDistance)
	{
		
		farWidth = 2 * shadowDistance * (float) Math.tan(Math.toRadians(fov / 2));
		nearWidth = 2 * zNear * (float) Math.tan(Math.toRadians(fov / 2));
		
		farHeight = farWidth / aspect;
		nearHeight = nearWidth / aspect;
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

	public float getFarHeight() {
		return farHeight;
	}

	public float getFarWidth() {
		return farWidth;
	}

	public float getNearHeight() {
		return nearHeight;
	}

	public float getNearWidth() {
		return nearWidth;
	}
}