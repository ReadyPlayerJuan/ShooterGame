package weapons.guns;

import entities.LivingEntity;
import renderEngine.DisplayManager;

public class Revolver extends Gun {
	int maxClipSize, clipSize;
	float clipReloadTime, clipReloadTimer;
	boolean prevAttacked;
	
	public Revolver(LivingEntity owner, boolean damagesPlayers, boolean damagesEnemies) {
		super(owner, "revolver", 0, damagesPlayers, damagesEnemies);
		
		maxClipSize = (int)(float)stats.get(11);
		clipReloadTime = stats.get(12);
		clipSize = maxClipSize;
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
