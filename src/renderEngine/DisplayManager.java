package renderEngine;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.PixelFormat;

public class DisplayManager {
	public static final int WIDTH = 1280;
	public static final int HEIGHT = 720;
	public static final int FPS_CAP = 60;
	
	public static final float PIXELLATE_AMOUNT = 1f;
	
	private static long lastFrameTime;
	private static float delta;
	private static int frameCount;
	private static long lastFpsUpdateTime;
	private static float totalTime = 0;
	
	public static void createDisplay() {
		ContextAttribs attribs = new ContextAttribs(3,3).withForwardCompatible(true).withProfileCore(true);
		
		try {
			Display.setDisplayMode(new DisplayMode(WIDTH, HEIGHT));
			Display.create(new PixelFormat().withDepthBits(24), attribs);
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
		
		lastFrameTime = getCurrentTime();
		updateFps();
		GL11.glViewport(0, 0, WIDTH, HEIGHT);
	}
	
	public static void updateDisplay() {
		Display.sync(FPS_CAP);
		Display.update();
		long currentFrameTime = getCurrentTime();
		delta = (currentFrameTime - lastFrameTime) / 1000f;
		frameCount++;
		if(currentFrameTime - lastFpsUpdateTime > 1000)
			updateFps();
		lastFrameTime = currentFrameTime;
		
		totalTime += delta;
	}
	
	private static void updateFps() {
		Display.setTitle(frameCount + " fps");
		frameCount = 0;
		lastFpsUpdateTime = lastFrameTime;
	}
	
	public static void closeDisplay() {
		Display.destroy();
	}
	
	public static float getDelta() {
		return delta;
	}
	
	public static float getTotalTime() {
		return totalTime;
	}
	
	private static long getCurrentTime() {
		return Sys.getTime() * 1000 / Sys.getTimerResolution();
	}
}
