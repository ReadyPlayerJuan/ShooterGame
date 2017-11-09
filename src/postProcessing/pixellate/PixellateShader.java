package postProcessing.pixellate;

import shaders.ShaderProgram;

public class PixellateShader extends ShaderProgram {

	private static final String VERTEX_FILE = "src/postProcessing/pixellate/pixellateVertex.txt";
	private static final String FRAGMENT_FILE = "src/postProcessing/pixellate/pixellateFragment.txt";
	
	public PixellateShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	@Override
	protected void getAllUniformLocations() {	
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
	}
}
