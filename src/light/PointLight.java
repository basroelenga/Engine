package light;

import cam.Camera;
import engine.Engine;
import math.Matrix4f;
import math.Vector3f;
import math.Vector4f;
import shaders.ShaderManager;
import shapes.UVSphere;
import utils.DrawShapes;

public class PointLight {

	String id;
	Vector3f position = new Vector3f();
	
	private boolean showLight = false;
	private UVSphere sphere;
	
	private Matrix4f modelMatrix = new Matrix4f();;
	
	public PointLight(Vector3f position, String id, boolean show)
	{
		
		this.id = id;
		this.position = position;
		
		this.showLight = show;
		
		sphere = new UVSphere(8);
	}
	
	public void update()
	{
		
		modelMatrix.setIdentity();
		
		modelMatrix.transelate(position.getX(), position.getY(), position.getZ());
		modelMatrix.scale(0.2f, 0.2f, 0.2f);
	}
	
	public void render()
	{
		
		if(showLight)
		{
			
			ShaderManager.getShader("basic").uploadMatrices(modelMatrix, Engine.projMatrix, Camera.getViewMatrix());
			ShaderManager.getShader("basic").uploadColor(new Vector4f(1, 1, 0, 1));
			
			DrawShapes.drawQuad(ShaderManager.getShader("basic"), sphere.getVaoID(), sphere.getAmountOfTriangles());
		}
	}
	
	public void setPosition(Vector3f position)
	{
		
		this.position = position;
	}
	
	public Vector3f getPosition()
	{
		return position;
	}
	
	public String getID()
	{
		return id;
	}
}
