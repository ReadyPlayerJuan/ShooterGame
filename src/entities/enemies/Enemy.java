package entities.enemies;

import org.lwjgl.util.vector.Vector2f;

import entities.LivingEntity;
import weapons.Bullet;
import weapons.guns.Pistol;

public class Enemy extends LivingEntity {
	public Enemy(Vector2f position) {
		super(position, new Vector2f(16, 16), 0);
		this.entityType = "enemy";
		setSpriteOffset(-8, 8);
		
		weapon = new Pistol(this, true, false);
		
		this.health = 1;
	}
	
	@Override
	public void collidedWith(LivingEntity other) {
		if(other != null) {
			if(other.getEntityType() == "bullet") {
				takeDamage((Bullet)other);
			}
		}
	}
}