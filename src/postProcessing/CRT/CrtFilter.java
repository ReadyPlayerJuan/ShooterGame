package postProcessing.CRT;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

import postProcessing.ImageRenderer;

public class CrtFilter {
	private ImageRenderer renderer;
	private CrtShader shader;
	
	public CrtFilter(int fboWidth, int fboHeight) {
		shader = new CrtShader();
		renderer = new ImageRenderer(fboWidth, fboHeight);
	}
	
	public CrtFilter() {
		shader = new CrtShader();
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
