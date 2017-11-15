package particles;

import java.nio.FloatBuffer;
import java.util.List;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL31;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector4f;

import loader.Loader;
import postProcessing.Fbo;

public class ParticleRenderer {
	public static final int MAX_INSTANCES = 100000;
	private static final int INSTANCE_DATA_LENGTH = 6;
	
	private static final FloatBuffer buffer = BufferUtils.createFloatBuffer(MAX_INSTANCES * INSTANCE_DATA_LENGTH);
	
	private Loader loader;
	
	private int pointer = 0;
	private int vao, vbo;
	private ParticleShader shader;
	
	public ParticleRenderer(Loader loader){
		this.loader = loader;
		
		vao = loader.loadVAO();
		vbo = loader.createEmptyVbo(MAX_INSTANCES * INSTANCE_DATA_LENGTH);
		
		loader.addAttribute(vao, vbo, 0, 2, INSTANCE_DATA_LENGTH, 0);
		loader.addAttribute(vao, vbo, 1, 4, INSTANCE_DATA_LENGTH, 8);
		
		shader = new ParticleShader();
	}
	
	public void render(List<Particle> particles, Fbo fbo, Vector2f mapPosition){
		prepare();
		
		pointer = 0;
		float[] vboData = new float[particles.size() * INSTANCE_DATA_LENGTH];
		
		for(Particle particle: particles) {
			Vector2f position = new Vector2f(particle.getPosition().x - mapPosition.x, fbo.getHeight() - particle.getPosition().y - mapPosition.y);
			position = new Vector2f(position.x * 2 / fbo.getWidth(), position.y * 2 / fbo.getHeight());
			
			storeParticleData(position, particle.getColor(), vboData);
		}
		loader.updateVbo(vbo, vboData, buffer);
		GL31.glDrawArraysInstanced(GL11.GL_POINTS, 0, 1, particles.size());
		
		
		finishRendering();
		/*Matrix4f viewMatrix = Maths.createViewMatrix(camera);
		prepare();
		for(Particle particle: particles) {
			updateModelViewMatrix(particle.getPosition(), particle.getRotation(), particle.getScale(), viewMatrix);
			GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, quad.getVertexCount());
		}
		finishRendering();*/
	}
	
	private void storeParticleData(Vector2f position, Vector4f color, float[] data) {
		data[pointer++] = position.x;
		data[pointer++] = position.y;
		data[pointer++] = color.x;
		data[pointer++] = color.y;
		data[pointer++] = color.z;
		data[pointer++] = color.w;
	}
	
	public void cleanUp(){
		shader.cleanUp();
	}
	
	private void prepare(){
		shader.start();
		GL30.glBindVertexArray(vao);
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
	}
	
	private void finishRendering(){
		GL11.glDisable(GL11.GL_BLEND);
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL30.glBindVertexArray(0);
		shader.stop();
	}
}