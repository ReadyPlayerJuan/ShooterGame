package weapons.guns;

import entities.LivingEntity;

public class Shotgun extends Gun {
	public Shotgun(LivingEntity owner, boolean damagesPlayers, boolean damagesEnemies) {
		super(owner, "shotgun", 0, damagesPlayers, damagesEnemies);
	}
}
