package weapons;

import java.util.ArrayList;

import org.lwjgl.util.vector.Vector2f;

import entities.LivingEntity;
import loader.Loader;

public abstract class Weapon {
	private static GunStats gunStats;
	
	public static void loadGunStats(Loader loader, String fileName) {
		gunStats = loader.loadGunStats(fileName);
	}
	
	public static ArrayList<Float> getStats(String gunName) {
		return gunStats.getMinStats(gunName);
	}
	
	
	protected LivingEntity owner;
	protected Vector2f positionOffset;

	protected boolean damagesPlayers, damagesEnemies;
	
	public Weapon(LivingEntity owner, boolean damagesPlayers, boolean damagesEnemies) {
		this.owner = owner;
		this.positionOffset = new Vector2f();
		this.damagesPlayers = damagesPlayers;
		this.damagesEnemies = damagesEnemies;
	}
	
	public Weapon(LivingEntity owner, Vector2f positionOffset) {
		this.owner = owner;
		this.positionOffset = positionOffset;
	}
	
	public void setOffset(Vector2f offset) {
		positionOffset = offset;
	}
	
	public Vector2f getOffset() {
		return positionOffset;
	}
	
	public abstract void update(boolean attacking, float direction);
}