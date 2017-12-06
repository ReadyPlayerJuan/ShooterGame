package entities;

import org.lwjgl.util.vector.Vector2f;

import entities.AI.AI;
import entities.AI.TestAI;
import entities.numbers.NumberString;
import hitboxes.LineHitbox;
import maps.MapHitboxes;
import particles.Particle;
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
				
				if(velocity.x * Math.signum(normal.x) < 0) { velocity.x = 0; }
				if(velocity.y * Math.signum(normal.y) < 0) { velocity.y = 0; }
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
		
		{
			float direction = -(3.14159f / 2) + ((float)Math.random() * 0.3f * randPosOrNeg());
			float distance = hitboxRadius + 2;
			float speed = 50f;
			new NumberString((int)bullet.getDamage(), false, new Vector2f(position.x + distance * (float)Math.cos(direction), position.y + distance * (float)Math.sin(direction)),
					new Vector2f(speed * (float)Math.cos(direction), speed * (float)Math.sin(direction)), 40);
		};
		
		
		if(health > 0) {
			Vector2f particlePosition = new Vector2f(bullet.getPosition());
			float distance = -4f;
			Vector2f.add(particlePosition, (Vector2f)(new Vector2f(bullet.getVelocity())).normalise(null).scale(-distance), particlePosition);
			
			float bulletDirection = (float)Math.atan2(bullet.getVelocity().y, bullet.getVelocity().x);
			float particleDirection = (float)Math.atan2(position.y - particlePosition.y, position.x - particlePosition.x);
			particleDirection = 3.14159f + (particleDirection + bulletDirection) / 2;
			
			for(int i = 0; i < 50; i++) {
				float speed = bullet.getVelocity().length() * ((float)Math.random() * 0.4f + 0.1f);
				float life = ((float)Math.random() * 0.4f) + 0.05f;
				float angle = particleDirection + ((float)Math.pow(Math.random(), 2) * 1.1f * randPosOrNeg());
				new Particle(0, new Vector2f(particlePosition), new Vector2f(speed * (float)Math.cos(angle) + velocity.x, speed * (float)Math.sin(angle) + velocity.y), speed / life / 1.2f, life);
			}
		} else {
			Vector2f particlePosition = new Vector2f(position);
			
			for(int i = 0; i < 450; i++) {
				float speed = 400f * ((float)Math.random() * 0.55f + 0.0f);
				float life = ((float)Math.pow(Math.random(), 3) * 0.7f) + 0.1f;
				float angle = (float)Math.random() * 3.14159f * 2;
				new Particle(0, new Vector2f(particlePosition), new Vector2f(speed * (float)Math.cos(angle) + velocity.x, speed * (float)Math.sin(angle) + velocity.y), speed / life / 1.1f, life);
			}
		}
	}
	
	private float randPosOrNeg() {
		return (float)((int)(Math.random() * 2)) * 2 - 1;
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
	
	public Weapon getWeapon() {
		return weapon;
	}
}
