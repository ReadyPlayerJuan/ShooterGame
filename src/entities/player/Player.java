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
		addAnimation(new EntityAnimation(TextureManager.TERRAIN_TEXTURE, new int[] {90}));
		ai = new PlayerController(this);
		
		weapon = new MachineGun(this);
		weapon.setOffset(new Vector2f());
		
		//spritePosition = new Vector2f();
		spriteOffset = new Vector2f(-8, 8);
		position = new Vector2f();
		velocity = new Vector2f();
		camera = new Camera();
		camera.setScale(3f);

		
		HITBOX_RADIUS = 8f;
		MAX_VELOCITY = 150f;
		ACCELERATION = 1500f;
		FRICTION = 750f;
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
	
	public Camera getCamera() {
		return camera;
	}
}
