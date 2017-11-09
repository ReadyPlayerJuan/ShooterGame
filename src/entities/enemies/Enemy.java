package entities.enemies;

import org.lwjgl.util.vector.Vector2f;

import entities.LivingEntity;
import weapons.guns.Gun;

public class Enemy extends LivingEntity {
	
	public Enemy(Vector2f position) {
		super(position, new Vector2f(16, 16), 0);
		setSpriteOffset(-8, 8);
		
		weapon = new Gun(this, 1);
		
		movementDirection = new Vector2f();
		velocity = new Vector2f();
		
		this.health = 1;
	}
}