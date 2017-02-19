package postprocessing;

import engine.Engine;
import engine.objects.Rectangle;
import fbo.FrameBufferObject;
import graphics.Texture;
import math.Vector4f;
import shaders.Shader;
import shaders.ShaderManager;

public class ShadowManager {

	private static FrameBufferObject depthFBO;
	private static boolean canRender = false;
	
	private static Rectangle rect;
	private static Shader shader;
	
	private ShadowManager() {}
	
	/**
	 * Bind a depth FBO to the shadow manager
	 * @param depthFBO The FBO the bind.
	 */
	public static void setDepthFBO(FrameBufferObject depthFBO)
	{
		
		depthFBO = depthFBO;
		canRender = true;
		
		shader = ShaderManager.getShader("basic");
		rect = new Rectangle("depth", new Texture("depth", depthFBO.getDepthTexID()), Engine.getWidth() - Engine.getWidth() / 3, Engine.getHeight() - Engine.getHeight() / 3, 0, Engine.getWidth() / 4, Engine.getHeight() / 4, 0, Engine.orthoMatrix, new Vector4f(1, 1, 1, 1));
	}
	
	/**
	 * Render the current depthbuffer texture that is attached
	 */
	public static void render()
	{
		
		if(canRender)
		{					
			rect.update();
			rect.render();
		}
	}
}