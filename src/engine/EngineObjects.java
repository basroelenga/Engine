package engine;

import java.util.LinkedHashMap;

import fbo.FrameBufferObject;
import graphics.Texture;
import math.Matrix4f;
import math.Vector4f;
import models.Model;
import shaders.Shader;
import shaders.ShaderManager;

public abstract class EngineObjects {

	// Some general properties of objects:
	// The "name" of the object
	protected String name;
	
	// Their position
	protected float x = 0f;
	protected float y = 0f;
	protected float z = 0f;
	
	// Their velocities
	protected float vx;
	protected float vy;
	protected float vz;
	
	// Their scaling
	protected float xs = 1f;
	protected float ys = 1f;
	protected float zs = 1f;
	
	// Their rotation
	protected float xa = 0f;
	protected float ya = 0f;
	protected float za = 0f;
	
	// Mass or weight of an object
	protected float mass;
	
	// Object can contain a model
	protected Model model;
	
	// Particle specific parameters
	// Particle type
	protected String pType;
	
	// Lifetime and time out
	protected float timeOut;
	protected float lifeTime  = 0f;
	
	// Requirements for rendering (The objects VAO ID, FBO, shader, shader properties and matrices)
	protected int vaoID;
	protected int amountOfTriangles;
	
	// Requirements for shadow rendering
	protected boolean renderDepthMap = false;
	protected Shader depthShader = ShaderManager.getShader("depth");
	
	// Determine which fbo to render the scene
	protected FrameBufferObject fbo;
	protected Shader shader;
	
	// Textures to be used
	// 1: The model texture
	// 2: The depth texture used for shadow rendering
	protected LinkedHashMap<String, Texture> textureMap = new LinkedHashMap<String, Texture>();
	
	protected Texture tex;
	protected Texture depthTex;
	
	// The default color is white
	protected Vector4f RGBAcolor = new Vector4f(1.0f, 1.0f, 1.0f, 1.0f);
	
	protected Matrix4f modelMatrix = new Matrix4f();
	protected Matrix4f viewMatrix = new Matrix4f();
	protected Matrix4f projectionMatrix = new Matrix4f();
	protected Matrix4f normalMatrix = new Matrix4f();
	
	protected Matrix4f MVP = new Matrix4f();
	
	// The update and render function
	public abstract void update();
	public abstract void render();
	
	// Prerender function which renders depth maps from the objects for all directional lights
	public abstract void prerender();
	
	// Calculate the MVP on the CPU
	public Matrix4f getMVP() {
		
		MVP.setIdentity();
		
		MVP.multiply(projectionMatrix);
		MVP.multiply(viewMatrix);
		MVP.multiply(modelMatrix);
		
		MVP.transpose();
		
		return MVP;
	}
	
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
	public float getMass() {
		return mass;
	}
	public Model getModel() {
		return model;
	}
	public String getParticleType()	{
		return pType;
	}
	public float getLifeTime() {
		return lifeTime;
	}
	public float getTimeOut() {
		return timeOut;
	}
	public String getName() {
		return name;
	}
	public int getVaoID() {
		return vaoID;
	}
	public int getAmountOfTriangles() {
		return amountOfTriangles;
	}
	public FrameBufferObject getFbo() {
		return fbo;
	}
	public void setFbo(FrameBufferObject fbo) {
		this.fbo = fbo;
	}
	public boolean isRenderDepthMap() {
		return renderDepthMap;
	}
	public void setRenderDepthMap(boolean renderDepthMap) {
		this.renderDepthMap = renderDepthMap;
	}
	public Shader getShader() {
		return shader;
	}
	public void setTexture(Texture tex)
	{
		this.tex = tex;
	}
	public Texture getTex() {
		return tex;
	}
	public Vector4f getRGBAcolor() {
		return RGBAcolor;
	}
	public void setRGBAcolor(Vector4f rGBAcolor) {
		RGBAcolor = rGBAcolor;
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