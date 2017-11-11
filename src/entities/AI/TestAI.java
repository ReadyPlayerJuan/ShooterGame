package entities.AI;

import java.util.List;

import org.lwjgl.util.vector.Vector2f;

import entities.Entity;
import entities.EntityManager;
import entities.player.Player;

public class TestAI extends AI {

	public TestAI(Entity owner) {
		super(owner);
	}

	@Override
	public void update() {
		List<Player> players = EntityManager.getPlayers();
		
		int nearestPlayerIndex = -1;
		float nearestPlayerDistance = 999f;
		if(players.size() > 0) {
			for(int i = 0; i < players.size(); i++) {
				float distance = Vector2f.sub(players.get(i).getPosition(), owner.getPosition(), null).length();
				
				if(distance < nearestPlayerDistance) {
					nearestPlayerIndex = i;
					nearestPlayerDistance = distance;
				}
			}
		}
		
		if(nearestPlayerIndex == -1) {
			attacking = false;
			movementDirection = new Vector2f();
		} else {
			Vector2f.sub(players.get(nearestPlayerIndex).getPosition(), owner.getPosition(), movementDirection);
			
			if(movementDirection.length() > 0) {
				movementDirection.normalise();
				
				attacking = true;
				attackDirection = (float)Math.atan2(-movementDirection.y, movementDirection.x);
			}
		}
	}
}
