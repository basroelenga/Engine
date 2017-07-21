package fbo;

import java.util.ArrayList;

public class FrameBufferObjectManager {

	private static ArrayList<FrameBufferObject> frameBufferList = new ArrayList<FrameBufferObject>();
	
	private FrameBufferObjectManager() {}
	
	public static void addFrameBufferObject(String name, String type, int WIDTH, int HEIGHT)
	{
		frameBufferList.add(new FrameBufferObject(name, type, WIDTH, HEIGHT));
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
	
	public static void deleteFrameBufferObject(String name)
	{
		
		boolean isDeleted = false;
		
		for(FrameBufferObject obj : frameBufferList)
		{
			
			if(obj.getName().equals(name))
			{
				
				obj.deleteBuffer();
				isDeleted = true;
			}
		}

		if(!isDeleted) System.err.println("Framebuffer object could not be deleted: " + name);
	}
	
	public static void cleanUp()
	{
		
		for(FrameBufferObject obj : frameBufferList) obj.deleteBuffer();
	}
}