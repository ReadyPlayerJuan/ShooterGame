package entities.AI;

import java.util.List;

import org.lwjgl.util.vector.Vector2f;

import entities.Entity;
import entities.EntityManager;
import entities.LivingEntity;
import entities.player.Player;
import renderEngine.DisplayManager;

public class FighterAI extends AI {
	private final float radius1 = 50f;
	private final float radius2 = 150f;
	private final float radius3 = 250f;
	private final float newPositionTimerMax = 0.3f;
	private float newPositionTimer = 0;
	
	private int strafeDirection = -1;
	
	public FighterAI(Entity owner) {
		super(owner);
	}
	
	@Override
	public void update() {
		float delta = DisplayManager.getDelta();
		newPositionTimer -= delta;
		
		
		List<Player> players = EntityManager.getPlayers();
		
		int nearestPlayerIndex = -1;
		float distance = 999f;
		if(players.size() > 0) {
			for(int i = 0; i < players.size(); i++) {
				float d = Vector2f.sub(players.get(i).getPosition(), owner.getPosition(), null).length();
				
				if(d < distance) {
					nearestPlayerIndex = i;
					distance = d;
				}
			}
		}
		
		if(nearestPlayerIndex == -1) {
			attacking = false;
			movementDirection = new Vector2f();
		} else {
			/*Vector2f attackVector;
			if(predictPlayerPosition) {
				float bulletSpeed = ((Gun)((LivingEntity)owner).getWeapon()).getBulletSpeed();
				float time = distance / bulletSpeed;
				Vector2f newPlayerPosition = Vector2f.add(players.get(nearestPlayerIndex).getPosition(), (Vector2f)(new Vector2f(players.get(nearestPlayerIndex).getVelocity())).scale(time), null);
				attackVector = Vector2f.sub(newPlayerPosition, owner.getPosition(), null);
			} else {
				attackVector = Vector2f.sub(players.get(nearestPlayerIndex).getPosition(), owner.getPosition(), null);
			}
			attackDirection = (float)Math.atan2(-attackVector.y, attackVector.x);*/
		}
		
		
		if(newPositionTimer < 0) {
			newPositionTimer = newPositionTimerMax;
			
			if(nearestPlayerIndex != -1) {
				float speed = ((LivingEntity)owner).getMaxVelocity();
				Player player = players.get(nearestPlayerIndex);
				Vector2f vectorToPlayer = (Vector2f)Vector2f.sub(player.getPosition(), owner.getPosition(), null).normalise();
				float directionToPlayer = (float)Math.atan2(vectorToPlayer.y, vectorToPlayer.x);
				Vector2f targetPosition = new Vector2f(owner.getPosition());
				
				if(distance > radius3) {
					attacking = false;
					//move randomly
					movementDirection = new Vector2f();
				} else if(distance > radius2) {
					float randomness = 8;
					
					
					targetPosition = new Vector2f(player.getPosition().x + ((float)Math.random() * randomness - (randomness / 2)),
							player.getPosition().y + ((float)Math.random() * randomness - (randomness / 2)));
					
					attacking = true;
				} else if(distance > radius1) {
					if(Math.random() < 0.7 * newPositionTimerMax) {
						strafeDirection *= -1;
					}

					float moveDistance = speed * newPositionTimer;
					float angle = directionToPlayer + ((float)Math.random() * 1.3f * strafeDirection);
					targetPosition = Vector2f.add(owner.getPosition(), new Vector2f(moveDistance * (float)Math.cos(angle), moveDistance * (float)Math.sin(angle)), null);
				} else {
					targetPosition = new Vector2f(player.getPosition());
				}
				
				Vector2f.sub(targetPosition, owner.getPosition(), movementDirection);
				
				if(movementDirection.length() > 0) {
					movementDirection.normalise();
				}
			}
		}
	}
}
