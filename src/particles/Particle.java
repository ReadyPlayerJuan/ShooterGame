package particles;

import org.lwjgl.util.vector.Vector2f;

import renderEngine.DisplayManager;

public class Particle {
	private Vector2f position, velocity;
	private float size;
	private float decceleration;
	private float rotation;

	private float lifeSpan;

	public Particle(Vector2f position, Vector2f velocity, float decceleration, float size, float rotation, float lifeSpan) {
		this.position = position;
		this.velocity = velocity;
		this.decceleration = decceleration;
		this.size = size;
		this.rotation = rotation;
		this.lifeSpan = lifeSpan;
		
		ParticleMaster.addParticle(this);
	}

	public void update() {
		float delta = DisplayManager.getDelta();
		lifeSpan -= delta;
		
		Vector2f.add(position, (Vector2f)velocity.scale(delta), position);
		
		if(velocity.length() != 0) {
			Vector2f normal = (Vector2f)(new Vector2f(velocity)).normalise();
			velocity = new Vector2f(velocity.x - (decceleration * normal.x * delta), velocity.y - (decceleration * normal.y * delta));
		}
	}
	
	public boolean isAlive() {
		return lifeSpan > 0;
	}
	
	public Vector2f getPosition() {
		return position;
	}

	public Vector2f getVelocity() {
		return velocity;
	}

	public float getSize() {
		return size;
	}

	public float getDecceleration() {
		return decceleration;
	}

	public float getRotation() {
		return rotation;
	}

	public float getLifeSpan() {
		return lifeSpan;
	}
}
