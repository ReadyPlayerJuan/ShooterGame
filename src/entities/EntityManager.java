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
	public static final int BACKGROUND = 0;
	public static final int DYNAMIC = 1;
	public static final int FOREGROUND = 2;
	
	
	private static Map<Integer, List<Entity>> backgroundEntitiesSorted = new HashMap<Integer, List<Entity>>();
	private static Map<Integer, List<Entity>> foregroundEntitiesSorted = new HashMap<Integer, List<Entity>>();
	
	private static List<Player> players = new ArrayList<Player>();
	private static List<Enemy> enemies = new ArrayList<Enemy>();
	private static List<Bullet> bullets = new ArrayList<Bullet>();
	
	private static List<Map<Integer, List<Entity>>> dynamicEntitiesSorted = new ArrayList<Map<Integer, List<Entity>>>();
	private static Map<Integer, List<Entity>> playersSorted = new HashMap<Integer, List<Entity>>();
	private static Map<Integer, List<Entity>> enemiesSorted = new HashMap<Integer, List<Entity>>();
	private static Map<Integer, List<Entity>> bulletsSorted = new HashMap<Integer, List<Entity>>();
	
	private static List<Bullet> newBullets = new ArrayList<Bullet>();
	
	
	private static MapHitboxes staticHitboxes;
	
	public static void initMap(GameMap map) {
		players = new ArrayList<Player>();
		enemies = new ArrayList<Enemy>();
		bullets = new ArrayList<Bullet>();
		
		
		backgroundEntitiesSorted = new HashMap<Integer, List<Entity>>();
		foregroundEntitiesSorted = new HashMap<Integer, List<Entity>>();
		dynamicEntitiesSorted = new ArrayList<Map<Integer, List<Entity>>>();
		
		
		processEntities(map.getBackgroundEntities(), backgroundEntitiesSorted);
		processEntities(map.getForegroundEntities(), foregroundEntitiesSorted);
		
		for(Enemy enemy: map.getEnemies()) {
			processEntity(enemy, enemiesSorted);
			enemies.add(enemy);
		}
		
		dynamicEntitiesSorted = new ArrayList<Map<Integer, List<Entity>>>();
		dynamicEntitiesSorted.add(playersSorted);
		dynamicEntitiesSorted.add(enemiesSorted);
		dynamicEntitiesSorted.add(bulletsSorted);
		
		
		staticHitboxes = map.getHitboxes();
	}
	
	public static void updateEntities() {
		for(Entity entity: players) {
			entity.update();
		}
		for(Entity entity: enemies) {
			entity.update();
		}
		for(Entity entity: bullets) {
			entity.update();
		}
		
		
		for(Bullet bullet: newBullets) {
			processEntity(bullet, bulletsSorted);
			bullets.add(bullet);
		}
		newBullets = new ArrayList<Bullet>();
		
		
		removeDeadEntities();
	}
	
	private static void removeDeadEntities() {
		for(Integer textureMapIndex: bulletsSorted.keySet()) {
			List<Entity> bulletSet = bulletsSorted.get(textureMapIndex);
			for(int i = 0; i < bulletSet.size(); i++) {
				if(!((Bullet)bulletSet.get(i)).isAlive()) {
					bulletSet.remove(i);
					i--;
				}
			}
		}
		for(int i = 0; i < bullets.size(); i++) {
			if(!((Bullet)bullets.get(i)).isAlive()) {
				bullets.remove(i);
				i--;
			}
		}

		for(Integer textureMapIndex: enemiesSorted.keySet()) {
			List<Entity> enemySet = enemiesSorted.get(textureMapIndex);
			for(int i = 0; i < enemySet.size(); i++) {
				if(!((Enemy)enemySet.get(i)).isAlive()) {
					enemySet.remove(i);
					i--;
				}
			}
		}
		for(int i = 0; i < enemies.size(); i++) {
			if(!((Enemy)enemies.get(i)).isAlive()) {
				enemies.remove(i);
				i--;
			}
		}
	}
	
	public static void moveEntities() {
		for(Map<Integer, List<Entity>> hashmap: dynamicEntitiesSorted) {
			for(Integer textureMapIndex: hashmap.keySet()) {
				for(Entity entity: hashmap.get(textureMapIndex)) {
					entity.move();
				}
			}
		}
	}
	
	public static void addBullet(Bullet bullet) {
		newBullets.add(bullet);
	}
	
	public static void addPlayer(Player player) {
		processEntity(player, playersSorted);
		players.add(player);
	}
	
	
	public static void processEntities(List<Entity> entities, Map<Integer, List<Entity>> hashmap) {
		for(Entity entity: entities) {
			int textureMapIndex = entity.getSprite().getTextureMapIndex();
			List<Entity> batch = hashmap.get(textureMapIndex);
			if(batch != null) {
				batch.add(entity);
			} else {
				List<Entity> newBatch = new ArrayList<Entity>();
				newBatch.add(entity);
				hashmap.put(textureMapIndex, newBatch);
			}
		}
	}
	
	public static void processEntity(Entity entity, Map<Integer, List<Entity>> hashmap) {
		int textureMapIndex = entity.getSprite().getTextureMapIndex();
		List<Entity> batch = hashmap.get(textureMapIndex);
		if(batch != null) {
			batch.add(entity);
		} else {
			List<Entity> newBatch = new ArrayList<Entity>();
			newBatch.add(entity);
			hashmap.put(textureMapIndex, newBatch);
		}
	}
	
	public static Map<Integer, List<Entity>> getBackgroundEntitiesSorted() {
		return backgroundEntitiesSorted;
	}

	public static Map<Integer, List<Entity>> getForegroundEntitiesSorted() {
		return foregroundEntitiesSorted;
	}

	public static List<Map<Integer, List<Entity>>> getDynamicEntitiesSorted() {
		return dynamicEntitiesSorted;
	}

	public static Map<Integer, List<Entity>> getPlayersSorted() {
		return playersSorted;
	}

	public static Map<Integer, List<Entity>> getEnemiesSorted() {
		return enemiesSorted;
	}

	public static Map<Integer, List<Entity>> getBulletsSorted() {
		return bulletsSorted;
	}
	
	/*public static List<Entity> getBackgroundEntities() {
		return backgroundEntities;
	}

	public static List<Entity> getForegroundEntities() {
		return foregroundEntities;
	}*/

	public static List<Player> getPlayers() {
		return players;
	}

	public static List<Enemy> getEnemies() {
		return enemies;
	}

	public static List<Bullet> getBullets() {
		return bullets;
	}

	public static MapHitboxes getStaticHitboxes() {
		return staticHitboxes;
	}
}