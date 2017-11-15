package entities.numbers;

import org.lwjgl.util.vector.Vector2f;

import entities.Entity;
import textures.TextureManager;

public class NumberEntity extends Entity {
	private int number;
	
	public NumberEntity(int number, Vector2f position) {
		super(TextureManager.NUMBERS_TEXTURE, number, position, new Vector2f(3, 5), 0);
		this.number = number;
	}
	
	public int getNumber() {
		return number;
	}
}
