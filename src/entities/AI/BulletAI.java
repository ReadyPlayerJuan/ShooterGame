package entities.AI;

import org.lwjgl.util.vector.Vector2f;

import entities.Entity;

public class BulletAI extends AI {

	public BulletAI(Entity owner, Vector2f movementDirection) {
		super(owner);
		
		this.movementDirection = movementDirection;
	}

	@Override
	public void update() {
		
	}
}
