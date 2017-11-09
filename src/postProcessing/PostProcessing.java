package postProcessing;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import loader.Loader;
import postProcessing.CRT.CrtFilter;
import postProcessing.bloom.BrightFilter;
import postProcessing.bloom.CombineFilter;
import postProcessing.contrast.ContrastFilter;
import postProcessing.gaussianBlur.HorizontalBlur;
import postProcessing.gaussianBlur.VerticalBlur;
import postProcessing.pixellate.PixellateFilter;
import renderEngine.DisplayManager;
import textures.RawModel;

public class PostProcessing {
	private static final float[] POSITIONS = {-1, 1, -1, -1, 1, 1, 1, -1};	
	private static RawModel quad;
	
	private static BlankFilter blankFilter;
	private static ContrastFilter contrastFilter;
	private static CrtFilter crtFilter;
	private static HorizontalBlur hBlur;
	private static VerticalBlur vBlur;
	private static HorizontalBlur hBlur2;
	private static VerticalBlur vBlur2;
	private static BrightFilter brightFilter;
	private static CombineFilter combineFilter;
	private static PixellateFilter pixellateFilter;

	private static final int postProcessingMode = 0;
	public static int filterMode = GL11.GL_LINEAR;
	
	public static void init(Loader loader) {
		quad = loader.loadToVAO(POSITIONS, 2);
		
		switch(postProcessingMode) {
		case 0:
			blankFilter = new BlankFilter();
			break;
		case 1:
			hBlur = new HorizontalBlur(Display.getWidth() / 2, Display.getHeight() / 2);
			vBlur = new VerticalBlur(Display.getWidth() / 2, Display.getHeight() / 2);
			hBlur2 = new HorizontalBlur(Display.getWidth() / 8, Display.getHeight() / 8);
			vBlur2 = new VerticalBlur(Display.getWidth() / 8, Display.getHeight() / 8);
			blankFilter = new BlankFilter();
			break;
		case 2:
			contrastFilter = new ContrastFilter();
			break;
		case 3:
			crtFilter = new CrtFilter();
			break;
		case 4:
			brightFilter = new BrightFilter(Display.getWidth() / 2, Display.getHeight() / 2);
			hBlur = new HorizontalBlur(Display.getWidth() / 4, Display.getHeight() / 4);
			vBlur = new VerticalBlur(Display.getWidth() / 4, Display.getHeight() / 4);
			combineFilter = new CombineFilter();
			break;
		case 5:
			brightFilter = new BrightFilter(Display.getWidth() / 2, Display.getHeight() / 2);
			hBlur = new HorizontalBlur(Display.getWidth() / 4, Display.getHeight() / 4);
			vBlur = new VerticalBlur(Display.getWidth() / 4, Display.getHeight() / 4);
			combineFilter = new CombineFilter(Display.getWidth(), Display.getHeight());
			
			contrastFilter = new ContrastFilter((int)((float)Display.getWidth() / DisplayManager.PIXELLATE_AMOUNT), (int)((float)Display.getHeight() / DisplayManager.PIXELLATE_AMOUNT));
			pixellateFilter = new PixellateFilter();
			//blankFilter = new BlankFilter();
			break;
		case 6:
			blankFilter = new BlankFilter((int)((float)Display.getWidth() / DisplayManager.PIXELLATE_AMOUNT), (int)((float)Display.getHeight() / DisplayManager.PIXELLATE_AMOUNT));
			pixellateFilter = new PixellateFilter();
			break;
		}
	}
	
	public static void doPostProcessing(int colorTexture) {
		start();
		filterMode = GL11.GL_LINEAR;
		
		switch(postProcessingMode) {
		case 0:
			blankFilter.render(colorTexture);
			break;
		case 1:
			hBlur.render(colorTexture);
			vBlur.render(hBlur.getOutputTexture());
			hBlur2.render(vBlur.getOutputTexture());
			vBlur2.render(hBlur2.getOutputTexture());
			blankFilter.render(vBlur2.getOutputTexture());
			break;
		case 2:
			contrastFilter.render(colorTexture);
			break;
		case 3:
			crtFilter.render(colorTexture);
			break;
		case 4:
			brightFilter.render(colorTexture);
			hBlur.render(brightFilter.getOutputTexture());
			vBlur.render(hBlur.getOutputTexture());
			combineFilter.render(vBlur.getOutputTexture(), colorTexture);
			break;
		case 5:
			brightFilter.render(colorTexture);
			hBlur.render(brightFilter.getOutputTexture());
			vBlur.render(hBlur.getOutputTexture());
			combineFilter.render(vBlur.getOutputTexture(), colorTexture);
			contrastFilter.render(combineFilter.getOutputTexture());
			
			pixellateFilter.render(contrastFilter.getOutputTexture());
			break;
		case 6:
			filterMode = GL11.GL_NEAREST;
			blankFilter.render(colorTexture);
			pixellateFilter.render(blankFilter.getOutputTexture());
			break;
		}
		end();
	}
	
	public static void cleanUp() {
		if(blankFilter != null)
			blankFilter.cleanUp();
		if(contrastFilter != null)
			contrastFilter.cleanUp();
		if(hBlur != null)
			hBlur.cleanUp();
		if(vBlur != null)
			vBlur.cleanUp();
		if(hBlur2 != null)
			hBlur2.cleanUp();
		if(vBlur2 != null)
			vBlur2.cleanUp();
		if(crtFilter != null)
			crtFilter.cleanUp();
		if(brightFilter != null)
			brightFilter.cleanUp();
		if(combineFilter != null)
			combineFilter.cleanUp();
		if(pixellateFilter != null)
			pixellateFilter.cleanUp();
	}
	
	private static void start() {
		GL30.glBindVertexArray(quad.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
	}
	
	private static void end() {
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
	}
}
