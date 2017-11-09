package entities.player;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector2f;

import entities.AnimatedEntity;
import entities.EntityAnimation;
import entities.EntityManager;
import maps.LineHitbox;
import maps.MapHitboxes;
import renderEngine.Camera;
import renderEngine.DisplayManager;
import textures.TextureManager;
import weapons.Gun;
import weapons.Shotgun;
import weapons.Weapon;

public class Player extends AnimatedEntity {
	private Weapon weapon;
	private Camera camera;
	
	private Vector2f velocity;
	
	private final float HITBOX_RADIUS = 8f;
	private final float MAX_VELOCITY = 200f;
	private final float ACCELERATION = 1500f;
	private final float FRICTION = 750f;
	private final boolean ROUND_CAMERA_COORDS = true;
	
	public Player() {
		super(new Vector2f(), new Vector2f(16, 16), 0);
		addAnimation(new EntityAnimation(TextureManager.TERRAIN_TEXTURE, new int[] {90}));
		
		weapon = new Shotgun(this);
		weapon.setOffset(new Vector2f());
		
		//spritePosition = new Vector2f();
		spriteOffset = new Vector2f(-8, 8);
		position = new Vector2f();
		velocity = new Vector2f();
		camera = new Camera();
		camera.setScale(3f);
	}
	
	public void update() {
		float delta = DisplayManager.getDelta();
		
		Vector2f movementDirection = new Vector2f();
		if(Keyboard.isKeyDown(Keyboard.KEY_W) || Keyboard.isKeyDown(Keyboard.KEY_UP)) {
			Vector2f.add(new Vector2f(0, -1), movementDirection, movementDirection);
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_A) || Keyboard.isKeyDown(Keyboard.KEY_LEFT)) {
			Vector2f.add(new Vector2f(-1, 0), movementDirection, movementDirection);
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_S) || Keyboard.isKeyDown(Keyboard.KEY_DOWN)) {
			Vector2f.add(new Vector2f(0, 1), movementDirection, movementDirection);
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_D) || Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) {
			Vector2f.add(new Vector2f(1, 0), movementDirection, movementDirection);
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
			weapon.attack((float)Math.atan2(Mouse.getY() - DisplayManager.HEIGHT/2, Mouse.getX() - DisplayManager.WIDTH/2));
		}
		
		if(movementDirection.length() > 0.1f) {
			movementDirection.normalise();
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
		
		//velocity = new Vector2f(velocity.x - (friction * velocity.x * delta), velocity.y - (friction * velocity.y * delta));
		if(ROUND_CAMERA_COORDS) {
			camera.setPosition(new Vector2f(-(int)position.x, -(int)position.y));
		} else {
			camera.setPosition(new Vector2f(-position.x, -position.y));
		}
		
		//spritePosition = new Vector2f((int)position.x - 8, (int)position.y + 8);
		//System.out.println(velocity.x + " " + velocity.y);
	}
	
	//public Entity getSprite() {
		//return sprite;
	//}
	
	public Camera getCamera() {
		return camera;
	}
	
	@Override
	public Vector2f getPosition() {
		return position;
	}
}
