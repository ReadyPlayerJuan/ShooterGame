package weapons.guns;

import entities.LivingEntity;

public class BananaLauncher extends Gun {

	public BananaLauncher(LivingEntity owner, boolean damagesPlayers, boolean damagesEnemies) {
		super(owner, "bananalauncher", 1, damagesPlayers, damagesEnemies);
	}
}
