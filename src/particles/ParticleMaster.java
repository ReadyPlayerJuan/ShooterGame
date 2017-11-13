package particles;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ParticleMaster {
	private static List<Particle> particles = new ArrayList<Particle>();
	//private static ParticleRenderer renderer;
	
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
	}
	
	public static List<Particle> getParticles() {
		return particles;
	}
	
	public static void addParticle(Particle particle) {
		if(particles.size() < ParticleRenderer.MAX_INSTANCES)
			particles.add(particle);
	}
}