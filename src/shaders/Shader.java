package shaders;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.*;

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
	
	private int lightProjectionMatrixLoc;
	private int lightViewMatrixLoc;
	private int biasMatrixLoc;
	
	private int cameraPosLoc;
	private int lightPosLoc;
	
	private int rgbColorLoc;
	private int rgbaColorLoc;
	
	private int pointLightsLoc;
	private int directionalLightsLoc;
	private int spotLightsLoc;
	
	private int textureSampleLoc;
	private int depthTextureSampleLoc;
	
	// List storing all the texture locations
	private ArrayList<Integer> texLocList = new ArrayList<Integer>();
	
	// Lists storing all the properties for point lights
	private ArrayList<Integer> pointLightColorLocList = new ArrayList<Integer>();
	private ArrayList<Integer> pointAmbIntensityLocList = new ArrayList<Integer>();
	
	private ArrayList<Integer> pointLightPosLocList = new ArrayList<Integer>();
	private ArrayList<Integer> pointAttenuationFactorLocList = new ArrayList<Integer>();
	
	// Lists storing all the properties for directional lights
	private ArrayList<Integer> directionalLightDirectionLocList = new ArrayList<Integer>();
	private ArrayList<Integer> directionalAmbIntensityLocList = new ArrayList<Integer>();
	
	private ArrayList<Integer> directionalLightColorLocList = new ArrayList<Integer>();
	
	// Lists stroing all the properties for spot lights
	private ArrayList<Integer> spotLightColorLocList = new ArrayList<Integer>();
	private ArrayList<Integer> spotAmbIntensityLocList = new ArrayList<Integer>();
	
	private ArrayList<Integer> spotLightPosLocList = new ArrayList<Integer>();
	private ArrayList<Integer> spotAttenuationFactorLocList = new ArrayList<Integer>();
	
	private ArrayList<Integer> spotLightDirLocList = new ArrayList<Integer>();
	private ArrayList<Integer> spotConeAngleLocList = new ArrayList<Integer>();
	
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
		
		addAllAtributes();
		
		linkShaderProgram(shaderProgram);
		
		modelMatrixLoc = glGetUniformLocation(shaderProgram, "modelMatrix");
		viewMatrixLoc = glGetUniformLocation(shaderProgram, "viewMatrix");
		normalMatrixLoc = glGetUniformLocation(shaderProgram, "normalMatrix");
		projectionMatrixLoc = glGetUniformLocation(shaderProgram, "projectionMatrix");
		
		lightViewMatrixLoc = glGetUniformLocation(shaderProgram, "lightViewMatrix");
		lightProjectionMatrixLoc = glGetUniformLocation(shaderProgram, "lightProjectionMatrix");
		biasMatrixLoc = glGetUniformLocation(shaderProgram, "biasMatrix");
		
		cameraPosLoc = glGetUniformLocation(shaderProgram, "camera_Position");
		lightPosLoc = glGetUniformLocation(shaderProgram, "light_Position");
		
		rgbColorLoc = glGetUniformLocation(shaderProgram, "rgbColor");
		rgbaColorLoc = glGetUniformLocation(shaderProgram, "rgbaColor");
		
		// The shader supports 2 textures:
		// - The first is the normal texture
		// - The second is the depth texture for shadow rendering
		textureSampleLoc = glGetUniformLocation(shaderProgram, "textureSample");
		depthTextureSampleLoc = glGetUniformLocation(shaderProgram, "depthTextureSample");
		
		texLocList.add(textureSampleLoc);
		texLocList.add(depthTextureSampleLoc);
		
		cutoffLoc = glGetUniformLocation(shaderProgram, "cutoff");
		
		// If there are lights involved, get the light list
		if(useLights) 
		{
			
			pointLightsLoc = glGetUniformLocation(shaderProgram, "number_of_point_lights");
			directionalLightsLoc = glGetUniformLocation(shaderProgram, "number_of_directional_lights");
			spotLightsLoc = glGetUniformLocation(shaderProgram, "number_of_spot_lights");
		}
	}
	
	/**
	 * Compile the shader code.
	 * @param shaderSource The shader sources.
	 * @param shaderType The type of shader.
	 * @param key The name of the shader.
	 * @return The compiled shader ID.
	 */
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
	
	/**
	 * Link the shader program.
	 * @param shaderProgram shader program.
	 */
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
	
	/**
	 * Add all the default attributes to the shader.
	 */
	private void addAllAtributes()
	{
		
		// Position, texture, normal
		glBindAttribLocation(shaderProgram, 0, "in_Position");
		glBindAttribLocation(shaderProgram, 1, "in_TexCoord");
		glBindAttribLocation(shaderProgram, 2, "in_Normals");
		
		// MVP, this attribute is only used in the case of instanced rendering.
		// Normally this will be done via a uniform upload.
		glBindAttribLocation(shaderProgram, 3, "in_MVP");
	}
	
	/**
	 * Add an extra attribute to the shader (except for the position and texture coordinates).
	 * Examples of attributes that can be added are the MVP in the case of instanced rendering.
	 * @param name Name of the attribute in the shader.
	 * @param index Index of the attribute (2 or higher).
	 */
	public void addAtribute(String name, int index)
	{
		
		glBindAttribLocation(shaderProgram, index, name);
		System.out.println(name + " , " + shaderProgram);
	}
	
	public int getAttributeLocation(String name)
	{
		return glGetAttribLocation(shaderProgram, name);
	}
	
	public int getUniform(String name)
	{
		return glGetUniformLocation(shaderProgram, name);
	}
	
	public void addPointLight()
	{
		
		int i = LightManager.getNumberOfPointLights() - 1;
		
		String color = "pointLights[" + i + "].lightColor";
		String amb = "pointLights[" + i + "].ambIntensity";
		
		String position = "pointLights[" + i + "].lightPos";
		String attenuation = "pointLights[" + i + "].attFactor";
		
		pointLightColorLocList.add(glGetUniformLocation(shaderProgram, color));
		pointAmbIntensityLocList.add(glGetUniformLocation(shaderProgram, amb));
		
		pointLightPosLocList.add(glGetUniformLocation(shaderProgram, position));
		pointAttenuationFactorLocList.add(glGetUniformLocation(shaderProgram, attenuation));
	}
	
	public void addDirectionalLight()
	{
		
		int i = LightManager.getNumberOfDirectionalLights() - 1;
		
		String color = "dirLights[" + i + "].lightColor";
		String amb = "dirLights[" + i + "].ambIntensity";
		
		String direction = "dirLights[" + i + "].lightDir";
		
		directionalLightColorLocList.add(glGetUniformLocation(shaderProgram, color));
		directionalAmbIntensityLocList.add(glGetUniformLocation(shaderProgram, amb));
		
		directionalLightDirectionLocList.add(glGetUniformLocation(shaderProgram, direction));
	}
	
	public void addSpotLight()
	{
		
		int i = LightManager.getNumberOfSpotLights() - 1;
		
		String color = "spotLights[" + i + "].lightColor";
		String amb = "spotLights[" + i + "].ambIntensity";
		
		String position = "spotLights[" + i + "].lightPos";
		String attenuation = "spotLights[" + i + "].attFactor";
		
		String direction = "spotLights[" + i + "].lightDir";
		String coneAngle = "spotLights[" + i + "].coneAngle";
		
		spotLightColorLocList.add(glGetUniformLocation(shaderProgram, color));
		spotAmbIntensityLocList.add(glGetUniformLocation(shaderProgram, amb));
		
		spotLightPosLocList.add(glGetUniformLocation(shaderProgram, position));
		spotAttenuationFactorLocList.add(glGetUniformLocation(shaderProgram, attenuation));
		
		spotLightDirLocList.add(glGetUniformLocation(shaderProgram, direction));
		spotConeAngleLocList.add(glGetUniformLocation(shaderProgram, coneAngle));
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
	
	public int getLightProjectionMatrixLoc() {
		return lightProjectionMatrixLoc;
	}

	public int getLightViewMatrixLoc() {
		return lightViewMatrixLoc;
	}
	
	public int getBiasMatrixLoc() {
		return biasMatrixLoc;
	}

	public int getCameraPocLoc() {
		return cameraPosLoc;
	}
	
	public int getLightPosLoc() {
		return lightPosLoc;
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

	public void setUseLighting(boolean useLights) {
		this.useLights = useLights;
	}
	
	public int getRgbColorLoc() {
		return rgbColorLoc;
	}

	public int getRgbaColorLoc() {
		return rgbaColorLoc;
	}

	public int getTextureSampleLoc() {
		return textureSampleLoc;
	}

	public int getDepthTextureSampleLoc() {
		return depthTextureSampleLoc;
	}

	public ArrayList<Integer> getTextureLocList() {
		return texLocList;
	}
	
	public int getNumberOfPointLightsLoc() {
		return pointLightsLoc;
	}
	
	public int getNumberOfDirectionalLightsLoc() {
		return directionalLightsLoc;
	}
	
	public int getNumberOfSpotLightsLoc() {
		return spotLightsLoc;
	}

	public ArrayList<Integer> getPointLightColorLocList() {
		return pointLightColorLocList;
	}

	public ArrayList<Integer> getPointAmbIntensityLocList() {
		return pointAmbIntensityLocList;
	}

	public ArrayList<Integer> getPointLightPosLocList() {
		return pointLightPosLocList;
	}

	public ArrayList<Integer> getPointAttenuationFactorLocList() {
		return pointAttenuationFactorLocList;
	}

	public ArrayList<Integer> getDirectionalLightDirectionLocList() {
		return directionalLightDirectionLocList;
	}

	public ArrayList<Integer> getDirectionalAmbIntensityLocList() {
		return directionalAmbIntensityLocList;
	}

	public ArrayList<Integer> getDirectionalLightColorLocList() {
		return directionalLightColorLocList;
	}

	public ArrayList<Integer> getSpotLightColorLocList() {
		return spotLightColorLocList;
	}

	public ArrayList<Integer> getSpotAmbIntensityLocList() {
		return spotAmbIntensityLocList;
	}

	public ArrayList<Integer> getSpotLightPosLocList() {
		return spotLightPosLocList;
	}

	public ArrayList<Integer> getSpotAttenuationFactorLocList() {
		return spotAttenuationFactorLocList;
	}

	public ArrayList<Integer> getSpotLightDirLocList() {
		return spotLightDirLocList;
	}

	public ArrayList<Integer> getSpotConeAngleLocList() {
		return spotConeAngleLocList;
	}
}