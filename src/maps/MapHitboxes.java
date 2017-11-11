package maps;

import java.util.ArrayList;

import hitboxes.LineHitbox;

public class MapHitboxes {
	private ArrayList<LineHitbox> hitboxesU, hitboxesR, hitboxesD, hitboxesL;
	
	public MapHitboxes(ArrayList<LineHitbox> hitboxes) {
		hitboxesU = new ArrayList<LineHitbox>();
		hitboxesR = new ArrayList<LineHitbox>();
		hitboxesD = new ArrayList<LineHitbox>();
		hitboxesL = new ArrayList<LineHitbox>();
		
		for(LineHitbox hitbox: hitboxes) {
			switch(hitbox.getDirection()) {
			case up:
				hitboxesU.add(hitbox); break;
			case right:
				hitboxesR.add(hitbox); break;
			case down:
				hitboxesD.add(hitbox); break;
			case left:
				hitboxesL.add(hitbox); break;
			}
		}
	}
	
	public MapHitboxes(ArrayList<LineHitbox> hitboxesU, ArrayList<LineHitbox> hitboxesR,
			ArrayList<LineHitbox> hitboxesD, ArrayList<LineHitbox> hitboxesL) {
		super();
		this.hitboxesU = hitboxesU;
		this.hitboxesR = hitboxesR;
		this.hitboxesD = hitboxesD;
		this.hitboxesL = hitboxesL;
	}

	public ArrayList<LineHitbox> getAllHitboxes() {
		ArrayList<LineHitbox> allHitboxes = new ArrayList<LineHitbox>();
		allHitboxes.addAll(hitboxesU);
		allHitboxes.addAll(hitboxesR);
		allHitboxes.addAll(hitboxesD);
		allHitboxes.addAll(hitboxesL);
		return allHitboxes;
	}
	
	public ArrayList<LineHitbox> getHitboxesU() {
		return hitboxesU;
	}

	public ArrayList<LineHitbox> getHitboxesR() {
		return hitboxesR;
	}

	public ArrayList<LineHitbox> getHitboxesD() {
		return hitboxesD;
	}

	public ArrayList<LineHitbox> getHitboxesL() {
		return hitboxesL;
	}
}
