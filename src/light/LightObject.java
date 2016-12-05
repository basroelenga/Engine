package light;

import cam.Camera;
import engine.Engine;
import math.Matrix4f;
import math.Vector3f;
import shaders.Shader;

public abstract class LightObject {

	// Some general properties of the light:
	// The "name" of the object
	protected String name;
	
	// Their position
	protected float x;
	protected float y;
	protected float z;
	
	// Their velocities
	protected float vx;
	protected float vy;
	protected float vz;
	
	// Their scaling
	protected float xs;
	protected float ys;
	protected float zs;
	
	// Should the position of the light be visible
	protected boolean show = false;
	
	// Requirements for rendering (The objects VAO ID, shader, shader properties and matrices)
	// This is for rendering the position of the light in the 3D space
	protected int vaoID;
	
	protected Shader shader;
	
	// The light is always displayed in perspective space
	protected Matrix4f modelMatrix = new Matrix4f();
	protected Matrix4f viewMatrix = Camera.getViewMatrix();
	protected Matrix4f projectionMatrix = Engine.projMatrix;
	protected Matrix4f normalMatrix;
	
	// The update and render function
	public abstract void update();
	public abstract void render();
	
	// The setter functions for the position, velocities, scale
	// The getter functions for the position, velocities, scale, rotation, name and render requirements
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
	public Vector3f getPosition() {
		return new Vector3f(x, y, z);
	}
	public float getVx() {
		return vx;
	}
	public void setVx(float vx) {
		this.vx = vx;
	}
	public float getVy() {
		return vy;
	}
	public void setVy(float vy) {
		this.vy = vy;
	}
	public float getVz() {
		return vz;
	}
	public void setVz(float vz) {
		this.vz = vz;
	}
	public float getXs() {
		return xs;
	}
	public void setXs(float xs) {
		this.xs = xs;
	}
	public float getYs() {
		return ys;
	}
	public void setYs(float ys) {
		this.ys = ys;
	}
	public float getZs() {
		return zs;
	}
	public void setZs(float zs) {
		this.zs = zs;
	}
	public boolean isShow() {
		return show;
	}
	public void setShow(boolean show) {
		this.show = show;
	}
	public String getName() {
		return name;
	}
	public int getVaoID() {
		return vaoID;
	}
	public Shader getShader() {
		return shader;
	}
	public Matrix4f getModelMatrix() {
		return modelMatrix;
	}
	public Matrix4f getViewMatrix() {
		return viewMatrix;
	}
	public Matrix4f getProjectionMatrix() {
		return projectionMatrix;
	}
	public Matrix4f getNormalMatrix() {
		return normalMatrix;
	}
}