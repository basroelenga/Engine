package fbo;

import java.util.ArrayList;

import engine.Engine;
import shaders.ShaderManager;

public class FrameBufferObjectManager {

	private static ArrayList<FrameBufferObject> frameBufferList = new ArrayList<FrameBufferObject>();
	
	private String[] frameBuffers = {"basic"};
	
	public FrameBufferObjectManager()
	{
		
		for(int i = 0; i < frameBuffers.length; i++)
		{
			frameBufferList.add(new FrameBufferObject(frameBuffers[i], ShaderManager.getShader("basictex"), Engine.getWidth(), Engine.getHeight()));
		}
	}
	
	public static FrameBufferObject getFrameBuffer(String name)
	{

		for(FrameBufferObject fbo : frameBufferList)
		{
			if(fbo.getName().equals(name))
			{
				return fbo;
			}
		}
		
		throw new RuntimeException("No such frame buffer object: " + name);
	}
	
	public static ArrayList<FrameBufferObject> getFBOList()
	{
		return frameBufferList;
	}
}