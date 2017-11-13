package entities;

import org.lwjgl.util.vector.Vector2f;

import entities.AI.AI;
import entities.AI.TestAI;
import hitboxes.LineHitbox;
import maps.MapHitboxes;
import renderEngine.DisplayManager;
import weapons.Bullet;
import weapons.Weapon;

public class LivingEntity extends AnimatedEntity {
	protected Weapon weapon;
	protected float health;
	
	protected AI ai;

	protected Vector2f velocity, nextPosition;
	protected boolean canBePushed = true;
	protected boolean pushed = false;
	
	protected float hitboxRadius = 8f;
	protected float maxVelocity = 150f;
	protected float acceleration = 1500f;
	protected float decceleration = 750f;
	protected float friction = 200f;
	
	protected final static float SOFT_COLLISION_PUSH_FACTOR = 0.12f + (0.00180952381f * (DisplayManager.FPS_CAP - 30));
	
	public LivingEntity(Vector2f position, Vector2f size, float rotation) {
		super(position, size, rotation);
		
		ai = new TestAI(this);
		
		velocity = new Vector2f();
	}
	
	@Override
	public void update() {
		float delta = DisplayManager.getDelta();
		
		ai.update();
		
		if(weapon != null) {
			weapon.update(ai.isAttacking(), ai.getAttackDirection());
		}

		pushed = false;
		collideWithEntities();
		
		Vector2f movementDirection = ai.getMovementDirection();
		if(movementDirection.length() > 0.1f) {
			float movementModifierX = (float)Math.pow(Math.min(1, Math.max(0, 1 - (velocity.x / (maxVelocity * Math.signum(movementDirection.x + 0.001f))))), 0.5);
			float movementModifierY = (float)Math.pow(Math.min(1, Math.max(0, 1 - (velocity.y / (maxVelocity * Math.signum(movementDirection.y + 0.001f))))), 0.5);
			
			velocity.x += acceleration * movementModifierX * delta * movementDirection.x;
			velocity.y += acceleration * movementModifierY * delta * movementDirection.y;
		} else {
			if(velocity.length() != 0 && !pushed) {
				Vector2f normal = (Vector2f)(new Vector2f(velocity)).normalise();
				velocity = new Vector2f(velocity.x - (decceleration * normal.x * delta), velocity.y - (decceleration * normal.y * delta));
			}
		}
		
		if(velocity.length() != 0) {
			Vector2f normal = (Vector2f)(new Vector2f(velocity)).normalise();
			velocity = new Vector2f(velocity.x - (friction * velocity.x * delta), velocity.y - (friction * velocity.y * delta));
			
			if(Math.signum(velocity.x / normal.x) == -1 || Math.signum(velocity.y / normal.y) == -1) {
				velocity = new Vector2f();
			}
		}
	}
	
	@Override
	public void move() {
		float delta = DisplayManager.getDelta();
		nextPosition = new Vector2f(position.x + (velocity.x * delta), position.y + (velocity.y * delta));
		
		collideWithTerrain();
		
		position = nextPosition;
		calculatePositionMatrix();
	}
	
	protected void collideWithTerrain() {
		MapHitboxes hitboxes = EntityManager.getStaticHitboxes();
		
		if(velocity.x > 0) {
			for(LineHitbox hitbox: hitboxes.getHitboxesL()) {
				if(nextPosition.y + hitboxRadius > hitbox.getEndPoint1().y && nextPosition.y - hitboxRadius < hitbox.getEndPoint2().y &&
						position.x + hitboxRadius <= hitbox.getCenterPosition().x && nextPosition.x + hitboxRadius > hitbox.getCenterPosition().x) {
					nextPosition.setX(hitbox.getCenterPosition().x - hitboxRadius);
					velocity.setX(0);
					this.collidedWith(null);
				}
			}
		}
		if(velocity.x < 0) {
			for(LineHitbox hitbox: hitboxes.getHitboxesR()) {
				if(nextPosition.y + hitboxRadius > hitbox.getEndPoint1().y && nextPosition.y - hitboxRadius < hitbox.getEndPoint2().y &&
						position.x - hitboxRadius >= hitbox.getCenterPosition().x && nextPosition.x - hitboxRadius < hitbox.getCenterPosition().x) {
					nextPosition.setX(hitbox.getCenterPosition().x + hitboxRadius);
					velocity.setX(0);
					this.collidedWith(null);
				}
			}
		}
		if(velocity.y > 0) {
			for(LineHitbox hitbox: hitboxes.getHitboxesU()) {
				if(nextPosition.x + hitboxRadius > hitbox.getEndPoint1().x && nextPosition.x - hitboxRadius < hitbox.getEndPoint2().x &&
						position.y + hitboxRadius <= hitbox.getCenterPosition().y && nextPosition.y + hitboxRadius > hitbox.getCenterPosition().y) {
					nextPosition.setY(hitbox.getCenterPosition().y - hitboxRadius);
					velocity.setY(0);
					this.collidedWith(null);
				}
			}
		}
		if(velocity.y < 0) {
			for(LineHitbox hitbox: hitboxes.getHitboxesD()) {
				if(nextPosition.x + hitboxRadius > hitbox.getEndPoint1().x && nextPosition.x - hitboxRadius < hitbox.getEndPoint2().x &&
						position.y - hitboxRadius >= hitbox.getCenterPosition().y && nextPosition.y - hitboxRadius < hitbox.getCenterPosition().y) {
					nextPosition.setY(hitbox.getCenterPosition().y + hitboxRadius);
					velocity.setY(0);
					this.collidedWith(null);
				}
			}
		}
	}
	
	protected void collideWithEntities() {
		for(LivingEntity entity: EntityManager.getEnemies()) {
			checkForCollisionWith(entity);
		}
		for(LivingEntity entity: EntityManager.getPlayers()) {
			checkForCollisionWith(entity);
		}
	}
	
	private void checkForCollisionWith(LivingEntity entity) {
		float delta = DisplayManager.getDelta();
		
		if(entity.getID() != this.getID() && entity.canBePushed()) {
			Vector2f displacement = Vector2f.sub(position, entity.getPosition(), null);
			float distance = displacement.length() / (hitboxRadius + entity.hitboxRadius);
			
			if(distance < 1 && displacement.length() > 0) {
				displacement.normalise();
				Vector2f perpendicular = new Vector2f(-displacement.y, displacement.x);
				
				float speed1 = (Vector2f.dot(velocity, displacement));
				float speed2 = (Vector2f.dot(entity.getVelocity(), displacement));
				float averageSpeed = (speed1 + speed2) / 2;
				
				Vector2f newVelocity1 = Vector2f.add((Vector2f)(new Vector2f(perpendicular)).scale(Vector2f.dot(velocity, perpendicular)),
						(Vector2f)(new Vector2f(displacement)).scale(averageSpeed), null);
				Vector2f newVelocity2 = Vector2f.add((Vector2f)(new Vector2f(perpendicular)).scale(Vector2f.dot(entity.getVelocity(), perpendicular)),
						(Vector2f)(new Vector2f(displacement)).scale(averageSpeed), null);
				
				this.setVelocity(newVelocity1);
				entity.setVelocity(newVelocity2);
				
				
				this.push((Vector2f)displacement.scale(entity.acceleration * SOFT_COLLISION_PUSH_FACTOR * delta));
				entity.push((Vector2f)displacement.scale(-1 * acceleration * SOFT_COLLISION_PUSH_FACTOR * delta));
				
				
				this.collidedWith(entity);
				entity.collidedWith(this);
			}
		}
	}
	
	public void collidedWith(LivingEntity other) {
		
	}
	
	protected void takeDamage(Bullet bullet) {
		health -= bullet.getDamage();
	}
	
	public void push(Vector2f velocity) {
		Vector2f.add(this.velocity, velocity, this.velocity);
	}
	
	public void setVelocity(Vector2f velocity) {
		this.velocity = velocity;
	}
	
	public boolean isAlive() {
		return health > 0;
	}

	public Vector2f getVelocity() {
		return velocity;
	}
	
	public float getHitboxRadius() {
		return hitboxRadius;
	}

	public float getMaxVelocity() {
		return maxVelocity;
	}

	public float getAcceleration() {
		return acceleration;
	}

	public float getDecceleration() {
		return decceleration;
	}

	public float getFriction() {
		return friction;
	}

	public boolean canBePushed() {
		return canBePushed;
	}
}
