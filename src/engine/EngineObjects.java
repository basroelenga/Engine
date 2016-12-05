package engine;

import graphics.Texture;
import math.Matrix4f;
import math.Vector4f;
import shaders.Shader;

public abstract class EngineObjects {

	// Some general properties of objects:
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
	
	// Their rotation
	protected float xa;
	protected float ya;
	protected float za;
	
	// Requirements for rendering (The objects VAO ID, shader, shader properties and matrices)
	protected int vaoID;
	
	protected Shader shader;
	
	protected Texture tex;
	
	protected Vector4f RGBAcolor;
	
	protected Matrix4f modelMatrix = new Matrix4f();
	protected Matrix4f viewMatrix;
	protected Matrix4f projectionMatrix;
	protected Matrix4f normalMatrix;
	
	// The update and render function
	public abstract void update();
	public abstract void render();
	
	// The setter functions for the position, velocities, scale and rotation
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
	public float getXa() {
		return xa;
	}
	public void setXa(float xa) {
		this.xa = xa;
	}
	public float getYa() {
		return ya;
	}
	public void setYa(float ya) {
		this.ya = ya;
	}
	public float getZa() {
		return za;
	}
	public void setZa(float za) {
		this.za = za;
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
	public Texture getTex() {
		return tex;
	}
	public Vector4f getRGBAcolor() {
		return RGBAcolor;
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