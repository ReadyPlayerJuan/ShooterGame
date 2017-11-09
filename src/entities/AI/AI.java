package entities.AI;

import org.lwjgl.util.vector.Vector2f;

import entities.Entity;

public abstract class AI {
	public Entity owner;
	protected Vector2f movementDirection;
	protected boolean attacking;
	protected float attackDirection;
	
	public AI(Entity owner) {
		this.owner = owner;
		
		movementDirection = new Vector2f();
	}
	
	public abstract void update();
	
	public Vector2f getMovementDirection() {
		return movementDirection;
	}
	
	public boolean isAttacking() {
		return attacking;
	}
	
	public float getAttackDirection() {
		return attackDirection;
	}
}