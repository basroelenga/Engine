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
import static org.lwjgl.opengl.GL20.glUniform3f;
import static org.lwjgl.opengl.GL20.glUniform4f;
import static org.lwjgl.opengl.GL20.glUniformMatrix4fv;
import static org.lwjgl.opengl.GL20.glUseProgram;
import static org.lwjgl.opengl.GL20.glValidateProgram;

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
	
	private int rgbColorLoc;
	private int rgbaColorLoc;
	
	private int ambColorLoc;
	private int ambIntensityLoc;
	
	private int lightPosLoc;
	
	private int vertexID;
	private int fragmentID;
	
	private int cutoffLoc;

	private String shaderName;
	
	public Shader(String shaderName)
	{
		
		this.shaderName = shaderName;
		
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
		
		rgbColorLoc = glGetUniformLocation(shaderProgram, "rgbColor");
		rgbaColorLoc = glGetUniformLocation(shaderProgram, "rgbaColor");
		
		ambColorLoc = glGetUniformLocation(shaderProgram, "ambColor");
		ambIntensityLoc = glGetUniformLocation(shaderProgram, "ambIntensity");
		
		lightPosLoc = glGetUniformLocation(shaderProgram, "lightPos");
		
		cutoffLoc = glGetUniformLocation(shaderProgram, "cutoff");
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
	
	public void uploadMatrices(Matrix4f modelMatrix, Matrix4f projectionMatrix, Matrix4f viewMatrix, Matrix4f normalMatrix)
	{
		
		bind();
		
		upload(modelMatrix, modelMatrixLoc);
		upload(viewMatrix, viewMatrixLoc);
		upload(projectionMatrix, projectionMatrixLoc);
		upload(normalMatrix, normalMatrixLoc);
		
		unbind();
	}
	
	private void upload(Matrix4f matrix, int loc)
	{	
		
		glUniformMatrix4fv(loc, true, matrix.toFloatBuffer());
	}
	
	public void uploadColor(Vector4f color)
	{
		
		bind();
		
		glUniform3f(rgbColorLoc, color.getX(), color.getY(), color.getZ());
		glUniform4f(rgbaColorLoc, color.getX(), color.getY(), color.getZ(), color.getW());
		
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
	
	public int getCutoffLoc() {
		return cutoffLoc;
	}
	
	public String getShaderName(){
		return shaderName;
	}

	public int getAmbColorLoc() {
		return ambColorLoc;
	}

	public void setAmbColorLoc(int ambColorLoc) {
		this.ambColorLoc = ambColorLoc;
	}

	public int getAmbIntensityLoc() {
		return ambIntensityLoc;
	}

	public void setAmbIntensityLoc(int ambIntensityLoc) {
		this.ambIntensityLoc = ambIntensityLoc;
	}

	public int getLightPosLoc() {
		return lightPosLoc;
	}

	public void setLightPosLoc(int lightPosLoc) {
		this.lightPosLoc = lightPosLoc;
	}
}