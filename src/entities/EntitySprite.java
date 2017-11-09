package entities;

import textures.TextureManager;
import textures.TextureMap;

public class EntitySprite {
	private int textureMapIndex;
	private int textureIndex;
	private int textureXOffset, textureYOffset;
	private float textureX, textureY;
	
	public EntitySprite(int textureMapIndex, int textureIndex) {
		this.textureMapIndex = textureMapIndex;
		this.textureIndex = textureMapIndex;
		TextureMap textureMap = TextureManager.getTexture(textureMapIndex);
		
		textureXOffset = textureIndex % textureMap.getNumCols();
		textureYOffset = textureIndex / textureMap.getNumCols();
		
		textureX = ((textureMap.getTextureWidth() + ((float)textureMap.getBorder() / (textureMap.getImageWidth()))) * textureXOffset)
				+ ((float)textureMap.getMargin() / (textureMap.getImageWidth()));
		textureY = ((textureMap.getTextureHeight() + ((float)textureMap.getBorder() / (textureMap.getImageHeight()))) * textureYOffset)
				+ ((float)textureMap.getMargin() / (textureMap.getImageHeight()));
	}
	
	public int getTextureMapIndex() {
		return textureMapIndex;
	}

	public int getTextureIndex() {
		return textureIndex;
	}

	public int getTextureXOffset() {
		return textureXOffset;
	}

	public int getTextureYOffset() {
		return textureYOffset;
	}

	public float getTextureX() {
		return textureX;
	}

	public float getTextureY() {
		return textureY;
	}
}