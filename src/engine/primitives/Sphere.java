package engine.primitives;

import cam.Camera;
import engine.Engine;
import engine.EngineObjects;
import graphics.Texture;
import light.LightManager;
import math.Vector3f;
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
		if(useLighting && LightManager.size() != 0)	shader = ShaderManager.getShader("light");
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
		
		shader.uploadMatrices(modelMatrix, projectionMatrix, viewMatrix, normalMatrix);
		shader.uploadColor(new Vector4f(1.0f, 0.0f, 0.0f, 1.0f));
		
		shader.uploadVector3f(new Vector3f(LightManager.getLight("light").getPosition()), shader.getLightPosLoc());
		shader.uploadVector3f(new Vector3f(1f, 1f, 1f), shader.getAmbColorLoc());
		shader.uploadVector3f(new Vector3f(0.4f, 0.4f, 0.4f), shader.getAmbIntensityLoc());
		
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