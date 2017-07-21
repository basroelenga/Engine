package postprocess;

import engine.Engine;
import engine.EngineObjectManager;
import fbo.FrameBufferObject;
import fbo.FrameBufferObjectManager;
import graphics.Texture;
import math.Matrix4f;
import matrices.MatrixObjectManager;
import shaders.Shader;
import shaders.ShaderManager;
import utils.DrawShapes;

public class PostProcessEffect {

	private String name;
	private Shader shader;
	
	private FrameBufferObject fbo;
	private Matrix4f modelMatrix;
	
	private Texture tex;
	
	private float angle = 0f;
	
	// Determines whether or not the effect should be rendered.
	// If this is false, it will render the standard shader.
	private boolean shouldRender = true;
	
	/**
	 * Create a post process effect.
	 * @param name Name of the post process effect, this is also the name of the FBO and the shader.
	 */
	public PostProcessEffect(String name)
	{
		
		this.name = name;
		
		// Get the fbo and shader
		fbo = FrameBufferObjectManager.getFrameBuffer(name);		
		tex = new Texture(name, fbo.getTextureID());
		
		shader = PostProcessEffectManager.getPostProcessShader(name);
		
		// Set up the standard render matrix
		modelMatrix = new Matrix4f();
		
		modelMatrix.translate(0, Engine.getHeight(), 0);
		modelMatrix.scale(Engine.getWidth(), Engine.getHeight(), 0);
		modelMatrix.rotateQ(180, 0, 0, false);
	}
	
	/**
	 * Render the post process effect
	 */
	public void render()
	{
		
		angle += 0.01f;
		
		if(shouldRender)
		{
			
			shader.uploadFloat(angle, shader.getUniform("offset"));
			
			shader.uploadMatrix4f(modelMatrix, shader.getModelMatrixLoc());
			shader.uploadMatrix4f(new Matrix4f(), shader.getViewMatrixLoc());
			shader.uploadMatrix4f(MatrixObjectManager.getMatrixObject("orthographicMatrixDefault").getMatrix(), shader.getProjectionMatrixLoc());
			
			DrawShapes.drawQuadToScreen(shader, tex, null, EngineObjectManager.getQuad().getVaoID());
		}
		else
		{
			shader = ShaderManager.getShader("basictex");
			
			shader.uploadMatrix4f(modelMatrix, shader.getModelMatrixLoc());
			shader.uploadMatrix4f(new Matrix4f(), shader.getViewMatrixLoc());
			shader.uploadMatrix4f(MatrixObjectManager.getMatrixObject("orthographicMatrixDefault").getMatrix(), shader.getProjectionMatrixLoc());
			
			DrawShapes.drawQuad(shader, tex, null, EngineObjectManager.getQuad().getVaoID());
		}
		
		// Reset shader to post process shader
		shader = PostProcessEffectManager.getPostProcessShader(name);
	}
	
	public void shouldRender(boolean shouldRender)
	{
		this.shouldRender = shouldRender;
	}
	
	public boolean getShouldRender()
	{
		return shouldRender;
	}
	
	public FrameBufferObject getFBO()
	{
		return fbo;
	}
	
	public Shader getShader()
	{
		return shader;
	}
	
	public String getName()
	{
		return name;
	}
}