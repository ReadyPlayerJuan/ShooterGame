package particles;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.vector.Vector4f;

import entities.Entity;
import entities.numbers.NumberString;

public class ParticleMaster {
	private static List<Particle> particles = new ArrayList<Particle>();
	private static List<NumberString> damageNumbers = new ArrayList<NumberString>();
	//private static ParticleRenderer renderer;
	
	private static int[][][] colorThemes = new int[][][] {
		{ {176, 18, 10}, {221, 25, 29}, {232, 78, 64} }, 								//red colors (blood)
		{ {77, 77, 77}, {58, 58, 58}, {105, 105, 105} },								//gray colors (bullets)
		{ {244, 81, 30}, {245, 124, 0}, {255, 167, 38}, {255, 213, 79}, {230, 81, 0} }, //orange/yellow colors (shooting)
	};
	
	public static void update() {
		int i = 0;
		while(i < particles.size()) {
			particles.get(i).update();
			if(!particles.get(i).isAlive()) {
				particles.remove(i);
				i--;
			}
			i++;
		}

		i = 0;
		while(i < damageNumbers.size()) {
			damageNumbers.get(i).update();
			if(!damageNumbers.get(i).isAlive()) {
				damageNumbers.remove(i);
				i--;
			}
			i++;
		}
	}
	
	public static Vector4f getParticleColor(int colorTheme) {
		if(colorTheme >= colorThemes.length || colorTheme < 0)
			return new Vector4f(0, 0, 0, 1);
		int colorIndex = (int)(Math.random() * colorThemes[colorTheme].length);
		int[] color = colorThemes[colorTheme][colorIndex];
		return new Vector4f(((float)color[0]) / 256.0f, ((float)color[1]) / 256.0f, ((float)color[2]) / 256.0f, 1);
	}
	
	public static List<Particle> getParticles() {
		return particles;
	}
	
	public static List<Entity> getDamageNumbers() {
		List<Entity> entities = new ArrayList<Entity>();
		for(NumberString s: damageNumbers) {
			entities.addAll(s.getEntities());
		}
		return entities;
	}
	
	public static void addParticle(Particle particle) {
		if(particles.size() < ParticleRenderer.MAX_INSTANCES)
			particles.add(particle);
	}
	
	public static void addDamageNumber(NumberString number) {
		damageNumbers.add(number);
	}
}