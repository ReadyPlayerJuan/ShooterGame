package entities.player;

import org.lwjgl.util.vector.Vector2f;

import entities.EntityAnimation;
import entities.LivingEntity;
import renderEngine.Camera;
import textures.TextureManager;
import weapons.guns.*;

public class Player extends LivingEntity {
	private Camera camera;
	private final boolean ROUND_CAMERA_COORDS = true;
	
	public Player() {
		super(new Vector2f(), new Vector2f(16, 16), 0);
		this.entityType = "player";
		addAnimation(new EntityAnimation(TextureManager.TERRAIN_TEXTURE, new int[] {90}));
		
		ai = new PlayerController(this);
		weapon = new MachineGun(this, false, true);
		weapon.setOffset(new Vector2f());
		
		spriteOffset = new Vector2f(-8, 8);
		camera = new Camera();
		camera.setScale(3f);
		
		hitboxRadius = 8f;
		maxVelocity = 150f;
		acceleration = 1500f;
		decceleration = 500f;
		friction = 200f;
	}
	
	@Override
	public void update() {
		super.update();
	}
	
	@Override
	public void move() {
		super.move();
		
		if(ROUND_CAMERA_COORDS) {
			camera.setPosition(new Vector2f(-(int)position.x, -(int)position.y));
		} else {
			camera.setPosition(new Vector2f(-position.x, -position.y));
		}
	}
	
	@Override
	public void collidedWith(LivingEntity other) {
		if(other != null) {
			if(other.getEntityType() == "bullet") {
				//take damages
			}
		}
	}
	
	public Camera getCamera() {
		return camera;
	}
}
