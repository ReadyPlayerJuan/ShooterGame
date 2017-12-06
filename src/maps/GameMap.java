package maps;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;

import entities.Entity;
import entities.enemies.Enemy;
import entities.enemies.TestEnemy;
import guis.GuiTexture;
import hitboxes.LineHitbox;
import loader.FileLoader;
import loader.Loader;
import postProcessing.Fbo;
import textures.TextureManager;
import textures.TextureMap;

public class GameMap {
	private ArrayList<ArrayList<Integer>> backgroundMap, foregroundMap;
	private List<Entity> backgroundEntities, foregroundEntities;
	private List<Enemy> enemies;
	
	private int textureMapIndex;
	TextureMap textureMap;
	
	private Fbo backgroundFbo, entityFbo, foregroundFbo;
	private ArrayList<Fbo> allFbos;
	private GuiTexture backgroundTexture, entityTexture, foregroundTexture;
	
	private MapHitboxes hitboxes;
	
	private int numRows, numCols, tileWidth, tileHeight, width, height;
	private Vector2f position;
	
	public GameMap(Loader loader, int textureMapIndex, String fileName, Vector2f position) {
		this.textureMapIndex = textureMapIndex;
		this.position = position;
		this.textureMap = TextureManager.getTexture(textureMapIndex);
		
		backgroundEntities = new ArrayList<Entity>();
		foregroundEntities = new ArrayList<Entity>();
		enemies = new ArrayList<Enemy>();
		
		backgroundMap = FileLoader.loadMap(fileName + "_Background");
		foregroundMap = FileLoader.loadMap(fileName + "_Foreground");
		
		tileWidth = textureMap.getTextureWidthPx();
		tileHeight = textureMap.getTextureHeightPx(); 
		numRows = backgroundMap.size();
		numCols = backgroundMap.get(0).size();
		width = tileWidth * numCols;
		height = tileHeight * numRows;
		
		for(int r = 0; r < numRows; r++) {
			for(int c = 0; c < numCols; c++) {
				
				int textureIndex = backgroundMap.get(r).get(c);
				if(textureIndex != -1) {
					backgroundEntities.add(new Entity(textureMapIndex, textureIndex, new Vector2f(position.x + (c * tileWidth), position.y + ((r+1) * tileHeight)),
							new Vector2f(tileWidth, tileWidth), 0));
					backgroundEntities.get(backgroundEntities.size()-1).setRotatePoint(0.5f, 0.5f);
				}
				
				
				textureIndex = foregroundMap.get(r).get(c);
				if(textureIndex != -1) {
					foregroundEntities.add(new Entity(textureMapIndex, textureIndex, new Vector2f(position.x + (c * tileWidth), position.y + ((r+1) * tileHeight)),
							new Vector2f(tileWidth, tileWidth), 0));
					foregroundEntities.get(foregroundEntities.size()-1).setRotatePoint(0.5f, 0.5f);
				}
			}
		}
		
		enemies.add(new TestEnemy(new Vector2f(600, 600)));
		//enemies.add(new TestEnemy(new Vector2f(500, 600)));
		//enemies.add(new TestEnemy(new Vector2f(450, 600)));
		
		backgroundFbo = new Fbo(width, height, Fbo.NONE, GL11.GL_NEAREST);
		entityFbo = new Fbo(width*1, height*1, Fbo.NONE, GL11.GL_NEAREST);
		foregroundFbo = new Fbo(width, height, Fbo.NONE, GL11.GL_NEAREST);
		
		backgroundTexture = new GuiTexture(backgroundFbo.getColourTexture(), new Vector2f(-0.5f, 0.5f), new Vector2f(0.5f, 0.5f));
		entityTexture = new GuiTexture(entityFbo.getColourTexture(), new Vector2f(0.5f, 0.5f), new Vector2f(0.5f, 0.5f));
		foregroundTexture = new GuiTexture(foregroundFbo.getColourTexture(), new Vector2f(-0.5f, -0.5f), new Vector2f(0.5f, 0.5f));
		
		allFbos = new ArrayList<Fbo>();
		allFbos.add(backgroundFbo);
		allFbos.add(entityFbo);
		allFbos.add(foregroundFbo);
		
		loadHitboxes();
	}
	
	public void loadHitboxes() {
		/*LineHitbox h1 = new LineHitbox(new Vector2f(16, 8), Direction.left, 8);
		LineHitbox h2 = new LineHitbox(new Vector2f(16, 24), Direction.left, 8);
		
		System.out.println(h1.getEndPoint1().x + ", " + h1.getEndPoint1().y + "     " + h1.getEndPoint2().x + ", " + h1.getEndPoint2().y);
		System.out.println(h2.getEndPoint1().x + ", " + h2.getEndPoint1().y + "     " + h2.getEndPoint2().x + ", " + h2.getEndPoint2().y);
		System.out.println(h1.canCombineWith(h2));
		
		LineHitbox h3 = h1.combineWith(h2);
		System.out.println(h3.getEndPoint1().x + ", " + h3.getEndPoint1().y + "     " + h3.getEndPoint2().x + ", " + h3.getEndPoint2().y);*/
		
		
		
		ArrayList<LineHitbox> tempHitboxes = new ArrayList<LineHitbox>();
		ArrayList<LineHitbox> tempHitboxes2 = new ArrayList<LineHitbox>();
		
		for(int r = 0; r < numRows; r++) {
			for(int c = 0; c < numCols; c++) {
				int textureIndex = backgroundMap.get(r).get(c);
				if(textureIndex != -1) {
					//System.out.println(textureIndex + "  " + textureMap.getHitbox(textureIndex).size());
					for(TileHitbox tileHitbox: textureMap.getHitbox(textureIndex)) {
						tempHitboxes.addAll(tileHitbox.getLineHitboxes((c * tileWidth) + (int)position.x,
								(r * tileHeight) + (int)position.y));
					}
				}
				
				
				textureIndex = foregroundMap.get(r).get(c);
				if(textureIndex != -1) {
					//System.out.println(textureIndex + "  " + textureMap.getHitbox(textureIndex).size());
					for(TileHitbox tileHitbox: textureMap.getHitbox(textureIndex)) {
						tempHitboxes.addAll(tileHitbox.getLineHitboxes((c * tileWidth) + (int)position.x,
								(r * tileHeight) + (int)position.y));
					}
				}
			}
		}
		
		while(tempHitboxes.size() > 0) {
			LineHitbox main = tempHitboxes.remove(0);
			
			for(int i = 0; i < tempHitboxes.size(); i++) {
				//System.out.println(main.getCenterPosition().x + ", " + main.getCenterPosition().y + "    " + tempHitboxes.get(i).getCenterPosition().x + ", " + tempHitboxes.get(i).getCenterPosition().y);
				
				if(main.canCombineWith(tempHitboxes.get(i))) {
					main = main.combineWith(tempHitboxes.remove(i));
					i--;
				}
			}
			
			tempHitboxes2.add(main);
		}
		
		hitboxes = new MapHitboxes(tempHitboxes2);
	}
	
	public void cleanUp() {
		backgroundFbo.cleanUp();
		entityFbo.cleanUp();
		foregroundFbo.cleanUp();
	}
	
	public MapHitboxes getHitboxes() {
		return hitboxes;
	}
	
	public int getNumRows() {
		return numRows;
	}

	public int getNumCols() {
		return numCols;
	}

	public int getTileWidth() {
		return tileWidth;
	}

	public int getTileHeight() {
		return tileHeight;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}
	
	public Vector2f getPosition() {
		return position;
	}

	public ArrayList<Fbo> getAllFbos() {
		return allFbos;
	}
	
	public GuiTexture getBackgroundTexture() {
		return backgroundTexture;
	}
	
	public GuiTexture getEntityTexture() {
		return entityTexture;
	}
	
	public GuiTexture getForegroundTexture() {
		return foregroundTexture;
	}
	
	public Fbo getBackgroundFbo() {
		return backgroundFbo;
	}
	
	public Fbo getEntityFbo() {
		return entityFbo;
	}
	
	public Fbo getForegroundFbo() {
		return foregroundFbo;
	}
	
	public TextureMap getTextureMap() {
		return textureMap;
	}
	
	public int getTextureMapIndex() {
		return textureMapIndex;
	}
	
	public ArrayList<ArrayList<Integer>> getBackgroundMap() {
		return backgroundMap;
	}
	
	public ArrayList<ArrayList<Integer>> getForegroundMap() {
		return foregroundMap;
	}
	
	public List<Entity> getBackgroundEntities() {
		return backgroundEntities;
	}
	
	public List<Entity> getForegroundEntities() {
		return foregroundEntities;
	}
	
	public List<Enemy> getEnemies() {
		return enemies;
	}
}
