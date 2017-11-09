package entities.player;

import org.lwjgl.util.vector.Vector2f;

import entities.Entity;
import entities.EntityManager;
import maps.LineHitbox;
import maps.MapHitboxes;
import renderEngine.DisplayManager;
import textures.TextureManager;

public class Bullet extends Entity {
	private boolean alive = true;
	
	private float bulletSpeed, damage;
	private Vector2f velocity;
	
	private final float HITBOX_RADIUS = 2;
	
	public Bullet(int bulletTextureIndex, Vector2f position, float direction, float speed, float damage) {
		super(TextureManager.PROJECTILE_TEXTURE, bulletTextureIndex, position, new Vector2f(8, 8), direction - (3.14159f / 2));
		name = "bullet";
		setRotatePoint(0.5f, 0.5f);
		setSpriteOffset(-4, 4);
		
		this.bulletSpeed = speed;
		this.damage = damage;
		
		velocity = new Vector2f((float)Math.cos(direction) * speed, -(float)Math.sin(direction) * speed);
	}
	
	@Override
	public void update() {
		
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
					velocity = new Vector2f();
					alive = false;
				}
			}
		}
		if(velocity.x < 0) {
			for(LineHitbox hitbox: hitboxes.getHitboxesR()) {
				if(nextPosition.y + HITBOX_RADIUS > hitbox.getEndPoint1().y && nextPosition.y - HITBOX_RADIUS < hitbox.getEndPoint2().y &&
						position.x - HITBOX_RADIUS >= hitbox.getCenterPosition().x && nextPosition.x - HITBOX_RADIUS < hitbox.getCenterPosition().x) {
					nextPosition.setX(hitbox.getCenterPosition().x + HITBOX_RADIUS);
					velocity = new Vector2f();
					alive = false;
				}
			}
		}
		if(velocity.y > 0) {
			for(LineHitbox hitbox: hitboxes.getHitboxesU()) {
				if(nextPosition.x + HITBOX_RADIUS > hitbox.getEndPoint1().x && nextPosition.x - HITBOX_RADIUS < hitbox.getEndPoint2().x &&
						position.y + HITBOX_RADIUS <= hitbox.getCenterPosition().y && nextPosition.y + HITBOX_RADIUS > hitbox.getCenterPosition().y) {
					nextPosition.setY(hitbox.getCenterPosition().y - HITBOX_RADIUS);
					velocity = new Vector2f();
					alive = false;
				}
			}
		}
		if(velocity.y < 0) {
			for(LineHitbox hitbox: hitboxes.getHitboxesD()) {
				if(nextPosition.x + HITBOX_RADIUS > hitbox.getEndPoint1().x && nextPosition.x - HITBOX_RADIUS < hitbox.getEndPoint2().x &&
						position.y - HITBOX_RADIUS >= hitbox.getCenterPosition().y && nextPosition.y - HITBOX_RADIUS < hitbox.getCenterPosition().y) {
					nextPosition.setY(hitbox.getCenterPosition().y + HITBOX_RADIUS);
					velocity = new Vector2f();
					alive = false;
				}
			}
		}
		
		position = nextPosition;
		calculatePositionMatrix();
	}
	
	public boolean isAlive() {
		return alive;
	}
	
	public float getBulletSpeed() {
		return bulletSpeed;
	}
	
	public float getDamage() {
		return damage;
	}
}