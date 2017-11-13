package textures;

import java.util.ArrayList;

import loader.FileLoader;
import loader.Loader;

public class TextureManager {
	private static ArrayList<TextureMap> textures;
	
	public static final int MISSING_TEXTURE = 0;
	public static final int TERRAIN_TEXTURE = 1;
	public static final int PROJECTILE_TEXTURE = 2;
	
	public static void loadTextures(Loader loader) {
		textures = new ArrayList<TextureMap>();

		TextureMap missingTexture = new TextureMap(loader.loadTexture("missingTexture"), 16, 16);
		TextureMap terrainTextureMap = new TextureMap(FileLoader.loadHitboxes("tileset"), loader.loadTexture("tileset"), 205, 188, 1, 1, 12, 11);
		TextureMap projectilesTextureMap = new TextureMap(loader.loadTexture("projectiles"), 37, 37, 1, 1, 4, 4);
		
		textures.add(missingTexture);
		textures.add(terrainTextureMap);
		textures.add(projectilesTextureMap);
	}
	
	public static TextureMap getTexture(int texture) {
		if(texture < textures.size() && texture >= 0)
			return textures.get(texture);
		return textures.get(MISSING_TEXTURE);
	}
}
