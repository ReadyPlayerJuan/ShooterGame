package entities;

import org.lwjgl.util.vector.Vector2f;

import entities.AI.AI;
import maps.LineHitbox;
import maps.MapHitboxes;
import renderEngine.DisplayManager;
import weapons.Weapon;

public class LivingEntity extends AnimatedEntity {
	protected Weapon weapon;
	protected float health;
	
	protected AI ai;
	
	protected Vector2f movementDirection;
	protected Vector2f velocity;
	
	protected float HITBOX_RADIUS = 8;
	protected float MAX_VELOCITY = 200f;
	protected float ACCELERATION = 1500f;
	protected float FRICTION = 750f;
	
	public LivingEntity(Vector2f position, Vector2f size, float rotation) {
		super(position, size, rotation);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void update() {
		float delta = DisplayManager.getDelta();

		ai.update();
		
		if(ai.isAttacking()) {
			weapon.attack(ai.getAttackDirection());
		}
		movementDirection = ai.getMovementDirection();
		
		
		
		if(movementDirection.length() > 0.1f) {
			velocity.x += ACCELERATION * delta * movementDirection.x;
			velocity.y += ACCELERATION * delta * movementDirection.y;
		}
		
		
		if(velocity.length() != 0) {
			Vector2f normal = (Vector2f)(new Vector2f(velocity)).normalise();
			velocity = new Vector2f(velocity.x - (FRICTION * normal.x * delta), velocity.y - (FRICTION * normal.y * delta));
			
			if(Math.signum(velocity.x / normal.x) == -1 || Math.signum(velocity.y / normal.y) == -1) {
				velocity = new Vector2f();
			}
			if(velocity.length() > MAX_VELOCITY) {
				velocity = (Vector2f)velocity.normalise(null).scale(MAX_VELOCITY);
			}
		}
		
		weapon.update();
	}
	
	@Override
	public void move() {
		float delta = DisplayManager.getDelta();
		MapHitboxes hitboxes = EntityManager.getStaticHitboxes();
		
		Vector2f nextPosition = new Vector2f(position.x + (velocity.x * delta), position.y + (velocity.y * delta));

		if(velocity.x > 0) {
			for(LineHitbox hitbox: hitboxes.getHitboxesL()) {
				if(nextPosition.y + HITBOX_RADIUS > hitbox.getEndPoint1().y && nextPosition.y - HITBOX_RADIUS < hitbox.getEndPoint2().y &&
						position.x + HITBOX_RADIUS <= hitbox.getCenterPosition().x && nextPosition.x + HITBOX_RADIUS > hitbox.getCenterPosition().x) {
					nextPosition.setX(hitbox.getCenterPosition().x - HITBOX_RADIUS);
					velocity.setX(0);
				}
			}
		}
		if(velocity.x < 0) {
			for(LineHitbox hitbox: hitboxes.getHitboxesR()) {
				if(nextPosition.y + HITBOX_RADIUS > hitbox.getEndPoint1().y && nextPosition.y - HITBOX_RADIUS < hitbox.getEndPoint2().y &&
						position.x - HITBOX_RADIUS >= hitbox.getCenterPosition().x && nextPosition.x - HITBOX_RADIUS < hitbox.getCenterPosition().x) {
					nextPosition.setX(hitbox.getCenterPosition().x + HITBOX_RADIUS);
					velocity.setX(0);
				}
			}
		}
		if(velocity.y > 0) {
			for(LineHitbox hitbox: hitboxes.getHitboxesU()) {
				if(nextPosition.x + HITBOX_RADIUS > hitbox.getEndPoint1().x && nextPosition.x - HITBOX_RADIUS < hitbox.getEndPoint2().x &&
						position.y + HITBOX_RADIUS <= hitbox.getCenterPosition().y && nextPosition.y + HITBOX_RADIUS > hitbox.getCenterPosition().y) {
					nextPosition.setY(hitbox.getCenterPosition().y - HITBOX_RADIUS);
					velocity.setY(0);
				}
			}
		}
		if(velocity.y < 0) {
			for(LineHitbox hitbox: hitboxes.getHitboxesD()) {
				if(nextPosition.x + HITBOX_RADIUS > hitbox.getEndPoint1().x && nextPosition.x - HITBOX_RADIUS < hitbox.getEndPoint2().x &&
						position.y - HITBOX_RADIUS >= hitbox.getCenterPosition().y && nextPosition.y - HITBOX_RADIUS < hitbox.getCenterPosition().y) {
					nextPosition.setY(hitbox.getCenterPosition().y + HITBOX_RADIUS);
					velocity.setY(0);
				}
			}
		}
		
		position = nextPosition;
		calculatePositionMatrix();
	}
	
	public boolean isAlive() {
		return health > 0;
	}
}
