package engine.primitives;

import cam.Camera;
import engine.Engine;
import engine.EngineObjects;
import graphics.Texture;
import light.LightManager;
import math.Vector4f;
import shaders.ShaderManager;
import shapes.UVSphere;
import utils.DrawShapes;

public class Sphere extends EngineObjects{

	private UVSphere sphere;
	
	public Sphere(float subdivision, float x, float y, float z, float scaling, Texture tex, boolean useLighting)
	{
		
		this.x = x;
		this.y = y;
		this.z = z;
		
		this.xs = scaling;
		this.ys = scaling;
		this.zs = scaling;
		
		this.za = 180;
		
		this.tex = tex;
		
		sphere = new UVSphere(subdivision);
		
		projectionMatrix = Engine.projMatrix;
		viewMatrix = Camera.getViewMatrix();

		// Define which shader to use
		if(useLighting && LightManager.getNumberOfLights() != 0) shader = ShaderManager.getShader("light");
		else shader = ShaderManager.getShader("basictex");
	}
	
	public void update()
	{
		
		modelMatrix.setIdentity();
		
		modelMatrix.transelate(x, y, z);
		modelMatrix.rotateQ(xa, ya, za, false);
		modelMatrix.scale(xs, ys, zs);
	}
	
	public void render()
	{
		
		shader.uploadMatrix4f(modelMatrix, shader.getModelMatrixLoc());
		shader.uploadMatrix4f(projectionMatrix, shader.getProjectionMatrixLoc());
		shader.uploadMatrix4f(normalMatrix, shader.getNormalMatrixLoc());

		shader.uploadVector4f(new Vector4f(1.0f, 0.0f, 0.0f, 1.0f), shader.getRgbaColorLoc());
		
		if(tex == null)
		{
			
			DrawShapes.drawUVSphere(shader, sphere);
		}
		else
		{
			
			DrawShapes.drawUVSphere(shader, sphere, tex);
		}
	}
}