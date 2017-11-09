package entities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import entities.enemies.Enemy;
import entities.player.Player;
import maps.GameMap;
import maps.MapHitboxes;
import weapons.Bullet;

public class EntityManager {
	private static final int NUM_ENTITY_LAYERS = 3;
	public static final int BACKGROUND = 0;
	public static final int DYNAMIC = 1;
	public static final int FOREGROUND = 2;
	
	private static List<Map<Integer, List<Entity>>> sortedEntities = new ArrayList<Map<Integer, List<Entity>>>();
	
	private static List<Entity> backgroundEntities = new ArrayList<Entity>();
	private static List<Entity> dynamicEntities = new ArrayList<Entity>();
	private static List<Entity> foregroundEntities = new ArrayList<Entity>();
	
	private static List<Bullet> bullets = new ArrayList<Bullet>();
	private static List<Enemy> enemies = new ArrayList<Enemy>();
	private static List<Player> players = new ArrayList<Player>();
	
	private static List<Bullet> newBullets = new ArrayList<Bullet>();
	
	
	private static MapHitboxes staticHitboxes;
	
	public static void initMap(GameMap map) {
		backgroundEntities = new ArrayList<Entity>();
		dynamicEntities = new ArrayList<Entity>();
		foregroundEntities = new ArrayList<Entity>();
		
		backgroundEntities.addAll(map.getBackgroundEntities());
		foregroundEntities.addAll(map.getForegroundEntities());
		dynamicEntities.addAll(map.getDynamicEntities());

		staticHitboxes = map.getHitboxes();
		
		
		for(int i = 0; i < NUM_ENTITY_LAYERS; i++) {
			sortedEntities.add(new HashMap<Integer, List<Entity>>());
		}
		
		processEntities(backgroundEntities, BACKGROUND);
		processEntities(dynamicEntities, DYNAMIC);
		processEntities(foregroundEntities, FOREGROUND);
	}
	
	public static void updateEntities() {
		for(Entity entity: dynamicEntities) {
			entity.update();
		}
		
		for(Bullet entity: newBullets) {
			dynamicEntities.add(entity);
			bullets.add(entity);
			
			ArrayList<Entity> list = new ArrayList<Entity>();
			list.add(entity);
			processEntities(list, DYNAMIC);
		}
		newBullets = new ArrayList<Bullet>();
		
		for(int i = 0; i < dynamicEntities.size(); i++) {
			if(dynamicEntities.get(i).getName().equals("bullet")) {
				if(!((Bullet)dynamicEntities.get(i)).isAlive()) {
					dynamicEntities.remove(i);
					i--;
				}
			}
		}
		//clearLayer(DYNAMIC);
		//processEntities(dynamicEntities, DYNAMIC);
	}
	
	public static void moveEntities() {
		for(Entity entity: dynamicEntities) {
			entity.move();
		}
	}
	
	public static void addBullet(Bullet bullet) {
		newBullets.add(bullet);
	}
	
	public static void addPlayer(Player player) {
		players.add(player);
		dynamicEntities.add(player);

		ArrayList<Entity> list = new ArrayList<Entity>();
		list.add(player);
		processEntities(list, DYNAMIC);
	}
	
	
	public static void processEntities(List<Entity> entities, int currentEntityLayer) {
		for(Entity entity: entities) {
			int textureMapIndex = entity.getSprite().getTextureMapIndex();
			List<Entity> batch = sortedEntities.get(currentEntityLayer).get(textureMapIndex);
			if(batch != null) {
				batch.add(entity);
			} else {
				List<Entity> newBatch = new ArrayList<Entity>();
				newBatch.add(entity);
				sortedEntities.get(currentEntityLayer).put(textureMapIndex, newBatch);
			}
		}
	}
	
	public static void clearLayer(int currentEntityLayer) {
		sortedEntities.get(currentEntityLayer).clear();
	}
	
	public static void clearAllLayers() {
		for(int i = 0; i < NUM_ENTITY_LAYERS; i++) {
			clearLayer(i);
		}
	}
	
	public static List<Bullet> getBullets() {
		return bullets;
	}
	
	public static List<Enemy> getEnemies() {
		return enemies;
	}
	
	public static List<Player> getPlayers() {
		return players;
	}
	
	public static List<Map<Integer, List<Entity>>> getSortedEntities() {
		return sortedEntities;
	}
	
	public static List<Entity> getBackgroundEntities() {
		return backgroundEntities;
	}
	
	public static List<Entity> getDynamicEntities() {
		return dynamicEntities;
	}
	
	public static List<Entity> getForegroundEntities() {
		return foregroundEntities;
	}
	
	public static MapHitboxes getStaticHitboxes() {
		return staticHitboxes;
	}
}