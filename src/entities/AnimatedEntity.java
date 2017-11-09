package entities;

import java.util.ArrayList;

import org.lwjgl.util.vector.Vector2f;

import renderEngine.DisplayManager;

public class AnimatedEntity extends Entity {
	private ArrayList<EntityAnimation> animations = new ArrayList<EntityAnimation>();
	
	private int currentAnimationIndex, currentFrameIndex;
	private float animationFrameLength;
	private float animationStartTime;
	
	public AnimatedEntity(Vector2f position, Vector2f size, float rotation) {
		super(-1, -1, position, size, rotation);
		
		animationFrameLength = 0.2f;
		setAnimation(0, true);
	}
	
	public void setAnimationFrameLength(float time) {
		animationFrameLength = time;
	}
	
	public void update() {
		super.update();
		
		int numSprites = animations.get(currentAnimationIndex).getNumSprites();
		
		if(numSprites > 1) {
			float time = DisplayManager.getTotalTime() - animationStartTime;
			int numFrames = (int)(time / animationFrameLength);
			currentFrameIndex = numFrames % numSprites;
		}
	}
	
	public void addAnimation(EntityAnimation animation) {
		animations.add(animation);
	}
	
	public void setAnimation(int animationIndex, boolean resetTime) {
		currentAnimationIndex = animationIndex;
		currentFrameIndex = 0;
		
		animationStartTime = DisplayManager.getTotalTime();
	}
	
	@Override
	public EntitySprite getSprite() {
		return animations.get(currentAnimationIndex).getSprite(currentFrameIndex);
	}
}
