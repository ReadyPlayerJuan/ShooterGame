package postProcessing;

import shaders.ShaderProgram;

public class BlankShader extends ShaderProgram {

	private static final String VERTEX_FILE = "src/postProcessing/blankVertex.txt";
	private static final String FRAGMENT_FILE = "src/postProcessing/blankFragment.txt";
	
	public BlankShader() {
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
