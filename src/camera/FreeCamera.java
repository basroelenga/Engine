package camera;

import math.Matrix4f;
import math.Quaternion;
import math.Vector3f;

public class FreeCamera extends CameraObject{

	/**
	 * Create a FPS camera at the specified position and angle.
	 * @param name The name of the camera object.
	 * @param position The position of the camera object.
	 * @param angles The orientation of the camera object.
	 */
	public FreeCamera(String name, float x, float y, float z, float yaw, float pitch, float roll)
	{
	
		this.name = name;
		
		this.x = x;
		this.y = y;
		this.z = z;
		
		this.yaw = yaw;
		this.pitch = pitch;
		this.roll = roll;
		
		this.position = new Vector3f(x, y, z);
		this.orientation = new Vector3f(yaw, pitch, roll);
	}
	
	/**
	 * Update the view matrix of the camera object.
	 */
	@Override
	public void update()
	{
				
		updateOrientationAndPosition();		
	}
	
	/**
	 * Update the orientation of the camera object to the view matrix using quaternions.
	 */
	private void updateOrientationAndPosition()
	{
		
		viewMatrix.setIdentity();
		
		// Update position and orientation
		position = new Vector3f(x, y, z);
		orientation = new Vector3f(yaw, pitch, roll);
		
		// Update quaternions and multiply them
		rotateTheta.updateQuaternion(yaw, new Vector3f(0, -1, 0));
		rotatePhi.updateQuaternion(pitch, new Vector3f(-1, 0, 0));
		
		Quaternion result = Quaternion.multiply(rotatePhi, rotateTheta);
		result.normalize();
		
		// Obtain the rotation matrix from the final quaternion
		viewRotationMatrix = result.toMatrix4f();
		viewMatrix.multiply(viewRotationMatrix);
		
		// Update the position to the view matrix
		viewPositionMatrix = new Matrix4f();
		viewPositionMatrix.translate(position);

		viewMatrix.multiply(viewPositionMatrix);
	}
}