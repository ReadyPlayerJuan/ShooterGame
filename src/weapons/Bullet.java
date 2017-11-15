package weapons;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.vector.Vector2f;

import entities.EntityAnimation;
import entities.EntityManager;
import entities.LivingEntity;
import entities.AI.BulletAI;
import hitboxes.LineHitbox;
import maps.MapHitboxes;
import particles.Particle;
import textures.TextureManager;

public class Bullet extends LivingEntity {
	private boolean damagesPlayers, damagesEnemies;
	private float bulletSpeed, damage, knockback;
	private int pierce, bounce;
	
	private List<Integer> entitiesHit = new ArrayList<Integer>();
	
	public Bullet(boolean damagesPlayers, boolean damagesEnemies, int bulletTextureIndex, Vector2f position, float direction, float speed, float damage, float knockback, float pierce, float bounce) {
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
		this.knockback = knockback;
		this.pierce = (int)pierce;
		this.bounce = (int)bounce;
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
	protected void collideWithTerrain() {
		MapHitboxes hitboxes = EntityManager.getStaticHitboxes();
		
		if(velocity.x > 0) {
			for(LineHitbox hitbox: hitboxes.getHitboxesL()) {
				if(nextPosition.y + hitboxRadius > hitbox.getEndPoint1().y && nextPosition.y - hitboxRadius < hitbox.getEndPoint2().y &&
						position.x + hitboxRadius <= hitbox.getCenterPosition().x && nextPosition.x + hitboxRadius > hitbox.getCenterPosition().x) {
					nextPosition.setX(hitbox.getCenterPosition().x - hitboxRadius);
					
					if(bounce > 0) {
						rotation = -rotation;
						velocity.x *= -1;
					}
					this.collidedWith(null);
				}
			}
		}
		if(velocity.x < 0) {
			for(LineHitbox hitbox: hitboxes.getHitboxesR()) {
				if(nextPosition.y + hitboxRadius > hitbox.getEndPoint1().y && nextPosition.y - hitboxRadius < hitbox.getEndPoint2().y &&
						position.x - hitboxRadius >= hitbox.getCenterPosition().x && nextPosition.x - hitboxRadius < hitbox.getCenterPosition().x) {
					nextPosition.setX(hitbox.getCenterPosition().x + hitboxRadius);
					
					if(bounce > 0) {
						rotation = -rotation;
						velocity.x *= -1;
					}
					this.collidedWith(null);
				}
			}
		}
		if(velocity.y > 0) {
			for(LineHitbox hitbox: hitboxes.getHitboxesU()) {
				if(nextPosition.x + hitboxRadius > hitbox.getEndPoint1().x && nextPosition.x - hitboxRadius < hitbox.getEndPoint2().x &&
						position.y + hitboxRadius <= hitbox.getCenterPosition().y && nextPosition.y + hitboxRadius > hitbox.getCenterPosition().y) {
					nextPosition.setY(hitbox.getCenterPosition().y - hitboxRadius);

					if(bounce > 0) {
						rotation = 3.14159f - rotation;
						velocity.y *= -1;
					}
					this.collidedWith(null);
				}
			}
		}
		if(velocity.y < 0) {
			for(LineHitbox hitbox: hitboxes.getHitboxesD()) {
				if(nextPosition.x + hitboxRadius > hitbox.getEndPoint1().x && nextPosition.x - hitboxRadius < hitbox.getEndPoint2().x &&
						position.y - hitboxRadius >= hitbox.getCenterPosition().y && nextPosition.y - hitboxRadius < hitbox.getCenterPosition().y) {
					nextPosition.setY(hitbox.getCenterPosition().y + hitboxRadius);
					
					if(bounce > 0) {
						rotation = 3.14159f - rotation;
						velocity.y *= -1;
					}
					this.collidedWith(null);
				}
			}
		}
	}
	
	@Override
	protected void collideWithEntities() {
		if(damagesEnemies) {
			for(LivingEntity entity: EntityManager.getEnemies()) {
				if(entity.getID() != this.getID()) {
					Vector2f displacement = Vector2f.sub(position, entity.getPosition(), null);
					float distance = displacement.length() / (hitboxRadius + entity.getHitboxRadius());
					
					if(distance < 1) {
						boolean alreadyHit = false;
						for(Integer i: entitiesHit) {
							if(entity.getID() == i)
								alreadyHit = true;
						}
						
						if(!alreadyHit) {
							entitiesHit.add(entity.getID());
							
							this.collidedWith(entity);
							entity.collidedWith(this);
						}
					}
				}
			}
		}
		if(damagesPlayers) {
			for(LivingEntity entity: EntityManager.getPlayers()) {
				if(entity.getID() != this.getID()) {
					Vector2f displacement = Vector2f.sub(position, entity.getPosition(), null);
					float distance = displacement.length() / (hitboxRadius + entity.getHitboxRadius());
					
					if(distance < 1) {
						boolean alreadyHit = false;
						for(Integer i: entitiesHit) {
							if(entity.getID() == i)
								alreadyHit = true;
						}
						
						if(!alreadyHit) {
							entitiesHit.add(entity.getID());
							
							this.collidedWith(entity);
							entity.collidedWith(this);
						}
					}
				}
			}
		}
	}
	
	@Override
	public void collidedWith(LivingEntity other) {
		if(other == null) {
			bounce--;
			entitiesHit = new ArrayList<Integer>();
			
			if(bounce < 0)
				destroyBullet(true);
		} else {
			pierce--;
			if(pierce < 0) {
				bounce--;
				entitiesHit = new ArrayList<Integer>();
				
				if(bounce < 0) {
					destroyBullet(false);
				} else {
					Vector2f toEntity = Vector2f.sub(position, other.getPosition(), null);
					float angleToEntity = (float)Math.atan2(toEntity.y, toEntity.x);
					if(angleToEntity < 0)
						angleToEntity += 3.14159f * 2;
					
					if((angleToEntity > 3.14159f / 4 && angleToEntity < 3.14159f * 3 / 4) || (angleToEntity > 3.14159f * 5 / 4 && angleToEntity < 3.14159f * 7 / 4)) {
						velocity.y *= -1;
						rotation = 3.14159f - rotation;
					} else {
						velocity.x *= -1;
						rotation = -rotation;
					}
					
					toEntity.normalise();
					position = Vector2f.add(other.getPosition(), (Vector2f)toEntity.scale(other.getHitboxRadius() + hitboxRadius + 0.01f), null);
				}
			}
			
			Vector2f unitVector = (Vector2f)(new Vector2f(velocity)).normalise();
			other.push((Vector2f)unitVector.scale(knockback));
		}
	}
	
	private void destroyBullet(boolean withParticles) {
		health = 0;
		
		if(withParticles) {
			for(int i = 0; i < 40; i++) {
				float speed = velocity.length() * ((float)Math.random() * 0.2f + 0.15f);
				float life = ((float)Math.random() * 0.35f) + 0.05f;
				float angle = (float)Math.atan2(velocity.y, velocity.x) + ((float)Math.random() * 0.8f - 0.4f);
				int colorTheme = 1;
				if(Math.random() < (1.0 / 8))
					colorTheme = 2;
				new Particle(colorTheme, new Vector2f(position), new Vector2f(speed * (float)Math.cos(angle), speed * (float)Math.sin(angle)), speed / life / 1, life);
			}
		}
	}
	
	public float getBulletSpeed() {
		return bulletSpeed;
	}
	
	public float getDamage() {
		return damage;
	}
	
	public float getKnockback() {
		return knockback;
	}
	
	public boolean damagesEnemies() {
		return damagesEnemies;
	}

	public boolean damagesPlayers() {
		return damagesPlayers;
	}
}