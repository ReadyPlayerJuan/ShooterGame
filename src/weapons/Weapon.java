package weapons;

import org.lwjgl.util.vector.Vector2f;

import entities.Entity;

public abstract class Weapon {
	protected Entity owner;
	protected Vector2f positionOffset;

	protected boolean damagesPlayers, damagesEnemies;
	
	public Weapon(Entity owner, boolean damagesPlayers, boolean damagesEnemies) {
		this.owner = owner;
		this.positionOffset = new Vector2f();
		this.damagesPlayers = damagesPlayers;
		this.damagesEnemies = damagesEnemies;
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
	
	public abstract void update(boolean attacking, float direction);
}