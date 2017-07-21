package camera;

import java.util.ArrayList;

public class CameraManager {

	private static ArrayList<FreeCamera> freeCameraList = new ArrayList<FreeCamera>();
	private static ArrayList<PlayerCamera> playerCameraList = new ArrayList<PlayerCamera>();
	
	private static ArrayList<CameraObject> cameraList = new ArrayList<CameraObject>();
	
	private CameraManager() {}
	
	public static void addFreeCamera(String name, float x, float y, float z, float yaw, float pitch, float roll)
	{
		freeCameraList.add(new FreeCamera(name, x, y, z, yaw, pitch, roll));
	}
	
	public static void addPlayerCamera(String name, float x, float y, float z, float yaw, float pitch, float roll)
	{
		playerCameraList.add(new PlayerCamera(name, x, y, z, yaw, pitch, roll));
	}
	
	public static void update()
	{
		
		for(PlayerCamera cam : playerCameraList)
		{
			cam.update();
		}
		
		for(FreeCamera cam : freeCameraList)
		{
			cam.update();
		}
	}
	
	public static FreeCamera getFreeCamera(String name)
	{
		
		for(FreeCamera cam : freeCameraList)
		{
			if(cam.getName().equals(name))
			{
				return cam;
			}
		}
		
		throw new RuntimeException("Camera does not exist");
	}
	
	public static PlayerCamera getPlayerCamera(String name)
	{
		
		for(PlayerCamera cam : playerCameraList)
		{
			if(cam.getName().equals(name))
			{
				return cam;
			}
		}
		
		throw new RuntimeException("Camera does not exist");
	}
	
	public static ArrayList<CameraObject> getCameraList()
	{
		
		cameraList.clear();
		
		cameraList.addAll(freeCameraList);
		cameraList.addAll(playerCameraList);
		
		return cameraList;
	}
}