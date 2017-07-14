package shadow;

import java.util.LinkedHashMap;
import java.util.Map;

import camera.Camera;
import light.DirectionalLight;
import math.Matrix4f;
import math.Quaternion;
import math.Vector3f;
import math.Vector4f;
import matrices.MatrixObject;
import matrices.MatrixObjectManager;

public class ShadowBox {

	private MatrixObject perspectiveMatrix;
	private DirectionalLight dLight;
	
	private Camera camera;
		
	/**
	 * Create a shadow box for a directional light
	 * @param lightRotationMatrix
	 * @param perspectiveMatrix
	 * @param camera
	 */
	public ShadowBox(DirectionalLight dLight, MatrixObject perspectiveMatrix, Camera camera)
	{
		
		this.perspectiveMatrix = perspectiveMatrix;
		this.dLight = dLight;
		
		this.camera = camera;

		// The frustum of the camera will be treated as a box (for simplification). This means the box will consist out of the
		// top, bottom, left, right of the far plane. The following function will obtain these points
		LinkedHashMap<String, Vector3f> perspectiveBoxPoints = getPerspectivePoints();
		
		// Print these points to confirm
		for(Map.Entry<String, Vector3f> entry : perspectiveBoxPoints.entrySet())
		{
			
			entry.getValue().print();
		}
		
		// Calculate the difference in angle between the rotation of the light and the rotation of the camera
		float theta_diff = dLight.getTheta() - camera.getOrientation().getY();
		float phi_diff = dLight.getPhi() - camera.getOrientation().getX();
		
		System.out.println(theta_diff + " , " + phi_diff);
		
		// Construct the rotation matrix for the difference in rotation between the light and camera frame
		Quaternion firstRotation = new Quaternion(theta_diff, new Vector3f(0, -1, 0));
		Quaternion secondRotation = new Quaternion(phi_diff, new Vector3f(1, 0, 0));
		
		Quaternion result = Quaternion.multiply(firstRotation, secondRotation);
		Matrix4f differenceRotationMatrix = result.toMatrix4f();
		
		// Rotate the points by this rotation matrix
		for(Map.Entry<String, Vector3f> entry : perspectiveBoxPoints.entrySet())
		{
		
			// Get the point
			Vector3f point = entry.getValue();
			
			// Rotate the point
			Vector3f rPoint = differenceRotationMatrix.multiply(new Vector4f(point, 0)).toVector3f();
			entry.setValue(rPoint);
		}
		
		// Now a 3D box needs to be constructed around these points
		// First calculate the values for the orthographic projection matrix, this is done by getting the maximum and minimum values for x, y and z.
		float maxX = perspectiveBoxPoints.entrySet().iterator().next().getValue().getX();
		float minX = perspectiveBoxPoints.entrySet().iterator().next().getValue().getX();
		
		float maxY = perspectiveBoxPoints.entrySet().iterator().next().getValue().getY();
		float minY = perspectiveBoxPoints.entrySet().iterator().next().getValue().getY();
		
		float maxZ = perspectiveBoxPoints.entrySet().iterator().next().getValue().getZ();
		float minZ = perspectiveBoxPoints.entrySet().iterator().next().getValue().getZ();
		
		System.out.println("=======================");
		
		for(Map.Entry<String, Vector3f> entry : perspectiveBoxPoints.entrySet())
		{
			
			//System.out.println(entry.getValue().getZ());
			
			if(entry.getValue().getX() > maxX) maxX = entry.getValue().getX();
			if(entry.getValue().getX() < minX) minX = entry.getValue().getX();
			
			if(entry.getValue().getY() > maxY) maxY = entry.getValue().getY();
			if(entry.getValue().getY() < minY) minY = entry.getValue().getY();
			
			if(entry.getValue().getZ() > maxZ) maxZ = entry.getValue().getZ();
			if(entry.getValue().getZ() < minZ) minZ = entry.getValue().getZ();
		}
		
		
		float lr = maxX - minX;
		float tb = maxY - minY;
		float fn = maxZ - minZ;
		
		// Generate the box
		MatrixObjectManager.generateOrthographicMatrix("shadowbox", lr, tb, fn);

		// Print rotated values
		for(Map.Entry<String, Vector3f> entry : perspectiveBoxPoints.entrySet())
		{
			
			entry.getValue().print();
		}
		
	}
	
	/**
	 * Get the corner box points of the perspective matrix
	 * @return The corner points
	 */
	private LinkedHashMap<String, Vector3f> getPerspectivePoints()
	{
	
		LinkedHashMap<String, Vector3f> cornerPoints = new LinkedHashMap<String, Vector3f>();
		
		float fov = perspectiveMatrix.getFov();
		float aspect = perspectiveMatrix.getAspect();

		float zDistance = perspectiveMatrix.getzFar();
		
		// Set the values for the far plane
		float zFarWidth = zDistance * (float) Math.tan(Math.toRadians(fov / 2f));
		float zFarHeight = zFarWidth / aspect;
		
		// The first four point in the z-far plane then are (this is in model space)
		Vector3f farLT = new Vector3f(-zFarWidth, zFarHeight, zDistance);
		Vector3f farRT = new Vector3f(zFarWidth, zFarHeight, zDistance);
		
		Vector3f farLB = new Vector3f(-zFarWidth, -zFarHeight, zDistance);
		Vector3f farRB = new Vector3f(zFarWidth, -zFarHeight, zDistance);
		
		cornerPoints.put("far_LT", farLT);
		cornerPoints.put("far_RT", farRT);
		cornerPoints.put("far_LB", farLB);
		cornerPoints.put("far_RB", farRB);
		
		// The points in the near plane are the same except the zDistance changes
		zDistance = perspectiveMatrix.getzNear();
		
		Vector3f nearLT = new Vector3f(-zFarWidth, zFarHeight, zDistance);
		Vector3f nearRT = new Vector3f(zFarWidth, zFarHeight, zDistance);
		
		Vector3f nearLB = new Vector3f(-zFarWidth, -zFarHeight, zDistance);
		Vector3f nearRB = new Vector3f(zFarWidth, -zFarHeight, zDistance);
		
		cornerPoints.put("near_LT", nearLT);
		cornerPoints.put("near_RT", nearRT);
		cornerPoints.put("near_LB", nearLB);
		cornerPoints.put("near_RB", nearRB);
		
		return cornerPoints;
	}
	
	private LinkedHashMap<String, Vector3f> getOrthographicPoints(float lr, float tb, float nf)
	{
		
		LinkedHashMap<String, Vector3f> cornerPoints = new LinkedHashMap<String, Vector3f>();

		// The near points
		Vector3f nearLT = new Vector3f(-0.5f * lr, 0.5f * tb, 1f);
		Vector3f nearRT = new Vector3f(0.5f * lr, 0.5f * tb, 1f);
		
		Vector3f nearLB = new Vector3f(-0.5f * lr, -0.5f * tb, 1f);
		Vector3f nearRB = new Vector3f(0.5f * lr, -0.5f * tb, 1f);
		
		cornerPoints.put("near_LT", nearLT);
		cornerPoints.put("near_RT", nearRT);
		cornerPoints.put("near_LB", nearLB);
		cornerPoints.put("near_RB", nearRB);
		
		// The far points
		Vector3f farLT = new Vector3f(-0.5f * lr, 0.5f * tb, nf);
		Vector3f farRT = new Vector3f(0.5f * lr, 0.5f * tb, nf);
		
		Vector3f farLB = new Vector3f(-0.5f * lr, -0.5f * tb, nf);
		Vector3f farRB = new Vector3f(0.5f * lr, -0.5f * tb, nf);
		
		cornerPoints.put("far_LT", farLT);
		cornerPoints.put("far_RT", farRT);
		cornerPoints.put("far_LB", farLB);
		cornerPoints.put("far_RB", farRB);
		
		return cornerPoints;
	}
}