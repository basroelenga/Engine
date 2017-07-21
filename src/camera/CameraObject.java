package camera;

import math.Matrix4f;
import math.Quaternion;
import math.Vector3f;

public abstract class CameraObject {

	protected String name;
	
	protected float x;
	protected float y;
	protected float z;
	
	protected float yaw;
	protected float pitch;
	protected float roll;
	
	// Ordering: yaw pitch roll
	protected Vector3f position;
	protected Vector3f orientation;
	
	protected Quaternion rotateTheta = new Quaternion();
	protected Quaternion rotatePhi = new Quaternion();
	
	protected Matrix4f viewRotationMatrix;
	protected Matrix4f viewPositionMatrix;
	
	protected Matrix4f viewMatrix = new Matrix4f();
	
	public abstract void update();
	
	/**
	 * Print the current orientation of the camera object.
	 */
	public void printOrientation()
	{
		System.out.println("theta: " + orientation.getY() + " ," + "phi: " + orientation.getX());
	}
	
	/**
	 * Print the current position of the camera object.
	 */
	public void printPosition()
	{
		System.out.println("x: " + position.getX() + " ," + "y: " + position.getY() + " ," + "z:" + position.getZ());
	}
	
	public String getName()	{
		return name;
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	public float getZ() {
		return z;
	}

	public void setZ(float z) {
		this.z = z;
	}

	public float getYaw() {
		return yaw;
	}

	public void setYaw(float yaw) {
		this.yaw = yaw;
	}

	public float getPitch() {
		return pitch;
	}

	public void setPitch(float pitch) {
		this.pitch = pitch;
	}

	public float getRoll() {
		return roll;
	}

	public void setRoll(float roll) {
		this.roll = roll;
	}

	public Vector3f getPosition() {
		return position;
	}

	public void setPosition(Vector3f position) {
		this.position = position;
	}

	public Vector3f getOrientation() {
		return orientation;
	}

	public void setOrientation(Vector3f orientation) {
		this.orientation = orientation;
	}

	public Quaternion getRotateTheta() {
		return rotateTheta;
	}

	public void setRotateTheta(Quaternion rotateTheta) {
		this.rotateTheta = rotateTheta;
	}

	public Quaternion getRotatePhi() {
		return rotatePhi;
	}

	public void setRotatePhi(Quaternion rotatePhi) {
		this.rotatePhi = rotatePhi;
	}

	public Matrix4f getViewRotationMatrix() {
		return viewRotationMatrix;
	}

	public void setViewRotationMatrix(Matrix4f viewRotationMatrix) {
		this.viewRotationMatrix = viewRotationMatrix;
	}

	public Matrix4f getViewPositionMatrix() {
		return viewPositionMatrix;
	}

	public void setViewPositionMatrix(Matrix4f viewPositionMatrix) {
		this.viewPositionMatrix = viewPositionMatrix;
	}

	public Matrix4f getViewMatrix() {
		return viewMatrix;
	}

	public void setViewMatrix(Matrix4f viewMatrix) {
		this.viewMatrix = viewMatrix;
	}

	public void setName(String name) {
		this.name = name;
	}
}