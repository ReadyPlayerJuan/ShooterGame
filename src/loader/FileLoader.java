package loader;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import maps.TileHitbox;
import weapons.GunStats;

public class FileLoader {
	public static GunStats loadGunStats(String fileName) {
		ArrayList<ArrayList<Float>> minStats = new ArrayList<ArrayList<Float>>();
		ArrayList<ArrayList<Float>> maxStats = new ArrayList<ArrayList<Float>>();
		ArrayList<String> names = new ArrayList<String>();
		
		
		try {
			BufferedReader reader = new BufferedReader(new FileReader("guns/" + fileName + ".txt"));
			
			ArrayList<Float> currentGunStats;
			String currentGunName;
			
			String line = "";
			while((line = reader.readLine()) != null) {
				if(line.length() > 5 && line.substring(0, 5).equals("name:")) {
					currentGunName = line.substring(6);
					currentGunStats = new ArrayList<Float>();
					
					while((line = reader.readLine()) != null) {
						if(line.equals("")) {
							break;
						} else if(line.charAt(0) == '-') {
							
						} else {
							int index = 0;
							while(line.charAt(index) != '=') {
								index++;
							}
							currentGunStats.add(Float.parseFloat(line.substring(index + 2)));
						}
					}
					
					minStats.add(currentGunStats);
					names.add(currentGunName);
				}
			}
			
			
			reader.close();
		} catch(FileNotFoundException ex) {
            System.out.println("Unable to open file '" + fileName + "'");
            ex.printStackTrace();
        } catch(IOException ex) {
            System.out.println("Error reading file '" + fileName + "'");
            ex.printStackTrace();
        }
		
		
		return new GunStats(minStats, maxStats, names);
	}
	
	public static ArrayList<ArrayList<TileHitbox>> loadHitboxes(String fileName) {
		ArrayList<ArrayList<TileHitbox>> hitboxes = new ArrayList<ArrayList<TileHitbox>>();
		
		
		try {
			BufferedReader reader = new BufferedReader(new FileReader("maps/" + fileName + ".tsx"));
			
			
			int[] hitboxData = new int[4];
			String hitboxDirection = "";
			
			
			String line = "";
			int tileIndex = 0;
			while((line = reader.readLine()) != null) {
				if(line.length() > 11 && line.substring(1, 11).equals("<tile id=\"")) {
					String numberS = "";
					int index = 11;
					while(line.charAt(index) != '"') {
						numberS += line.charAt(index);
						index++;
					}
					int number = Integer.parseInt(numberS);
					//System.out.println(numberS + " " + number);
					
					while(tileIndex < number) {
						tileIndex++;
					}
					
					//tileIndex++;
				} else if(line.length() >= 15 && line.substring(0, 15).equals("   <object id=\"")) {
					//String line2 = "";
					
					int currentData = 0;
					for(int i = 18; i < line.length(); i++) {
						String n = "";
						if(line.charAt(i) == '"') {
							i++;
							while(line.charAt(i) != '"') {
								n += line.charAt(i);
								i++;
							}
							
							hitboxData[currentData] = Integer.parseInt(n);
							currentData++;
						}
					}
				} else if(line.length() >= 38 && line.substring(0, 38).equals("     <property name=\"direction\" value=")) {
					String n = "";
					int i = 39;
					while(line.charAt(i) != '"') {
						n += line.charAt(i);
						i++;
					}
					hitboxDirection = n;
					
				} else if(line.length() >= 12 && line.substring(0, 12).equals("   </object>")) {
					//System.out.println(tileIndex + "   " + hitboxData[0] + " " + hitboxData[1] + " " + hitboxData[2] + " " + hitboxData[3] + " " + hitboxDirection);
					
					while(hitboxes.size() <= tileIndex) {
						hitboxes.add(new ArrayList<TileHitbox>());
					}
					
					hitboxes.get(tileIndex).add(new TileHitbox(hitboxData[0], hitboxData[1], hitboxData[2], hitboxData[3], hitboxDirection));
				}
            }
			reader.close();
		} catch(FileNotFoundException ex) {
            System.out.println("Unable to open file '" + fileName + "'");
            ex.printStackTrace();
        } catch(IOException ex) {
            System.out.println("Error reading file '" + fileName + "'");
            ex.printStackTrace();
        }
		
		return hitboxes;
	}
	
	public static ArrayList<ArrayList<Integer>> loadMap(String fileName) {
		ArrayList<ArrayList<Integer>> map = new ArrayList<ArrayList<Integer>>();
		
		try {
			BufferedReader reader = new BufferedReader(new FileReader("maps/" + fileName + ".csv"));
			
			String line = "";
			while((line = reader.readLine()) != null) {
				map.add(new ArrayList<Integer>());
				
				String nextNumber = "";
				int index = 0;
				while(index < line.length()) {
					while(line.charAt(index) != ',') {
						nextNumber += line.charAt(index);
						index++;
						
						if(index == line.length()) {
							break;
						}
					}
					index++;
					map.get(map.size()-1).add(Integer.parseInt(nextNumber));
					nextNumber = "";
				}
            }
			
			reader.close();
		} catch(FileNotFoundException ex) {
            System.out.println("Unable to open file '" + fileName + "'");
            ex.printStackTrace();
        } catch(IOException ex) {
            System.out.println("Error reading file '" + fileName + "'");
            ex.printStackTrace();
        }
		
		/*for(ArrayList<Integer> a: map) {
			for(Integer b: a) {
				System.out.print(b + " , ");
			}
			System.out.println();
		}*/
		
		return map;
	}
}