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
		ambIntensity = new Vector3f(0.02f, 0.02f, 0.02f);
		
		attenuationFactor = 0.01f;
	}
	
	public void update()
	{
				
		// Update the position of the light (for rendering)
		modelMatrix.setIdentity();
		
		modelMatrix.translate(x, y, z);
		modelMatrix.scale(xs, ys, zs);
		
		lightPos = new Vector3f(x, y, z);
	}
	
	public void uploadToShader(int light, Shader uShader)
	{
		
		uShader.uploadFloat(attenuationFactor, uShader.getPointAttenuationFactorLocList().get(light));

		uShader.uploadVector3f(lightPos, uShader.getPointLightPosLocList().get(light));
		uShader.uploadVector3f(lightColor, uShader.getPointLightColorLocList().get(light));
		uShader.uploadVector3f(ambIntensity, uShader.getPointAmbIntensityLocList().get(light));
	}
	
	public void render()
	{
		
		if(show)
		{
			
			shader.uploadMatrix4f(modelMatrix, shader.getModelMatrixLoc());
			shader.uploadMatrix4f(projectionMatrix, shader.getProjectionMatrixLoc());
			
			shader.uploadVector4f(new Vector4f(lightColor.getX(), lightColor.getY(), lightColor.getZ(), 1), shader.getRgbaColorLoc());
			
			DrawShapes.drawUVSphere(shader, null, sphere.getVaoID(), sphere.getAmountOfTriangles());
		}
	}
}