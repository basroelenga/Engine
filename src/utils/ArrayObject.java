package utils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class ArrayObject {
	
	float[][] dataArrayF;
	String[][] dataArrayS;
	
	String type = "";
	
	/**
	 * Create an array object to load in data.
	 * @param data The loaded data.
	 * @param delimiter The delimiter used for separating values (default = " ").
	 * @param unpack Transpose the array.
	 * @param usecols Select which columns to use.
	 */
	public ArrayObject(String data, String delimiter, boolean unpack, String usecols)
	{

		type = "String";

		// Split all the rows from each other
		String[] rows = data.split("\n");
		
		// Check if there is a delimiter, otherwise use default
		if (delimiter == null) delimiter = " ";
		
		// This loops checks if all the rows are the same size
		for(int i = 0; i < rows.length - 1; i++)
		{
			
			String[] columnF = rows[i].split(delimiter);			
			String[] columnS = rows[i + 1].split(delimiter);
			
			if(columnF.length != columnS.length)
			{
				
				System.err.println("Row lengths do not match");
				throw new RuntimeException();
			}
		}
		
		// Construct the 2D array
		dataArrayS = new String[data.split("\n").length][data.split("\n")[0].split(delimiter).length];

		for(int i = 0; i < rows.length; i++)
		{
			
			String[] column = rows[i].split(delimiter);
			
			for(int j = 0; j < column.length; j++)
			{
				
				dataArrayS[i][j] = column[j];
			}
		}
		
		// Filter columns
		if(usecols != null)
		{

			String[] cols = usecols.split(",");
			String[][] tempDataArray = new String[rows[0].split(delimiter).length][cols.length];
			
			for(int i = 0; i < rows.length; i++)
			{
				
				for(int j = 0; j < cols.length; j++)
				{
					
					tempDataArray[i][j] = dataArrayS[i][Integer.parseInt(cols[j])];
				}
			}
			
			dataArrayS = tempDataArray;
		}

		// Transpose the array (if true)
		if(unpack) transposeS();
	}
	
	public ArrayObject(String[][] data)
	{
		
		type = "String";
		this.dataArrayS = data;
	}
	
	public ArrayObject(float[][] data)
	{
		
		type = "Float";
		this.dataArrayF = data;
	}
	
	public void transposeS()
	{
		
		if(dataArrayS == null)
		{	
			
			System.err.println("Invalid ArrayObject type: Expected string type");
			return;
		}	
			
		String[][] transposedDataArray = new String[dataArrayS[0].length][dataArrayS.length];

		for(int i = 0; i < dataArrayS[0].length; i++)
		{
			
			for(int j = 0; j <  dataArrayS.length; j++)
			{
				
				transposedDataArray[i][j] = dataArrayS[j][i];
			}
		}
		
		dataArrayS = transposedDataArray;
	}
	
	public void print()
	{
		
		
		switch(type)
		{
		
		case "String":
			
			for(int i = 0; i < dataArrayS.length; i++)
			{
				
				for(int j = 0; j < dataArrayS[0].length; j++)
				{
					
					if(j == dataArrayS[0].length - 1 )
					{
						
						System.out.println(dataArrayS[i][j]);
					}
					else
					{
						
						System.out.print(dataArrayS[i][j] + " ");
					}
				}
			}
			
			break;
			
		case "Float":
			
			for(int i = 0; i < dataArrayF.length; i++)
			{
				
				for(int j = 0; j < dataArrayF[0].length; j++)
				{
					
					if(j == dataArrayF[0].length - 1 )
					{
						
						System.out.println(dataArrayF[i][j]);
					}
					else
					{
						
						System.out.print(dataArrayF[i][j] + " ");
					}
				}
			}
			
			break;
		}
	}
	
	public void save(String name, String delimiter)
	{
		
		StringBuilder builder = new StringBuilder();
		
		try {
			
			BufferedWriter writer = new BufferedWriter(new FileWriter("res/" + name + ".txt"));
			
			switch(type)
			{
			
			case "String":
				
				for(int i = 0; i < dataArrayS.length; i++){
					for(int j = 0; j < dataArrayS[i].length; j++)
					{
						
						builder.append(dataArrayF[i][j] + delimiter);
					}
					
					builder.append("\n");
				}
				
				writer.write(builder.toString());
				writer.close();
				
				break;
				
			case "Float":
				
				for(int i = 0; i < dataArrayF.length; i++){
					for(int j = 0; j < dataArrayF[i].length; j++)
					{
						
						builder.append(dataArrayF[i][j] + delimiter);
					}
					
					builder.append("\n");
				}
				
				writer.write(builder.toString());
				writer.close();
				
				break;
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public String get(int i, int j)
	{
		
		String r = null;
		
		switch(type)
		{
		
		case "String":
			
			r = dataArrayS[i][j];
			break;
		
		case "Float":
			
			r = Float.toString(dataArrayF[i][j]);
			break;
		}
		
		return r;
	}
	
	public String[] get(int i)
	{
		return dataArrayS[i];
	}
	
	public String[][] getDataArray()
	{
		return dataArrayS;
	}
}