package maps;

import org.lwjgl.util.vector.Vector2f;
import toolbox.Maths.Direction;

public class LineHitbox {
	private Vector2f centerPosition;
	private Vector2f endPoint1, endPoint2;
	private Direction direction;
	private float radius;
	
	public LineHitbox(Vector2f centerPosition, Direction direction, float radius) {
		this.centerPosition = centerPosition;
		this.direction = direction;
		this.radius = radius;
		
		
		switch(direction) {
		case left:
		case right:
			endPoint1 = new Vector2f(centerPosition.x, centerPosition.y - radius);
			endPoint2 = new Vector2f(centerPosition.x, centerPosition.y + radius);
			break;
		case up:
		case down:
			endPoint1 = new Vector2f(centerPosition.x - radius, centerPosition.y);
			endPoint2 = new Vector2f(centerPosition.x + radius, centerPosition.y);
			break;
		}
	}
	
	public LineHitbox(Vector2f endPoint1, Vector2f endPoint2, Direction direction) {
		this.endPoint1 = new Vector2f(Math.min(endPoint1.x, endPoint2.x), Math.min(endPoint1.y, endPoint2.y));
		this.endPoint2 = new Vector2f(Math.max(endPoint1.x, endPoint2.x), Math.max(endPoint1.y, endPoint2.y));
		this.direction = direction;
		
		this.centerPosition = new Vector2f((endPoint1.x + endPoint2.x) / 2, (endPoint1.y + endPoint2.y) / 2);
		radius = Math.abs(((endPoint1.x - endPoint2.x) / 2) + ((endPoint1.y - endPoint2.y) / 2));
	}
	
	public boolean canCombineWith(LineHitbox other) {
		if(direction != other.getDirection())
			return false;
		
		if(endPoint1.equals(other.getEndPoint1()) || endPoint1.equals(other.getEndPoint2()) || endPoint2.equals(other.getEndPoint1()) || endPoint2.equals(other.getEndPoint2()))
			return true;
		
		return false;
	}
	
	public LineHitbox combineWith(LineHitbox other) {
		switch(direction) {
		case left:
		case right:
			if(endPoint1.y < other.getEndPoint1().y) {
				return new LineHitbox(endPoint1, other.getEndPoint2(), direction);
			} else {
				return new LineHitbox(other.getEndPoint1(), endPoint2, direction);
			}
		case up:
		case down:
			if(endPoint1.x < other.getEndPoint1().x) {
				return new LineHitbox(endPoint1, other.getEndPoint2(), direction);
			} else {
				return new LineHitbox(other.getEndPoint1(), endPoint2, direction);
			}
		default:
			return null;
		}
	}
	
	public Vector2f getCenterPosition() {
		return centerPosition;
	}
	
	public Direction getDirection() {
		return direction;
	}
	
	public float getRadius() {
		return radius;
	}
	
	public Vector2f getEndPoint1() {
		return endPoint1;
	}
	
	public Vector2f getEndPoint2() {
		return endPoint2;
	}
}