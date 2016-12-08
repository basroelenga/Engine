package light;

import math.Vector3f;
import math.Vector4f;
import shaders.Shader;
import shaders.ShaderManager;
import shapes.UVSphere;
import utils.DrawShapes;

public class PointLight extends LightObject{

	private UVSphere sphere;

	public PointLight(String name, float x, float y, float z, Vector3f lightColor, boolean show) 
	{
		
		this.name = name;
		
		this.x = x;
		this.y = y;
		this.z = z;
		
		this.xs = 0.2f;
		this.ys = 0.2f;
		this.zs = 0.2f;
		
		this.show = show;
		
		// The sphere that show the position
		sphere = new UVSphere(8);
		vaoID = sphere.getVaoID();
		
		// The sphere should not be influenced by other lighting, thus use basic shader
		shader = ShaderManager.getShader("basic");
		
		// Set light properties
		this.lightColor = lightColor;
		ambIntensity = new Vector3f(0.2f, 0.2f, 0.2f);
		
		attenuationFactor = 0.1f;
	}
	
	public void update()
	{
		
		// Update the position of the light (for rendering)
		modelMatrix.setIdentity();
		
		modelMatrix.transelate(x, y, z);
		modelMatrix.scale(xs, ys, zs);
		
		lightPos = new Vector3f(x, y, z);
		
		// Upload the position (and other properties) of the light to all shaders
		for(Shader shader : ShaderManager.getShaderList())
		{
			
			if(name.equals("light1"))
			{
				shader.uploadFloat(attenuationFactor, shader.getAttenuationPosLoc1());
				
				shader.uploadVector3f(lightPos, shader.getLightPosLoc1());
				shader.uploadVector3f(lightColor, shader.getLightColorLoc1());
				shader.uploadVector3f(ambIntensity, shader.getAmbIntensityLoc1());
			}
			else
			{
			shader.uploadFloat(attenuationFactor, shader.getAttenuationPosLoc2());
			
			shader.uploadVector3f(lightPos, shader.getLightPosLoc2());
			shader.uploadVector3f(lightColor, shader.getLightColorLoc2());
			shader.uploadVector3f(ambIntensity, shader.getAmbIntensityLoc2());
			}
		}
	}
	
	public void render()
	{
		
		if(show)
		{
			
			shader.uploadMatrix4f(modelMatrix, shader.getModelMatrixLoc());
			shader.uploadMatrix4f(projectionMatrix, shader.getProjectionMatrixLoc());
			
			shader.uploadVector4f(new Vector4f(1, 1, 0, 1), shader.getRgbaColorLoc());
			
			DrawShapes.drawQuad(shader, vaoID, sphere.getAmountOfTriangles());
		}
	}
}