package renderEngine;

import org.lwjgl.util.vector.Vector2f;

public class Camera {
	private Vector2f position = new Vector2f(0, 0);
	private Vector2f scale = new Vector2f(1, 1);
	private float rotation = 0;
	
	public Camera() {
		
	}
	
	/*public void move() {
		float delta = DisplayManager.getDelta();
		
		
		if(Keyboard.isKeyDown(Keyboard.KEY_W) || Keyboard.isKeyDown(Keyboard.KEY_UP)) {
			position.x += panSpeed * delta * Math.cos(Math.toRadians(90));
			position.y += panSpeed * delta * Math.sin(Math.toRadians(90));
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_A) || Keyboard.isKeyDown(Keyboard.KEY_LEFT)) {
			position.x += panSpeed * delta * Math.cos(Math.toRadians(180));
			position.y += panSpeed * delta * Math.sin(Math.toRadians(180));
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_S) || Keyboard.isKeyDown(Keyboard.KEY_DOWN)) {
			position.x += panSpeed * delta * Math.cos(Math.toRadians(270));
			position.y += panSpeed * delta * Math.sin(Math.toRadians(270));
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_D) || Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) {
			position.x += panSpeed * delta * Math.cos(Math.toRadians(0));
			position.y += panSpeed * delta * Math.sin(Math.toRadians(0));
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_E)) {
			rotation -= rotateSpeed * delta;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_Q)) {
			rotation += rotateSpeed * delta;
		}
		//System.out.println(getPositionRemainder(8));
	}*/
	
	public void setPosition(Vector2f position) {
		this.position = position;
	}
	
	public void setScale(float scale) {
		setScale(new Vector2f(scale, scale));
	}
	
	public void setScale(Vector2f scale) {
		this.scale = scale;
	}
	
	public void setRotation(float rotation) {
		this.rotation = rotation;
	}

	public Vector2f getPosition() {
		return position;
	}
	
	public Vector2f getPositionRounded(float resolution) {
		return new Vector2f((float)Math.floor((position.x + resolution/2) / resolution) * resolution, (float)Math.floor((position.y + resolution/2) / resolution) * resolution);
	}
	
	public Vector2f getPositionRemainder(float resolution) {
		Vector2f rounded = getPositionRounded(resolution);
		return new Vector2f(position.x - rounded.x, position.y - rounded.y);
	}
	
	public Vector2f getScale() {
		return scale;
	}
	
	public float getRotation() {
		return rotation;
	}
}
