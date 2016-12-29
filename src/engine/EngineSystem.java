package engine;

public abstract class EngineSystem {

	// Some general properties of systems:
	// The "name" of the system
	protected String name;
	
	// Their position
	protected float x;
	protected float y;
	protected float z;
	
	public abstract void update();
	public abstract void render();
	
	public String getName() {return name;}
}
