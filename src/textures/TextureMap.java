package textures;

import java.util.ArrayList;

import maps.TileHitbox;

public class TextureMap {
	private int textureID;
	
	private int margin, border;
	private int rows, cols;
	private int textureMapWidth, textureMapHeight, imageWidth, imageHeight;
	private int extraSpace;
	private int textureWidthPx, textureHeightPx;
	private float textureWidth, textureHeight;
	
	private ArrayList<ArrayList<TileHitbox>> hitboxes;
	
	public TextureMap(ArrayList<ArrayList<TileHitbox>> hitboxes, int ID, int width, int height, int margin, int border, int numCols, int numRows) {
		textureID = ID;
		rows = numRows;
		cols = numCols;
		this.textureMapWidth = width;
		this.textureMapHeight = height;
		this.margin = margin;
		this.border = border;
		
		this.hitboxes = hitboxes;
		
		int i = 0;
		while(Math.pow(2, i) < width) {
			i++;
		}
		imageWidth = (int)(Math.pow(2, i));
		imageHeight = imageWidth;
		extraSpace = imageWidth - textureMapWidth;
		
		textureWidthPx = Math.round((((float)textureMapWidth - margin*2 - (border * (cols-1))) / (float)cols));
		textureHeightPx = Math.round((((float)textureMapHeight - margin*2 - (border * (rows-1))) / (float)rows));
		textureWidth = (float)textureWidthPx / ((float)imageWidth);
		textureHeight = (float)textureHeightPx / ((float)imageHeight);
	}

	public TextureMap(int ID, int width, int height, int margin, int border, int numCols, int numRows) {
		textureID = ID;
		rows = numRows;
		cols = numCols;
		this.textureMapWidth = width;
		this.textureMapHeight = height;
		this.margin = margin;
		this.border = border;
		
		this.hitboxes = new ArrayList<ArrayList<TileHitbox>>();
		
		int i = 0;
		while(Math.pow(2, i) < width) {
			i++;
		}
		imageWidth = (int)(Math.pow(2, i));
		imageHeight = imageWidth;
		extraSpace = imageWidth - textureMapWidth;
		
		textureWidthPx = Math.round((((float)textureMapWidth - margin*2 - (border * (cols-1))) / (float)cols));
		textureHeightPx = Math.round((((float)textureMapHeight - margin*2 - (border * (rows-1))) / (float)rows));
		textureWidth = (float)textureWidthPx / ((float)imageWidth);
		textureHeight = (float)textureHeightPx / ((float)imageHeight);
	}
	
	public TextureMap(int ID, int width, int height) {
		textureID = ID;
		rows = 1;
		cols = 1;
		this.textureMapWidth = width;
		this.textureMapHeight = height;
		this.margin = 0;
		this.border = 0;
		
		this.hitboxes = new ArrayList<ArrayList<TileHitbox>>();
		
		imageWidth = width;
		imageHeight = height;
		extraSpace = 0;
		
		textureWidthPx = width;
		textureHeightPx = height;
		textureWidth = 1;
		textureHeight = 1;
	}
	
	public ArrayList<TileHitbox> getHitbox(int tileID) {
		return hitboxes.get(tileID);
	}
	
	public int getTextureID() {
		return textureID;
	}
	
	public int getNumRows() {
		return rows;
	}
	
	public int getNumCols() {
		return cols;
	}
	
	public int getImageWidth() {
		return imageWidth;
	}
	
	public int getImageHeight() {
		return imageHeight;
	}
	
	public int getTextureMapWidth() {
		return textureMapWidth;
	}
	
	public int getTextureMapHeight() {
		return textureMapHeight;
	}
	
	public int getTextureWidthPx() {
		return textureWidthPx;
	}
	
	public int getTextureHeightPx() {
		return textureHeightPx;
	}
	
	public float getTextureWidth() {
		return textureWidth;
	}
	
	public float getTextureHeight() {
		return textureHeight;
	}
	
	public int getBorder() {
		return border;
	}
	
	public int getMargin() {
		return margin;
	}
	
	public int getExtraSpace() {
		return extraSpace;
	}
}
