package weapons.guns;

import entities.Entity;

public class Pistol extends Gun {
	public Pistol(Entity owner, boolean damagesPlayers, boolean damagesEnemies) {
		super(owner, 0, damagesPlayers, damagesEnemies);
		
		speed = 300f;
		bulletSpeedVariation = 20f;
		damage = 1f;
		fireRate = 2.2f;
		fireAngleVariation = (3.14159f / 12);
		accuracy = 2.0f;
		pierce = 0;
		kick = 0;
		knockback = 0;
		numBullets = 1;
	}
}