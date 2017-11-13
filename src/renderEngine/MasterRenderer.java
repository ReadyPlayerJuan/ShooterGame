package renderEngine;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;

import entities.EntityManager;
import entities.player.Player;
import guis.GuiTexture;
import loader.Loader;
import maps.GameMap;
import particles.ParticleMaster;
import particles.ParticleRenderer;
import postProcessing.Fbo;
import postProcessing.PostProcessing;
import renderEngine.entities.EntityRenderer;
import renderEngine.guis.GuiRenderer;
import renderEngine.maps.TerrainRenderer;

public class MasterRenderer {
	private EntityRenderer entityRenderer;
	private GuiRenderer guiRenderer;
	private TerrainRenderer terrainRenderer;
	private ParticleRenderer particleRenderer;
	
	private List<GuiTexture> guis = new ArrayList<GuiTexture>();
	private Fbo terrainFbo, mainFbo;
	
	
	
	public MasterRenderer(Loader loader) {
		entityRenderer = new EntityRenderer(loader);
		guiRenderer = new GuiRenderer(loader);
		terrainRenderer = new TerrainRenderer(loader);
		particleRenderer = new ParticleRenderer(loader);
		
		terrainFbo = new Fbo((int)((float)Display.getWidth() / DisplayManager.PIXELLATE_AMOUNT), (int)((float)Display.getHeight() / DisplayManager.PIXELLATE_AMOUNT), Fbo.NONE, GL11.GL_NEAREST);
		guis.add(new GuiTexture(terrainFbo.getColourTexture(), new Vector2f(0, 0), new Vector2f(1, 1)));
		
		mainFbo = new Fbo(Display.getWidth(), Display.getHeight(), Fbo.DEPTH_RENDER_BUFFER);
		PostProcessing.init(loader);
	}
	
	public void renderLayer(GameMap map, int layer) {
		switch(layer) {
		case EntityManager.BACKGROUND:
			Fbo backgroundFbo = map.getBackgroundFbo();
			
			backgroundFbo.bindFrameBuffer();
			//clearScreen();
			entityRenderer.renderLayer(EntityManager.getBackgroundEntitiesSorted(), backgroundFbo, map.getPosition());
			backgroundFbo.unbindFrameBuffer();
			break;
			
		case EntityManager.DYNAMIC:
			Fbo entityFbo = map.getEntityFbo();
			
			entityFbo.bindFrameBuffer();
			
			clearScreen();
			entityRenderer.renderLayer(EntityManager.getPlayersSorted(), entityFbo, map.getPosition());
			entityRenderer.renderLayer(EntityManager.getEnemiesSorted(), entityFbo, map.getPosition());
			entityRenderer.renderLayer(EntityManager.getBulletsSorted(), entityFbo, map.getPosition());
			
			GL11.glPointSize(1f);
			ParticleMaster.update();
			particleRenderer.render(ParticleMaster.getParticles(), entityFbo, map.getPosition());
			
			entityFbo.unbindFrameBuffer();
			break;
			
		case EntityManager.FOREGROUND:
			Fbo foregroundFbo = map.getForegroundFbo();
			
			foregroundFbo.bindFrameBuffer();
			//clearScreen();
			entityRenderer.renderLayer(EntityManager.getForegroundEntitiesSorted(), foregroundFbo, map.getPosition());
			foregroundFbo.unbindFrameBuffer();
			break;
		}
	}
	
	public void renderScene(GameMap map, Player player) {
		//EntityManager.clearLayer(EntityManager.DYNAMIC);
		//EntityManager.processEntities(EntityManager.getDynamicEntities(), EntityManager.DYNAMIC);
		//System.out.println(map.getDynamicEntities().size());
		
		renderLayer(map, EntityManager.DYNAMIC);
		
		clearScreen();
		terrainRenderer.render(map, player.getCamera());
		
		ArrayList<GuiTexture> guis = new ArrayList<GuiTexture>();
		//guis.add(map.getBackgroundTexture());
		//guis.add(map.getEntityTexture());
		//guis.add(map.getForegroundTexture());
		guiRenderer.render(guis);
	}
	
	
	public void clearScreen() {
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		GL11.glClearColor(0, 0, 0, 0);
	}
	
	public void cleanUp() {
		entityRenderer.cleanUp();
		guiRenderer.cleanUp();
		particleRenderer.cleanUp();
		terrainRenderer.cleanUp();
		terrainFbo.cleanUp();
		mainFbo.cleanUp();
	}
}
