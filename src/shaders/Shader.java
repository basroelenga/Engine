package shaders;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.GL_COMPILE_STATUS;
import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_LINK_STATUS;
import static org.lwjgl.opengl.GL20.GL_VALIDATE_STATUS;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;
import static org.lwjgl.opengl.GL20.glAttachShader;
import static org.lwjgl.opengl.GL20.glBindAttribLocation;
import static org.lwjgl.opengl.GL20.glCompileShader;
import static org.lwjgl.opengl.GL20.glCreateProgram;
import static org.lwjgl.opengl.GL20.glCreateShader;
import static org.lwjgl.opengl.GL20.glDeleteProgram;
import static org.lwjgl.opengl.GL20.glDeleteShader;
import static org.lwjgl.opengl.GL20.glDetachShader;
import static org.lwjgl.opengl.GL20.glGetProgramInfoLog;
import static org.lwjgl.opengl.GL20.glGetProgrami;
import static org.lwjgl.opengl.GL20.glGetShaderInfoLog;
import static org.lwjgl.opengl.GL20.glGetShaderi;
import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL20.glLinkProgram;
import static org.lwjgl.opengl.GL20.glShaderSource;
import static org.lwjgl.opengl.GL20.glUniform1f;
import static org.lwjgl.opengl.GL20.glUniform1i;
import static org.lwjgl.opengl.GL20.glUniform3f;
import static org.lwjgl.opengl.GL20.glUniform4f;
import static org.lwjgl.opengl.GL20.glUniformMatrix4fv;
import static org.lwjgl.opengl.GL20.glUseProgram;
import static org.lwjgl.opengl.GL20.glValidateProgram;

import java.util.ArrayList;

import light.LightManager;
import math.Matrix4f;
import math.Vector3f;
import math.Vector4f;
import utils.FileIO;

public class Shader {

	private int shaderProgram;
	
	private int modelMatrixLoc;
	private int viewMatrixLoc;
	private int normalMatrixLoc;
	private int projectionMatrixLoc;
	
	private int cameraPosLoc;
	
	private int rgbColorLoc;
	private int rgbaColorLoc;
	
	private int lightsLoc;
	
	private ArrayList<Integer> lightColorLocList = new ArrayList<Integer>();
	private ArrayList<Integer> ambIntensityLocList = new ArrayList<Integer>();
	
	private ArrayList<Integer> lightPosLocList = new ArrayList<Integer>();
	private ArrayList<Integer> attenuationFactorLocList = new ArrayList<Integer>();
	
	private int vertexID;
	private int fragmentID;
	
	private int cutoffLoc;

	private String shaderName;
	
	private boolean useLights;
	
	public Shader(String shaderName, boolean useLights)
	{
		
		this.shaderName = shaderName;
		this.useLights = useLights;
		
		String shaderVertexSource = FileIO.loadShader(shaderName + "_vertex_shader");
		String shaderFragmentSource = FileIO.loadShader(shaderName + "_fragment_shader");
			
		vertexID = compileShader(shaderVertexSource, GL_VERTEX_SHADER, shaderName);
		fragmentID = compileShader(shaderFragmentSource, GL_FRAGMENT_SHADER, shaderName);
				
		shaderProgram = glCreateProgram();
		
		if(shaderProgram == 0){
			
			System.err.println("Shader program could not be created: " + shaderName);
		}
		
		glAttachShader(shaderProgram, vertexID);
		glAttachShader(shaderProgram, fragmentID);
		
		glBindAttribLocation(shaderProgram, 0, "in_Position");
		glBindAttribLocation(shaderProgram, 1, "in_TexCoord");
		
		linkShaderProgram(shaderProgram);
		
		modelMatrixLoc = glGetUniformLocation(shaderProgram, "modelMatrix");
		viewMatrixLoc = glGetUniformLocation(shaderProgram, "viewMatrix");
		normalMatrixLoc = glGetUniformLocation(shaderProgram, "normalMatrix");
		projectionMatrixLoc = glGetUniformLocation(shaderProgram, "projectionMatrix");
		
		cameraPosLoc = glGetUniformLocation(shaderProgram, "cameraPos");
		
		rgbColorLoc = glGetUniformLocation(shaderProgram, "rgbColor");
		rgbaColorLoc = glGetUniformLocation(shaderProgram, "rgbaColor");
		
		cutoffLoc = glGetUniformLocation(shaderProgram, "cutoff");
		
		// The number of lights in the engine define the length of the lights arraylist properties
		// These properties should also only be created if the use lights is true
		if(useLights)
		{
			
			lightsLoc = glGetUniformLocation(shaderProgram, "number_of_lights");

			for(int i = 0; i < LightManager.getNumberOfLights(); i++)
			{
			
				String color = "pointLights[" + i + "].lightColor";
				String amb = "pointLights[" + i + "].ambIntensity";
				
				String position = "pointLights[" + i + "].lightPos";
				String attenuation = "pointLights[" + i + "].attFactor";
				
				System.out.println(position);
				
				lightColorLocList.add(glGetUniformLocation(shaderProgram, color));
				ambIntensityLocList.add(glGetUniformLocation(shaderProgram, amb));
				
				lightPosLocList.add(glGetUniformLocation(shaderProgram, position));
				attenuationFactorLocList.add(glGetUniformLocation(shaderProgram, attenuation));
			}
		}
	}
	
	private int compileShader(String shaderSource, int shaderType, String key)
	{
		
		int shaderID = glCreateShader(shaderType);
		
		if(shaderID == 0){
			System.err.println("Shader could not be created");
			return 0;
		}
		
		glShaderSource(shaderID, shaderSource);
		glCompileShader(shaderID);
		
		if(glGetShaderi(shaderID, GL_COMPILE_STATUS) == GL_FALSE){
			
			System.err.println(key + " shader wasn't compiled correctly");
			System.err.println(glGetShaderInfoLog(shaderID, 2048));
			
			glDeleteShader(shaderID);
			
			System.exit(1);
		}else{
			
			switch(shaderType)
			{
			
			case GL_VERTEX_SHADER:
				
				System.out.println("Vertex shader: " + shaderName + " succesfully compiled");
				break;
				
			case GL_FRAGMENT_SHADER:
				
				System.out.println("Fragment Shader: " + shaderName + " succesfully compiled");
				break;
			}
		}
		
		return shaderID;
	}
	
	private void linkShaderProgram(int shaderProgram)
	{
		
		glLinkProgram(shaderProgram);
		
		if(glGetProgrami(shaderProgram, GL_LINK_STATUS) == GL_FALSE){
			
			System.err.println("Could not link shaders");
			System.err.println(glGetProgramInfoLog(shaderProgram, 1024));
			glDeleteProgram(shaderProgram);
			
			System.exit(1);
		}else{
			
			System.out.println("Shader: " + shaderName + " succesfully linked");
		}
		
		glValidateProgram(shaderProgram);
		
		if(glGetProgrami(shaderProgram, GL_VALIDATE_STATUS) == GL_FALSE){
			
			System.err.println("Shader program could not be validated");
			System.err.println(glGetProgramInfoLog(shaderProgram, 1024));
			glDeleteProgram(shaderProgram);
			
			System.exit(1);
		}else{
			
			System.out.println("Shader: " + shaderName + " succesfully validated");
		}
	}
	
	public void uploadInt(int uniform, int uniformLocation)
	{
		
		bind();
		
		glUniform1i(uniformLocation, uniform);
		
		unbind();
	}
	
	public void uploadFloat(float uniform, int uniformLocation)
	{
		
		bind();
		
		glUniform1f(uniformLocation, uniform);

		unbind();
	}
	
	public void uploadVector3f(Vector3f vector, int loc)
	{
		
		bind();
		
		glUniform3f(loc, vector.getX(), vector.getY(), vector.getZ());
		
		unbind();
	}
	
	public void uploadVector4f(Vector4f vector, int loc)
	{
		
		bind();
		
		glUniform4f(loc, vector.getX(), vector.getY(), vector.getZ(), vector.getW());
		
		unbind();
	}
	
	public void uploadMatrix4f(Matrix4f matrix, int loc)
	{	
		
		bind();
		
		glUniformMatrix4fv(loc, true, matrix.toFloatBuffer());
		
		unbind();
	}
	
	public void bind()
	{
		
		glUseProgram(shaderProgram);
	}
	
	public void unbind()
	{
		
		glUseProgram(0);
	}
	
	public void destroy()
	{
		
		glDetachShader(shaderProgram, vertexID);
		glDetachShader(shaderProgram, fragmentID);
		
		glDeleteShader(vertexID);
		glDeleteShader(fragmentID);
		
		glDeleteProgram(shaderProgram);
	}
	
	public int getShaderProgramID() {
		return shaderProgram;
	}
	
	public int getModelMatrixLoc() {
		return modelMatrixLoc;
	}
	
	public int getViewMatrixLoc() {
		return viewMatrixLoc;
	}
	
	public int getProjectionMatrixLoc() {
		return projectionMatrixLoc;
	}
	
	public int getNormalMatrixLoc() {
		return normalMatrixLoc;
	}
	
	public int getCameraPocLoc() {
		return cameraPosLoc;
	}
	
	public int getCutoffLoc() {
		return cutoffLoc;
	}
	
	public String getShaderName() {
		return shaderName;
	}
	
	public boolean getUseLighting() {
		return useLights;
	}

	public int getRgbColorLoc() {
		return rgbColorLoc;
	}

	public int getRgbaColorLoc() {
		return rgbaColorLoc;
	}

	public int getNumberOfLightsLoc()
	{
		return lightsLoc;
	}

	public ArrayList<Integer> getLightColorLocList() {
		return lightColorLocList;
	}

	public ArrayList<Integer> getAmbIntensityLocList() {
		return ambIntensityLocList;
	}

	public ArrayList<Integer> getLightPosLocList() {
		return lightPosLocList;
	}

	public ArrayList<Integer> getAttenuationFactorLocList() {
		return attenuationFactorLocList;
	}
	
}