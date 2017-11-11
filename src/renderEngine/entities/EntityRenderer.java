package renderEngine.entities;

import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import entities.Entity;
import entities.EntityManager;
import entities.EntitySprite;
import loader.Loader;
import postProcessing.Fbo;
import textures.RawModel;
import textures.TextureManager;
import textures.TextureMap;

public class EntityRenderer {
	private final RawModel quad;
	private EntityShader shader;
	
	
	public EntityRenderer(Loader loader) {
		float[] positions = {0, 1, 0, 0, 1, 1, 1, 0};
		quad = loader.loadToVAO(positions, 2);
		
		shader = new EntityShader();
	}
	
	public void renderLayer(Map<Integer, List<Entity>> entities, Fbo fbo, Vector2f mapPosition) {
		shader.start();
		GL30.glBindVertexArray(quad.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		
		
		for(Integer textureMapIndex: entities.keySet()) {
			TextureMap textureMap = TextureManager.getTexture(textureMapIndex);
			
			GL13.glActiveTexture(GL13.GL_TEXTURE0);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureMap.getTextureID());
			
			List<Entity> batch = entities.get(textureMapIndex);
			for(Entity entity: batch) {
				EntitySprite sprite = entity.getSprite();
				
				shader.setTextureSize(textureMap.getTextureWidth(), textureMap.getTextureHeight() - 0.00001f);
				shader.setCurrentTextureCoords(sprite.getTextureX(), sprite.getTextureY());
				
				Vector2f position = new Vector2f((int)entity.getPosition().x + entity.getSpriteOffset().x - mapPosition.x, (int)entity.getPosition().y + entity.getSpriteOffset().y - mapPosition.y);
				Vector2f scale = entity.getSize();
				float rotation = entity.getRotation();
				
				Matrix4f matrix = new Matrix4f();
				Matrix4f.setIdentity(matrix);
				Matrix4f.translate(new Vector2f(position.x * 2 / fbo.getWidth(), -position.y * 2 / fbo.getHeight()), matrix, matrix);
				
				Matrix4f.translate(new Vector2f(textureMap.getTextureWidthPx() * entity.getRotatePoint().x * 2 / fbo.getWidth(), textureMap.getTextureHeightPx() * entity.getRotatePoint().y * 2 / fbo.getHeight()), matrix, matrix);
				Matrix4f.rotate(rotation, new Vector3f(0, 0, 1), matrix, matrix);
				Matrix4f.translate(new Vector2f(-textureMap.getTextureWidthPx() * entity.getRotatePoint().x * 2 / fbo.getWidth(), -textureMap.getTextureHeightPx() * entity.getRotatePoint().y * 2 / fbo.getHeight()), matrix, matrix);

				Matrix4f.scale(new Vector3f(scale.x * 2 / fbo.getWidth(), scale.y * 2 / fbo.getHeight(), 1.0f), matrix, matrix);
				
				shader.loadTransformation(matrix);
				
				GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, quad.getVertexCount());
			}
		}
		
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
		shader.stop();
	}
	
	/*public void renderPlayer(Player player) {
		shader.start();
		GL30.glBindVertexArray(playerQuad.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		
		
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, player.getTextureID());
		
		shader.setTextureSize(player.getTextureWidth(), player.getTextureHeight() - 0.00001f);
		shader.setCurrentTextureCoords(player.getSprite().getTextureX(), player.getSprite().getTextureY());
		
		Vector2f position = new Vector2f(1f, 1f);
		Vector2f scale = new Vector2f(player.getSize().x * player.getCamera().getScale().x, player.getSize().y * player.getCamera().getScale().y);
		
		Matrix4f matrix = new Matrix4f();
		Matrix4f.setIdentity(matrix);
		Matrix4f.translate(new Vector2f(position.x, -position.y), matrix, matrix);
		
		//Matrix4f.translate(new Vector2f(entity.getTexture().getTextureWidthPx() * entity.getRotatePoint().x * 2 / fbo.getWidth(), entity.getTexture().getTextureHeightPx() * entity.getRotatePoint().y * 2 / fbo.getHeight()), matrix, matrix);
		//Matrix4f.rotate(rotation, new Vector3f(0, 0, 1), matrix, matrix);
		//Matrix4f.translate(new Vector2f(-entity.getTexture().getTextureWidthPx() * entity.getRotatePoint().x * 2 / fbo.getWidth(), -entity.getTexture().getTextureHeightPx() * entity.getRotatePoint().y * 2 / fbo.getHeight()), matrix, matrix);
		
		Matrix4f.scale(new Vector3f(scale.x * 2 / DisplayManager.WIDTH, scale.y * 2 / DisplayManager.HEIGHT, 1.0f), matrix, matrix);
		
		shader.loadTransformation(matrix);
		
		GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, playerQuad.getVertexCount());
		
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
		shader.stop();
		
	}*/
	
	public void cleanUp() {
		shader.cleanUp();
	}
}