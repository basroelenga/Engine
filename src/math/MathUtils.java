package math;

import java.util.ArrayList;

public class MathUtils {

	private MathUtils(){}
	
	public static Vector3f generateNormal(ArrayList<Vector3f> tempVList, Vector3f currentV)
	{
		
		Vector3f crossP = new Vector3f();
		
		for(int j = 0; j < tempVList.size(); j++)
		{
			
			Vector3f firstN = new Vector3f();
			
			firstN.setX(tempVList.get(j).getX() - currentV.getX());
			firstN.setY(tempVList.get(j).getY() - currentV.getY());
			firstN.setZ(tempVList.get(j).getZ() - currentV.getZ());
			
			Vector3f secondN = new Vector3f();
			
			if(j == tempVList.size() - 1)
			{
				
				secondN.setX(tempVList.get(j - 3).getX() - currentV.getX());
				secondN.setY(tempVList.get(j - 3).getY() - currentV.getY());
				secondN.setZ(tempVList.get(j - 3).getZ() - currentV.getZ());
			}
			else
			{
				
				secondN.setX(tempVList.get(j + 1).getX() - currentV.getX());
				secondN.setY(tempVList.get(j + 1).getY() - currentV.getY());
				secondN.setZ(tempVList.get(j + 1).getZ() - currentV.getZ());
			}
			
			crossP.add(firstN.crossProductR(secondN));
		}
		
		crossP.normalize();
		return crossP;
	}
}