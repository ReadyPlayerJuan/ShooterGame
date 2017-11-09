package weapons.guns;

import entities.Entity;

public class Shotgun extends Gun {

	public Shotgun(Entity owner) {
		super(owner, 0);
		
		speed = 300f;
		bulletSpeedVariation = 20f;
		damage = 1f;
		fireRate = 1.1f;
		fireAngleVariation = (3.14159f / 8);
		accuracy = 1.5f;
		pierce = 0;
		kick = 0;
		knockback = 0;
		numBullets = 6;
	}

}
