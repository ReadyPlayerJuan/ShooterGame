package weapons.guns;

import org.lwjgl.util.vector.Vector2f;

import entities.Entity;
import entities.EntityManager;
import renderEngine.DisplayManager;
import weapons.Bullet;
import weapons.Weapon;

public class Gun extends Weapon {
	private int bulletTextureIndex;

	protected float fireTimer;
	protected float speed, bulletSpeedVariation, damage, fireRate, fireAngleVariation, accuracy, pierce, kick, knockback, numBullets;
	
	public Gun(Entity owner, int bulletTextureIndex) {
		super(owner);
		this.bulletTextureIndex = bulletTextureIndex;
		
		speed = 300f;
		bulletSpeedVariation = 0;
		damage = 1f;
		fireRate = 4.0f;
		fireAngleVariation = (3.14159f / 24);
		accuracy = 4.0f;
		pierce = 0;
		kick = 0;
		knockback = 0;
		numBullets = 1;
		
		
		fireTimer = 1.0f / fireRate;
		update();
	}
	
	public void update() {
		float delta = DisplayManager.getDelta();
		
		fireTimer += delta;
		
		float fireDelay = 1.0f / fireRate;
		if(fireTimer > fireDelay)
			fireTimer = fireDelay;
	}

	@Override
	public void attack(float direction) {
		float fireDelay = 1.0f / fireRate;
		
		while(fireTimer >= fireDelay) {
			shoot(direction);
			
			fireTimer -= fireDelay;
		}
	}
	
	public void shoot(float direction) {
		for(int i = 0; i < numBullets; i++) {
			float angleVariation = (float)Math.pow(Math.random(), accuracy) * fireAngleVariation * (int)(((int)(Math.random() * 2) - 0.5) * 2);
			float speedVariation = (float)Math.random() * bulletSpeedVariation * (int)(((int)(Math.random() * 2) - 0.5) * 2);
			
			EntityManager.addBullet(new Bullet(bulletTextureIndex, Vector2f.add(owner.getPosition(), positionOffset, null), direction + angleVariation, speed + speedVariation, damage));
		}
	}
}