package postProcessing.pixellate;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

import postProcessing.ImageRenderer;

public class PixellateFilter {
	private ImageRenderer renderer;
	private PixellateShader shader;
	
	public PixellateFilter(int fboWidth, int fboHeight) {
		shader = new PixellateShader();
		renderer = new ImageRenderer(fboWidth, fboHeight);
	}
	
	public PixellateFilter() {
		shader = new PixellateShader();
		renderer = new ImageRenderer();
	}
	
	public int getOutputTexture() {
		return renderer.getOutputTexture();
	}
	
	public void render(int texture) {
		shader.start();
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture);
		GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
		GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
		renderer.renderQuad();
		shader.stop();
	}
	
	public void cleanUp() {
		renderer.cleanUp();
		shader.cleanUp();
	}
}
