package loader;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.EXTTextureFilterAnisotropic;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GLContext;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

import de.matthiasmann.twl.utils.PNGDecoder;
import maps.TileHitbox;
import textures.RawModel;

public class Loader {
	private List<Integer> vaos = new ArrayList<Integer>();
	private List<Integer> vbos = new ArrayList<Integer>();
	private List<Integer> textures = new ArrayList<Integer>();
	
	private int filterMode = GL11.GL_LINEAR;
	
	public ArrayList<ArrayList<TileHitbox>> loadHitboxes(String fileName) {
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
	
	public ArrayList<ArrayList<Integer>> loadMap(String fileName) {
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
	
	public RawModel loadToVAO(float[] positions, float[] textureCoords, float[] normals, int[] indices) {
		int vaoID = createVAO();
		bindIndicesBuffer(indices);
		storeDataInAttributeList(0, 3, positions);
		storeDataInAttributeList(1, 2, textureCoords);
		storeDataInAttributeList(2, 3, normals);
		unbindVAO();
		return new RawModel(vaoID, indices.length);
	}
	
	public void setFilterMode(int mode) {
		filterMode = mode;
	}
	
	public RawModel loadToVAO(float[] positions, int dimensions) {
		int vaoID = createVAO();
		this.storeDataInAttributeList(0, dimensions, positions);
		unbindVAO();
		return new RawModel(vaoID, positions.length / dimensions);
	}
	
	public RawModel loadToVAO(float[] positions, float[] textureCoords, int dimensions) {
		int vaoID = createVAO();
		storeDataInAttributeList(0, dimensions, positions);
		storeDataInAttributeList(1, 2, textureCoords);
		unbindVAO();
		return new RawModel(vaoID, positions.length / dimensions);
	}
	
	public int loadCubeMap(String[] textureFiles) {
		int texID = GL11.glGenTextures();
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, texID);
		
		for(int i = 0; i < textureFiles.length; i++) {
			TextureData data = decodeTextureFile("res/" + textureFiles[i] + ".png");
			GL11.glTexImage2D(GL13.GL_TEXTURE_CUBE_MAP_POSITIVE_X + i, 0, GL11.GL_RGBA, data.getWidth(), data.getHeight(), 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, data.getBuffer());
		}
		GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
		GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
		GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
		GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);
		textures.add(texID);
		return texID;
	}
	
	private TextureData decodeTextureFile(String fileName) {
		int width = 0;
		int height = 0;
		ByteBuffer buffer = null;
		try {
			FileInputStream in = new FileInputStream(fileName);
			PNGDecoder decoder = new PNGDecoder(in);
			width = decoder.getWidth();
			height = decoder.getHeight();
			buffer = ByteBuffer.allocateDirect(4 * width * height);
			decoder.decode(buffer, width * 4, PNGDecoder.Format.RGBA);
			buffer.flip();
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Tried to load texture " + fileName + ", didn't work");
			System.exit(-1);
		}
		return new TextureData(height, width, buffer);
	}
	
	public int loadTexture(String fileName) {
		Texture texture = null;
		try {
			texture = TextureLoader.getTexture("PNG", new FileInputStream("res/"+fileName+".png"));
			
			GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR_MIPMAP_LINEAR);
			GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL14.GL_TEXTURE_LOD_BIAS, 0);
			
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, filterMode);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, filterMode);
			
			if(GLContext.getCapabilities().GL_EXT_texture_filter_anisotropic) {
				float amount = Math.min(4f, GL11.glGetFloat(EXTTextureFilterAnisotropic.GL_MAX_TEXTURE_MAX_ANISOTROPY_EXT));
				GL11.glTexParameterf(GL11.GL_TEXTURE_2D, EXTTextureFilterAnisotropic.GL_TEXTURE_MAX_ANISOTROPY_EXT, amount);
			} else {
				System.out.println("Anisotropic filtering not supported");
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		int textureID = texture.getTextureID();
		textures.add(textureID);
		
		
		return textureID;
	}
	
	public void cleanUp() {
		for(int vao: vaos) {
			GL30.glDeleteVertexArrays(vao);
		}
		
		for(int vbo: vbos) {
			GL15.glDeleteBuffers(vbo);
		}
		
		for(int texture: textures) {
			GL11.glDeleteTextures(texture);
		}
	}
	
	private int createVAO() {
		int vaoID = GL30.glGenVertexArrays();
		vaos.add(vaoID);
		GL30.glBindVertexArray(vaoID);
		return vaoID;
	}
	
	private void storeDataInAttributeList(int attributeNumber, int coordinateSize, float[] data) {
		int vboID = GL15.glGenBuffers();
		vbos.add(vboID);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
		
		FloatBuffer buffer = storeDataInFloatBuffer(data);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
		GL20.glVertexAttribPointer(attributeNumber, coordinateSize, GL11.GL_FLOAT, false, 0, 0);
		
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
	}
	
	private void unbindVAO() {
		GL30.glBindVertexArray(0);
	}
	
	private void bindIndicesBuffer(int[] indices) {
		int vboID = GL15.glGenBuffers();
		vbos.add(vboID);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboID);
		IntBuffer buffer = storeDataInIntBuffer(indices);
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
	}
	
	private IntBuffer storeDataInIntBuffer(int[] data) {
		IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		return buffer;
	}
	
	private FloatBuffer storeDataInFloatBuffer(float[] data) {
		FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		return buffer;
	}
}
