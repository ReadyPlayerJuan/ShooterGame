package weapons.guns;

import entities.LivingEntity;

public class MachineGun extends Gun {
	public MachineGun(LivingEntity owner, boolean damagesPlayers, boolean damagesEnemies) {
		super(owner, "machinegun", 0, damagesPlayers, damagesEnemies);
	}
}