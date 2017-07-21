package light;

import camera.FreeCamera;
import camera.CameraManager;
import camera.PlayerCamera;
import fbo.FrameBufferObject;
import math.Matrix4f;
import math.Vector3f;
import matrices.MatrixObjectManager;
import shaders.Shader;

public abstract class LightObject {

	// Some general properties of the light:
	// The "name" of the object
	protected String name;
	
	// Their position
	protected float x;
	protected float y;
	protected float z;
	
	// Their normalized direction
	protected float xDir;
	protected float yDir;
	protected float zDir;
	
	// Their velocities
	protected float vx;
	protected float vy;
	protected float vz;
	
	// Their scaling
	protected float xs;
	protected float ys;
	protected float zs;
	
	// Direction in polar coordinates
	protected float radius;
	protected float theta;
	protected float phi;
	
	// Toggle shadows (default is false)
	protected boolean renderShadows = true;
	protected boolean renderShadowMap = true;
	
	// Shadow render distance, this is the distance that will be used as zFar.
	protected float shadowDistance = 10f;
	
	// A camera is needed to do the shadow calculation from
	protected PlayerCamera playercam;
	protected FreeCamera freecam;
	
	// The cone angle for spotlights
	protected float coneAngle;
	
	// Should the position of the light be visible
	protected boolean show = false;
	
	// Properties of the light
	protected float attenuationFactor = 0f;
	
	protected Vector3f lightColor;
	protected Vector3f ambIntensity;
	
	protected Vector3f lightPos;
	protected Vector3f lightDir;
	
	// Requirements for rendering (The objects VAO ID, shader, shader properties and matrices)
	// This is for rendering the position of the light in the 3D space
	protected int vaoID;
	
	protected Shader shader;
	
	// The light is always displayed(as a sphere) in perspective space
	protected Matrix4f modelMatrix = new Matrix4f();
	protected Matrix4f viewMatrix = CameraManager.getPlayerCamera("playercam").getViewMatrix();
	protected Matrix4f projectionMatrix = MatrixObjectManager.getMatrixObject("projectionMatrixDefault").getMatrix();
	protected Matrix4f normalMatrix = new Matrix4f();
	
	// Requirements for shadows, the view light matrix is the view matrix as seen in the direction of the light
	// Only works for directional lights
	protected FrameBufferObject depthBuffer;
	
	protected Matrix4f viewLightMatrix = new Matrix4f();
	protected Matrix4f projectionLightMatrix = new Matrix4f();
	
	// The update and render function
	public abstract void update();
	public abstract void render();
	
	// Upload lights to shader
	public abstract void uploadToShader(int light, Shader uShader);
	
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
	public float getxDir() {
		return xDir;
	}
	public void setxDir(float xDir) {
		this.xDir = xDir;
	}
	public float getyDir() {
		return yDir;
	}
	public void setyDir(float yDir) {
		this.yDir = yDir;
	}
	public float getzDir() {
		return zDir;
	}
	public void setzDir(float zDir) {
		this.zDir = zDir;
	}
	public float getConeAngle(){
		return this.coneAngle;
	}
	public void setConeAngle(float angle){
		this.coneAngle = angle;
	}
	public Vector3f getPosition() {
		return new Vector3f(x, y, z);
	}
	public Vector3f getDirection() {
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
	public float getRadius() {
		return radius;
	}
	public void setRadius(float radius) {
		this.radius = radius;
	}
	public float getTheta() {
		return theta;
	}
	public void setTheta(float theta) {
		this.theta = theta;
	}
	public float getPhi() {
		return phi;
	}
	public void setPhi(float phi) {
		this.phi = phi;
	}
	public PlayerCamera getCam() {
		return playercam;
	}
	public boolean isRenderShadows() {
		return renderShadows;
	}
	public void setRenderShadows(boolean renderShadows) {
		this.renderShadows = renderShadows;
	}
	public boolean isRenderShadowMap() {
		return renderShadowMap;
	}
	public void setRenderShadowMap(boolean renderShadowMap) {
		this.renderShadowMap = renderShadowMap;
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
	public FrameBufferObject getDepthBuffer() {
		return depthBuffer;
	}
	public Matrix4f getProjectionLightMatrix() {
		return projectionLightMatrix;
	}
	public Matrix4f getViewLightMatrix() {
		return viewLightMatrix;
	}
}