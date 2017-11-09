package entities.enemies;

import org.lwjgl.util.vector.Vector2f;

import entities.EntityAnimation;
import entities.AI.TestAI;
import textures.TextureManager;

public class TestEnemy extends Enemy {

	public TestEnemy(Vector2f position) {
		super(position);
		addAnimation(new EntityAnimation(TextureManager.TERRAIN_TEXTURE, new int[] {72}));
		
		MAX_VELOCITY = 100f;
		
		ai = new TestAI(this);
	}
	
	
	@Override
	public void update() {
		super.update();
	}
}
