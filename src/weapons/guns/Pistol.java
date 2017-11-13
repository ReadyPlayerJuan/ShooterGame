package weapons.guns;

import entities.LivingEntity;

public class Pistol extends Gun {
	public Pistol(LivingEntity owner, boolean damagesPlayers, boolean damagesEnemies) {
		super(owner, "pistol", 0, damagesPlayers, damagesEnemies);
	}
}