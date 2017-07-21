package postprocess;

import java.util.ArrayList;

import fbo.FrameBufferObject;
import fbo.FrameBufferObjectManager;
import shaders.Shader;

public class PostProcessEffectManager {
	
	private static boolean isEnabled = true;
	
	private static String[] postProcessShaders = {"testeffect"};
	private static ArrayList<Shader> postProcessShaderList = new ArrayList<Shader>();
	
	private static ArrayList<PostProcessEffect> ppList = new ArrayList<PostProcessEffect>();
	
	/**
	 * Load all the available post process effects into the shaders.
	 */
	public static void loadPostProcessShaders()
	{
		
		for(int i = 0; i < postProcessShaders.length; i++)
		{
			
			postProcessShaderList.add(new Shader(postProcessShaders[i], false));
		}
	}
	
	/**
	 * Create a new post process effect.
	 * @param name The name of the post process effect, this name should also match the name
	 * of the shader in which the post process effect is stored.
	 */
	public static void addPostProcessEffect(String name)
	{
		
		boolean exist = false;
		
		for(FrameBufferObject obj : FrameBufferObjectManager.getFBOList())
		{
			if(obj.getName().equals(name)) exist = true;
		}
		
		if(!exist) FrameBufferObjectManager.addFrameBufferObject(name, "RGBA", 2048, 2048);
		else {System.err.println("Framebuffer object already exists"); return;}
		
		ppList.add(new PostProcessEffect(name));
	}
	
	/**
	 * Retrieve a post process effect from the post process effect manager.
	 * @param name The name of the post process effect.
	 * @return The post process effect.
	 */
	public static PostProcessEffect getPostProcessEffect(String name)
	{
		
		for(PostProcessEffect ppe : ppList)
		{
			if(ppe.getName().equals(name)) return ppe;
		}
		
		throw new RuntimeException("Post process effect does not exist");
	}
	
	/**
	 * Get a shader which is loaded into the post process effect manager.
	 * @param name The name of the post process effect shader.
	 * @return The post process effect shader.
	 */
	public static Shader getPostProcessShader(String name)
	{
		
		for(Shader shader : postProcessShaderList)
		{
			if(shader.getShaderName().equals(name))	return shader;
		}
		
		throw new RuntimeException("Post process shader does not exist");
	}
	
	/**
	 * Render all the post process effect in no particular order. This render order can maybe cause trouble when rendering
	 * more than one post process effect due the layering. Which is all concentrated on z = 0f.
	 */
	public static void render()
	{
		
		for(PostProcessEffect ppe : ppList)
		{
			ppe.render();
		}
	}
	
	/**
	 * Enable all post process effects.
	 */
	public static void enable()
	{
		
		for(PostProcessEffect pp : ppList) pp.shouldRender(true);
		isEnabled = true;
	}
	
	/**
	 * Disable all post process effects.
	 */
	public static void disable()
	{
		
		for(PostProcessEffect pp : ppList) pp.shouldRender(false);
		isEnabled = false;
	}
	
	/**
	 * Check if the post processing effects are rendered.
	 * @return Are post processing effects rendered.
	 */
	public static boolean isEnabled()
	{
		return isEnabled;
	}
}