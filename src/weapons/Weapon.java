package weapons;

import org.lwjgl.util.vector.Vector2f;

import entities.Entity;

public abstract class Weapon {
	protected Entity owner;
	protected Vector2f positionOffset;
	
	public Weapon(Entity owner) {
		this.owner = owner;
		this.positionOffset = new Vector2f();
	}
	
	public Weapon(Entity owner, Vector2f positionOffset) {
		this.owner = owner;
		this.positionOffset = positionOffset;
	}
	
	public void setOffset(Vector2f offset) {
		positionOffset = offset;
	}
	
	public Vector2f getOffset() {
		return positionOffset;
	}
	
	public abstract void attack(float direction);
	public abstract void update();
}