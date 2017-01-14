package engine;

import java.nio.FloatBuffer;
import java.util.Random;

import fbo.FrameBufferObject;
import shaders.Shader;
import shapes.Point;
import shapes.Quad;

public abstract class EngineSystem {

	// Some general properties of systems:
	// The "name" of the system
	protected String name;
	
	// Particle system properties
	protected String psType;
	protected String pType;
	
	protected String[] allowedPType = {"sphere", "quad", "point"};
	
	// These are the particle types which contain MVP attributes in there VAO and can be used for instanced rendering.
	protected Point point;
	protected Quad quad;
	
	// Maximum particles in the system
	protected int maxParticles;
	
	// Floatbuffer for instanced rendering
	protected FloatBuffer buffer;
	
	// Their position
	protected float x;
	protected float y;
	protected float z;
	
	// Their direction
	protected float xDir;
	protected float yDir;
	protected float zDir;
	
	protected Random rand = new Random();
	
	// Frame buffer
	protected FrameBufferObject fbo;
	
	// Shader (for now just the basic instanced shader)
	protected Shader shader;
	
	public abstract void update();
	public abstract void render();
	
	public String getName() {
		return name;
	}
	public String getPsType() {
		return psType;
	}
	public String getpType() {
		return pType;
	}
	public String[] getAllowedPType() {
		return allowedPType;
	}
	public Point getPoint() {
		return point;
	}
	public Quad getQuad() {
		return quad;
	}
	public int getMaxParticles() {
		return maxParticles;
	}
	public FloatBuffer getBuffer() {
		return buffer;
	}
	public float getX() {
		return x;
	}
	public float getY() {
		return y;
	}
	public float getZ() {
		return z;
	}
	public float getxDir() {
		return xDir;
	}
	public float getyDir() {
		return yDir;
	}
	public float getzDir() {
		return zDir;
	}
	public Random getRand() {
		return rand;
	}
	public FrameBufferObject getFbo() {
		return fbo;
	}
	public Shader getShader() {
		return shader;
	}
}