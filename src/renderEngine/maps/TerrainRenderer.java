package renderEngine.maps;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import loader.Loader;
import maps.GameMap;
import postProcessing.Fbo;
import renderEngine.Camera;
import renderEngine.DisplayManager;
import textures.RawModel;

public class TerrainRenderer {
	private final RawModel quad;
	private TerrainShader shader;
	
	public TerrainRenderer(Loader loader) {
		float[] positions = {-1, 1, -1, -1, 1, 1, 1, -1};
		quad = loader.loadToVAO(positions, 2);
		shader = new TerrainShader();
		//Matrix4f.scale(new Vector3f(0.5f, 0.5f, 1f), coordinateMatrix, coordinateMatrix);
		//Matrix4f.scale(new Vector3f(1f / DisplayManager.WIDTH, 1f / DisplayManager.HEIGHT, 1f), coordinateMatrix, coordinateMatrix);
	}
	
	public void render(GameMap map, Camera camera) {
		shader.start();
		GL30.glBindVertexArray(quad.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		
		for(Fbo fbo: map.getAllFbos()) {
			GL13.glActiveTexture(GL13.GL_TEXTURE0);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, fbo.getColourTexture());
			
			Vector2f position = camera.getPosition();
			Vector2f scale = camera.getScale();
			
			Matrix4f matrix = new Matrix4f();
			Matrix4f.setIdentity(matrix);
			Matrix4f.scale(new Vector3f(scale.x * (fbo.getWidth() / (float)DisplayManager.WIDTH), scale.y * 2 * (fbo.getHeight() / DisplayManager.WIDTH), 1), matrix, matrix);
			Matrix4f.translate(new Vector2f((position.x + map.getWidth() / 2) * (2.0f / fbo.getWidth()), (position.y + map.getHeight() / 2) * (-2.0f / fbo.getHeight())), matrix, matrix);
			//Matrix4f.scale(new Vector3f(scale.x * 2 / fbo.getWidth(), scale.y * 2 / fbo.getHeight(), 1.0f), matrix, matrix);
			
			//Matrix4f.setIdentity(matrix);
			shader.loadTransformation(matrix);
			
			GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, quad.getVertexCount());
		}
		
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
		shader.stop();
	}
	
	public void cleanUp() {
		shader.cleanUp();
	}
}
