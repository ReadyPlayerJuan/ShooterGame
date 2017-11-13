package engineTester;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;

import entities.EntityManager;
import entities.player.Player;
import loader.Loader;
import maps.GameMap;
import particles.Particle;
import particles.ParticleMaster;
import postProcessing.PostProcessing;
import renderEngine.DisplayManager;
import renderEngine.MasterRenderer;
import textures.TextureManager;
import weapons.Weapon;


public class MainGameLoop {
	public static void main(String[] args) {
		DisplayManager.createDisplay();
		
		/* LOAD LOADER */
		Loader loader = new Loader();
		
		
		/* LOAD TEXTURES */
		loader.setFilterMode(GL11.GL_NEAREST);
		TextureManager.loadTextures(loader);
		Weapon.loadGunStats("baseGuns");
		
		
		/* INITIALIZE PLAYERS */
		Player player = new Player();
		player.setPosition(400, 600);
		
		
		/* LOAD MAPS */
		GameMap testMap = new GameMap(loader, TextureManager.TERRAIN_TEXTURE, "testMap", new Vector2f(0, 0));
		EntityManager.initMap(testMap);
		EntityManager.addPlayer(player);
		
		
		/* LOAD RENDERERS */
		MasterRenderer masterRenderer = new MasterRenderer(loader);
		
		
		/* INITIAL RENDER LAYERS */
		//masterRenderer.addEntities(testMap);
		masterRenderer.renderLayer(testMap, EntityManager.BACKGROUND);
		masterRenderer.renderLayer(testMap, EntityManager.DYNAMIC);
		masterRenderer.renderLayer(testMap, EntityManager.FOREGROUND);
		
		while(!Display.isCloseRequested() && !Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
			EntityManager.updateEntities();
			EntityManager.moveEntities();
			
			float speed = 100f;
			for(float rotation = 0; rotation < 3.14159f * 2; rotation += (3.14159f / 360)) {
				new Particle(new Vector2f(player.getPosition().x, 1600-player.getPosition().y), new Vector2f(speed * (float)Math.cos(rotation), speed * (float)Math.sin(rotation)), speed / 1, 0, 0, 1);
			}

			System.out.println(ParticleMaster.getParticles().size());
			//System.out.println(EntityManager.getBullets().size());
			masterRenderer.renderScene(testMap, player);
			
			DisplayManager.updateDisplay();
		}
		
		PostProcessing.cleanUp();
		masterRenderer.cleanUp();
		loader.cleanUp();
		testMap.cleanUp();
		
		DisplayManager.closeDisplay();
	}
}