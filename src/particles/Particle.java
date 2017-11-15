package particles;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector4f;

import renderEngine.DisplayManager;

public class Particle {
	private Vector2f position, velocity;
	private float decceleration;
	
	private Vector4f color;
	private float lifeSpan;

	public Particle(int colorTheme, Vector2f position, Vector2f velocity, float decceleration, float lifeSpan) {
		this.position = position;
		this.velocity = velocity;
		this.decceleration = decceleration;
		this.lifeSpan = lifeSpan;
		
		color = ParticleMaster.getParticleColor(colorTheme);
		
		ParticleMaster.addParticle(this);
	}

	public void update() {
		float delta = DisplayManager.getDelta();
		lifeSpan -= delta;
		
		Vector2f.add(position, (Vector2f)(new Vector2f(velocity)).scale(delta), position);
		
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

	public float getDecceleration() {
		return decceleration;
	}

	public float getLifeSpan() {
		return lifeSpan;
	}

	public Vector4f getColor() {
		return color;
	}
}
