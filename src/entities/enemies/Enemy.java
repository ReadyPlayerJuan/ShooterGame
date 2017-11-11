package entities.enemies;

import org.lwjgl.util.vector.Vector2f;

import entities.LivingEntity;
import weapons.guns.Gun;

public class Enemy extends LivingEntity {
	
	public Enemy(Vector2f position) {
		super(position, new Vector2f(16, 16), 0);
		this.entityType = "enemy";
		setSpriteOffset(-8, 8);
		
		weapon = new Gun(this, 0, true, false);
		
		this.health = 1;
	}
}