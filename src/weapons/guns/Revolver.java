package weapons.guns;

import entities.Entity;
import renderEngine.DisplayManager;

public class Revolver extends Gun {
	int maxClipSize, clipSize;
	float clipReloadTime, clipReloadTimer;
	boolean prevAttacked;
	
	public Revolver(Entity owner, boolean damagesPlayers, boolean damagesEnemies) {
		super(owner, 0, damagesPlayers, damagesEnemies);
		
		speed = 350f;
		bulletSpeedVariation = 20f;
		damage = 1f;
		fireRate = 12.0f;
		fireAngleVariation = (3.14159f / 16);
		accuracy = 4.0f;
		pierce = 0;
		kick = 0;
		knockback = 0;
		numBullets = 1;
		
		maxClipSize = 6;
		clipSize = maxClipSize;
		clipReloadTime = 0.8f;
	}
	
	@Override
	public void update(boolean attacking, float direction) {
		float delta = DisplayManager.getDelta();
		
		fireTimer += delta;
		float fireDelay = 1.0f / fireRate;
		
		if(clipSize > 0) {
			if(attacking) {
				if(!prevAttacked) {
					shoot(direction);
					clipSize--;
					fireTimer = 0;
				} else {
					if(fireTimer > fireDelay) {
						fireTimer -= fireDelay;
						shoot(direction);
						clipSize--;
					}
				}
			}
		}
		if(clipSize == 0 || !attacking) {
			clipReloadTimer += delta;
			if(clipReloadTimer > clipReloadTime) {
				clipSize = maxClipSize;
				clipReloadTimer = 0;
				fireTimer = 0;
			}
		} else {
			clipReloadTimer = 0;
		}
		
		prevAttacked = attacking;
	}
}
