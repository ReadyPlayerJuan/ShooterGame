package renderEngine.entities;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import shaders.ShaderProgram;

public class EntityShader extends ShaderProgram {
	private static final String VERTEX_FILE = "src/renderEngine/entities/vertexShader.txt";
	private static final String FRAGMENT_FILE = "src/renderEngine/entities/fragmentShader.txt";
	
	private int location_transformationMatrix;
	private int location_texCoords;
	private int location_texSize;
	private int location_tint;

	public EntityShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}
	
	public void loadTransformation(Matrix4f matrix) {
		super.loadMatrix(location_transformationMatrix, matrix);
	}
	
	public void loadTint(Vector3f tint) {
		super.loadVector(location_tint, tint);
	}
	
	public void setTextureSize(float width, float height) {
		super.loadVector(location_texSize, new Vector2f(width, height));
	}
	
	public void setCurrentTextureCoords(float x, float y) {
		super.loadVector(location_texCoords, new Vector2f(x, y));
	}

	@Override
	protected void getAllUniformLocations() {
		location_transformationMatrix = super.getUniformLocation("transformationMatrix");
		location_texCoords = super.getUniformLocation("texCoords");
		location_texSize = super.getUniformLocation("texSize");
		location_tint = super.getUniformLocation("tint");
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
	}
}