package light;

import math.Vector4f;
import shaders.ShaderManager;
import shapes.UVSphere;
import utils.DrawShapes;

public class PointLight extends LightObject{

	private UVSphere sphere;

	public PointLight(String name, float x, float y, float z, boolean show) 
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
		
		// The sphere should not be influenced by other lighting, thus shader
		shader = ShaderManager.getShader("basic");
	}
	
	public void update()
	{
		
		modelMatrix.setIdentity();
		
		modelMatrix.transelate(x, y, z);
		modelMatrix.scale(xs, ys, zs);
	}
	
	public void render()
	{
		
		if(show)
		{
			
			shader.uploadMatrices(modelMatrix, projectionMatrix, viewMatrix, normalMatrix);
			shader.uploadColor(new Vector4f(1, 1, 0, 1));
			
			DrawShapes.drawQuad(shader, vaoID, sphere.getAmountOfTriangles());
		}
	}
}