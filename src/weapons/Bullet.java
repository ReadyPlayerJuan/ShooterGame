package weapons;

import org.lwjgl.util.vector.Vector2f;

import entities.EntityAnimation;
import entities.EntityManager;
import entities.LivingEntity;
import entities.AI.BulletAI;
import renderEngine.DisplayManager;
import textures.TextureManager;

public class Bullet extends LivingEntity {
	private boolean damagesPlayers, damagesEnemies;
	private float bulletSpeed, damage;
	
	public Bullet(boolean damagesPlayers, boolean damagesEnemies, int bulletTextureIndex, Vector2f position, float direction, float speed, float damage) {
		super(position, new Vector2f(8, 8), direction - (3.14159f / 2));
		this.entityType = "bullet";
		
		velocity = new Vector2f((float)Math.cos(direction) * speed, -(float)Math.sin(direction) * speed);
		this.ai = new BulletAI(this, velocity);
		this.health = 1;
		
		addAnimation(new EntityAnimation(TextureManager.PROJECTILE_TEXTURE, new int[] {bulletTextureIndex}));
		setRotatePoint(0.5f, 0.5f);
		setSpriteOffset(-4, 4);
		
		this.bulletSpeed = speed;
		this.damage = damage;
		this.canBePushed = false;
		this.damagesPlayers = damagesPlayers;
		this.damagesEnemies = damagesEnemies;
		
		hitboxRadius = 2;
		maxVelocity = 999f;
		acceleration = 0f;
		decceleration = 0f;
		friction = 0f;
	}
	
	@Override
	public void update() {
		super.update();
	}
	
	@Override
	public void move() {
		super.move();
	}
	
	@Override
	protected void collideWithEntities() {
		float delta = DisplayManager.getDelta();
		
		if(damagesEnemies) {
			for(LivingEntity entity: EntityManager.getEnemies()) {
				if(entity.getID() != this.getID()) {
					Vector2f displacement = Vector2f.sub(position, entity.getPosition(), null);
					float distance = displacement.length() / (hitboxRadius + entity.getHitboxRadius());
					
					if(distance < 1 && displacement.length() > 0) {
						displacement.normalise();
						
						//entity.push((Vector2f)displacement.scale(-1 * acceleration * SOFT_COLLISION_PUSH_FACTOR * delta));
						
						this.collidedWith(entity);
						entity.collidedWith(this);
					}
				}
			}
		}
		if(damagesPlayers) {
			for(LivingEntity entity: EntityManager.getPlayers()) {
				if(entity.getID() != this.getID()) {
					Vector2f displacement = Vector2f.sub(position, entity.getPosition(), null);
					float distance = displacement.length() / (hitboxRadius + entity.getHitboxRadius());
					
					if(distance < 1 && displacement.length() > 0) {
						displacement.normalise();
						
						//entity.push((Vector2f)displacement.scale(-1 * acceleration * SOFT_COLLISION_PUSH_FACTOR * delta));
						
						this.collidedWith(entity);
						entity.collidedWith(this);
					}
				}
			}
		}
	}
	
	@Override
	public void collidedWith(LivingEntity other) {
		if(other == null) {
			health = 0;
		} else {
			health = 0;
		}
	}
	
	public float getBulletSpeed() {
		return bulletSpeed;
	}
	
	public float getDamage() {
		return damage;
	}
	
	public boolean damagesEnemies() {
		return damagesEnemies;
	}

	public boolean damagesPlayers() {
		return damagesPlayers;
	}
}