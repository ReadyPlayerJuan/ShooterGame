package entities.player;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector2f;

import entities.Entity;
import entities.AI.AI;
import renderEngine.DisplayManager;

public class PlayerController extends AI {
	public PlayerController(Entity owner) {
		super(owner);
	}

	@Override
	public void update() {
		movementDirection = new Vector2f();
		
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
		if(Keyboard.isKeyDown(Keyboard.KEY_SPACE) || Mouse.isButtonDown(0)) {
			attacking = true;
			attackDirection = (float)Math.atan2(Mouse.getY() - DisplayManager.HEIGHT/2, Mouse.getX() - DisplayManager.WIDTH/2);
		} else {
			attacking = false;
		}
		
		if(movementDirection.length() > 0.1f) {
			movementDirection.normalise();
		}
	}
}
