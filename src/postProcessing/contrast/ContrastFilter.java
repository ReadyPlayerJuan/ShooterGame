package postProcessing.contrast;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

import postProcessing.ImageRenderer;

public class ContrastFilter {
	private ImageRenderer renderer;
	private ContrastShader shader;
	
	public ContrastFilter(int fboWidth, int fboHeight) {
		shader = new ContrastShader();
		renderer = new ImageRenderer(fboWidth, fboHeight);
	}
	
	public ContrastFilter() {
		shader = new ContrastShader();
		renderer = new ImageRenderer();
	}
	
	public int getOutputTexture() {
		return renderer.getOutputTexture();
	}
	
	public void render(int texture) {
		shader.start();
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture);
		renderer.renderQuad();
		shader.stop();
	}
	
	public void cleanUp() {
		renderer.cleanUp();
		shader.cleanUp();
	}
}
