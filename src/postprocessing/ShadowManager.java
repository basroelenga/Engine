package postprocessing;

import fbo.FrameBufferObject;
import fbo.FrameBufferObjectManager;

public class ShadowManager {

	public static boolean requestDepthMap = false;
	
	private ShadowManager() {}
	
	public static void createDepthMap(String name)
	{
		
		FrameBufferObjectManager.addShadowFrameBufferObject(name, 1024, 1024);
		requestDepthMap = true;
	}
	
	public static FrameBufferObject getShadowBuffer(String name)
	{
		return FrameBufferObjectManager.getFrameBuffer(name);
	}
}
