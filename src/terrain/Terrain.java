package terrain;

import java.util.ArrayList;

import cam.Camera;

public class Terrain {

	private ArrayList<TerrainTile> tileList = new ArrayList<TerrainTile>();
	
	private float x;
	private float y;
	private float z;
	
	public Terrain()
	{
		
		int quality = 256;
		
		tileList.add(new TerrainTile(0, 0, quality));
		
		tileList.add(new TerrainTile(1, 0, quality));
		tileList.add(new TerrainTile(-1, 0, quality));
		
		tileList.add(new TerrainTile(0, 1, quality));
		tileList.add(new TerrainTile(0, -1, quality));
		
		tileList.add(new TerrainTile(1, 1, quality));
		tileList.add(new TerrainTile(-1, -1, quality));
		
		tileList.add(new TerrainTile(-1, 1, quality));
		tileList.add(new TerrainTile(1, -1, quality));
		
		// Generate normals
		for(TerrainTile tile : tileList) tile.generateNormals();
		
		// Set the vaoID 
		for(TerrainTile tile : tileList) tile.setVaoID();;
	}
	
	private void updateTiles()
	{
		
		x = Camera.getX();
		y = Camera.getY();
		z = Camera.getZ();
		
	}
	
	public void update()
	{
		
		updateTiles();
		for(TerrainTile tile : tileList) tile.update();
	}
	
	public void render()
	{
		
		for(TerrainTile tile : tileList) tile.render();
	}
	
	public void getTerrainTile(int x, int z)
	{
		
	}
}