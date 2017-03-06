package light;

import camera.Camera;
import engine.Engine;
import engine.objects.Rectangle;
import fbo.FrameBufferObjectManager;
import graphics.Texture;
import math.Quaternion;
import math.Vector3f;
import math.Vector4f;
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
				
		// First calculate the rotation of the camera from the lights point of view
		Quaternion firstRotation = new Quaternion(getPhi(), new Vector3f(0, 1, 0));
		Quaternion secondRotation = new Quaternion(getTheta(), new Vector3f(1, 0, 0));
		
		// Assume that only the orientation of the light is important
		// This means that shadows will only be casted around the zero point and will not be dynamic
		Quaternion result = Quaternion.multiply(firstRotation, secondRotation);
		viewLightMatrix = result.toMatrix4f();
		
		// This means that the projection matrix is also constant
		MatrixObjectManager.generateOrthographicMatrix("lightdirmatrix", -2, 5, -2, 2, -2, 2);
		projectionLightMatrix = MatrixObjectManager.getMatrixObject("lightdirmatrix").getMatrix();
		
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
	
	private Vector4f calculateLightCorner(Vector3f startPoint, Vector3f direction, float width)
	{
		
		Vector3f point3f = Vector3f.add(startPoint, new Vector3f(direction.getX() * width, direction.getY() * width, direction.getZ() * width));
		Vector4f point4f = new Vector4f(point3f.getX(), point3f.getY(), point3f.getZ(), 1f);
		
		return new Vector4f();
		
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