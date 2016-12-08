package shaders;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.GL_COMPILE_STATUS;
import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_LINK_STATUS;
import static org.lwjgl.opengl.GL20.GL_VALIDATE_STATUS;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;
import static org.lwjgl.opengl.GL20.*;

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
	
	private int lightColorLoc1;
	private int ambIntensityLoc1;
	
	private int lightPosLoc1;
	private int attenuationFactorLoc1;
	
	private int lightColorLoc2;
	private int ambIntensityLoc2;
	
	private int lightPosLoc2;
	private int attenuationFactorLoc2;
	
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
		
		cameraPosLoc = glGetUniformLocation(shaderProgram, "cameraPos");
		
		rgbColorLoc = glGetUniformLocation(shaderProgram, "rgbColor");
		rgbaColorLoc = glGetUniformLocation(shaderProgram, "rgbaColor");
		
		lightsLoc = glGetUniformLocation(shaderProgram, "number_of_lights");
		
		lightColorLoc1 = glGetUniformLocation(shaderProgram, "pointLights[0].lightColor");
		ambIntensityLoc1 = glGetUniformLocation(shaderProgram, "pointLights[0].ambIntensity");
		
		lightPosLoc1 = glGetUniformLocation(shaderProgram, "pointLights[0].lightPos");
		attenuationFactorLoc1 = glGetUniformLocation(shaderProgram, "pointLights[0].attFactor");
		
		lightColorLoc2 = glGetUniformLocation(shaderProgram, "pointLights[1].lightColor");
		ambIntensityLoc2 = glGetUniformLocation(shaderProgram, "pointLights[1].ambIntensity");
		
		lightPosLoc2 = glGetUniformLocation(shaderProgram, "pointLights[1].lightPos");
		attenuationFactorLoc2 = glGetUniformLocation(shaderProgram, "pointLights[1].attFactor");
		
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
	
	public String getShaderName(){
		return shaderName;
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
	
	public int getLightColorLoc1() {
		return lightColorLoc1;
	}

	public int getAmbIntensityLoc1() {
		return ambIntensityLoc1;
	}

	public int getLightPosLoc1() {
		return lightPosLoc1;
	}

	public int getAttenuationPosLoc1() {
		return attenuationFactorLoc1;
	}
	
	public int getLightColorLoc2() {
		return lightColorLoc2;
	}

	public int getAmbIntensityLoc2() {
		return ambIntensityLoc2;
	}

	public int getLightPosLoc2() {
		return lightPosLoc2;
	}

	public int getAttenuationPosLoc2() {
		return attenuationFactorLoc2;
	}
}