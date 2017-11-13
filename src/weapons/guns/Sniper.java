package weapons.guns;

import entities.LivingEntity;

public class Sniper extends Gun {
	public Sniper(LivingEntity owner, boolean damagesPlayers, boolean damagesEnemies) {
		super(owner, "sniper", 0, damagesPlayers, damagesEnemies);
	}
}