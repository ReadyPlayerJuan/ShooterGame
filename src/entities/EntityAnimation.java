package entities;

import java.util.ArrayList;

import textures.TextureManager;

public class EntityAnimation {
	private ArrayList<EntitySprite> sprites = new ArrayList<EntitySprite>();
	
	public EntityAnimation(int textureMap, int[] textureIndices) {
		for(Integer i: textureIndices) {
			sprites.add(new EntitySprite(textureMap, i));
		}
	}
	
	public int getNumSprites() {
		return sprites.size();
	}
	
	public EntitySprite getSprite(int index) {
		if(index < sprites.size())
			return sprites.get(index);
		return new EntitySprite(TextureManager.MISSING_TEXTURE, 0);
	}
	
	public ArrayList<EntitySprite> getAllSprites() {
		return sprites;
	}
}