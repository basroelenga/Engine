package fbo;

import java.util.ArrayList;

public class FrameBufferObjectManager {

	private static ArrayList<FrameBufferObject> frameBufferList = new ArrayList<FrameBufferObject>();
	
	private FrameBufferObjectManager() {}
	
	public static void addDefaultFrameBufferObject(String name, String type, int WIDTH, int HEIGHT)
	{
		frameBufferList.add(new FrameBufferObject(name, type, WIDTH, HEIGHT));
	}
	
	public static void addShadowFrameBufferObject(String name, int WIDTH, int HEIGHT)
	{
		frameBufferList.add(new FrameBufferObject(name, WIDTH, HEIGHT));
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