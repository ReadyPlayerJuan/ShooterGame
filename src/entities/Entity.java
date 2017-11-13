package entities;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import renderEngine.DisplayManager;

public class Entity {
	private int ID;
	protected String entityType = "entity";
	protected String name = "entity";
	
	protected Vector2f position;
	protected Vector2f spriteOffset;
	private Vector2f size;
	protected float rotation;
	private Vector2f rotatePoint;
	
	private EntitySprite sprite;
	
	private Matrix4f positionMatrix;
	
	/*public Entity(TextureMap texture, Vector2f position, Vector2f size, float rotation) {
		super();
		this.textureMap = texture;
		this.position = position;
		this.size = size;
		this.rotation = rotation;
		
		rotatePoint = new Vector2f(0, 0);

		loadSprite(0);
		calculatePositionMatrix();
	}*/
	
	private static int nextID = 0;
	public static int getEntityID() {
		return nextID++;
	}
	
	public Entity(int textureMapIndex, int textureIndex, Vector2f position, Vector2f size, float rotation) {
		this.ID = getEntityID();
		this.position = position;
		this.size = size;
		this.rotation = rotation;
		
		spriteOffset = new Vector2f();
		rotatePoint = new Vector2f();
		
		loadSprite(textureMapIndex, textureIndex);
		calculatePositionMatrix();
	}
	
	public void update() {
	}
	
	public void move() {
	}
	
	private void loadSprite(int textureMapIndex, int textureIndex) {
		sprite = new EntitySprite(textureMapIndex, textureIndex);
	}
	
	protected void calculatePositionMatrix() {
		positionMatrix = new Matrix4f();
		Matrix4f.setIdentity(positionMatrix);
		Matrix4f.scale(new Vector3f(2f * (1 / (float)DisplayManager.WIDTH), 2f * (1 / (float)DisplayManager.HEIGHT), 1.0f), positionMatrix, positionMatrix);
		Matrix4f.translate(position, positionMatrix, positionMatrix);
	}
	
	public void setRotatePoint(float x, float y) {
		rotatePoint = new Vector2f(x, y);
	}
	
	public EntitySprite getSprite() {
		return sprite;
	}
	
	public Matrix4f getPositionMatrix() {
		return new Matrix4f(positionMatrix);
	}

	public Vector2f getPosition() {
		return position;
	}
	
	public void setPosition(float x, float y) {
		setPosition(new Vector2f(x, y));
	}

	public void setPosition(Vector2f position) {
		this.position = position;
		calculatePositionMatrix();
	}
	
	public void changePosition(float dx, float dy) {
		this.position.x += dx;
		this.position.y += dy;
		calculatePositionMatrix();
	}

	public Vector2f getSize() {
		return size;
	}

	public void setSize(Vector2f size) {
		this.size = size;
	}
	
	public void changeSize(float dw, float dh) {
		this.size.x += dw;
		this.size.y += dh;
	}

	public float getRotation() {
		return rotation;
	}

	public void setRotation(float rot) {
		this.rotation = rot;
	}
	
	public void changeRotation(float dr) {
		this.rotation += dr;
	}
	
	public Vector2f getRotatePoint() {
		return rotatePoint;
	}
	
	public Vector2f getSpriteOffset() {
		return spriteOffset;
	}
	
	public void setSpriteOffset(Vector2f offset) {
		spriteOffset = offset;
	}
	
	public void setSpriteOffset(float x, float y) {
		setSpriteOffset(new Vector2f(x, y));
	}
	
	public String getName() {
		return name;
	}
	
	public String getEntityType() {
		return entityType;
	}
	
	public int getID() {
		return ID;
	}
}
