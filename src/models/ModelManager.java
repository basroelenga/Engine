package models;

import java.util.ArrayList;

import utils.FileIO;

public class ModelManager {

	private static ArrayList<Model> modelList = new ArrayList<Model>();
	
	public static void loadModels()
	{
		
		// Check all the files in the model map
		ArrayList<String> fileNames = FileIO.getFilesInFolder("/models");
		
		for(String names : fileNames)
		{
			
			modelList.add(new Model(names));
		}
	}
	
	public static Model getModel(String name)
	{
		
		for(Model model : modelList)
		{

			if(model.getModelName().equals(name)) return model;
		}
		
		throw new RuntimeException("Model not found in list " + name);
	}
}