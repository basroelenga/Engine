package game.obj;

import cam.Camera;
import engine.Engine;
import graphics.Texture;
import light.LightManager;
import math.Matrix4f;
import math.Vector3f;
import math.Vector4f;
import shaders.ShaderManager;
import shapes.UVSphere;
import utils.DrawShapes;

public class Sphere {

	private float x;
	private float y;
	private float z;
	
	private float scaling;
	
	private Texture tex;
	
	private UVSphere sphere;
	
	private Matrix4f modelMatrix;
	
	private String shader;
	
	public Sphere(float subdivision, float x, float y, float z, float scaling, Texture tex, boolean useLighting)
	{
		
		this.x = x;
		this.y = y;
		this.z = z;
		
		this.scaling = scaling;
		this.tex = tex;
		
		sphere = new UVSphere(subdivision);
		modelMatrix = new Matrix4f();
		
		if(useLighting && LightManager.size() != 0)
		{
			
			shader = "lighting";
		}
		else
		{
			
			shader = "basictex";
		}
	}
	
	public void update()
	{
		
		modelMatrix.setIdentity();
		
		modelMatrix.transelate(x, y, z);
		modelMatrix.rotateQ(0, 0, 180f, false);
		modelMatrix.scale(scaling, scaling, scaling);
	}
	
	public void render()
	{
		
		ShaderManager.getShader(shader).uploadMatrices(modelMatrix, Engine.projMatrix, Camera.getViewMatrix());
		ShaderManager.getShader(shader).uploadColor(new Vector4f(1.0f, 1.0f, 1.0f, 1.0f));
		
		ShaderManager.getShader(shader).uploadVector3f(new Vector3f(LightManager.getPointLight("light").getPosition()), ShaderManager.getShader(shader).getLightPosLoc());
		ShaderManager.getShader(shader).uploadVector3f(new Vector3f(1f, 1f, 1f), ShaderManager.getShader(shader).getAmbColorLoc());
		ShaderManager.getShader(shader).uploadVector3f(new Vector3f(0.5f, 0.5f, 0.5f), ShaderManager.getShader(shader).getAmbIntensityLoc());
		
		if(tex == null)
		{
			
			DrawShapes.drawUVSphere(ShaderManager.getShader(shader), sphere);
		}
		else
		{
			
			DrawShapes.drawUVSphere(ShaderManager.getShader(shader), sphere, tex);
		}
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
	
	public float getScaling()
	{
		return scaling;
	}
	
	public void setScaling(float scaling)
	{
		this.scaling = scaling;
	}
}