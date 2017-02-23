package camera;

import java.util.ArrayList;

public class CameraManager {

	private static ArrayList<Camera> cameraList = new ArrayList<Camera>();
	
	private CameraManager() {}
	
	public static void addCamera(String name, float x, float y, float z)
	{
		cameraList.add(new Camera(name, x, y, z));
	}
	
	public static void update()
	{
		
		for(Camera cam : cameraList)
		{
			cam.update();
		}
	}
	
	public static Camera getCamera(String name)
	{
		
		for(Camera cam : cameraList)
		{
			if(cam.getName().equals(name))
			{
				return cam;
			}
		}
		
		throw new RuntimeException("Camera does not exist");
	}
}