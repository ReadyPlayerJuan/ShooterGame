package entities.enemies;

import org.lwjgl.util.vector.Vector2f;

import entities.EntityAnimation;
import entities.AI.*;
import textures.TextureManager;
import weapons.guns.*;

public class TestEnemy extends Enemy {

	public TestEnemy(Vector2f position) {
		super(position);
		addAnimation(new EntityAnimation(TextureManager.TERRAIN_TEXTURE, new int[] {72}));
		
		ai = new ShooterAI(this);
		weapon = new Pistol(this, true, false);
		health = 80;
		
		hitboxRadius = 8f;
		maxVelocity = 100f;
		acceleration = 1000f;
		decceleration = 500f;
		friction = 3.0f;
	}
	
	@Override
	public void update() {
		super.update();
	}
}