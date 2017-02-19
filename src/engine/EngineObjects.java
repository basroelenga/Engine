package engine;

import fbo.FrameBufferObject;
import fbo.FrameBufferObjectManager;
import graphics.Texture;
import light.LightManager;
import light.LightObject;
import math.Matrix4f;
import math.Vector4f;
import models.Model;
import shaders.Shader;
import shaders.ShaderManager;
import shapes.Point;
import shapes.Quad;
import shapes.Triangle;
import utils.DrawShapes;

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
	protected float xa = 0f;
	protected float ya = 0f;
	protected float za = 0f;
	
	// Mass or weight of an object
	protected float mass;
	
	// Particle specific parameters
	// Particle type
	protected String pType;
	
	// Lifetime and time out
	protected float timeOut;
	protected float lifeTime  = 0f;
	
	// Requirements for rendering (The objects VAO ID, FBO, shader, shader properties and matrices)
	protected int vaoID;
	
	// Requirements for shadow rendering
	protected boolean renderDepthMap = false;
	protected FrameBufferObject depthBuffer = FrameBufferObjectManager.getFrameBuffer("shadow");
	protected Shader depthShader = ShaderManager.getShader("depth");
	
	// Determine which fbo to render the scene
	protected FrameBufferObject fbo;
	protected Shader shader;
	
	// Texture to be used
	protected Texture tex;
	
	// Default primitive objects
	protected Point point;
	protected Triangle triangle;
	protected Quad quad;
	
	// A model object
	protected Model model;
	
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
	
	// Should be the same for all objects
	public void prerender()
	{
		
		if(renderDepthMap)
		{
			
			// Update the FBO of every directional light
			for(LightObject dLight : LightManager.getDirectionalLightList())
			{
				
				depthShader.uploadMatrix4f(modelMatrix, depthShader.getModelMatrixLoc());
				depthShader.uploadMatrix4f(dLight.getProjectionLightMatrix(), depthShader.getProjectionMatrixLoc());
				
				DrawShapes.drawModel(depthShader, model, fbo);
			}
		}
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
	public Model getModel() {
		return model;
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
	public Matrix4f getMVP() {
		
		MVP.setIdentity();
		
		MVP.multiply(projectionMatrix);
		MVP.multiply(viewMatrix);
		MVP.multiply(modelMatrix);
		
		MVP.transpose();
		
		return MVP;
	}
}