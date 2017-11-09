package weapons.guns;

import entities.Entity;

public class Sniper extends Gun {

	public Sniper(Entity owner) {
		super(owner, 0);
		
		speed = 500f;
		bulletSpeedVariation = 10f;
		damage = 1f;
		fireRate = 0.9f;
		fireAngleVariation = (3.14159f / 24);
		accuracy = 6.0f;
		pierce = 0;
		kick = 0;
		knockback = 0;
		numBullets = 1;
	}
}