package postProcessing.CRT;

import shaders.ShaderProgram;

public class CrtShader extends ShaderProgram {
	private static final String VERTEX_FILE = "src/postProcessing/CRT/crtVertex.txt";
	private static final String FRAGMENT_FILE = "src/postProcessing/CRT/crtFragment.txt";
	
	public CrtShader() {
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
