package maps;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.vector.Vector2f;
import toolbox.Maths.Direction;

public class TileHitbox {
	private int x, y, width, height;
	private List<Direction> collisionDirections;
	
	public TileHitbox(int x, int y, int width, int height, String directions) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		
		collisionDirections = new ArrayList<Direction>();
		for(int i = 0; i < directions.length(); i++) {
			switch(directions.charAt(i)) {
			case 'u':
				collisionDirections.add(Direction.up); break;
			case 'd':
				collisionDirections.add(Direction.down); break;
			case 'l':
				collisionDirections.add(Direction.left); break;
			case 'r':
				collisionDirections.add(Direction.right); break;
			}
		}
	}
	
	public ArrayList<LineHitbox> getLineHitboxes(int tileX, int tileY) {
		ArrayList<LineHitbox> hitboxes = new ArrayList<LineHitbox>();
		
		for(Direction direction: collisionDirections) {
			switch(direction) {
			case left:
				hitboxes.add(new LineHitbox(new Vector2f(x + tileX, y + tileY), new Vector2f(x + tileX, y + tileY + height), direction));
				break;
			case right:
				hitboxes.add(new LineHitbox(new Vector2f(x + tileX + width, y + tileY), new Vector2f(x + tileX + width, y + tileY + height), direction));
				break;
			case up:
				hitboxes.add(new LineHitbox(new Vector2f(x + tileX, y + tileY), new Vector2f(x + tileX + width, y + tileY), direction));
				break;
			case down:
				hitboxes.add(new LineHitbox(new Vector2f(x + tileX, y + tileY + height), new Vector2f(x + tileX + width, y + tileY + height), direction));
				break;
			}
		}
		
		return hitboxes;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public List<Direction> getCollisionDirections() {
		return collisionDirections;
	}
}
