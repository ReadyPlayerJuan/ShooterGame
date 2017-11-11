package entities.enemies;

import org.lwjgl.util.vector.Vector2f;

import entities.EntityAnimation;
import entities.AI.TestAI;
import textures.TextureManager;
import weapons.guns.*;

public class TestEnemy extends Enemy {

	public TestEnemy(Vector2f position) {
		super(position);
		addAnimation(new EntityAnimation(TextureManager.TERRAIN_TEXTURE, new int[] {72}));
		
		ai = new TestAI(this);
		weapon = new Shotgun(this, true, false);

		hitboxRadius = 8f;
		maxVelocity = 100f;
		acceleration = 1000f;
		decceleration = 500f;
		friction = 200f;
	}
	
	
	@Override
	public void update() {
		super.update();
	}
}
