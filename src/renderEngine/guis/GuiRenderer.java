package renderEngine.guis;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import guis.GuiTexture;
import loader.Loader;
import textures.RawModel;

public class GuiRenderer {
	private final RawModel quad;
	private GuiShader shader;
	
	public GuiRenderer(Loader loader) {
		float[] positions = {-1, 1, -1, -1, 1, 1, 1, -1};
		quad = loader.loadToVAO(positions, 2);
		shader = new GuiShader();
	}
	
	public void render(GuiTexture gui) {
		ArrayList<GuiTexture> guis = new ArrayList<GuiTexture>();
		guis.add(gui);
		render(guis);
	}
	
	public void render(List<GuiTexture> guis) {
		shader.start();
		GL30.glBindVertexArray(quad.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		
		for(GuiTexture gui: guis) {
			GL13.glActiveTexture(GL13.GL_TEXTURE0);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, gui.getTexture());
			
			Vector2f position = gui.getPosition();
			Vector2f scale = gui.getScale();
			
			Matrix4f matrix = new Matrix4f();
			Matrix4f.setIdentity(matrix);
			Matrix4f.translate(position, matrix, matrix);
			//Matrix4f.translate(new Vector2f(camera.getPositionRemainder(8).x * 2 / (float)Display.getWidth(), 0), matrix, matrix);
			Matrix4f.scale(new Vector3f(scale.x, scale.y, 1.0f), matrix, matrix);
			
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
