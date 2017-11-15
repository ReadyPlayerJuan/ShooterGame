package entities.numbers;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.vector.Vector2f;

import entities.Entity;
import particles.ParticleMaster;
import renderEngine.DisplayManager;

public class NumberString {
	private List<Entity> entities;
	private final float lifeTime = 0.4f;
	private float life = lifeTime;
	
	private Vector2f velocity;
	private float friction;
	
	public NumberString(int number, boolean withSign, Vector2f position, Vector2f velocity, float friction) {
		this.velocity = velocity;
		this.friction = friction / lifeTime;
		
		int numNumbers = ("" + Math.abs(number)).length();
		if(withSign)
			numNumbers++;
		entities = new ArrayList<Entity>();
		
		position.x -= (float)numNumbers * (4 / 2.0f) - 1;
		int index = 0;
		if(withSign) {
			if(number >= 0) {
				entities.add(new NumberEntity(10, new Vector2f(position.x + (index * 4), position.y)));
			} else {
				entities.add(new NumberEntity(11, new Vector2f(position.x + (index * 4), position.y)));
			}
			index++;
		}
		for(int i = index; i < numNumbers; i++) {
			entities.add(new NumberEntity(Integer.parseInt(("" + Math.abs(number)).charAt(i - index) + ""), new Vector2f(position.x + (i * 4), position.y)));
		}
		
		ParticleMaster.addDamageNumber(this);
	}
	
	public void update() {
		float delta = DisplayManager.getDelta();
		
		life -= delta;
		
		Vector2f positionChange = (Vector2f)(new Vector2f(velocity)).scale(delta);
		for(Entity e: entities) {
			e.changePosition(positionChange.x, positionChange.y);
		}
		
		if(velocity.length() > 0) {
			Vector2f normal = (Vector2f)(new Vector2f(velocity)).normalise();
			velocity = new Vector2f(velocity.x - (friction * normal.x * delta), velocity.y - (friction * normal.y * delta));
		}
	}
	
	public boolean isAlive() {
		return life > 0;
	}
	
	public List<Entity> getEntities() {
		return entities;
	}
}