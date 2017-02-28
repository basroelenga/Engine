package camera;

import java.util.ArrayList;

import math.Vector3f;

public class CameraManager {

	private static ArrayList<Camera> cameraList = new ArrayList<Camera>();
	
	private CameraManager() {}
	
	public static void addCamera(String name, Vector3f position, Vector3f angles)
	{
		cameraList.add(new Camera(name, position, angles));
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