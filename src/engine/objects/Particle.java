package engine.objects;

import cam.Camera;
import engine.Engine;
import engine.EngineObjectManager;
import engine.EngineObjects;
import graphics.Texture;
import shaders.ShaderManager;
import shapes.Point;
import shapes.UVSphere;
import utils.DrawShapes;

public class Particle extends EngineObjects{

	private UVSphere uvSphere;
	private Point point;
	
	public Particle(float x, float y, float z, float vx, float vy, float vz, float scaling, float mass, Texture tex, String type)
	{
		
		this.x = x;
		this.y = y;
		this.z = z;
		
		this.xs = scaling;
		this.ys = scaling;
		this.zs = scaling;
		
		this.vx = vx;
		this.vy = vy;
		this.vz = vz;
		
		this.mass = mass;
		this.tex = tex;
		
		this.ptype = type;
		
		this.viewMatrix = Camera.getViewMatrix();
		this.projectionMatrix = Engine.projMatrix;
		
		// Select the type of particle
		switch(type)
		{
		
		case "sphere":
			
			uvSphere = EngineObjectManager.getUVSphere(12);
			shader = ShaderManager.getShader("light");
			
			break;
		
		case "point":
			
			point = EngineObjectManager.getPoint();
			shader = ShaderManager.getShader("basic");
			
			break;
		
		// The default for now is also a sphere
		default:
			
			// A default of 5 subdivisions for a UV sphere
			uvSphere = EngineObjectManager.getUVSphere(5);
			shader = ShaderManager.getShader("light");
			
			break;
		}
	}
	
	public void update()
	{
		
		// Update life
		lifeTime += 1f;
		
		// Update positions (for now a per cycle update system)
		x += vx;
		y += vy;
		z += vz;
		
		// Construct model matrix
		modelMatrix.setIdentity();
		
		modelMatrix.transelate(x, y, z);
		modelMatrix.scale(xs, ys, zs);
	}
	
	public void render()
	{
	
		shader.uploadMatrix4f(modelMatrix, shader.getModelMatrixLoc());
		shader.uploadMatrix4f(projectionMatrix, shader.getProjectionMatrixLoc());
		shader.uploadMatrix4f(viewMatrix, shader.getViewMatrixLoc());
		
		shader.uploadVector4f(RGBAcolor, shader.getRgbaColorLoc());
		
		// Depending on the type a different draw method is used
		switch(ptype)
		{
		
		case "sphere":
			
			if(tex != null)	DrawShapes.drawUVSphere(shader, uvSphere, tex);
			else DrawShapes.drawUVSphere(shader, uvSphere);
			
			break;
			
		case "point":
			
			DrawShapes.drawPoint(shader, point);
		}
	}
}