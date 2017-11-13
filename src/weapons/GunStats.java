package weapons;

import java.util.ArrayList;

public class GunStats {
	private ArrayList<ArrayList<Float>> minStats, maxStats;
	private ArrayList<String> names;
	
	public GunStats(ArrayList<ArrayList<Float>> minStats, ArrayList<ArrayList<Float>> maxStats,
			ArrayList<String> names) {
		this.minStats = minStats;
		this.maxStats = maxStats;
		this.names = names;
	}

	public ArrayList<ArrayList<Float>> getMinStats() {
		return minStats;
	}

	public ArrayList<ArrayList<Float>> getMaxStats() {
		return maxStats;
	}

	public ArrayList<String> getNames() {
		return names;
	}
	
	public ArrayList<Float> getMinStats(String name) {
		int index = 0;
		for(int i = 0; i < names.size(); i++) {
			if(names.get(i).equals(name))
				index = i;
		}
		
		return minStats.get(index);
	}
	
	public ArrayList<Float> getMaxStats(String name) {
		int index = 0;
		for(int i = 0; i < names.size(); i++) {
			if(names.get(i).equals(name))
				index = i;
		}
		
		return maxStats.get(index);
	}
}
