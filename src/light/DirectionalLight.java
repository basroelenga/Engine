package light;

import java.util.HashMap;
import java.util.Map;

import camera.Camera;
import engine.Engine;
import engine.objects.Rectangle;
import fbo.FrameBufferObjectManager;
import graphics.Texture;
import math.Matrix4f;
import math.Quaternion;
import math.Vector3f;
import math.Vector4f;
import matrices.MatrixObject;
import matrices.MatrixObjectManager;
import shaders.Shader;

public class DirectionalLight extends LightObject{

	private Rectangle rect;
	
	public DirectionalLight(String name, Camera cam, float xDir, float yDir, float zDir, Vector3f lightColor)
	{
		
		this.name = name;
		
		this.xDir = xDir;
		this.yDir = yDir;
		this.zDir = zDir;
		
		this.cam = cam;
		this.lightColor = lightColor;
		
		ambIntensity = new Vector3f(0.2f, 0.2f, 0.2f);
		
		// Create the depth buffer for the light
		FrameBufferObjectManager.addShadowFrameBufferObject(name, 2048, 2048);
		depthBuffer = FrameBufferObjectManager.getFrameBuffer(name);
		
		// Rendering the depth map
		rect = new Rectangle("depth", new Texture("depth", depthBuffer.getDepthTexID()), Engine.getWidth() - Engine.getWidth() / 3, Engine.getHeight() - Engine.getHeight() / 3, Engine.getWidth() / 4, Engine.getHeight() / 4);
		
		// Calculate the initial direction of the light in polar coordinates
		getPolarDirection();
		
		// Calculate the width and height of the projection matrix used for the shadow calculation
		// Now the correct widths and heights can be obtained from the projection matrix.
		//MatrixObjectManager.getMatrixObject("projectionMatrixDefault").calculateWidthAndHeights(shadowDistance);
	}
	
	/**
	 * Get the direction of the light in polar coordinates
	 */
	private void getPolarDirection()
	{
		
		// Convert it to polar coordinates, this gives us the direction the light is looking at
		float radius = (float) Math.sqrt(Math.pow(xDir, 2) + Math.pow(yDir, 2) + Math.pow(zDir, 2));
		float theta = (float) (Math.acos(yDir / radius) * (180f / Math.PI));
		float phi = (float) (Math.atan2(xDir, zDir) * (180f / Math.PI));
		
		this.setRadius(radius);
		this.setTheta(theta);
		this.setPhi(phi);
	}

	@Override
	public void update() {
		
		// Update direction of light
		lightDir = new Vector3f(xDir, yDir, zDir);
		getPolarDirection();
				
		// Where are the 8 lightcorners of the perspective matrix in space
		// First the near points, these points are in a plane z-near distance from the position of the camera
		
		// Get the matrix object that needs to be used
		MatrixObject mObj = MatrixObjectManager.getMatrixObject("projectionMatrixDefault");
		
		HashMap<String, Vector3f> nearPoints = getCornerPoints("near", mObj, mObj.getzNear());
		HashMap<String, Vector3f> farPoints = getCornerPoints("far", mObj, shadowDistance);

		// Put all the points in the same map
		HashMap<String, Vector3f> cornerPoints = new HashMap<String, Vector3f>();
		cornerPoints.putAll(nearPoints);
		cornerPoints.putAll(farPoints);
		
		// Now a 3D box needs to be constructed around these points
		// First calculate the values for the orthographic projection matrix, this is done by getting the maximum and minimum values for x, y and z.
		float maxX = cornerPoints.entrySet().iterator().next().getValue().getX();
		float minX = cornerPoints.entrySet().iterator().next().getValue().getX();
		
		float maxY = cornerPoints.entrySet().iterator().next().getValue().getY();
		float minY = cornerPoints.entrySet().iterator().next().getValue().getY();
		
		float maxZ = cornerPoints.entrySet().iterator().next().getValue().getZ();
		float minZ = cornerPoints.entrySet().iterator().next().getValue().getZ();
		
		System.out.println("=======================");
		
		for(Map.Entry<String, Vector3f> entry : cornerPoints.entrySet())
		{
			
			System.out.println(entry.getValue().getZ());
			
			if(entry.getValue().getX() > maxX) maxX = entry.getValue().getX();
			if(entry.getValue().getX() < minX) minX = entry.getValue().getX();
			
			if(entry.getValue().getY() > maxY) maxY = entry.getValue().getY();
			if(entry.getValue().getY() < minY) minY = entry.getValue().getY();
			
			if(entry.getValue().getZ() > maxZ) maxZ = entry.getValue().getZ();
			if(entry.getValue().getZ() < minZ) minZ = entry.getValue().getZ();
		}
		
		float lr = maxX - minX;
		float tb = maxY - minY;
		float nf = maxZ - minZ;
		
		System.out.println(maxZ + " , " + minZ);
		System.out.println(lr + " , " + tb + " , " + nf);
		
		// The orthographic projection matrix around the perspective matrix then is
		MatrixObjectManager.generateOrthographicMatrix("lightdirmatrix", lr, tb, nf);
		projectionLightMatrix = MatrixObjectManager.getMatrixObject("lightdirmatrix").getMatrix();
		
		// First calculate the rotation of the camera from the lights point of view
		Quaternion firstRotation = new Quaternion(getPhi(), new Vector3f(0, 1, 0));
		Quaternion secondRotation = new Quaternion(getTheta(), new Vector3f(1, 0, 0));
		
		// Assume that only the orientation of the light is important
		// This means that shadows will only be casted around the zero point and will not be dynamic
		Quaternion result = Quaternion.multiply(firstRotation, secondRotation);
		Matrix4f lightRotationMatrix = result.toMatrix4f();
		
		// Use the position of the camera to place the view matrix in the right place
		Matrix4f lightPositionMatrix = cam.getPositionMatrix();

		// Calculate the light view matrix
		viewLightMatrix.setIdentity();
		
		viewLightMatrix.multiply(lightRotationMatrix);
		viewLightMatrix.multiply(lightPositionMatrix);
		
		
		
		/**
		// Calculate the forward vector
		Vector3f forwardVector = cameraRotationMatrix.multiply(new Vector4f(0, 0, -1, 0)).toVector3f();
		
		// Scale the forward vector with the far plane
		Vector3f toFarPlane = new Vector3f(forwardVector);
		toFarPlane.scale(shadowDistance);

		// Scale the forward vector with the near plane
		Vector3f toNearPlane = new Vector3f(forwardVector);
		toNearPlane.scale(MatrixObjectManager.getMatrixObject("projectionMatrixDefault").getzNear());
		
		// Calculate the centers by adding the forward vectors to the camera position
		Vector3f centerFar = Vector3f.add(toFarPlane, CameraManager.getCamera("cam").getPosition());
		Vector3f centerNear = Vector3f.add(toNearPlane, CameraManager.getCamera("cam").getPosition());
		
		// Calculate the up, down, left and right vectors
		Vector3f upVector = cameraRotationMatrix.multiply(new Vector4f(0, 1, 0, 0)).toVector3f();
		Vector3f downVector = new Vector3f(-upVector.getX(), -upVector.getY(), -upVector.getZ());
		
		Vector3f rightVector = Vector3f.cross(forwardVector, upVector);
		Vector3f leftVector = new Vector3f(-rightVector.getX(), -rightVector.getY(), -rightVector.getZ());
		
		float farHeight = MatrixObjectManager.getMatrixObject("projectionMatrixDefault").getFarHeight();
		float nearHeight = MatrixObjectManager.getMatrixObject("projectionMatrixDefault").getNearHeight();
		
		float farWidth = MatrixObjectManager.getMatrixObject("projectionMatrixDefault").getFarWidth();
		float nearWidth = MatrixObjectManager.getMatrixObject("projectionMatrixDefault").getNearWidth();
		
		// Calculate the far top, far bottom, near top and near bottom vectors
		Vector3f farTop = Vector3f.add(centerFar, new Vector3f(upVector.getX() * farHeight / 2, upVector.getY() * farHeight / 2, upVector.getZ() * farHeight / 2));
		Vector3f nearTop = Vector3f.add(centerNear, new Vector3f(upVector.getX() * nearHeight / 2, upVector.getY() * nearHeight / 2, upVector.getZ() * nearHeight / 2));

		Vector3f farBottom = Vector3f.add(centerFar, new Vector3f(downVector.getX() * farHeight / 2, downVector.getY() * farHeight / 2, downVector.getZ() * farHeight / 2));
		Vector3f nearBottom = Vector3f.add(centerNear, new Vector3f(downVector.getX() * nearHeight / 2, downVector.getY() * nearHeight / 2, downVector.getZ() * nearHeight / 2));
		*/
	}
	
	/**
	 * Calculate the corner points at a certain z distance of a matrix object (perspective matrix object).
	 * @param mObj The matrix object.
	 * @param zDistance The z distance.
	 * @return A hashmap containing the corner points.
	 */
	private HashMap<String, Vector3f> getCornerPoints(String key, MatrixObject mObj, float zDistance)
	{
		
		HashMap<String, Vector3f> cornerMap = new HashMap<String, Vector3f>();
		
		// The distance from the near center to the actual points are
		float fov = mObj.getFov();
		float aspect = mObj.getAspect();

		float zNearWidth = zDistance * (float) Math.tan(Math.toRadians(fov / 2f));
		float zNearHeight = zNearWidth / aspect;
		
		// The first four point in the z-near plane then are (if there is no camera rotation)
		Vector3f nearLT = new Vector3f(-zNearWidth, zNearHeight, zDistance);
		Vector3f nearRT = new Vector3f(zNearWidth, zNearHeight, zDistance);
		
		Vector3f nearLB = new Vector3f(-zNearWidth, -zNearHeight, zDistance);
		Vector3f nearRB = new Vector3f(zNearWidth, -zNearHeight, zDistance);
		
		// Rotate each of the points by using the camera rotation matrix
		cornerMap.put(key + "LT", cam.getRotationMatrix().multiply(new Vector4f(nearLT, 0f)).toVector3f());
		cornerMap.put(key + "RT", cam.getRotationMatrix().multiply(new Vector4f(nearRT, 0f)).toVector3f());
		cornerMap.put(key + "LB", cam.getRotationMatrix().multiply(new Vector4f(nearLB, 0f)).toVector3f());
		cornerMap.put(key + "RB", cam.getRotationMatrix().multiply(new Vector4f(nearRB, 0f)).toVector3f());
		
		return cornerMap;
	}
	
	@Override
	public void uploadToShader(int light, Shader uShader) {
		
		uShader.uploadVector3f(lightDir, uShader.getDirectionalLightDirectionLocList().get(light));
		uShader.uploadVector3f(lightColor, uShader.getDirectionalLightColorLocList().get(light));
		uShader.uploadVector3f(ambIntensity, uShader.getDirectionalAmbIntensityLocList().get(light));
	}

	@Override
	public void render() {
		
		rect.update();
		rect.render();
	}
}