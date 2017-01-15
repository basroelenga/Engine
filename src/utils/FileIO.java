package utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class FileIO {

	private FileIO(){}
	
	public static ArrayList<String> getFilesInFolder(String path){
		
		ArrayList<String> fileNames = new ArrayList<String>();
		
		File folder = new File("res/" + path);
		File[] fileList = null;
		if(folder.exists()){
			
			fileList = folder.listFiles();
		}else{
			
			System.err.println("Folder does not contain files: " + path);
		}
		
		for(int i = 0; i < fileList.length; i++){

			if (fileList[i].isFile()) {
				String[] nameParts = fileList[i].getName().split("\\.");
				
				fileNames.add(nameParts[0]);
			}
		}
		
		return fileNames;
	}
	
	public static ArrayObject loadtxt(String path, String delimiter, boolean unpack, String usecols)
	{
		
		StringBuilder builder = new StringBuilder();
		
		try {
			
			BufferedReader reader = new BufferedReader(new FileReader(new File("res/" + path)));
			
			String line;
			
			while((line = reader.readLine()) != null)
			{
				
				builder.append(line);
				builder.append("\n");
			}
			
			reader.close();
		} catch (IOException e) {

			System.err.println("Could not load data");
			e.printStackTrace();
		}
		
		ArrayObject array = new ArrayObject(builder.toString(), delimiter, unpack, usecols);
		
		return array;
	}
	
	public static String loadModel(String path)
	{
		
		try {
			
			BufferedReader reader = new BufferedReader(new FileReader("res/models/" + path + ".obj"));

			StringBuilder data = new StringBuilder();
			String line = reader.readLine();
			
			while(line != null){
				data.append(line).append("\n");
				line = reader.readLine();
			}
			
			reader.close();
			
			return data.toString();
		} catch (IOException e) {
			
			System.err.println("Could not load or find file: " + path);
			e.printStackTrace();
		}

		throw new NullPointerException("No shader data returned from: " + path);
	}
	
	public static String loadShader(String path)
	{
		try {
			
			BufferedReader reader = new BufferedReader(new FileReader("res/shaders/" + path + ".glsl"));

			StringBuilder data = new StringBuilder();
			String line = reader.readLine();
			
			while(line != null){
				data.append(line).append("\n");
				line = reader.readLine();
			}
			
			reader.close();
			
			return data.toString();
		} catch (IOException e) {
			
			System.err.println("Could not load or find file: " + path);
			e.printStackTrace();
		}

		throw new NullPointerException("No shader data returned from: " + path);
	}
	
	public static InputStream loadTexture(String path)
	{
		
		InputStream stream = null;
		
		try {
			stream = new FileInputStream(new File("res/textures/" + path + ".png"));
			
		} catch (FileNotFoundException e) {
			
			System.err.println("File (Texture) not found: " + path);
			e.printStackTrace();
		}
		
		return stream;
	}
}