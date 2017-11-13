package weapons.guns;

import java.util.ArrayList;

import org.lwjgl.util.vector.Vector2f;

import entities.EntityManager;
import entities.LivingEntity;
import renderEngine.DisplayManager;
import weapons.Bullet;
import weapons.Weapon;

public class Gun extends Weapon {
	private int bulletTextureIndex;

	protected float fireTimer;
	protected ArrayList<Float> stats;
	protected float bulletSpeed, bulletSpeedVariation, damage, fireRate, spread, accuracy, pierce, kick, knockback, numBullets, bounce;
	
	public Gun(LivingEntity owner, String gunName, int bulletTextureIndex, boolean damagesPlayers, boolean damagesEnemies) {
		super(owner, damagesPlayers, damagesEnemies);
		this.bulletTextureIndex = bulletTextureIndex;
		
		stats = Weapon.getStats(gunName);
		bulletSpeed = stats.get(0);
		bulletSpeedVariation = stats.get(1);
		damage = stats.get(2);
		fireRate = stats.get(3);
		spread = stats.get(4);
		accuracy = stats.get(5);
		pierce = stats.get(6);
		bounce = stats.get(7);
		kick = stats.get(8);
		knockback = stats.get(9);
		numBullets = stats.get(10);
		
		
		fireTimer = 1.0f / fireRate;
		update(false, 0);
	}
	
	@Override
	public void update(boolean attacking, float direction) {
		float delta = DisplayManager.getDelta();

		float fireDelay = 1.0f / fireRate;
		if(attacking) {
			fireTimer += delta;
			
			if(fireTimer >= fireDelay) {
				shoot(direction);
				
				fireTimer -= fireDelay;
			}
		} else {
			fireTimer += delta;
			
			if(fireTimer > fireDelay)
				fireTimer = fireDelay;
		}
	}
	
	public void shoot(float direction) {
		for(int i = 0; i < numBullets; i++) {
			float angleVariation = (float)(Math.pow(Math.random(), accuracy) * Math.toRadians(spread) * (int)(((int)(Math.random() * 2) - 0.5) * 2));
			float speedVariation = (float)Math.random() * bulletSpeedVariation * (int)(((int)(Math.random() * 2) - 0.5) * 2);
			
			EntityManager.addBullet(new Bullet(damagesPlayers, damagesEnemies, bulletTextureIndex, Vector2f.add(owner.getPosition(), positionOffset, null),
					direction + angleVariation, bulletSpeed + speedVariation, damage, knockback, pierce, bounce));
		}
		Vector2f directionV = new Vector2f((float)Math.cos(direction), -(float)Math.sin(direction));
		owner.push((Vector2f)directionV.scale(kick * -1));
	}
}