package arenashooter.engine.graphics.fonts;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

import arenashooter.engine.ContentManager;
import arenashooter.engine.graphics.Texture;
import arenashooter.game.Main;

/**
 * Represents a distance field font generated using <a href="https://github.com/libgdx/libgdx/wiki/Hiero">Hiero</a><br/>
 * Only one page is supported
 */
public class Font {
	private static HashMap<String, Font> fonts = new HashMap<>();
	
	//Font data
	/** Path to the fnt file */
	final String path;
	int scaleW, scaleH;
	private Texture tex;
	Map<Character, FontChar> chars = new HashMap<>();
	int[] padding = new int[4];

	private Font(String path) {
		this.path = path;
	}
	
	public Texture getTexture() { return tex; }
	
	public static Font loadFont(String path) {
		//Check if the font has already been loaded
		Font font = fonts.get(path);
		if(font != null) return font;
		else font = new Font(path);
		
		//Attempt to load fnt file
		try( InputStream in = ContentManager.getRes(path);
				Reader r = new InputStreamReader(in);
				BufferedReader reader = new BufferedReader(r) ) {
			
			String line = "";
			while( (line = reader.readLine()) != null ) {
				readLine(font, line);
			}
		} catch(Exception e) {
			Main.getRenderer().getLogger().error("Could not load font : "+path, e);
			return null;
		}
		
		return font;
	}
	
	public String getPath() { return path; }
	
	private static void readLine(Font font, String line) {
		String[] parts = line.split(" ");
		
		if(parts.length>0) {
			switch(parts[0]) {
			case "info":
				readInfo(font, parts);
				break;
			case "common":
				readCommon(font, parts);
				break;
			case "page":
				readPageInfo(font, parts);
				break;
			case "chars": //No need to read this because the collection does not have a fixed size
				break;
			case "char":
				readChar(font, parts);
				break;
			case "kernings": //TODO: Implement kernings
				break;
			case "kerning":
				break;
			default:
				break;
			}
		}
	}
	
	private static void readInfo(Font font, String[] line) {
		for( int i=1; i<line.length; i++ ) { //For each key-value pair
			String[] parts = line[i].split("="); //Split key and value
			
			switch(parts[0]) { //Switch on key
			case "padding":
				String[] sides = parts[1].split(","); //Split int array
				for( int side = 0; side<4; side++ )
					font.padding[side] = Integer.parseInt(sides[side]);
				break;
				
			default:
				break;
			}
		}
	}
	
	private static void readCommon(Font font, String[] line) {
		for( int i=1; i<line.length; i++ ) { //For each key-value pair
			String[] parts = line[i].split("="); //Split key and value
			
			switch(parts[0]) { //Switch on key
			case "scaleW":
				font.scaleW = Integer.parseInt(parts[1]);
				break;
				
			case "scaleH":
				font.scaleH = Integer.parseInt(parts[1]);
				break;
			
			default:
				break;
			}
		}
	}
	
	private static void readPageInfo(Font font, String[] line) {
		for( int i=1; i<line.length; i++ ) { //For each key-value pair
			String[] parts = line[i].split("="); //Split key and value
			
			switch(parts[0]) { //Switch on key
			case "file":
				int lastIndex = font.path.lastIndexOf("/");
				String texturePath = parts[1].substring(1, parts[1].length()-1);
				if(lastIndex >= 0)
					texturePath = font.path.substring(0, lastIndex)+'/'+texturePath;
				
				font.tex = Main.getRenderer().loadTexture(texturePath);
				break;
				
			default:
				break;
			}
		}
	}
	
	private static void readChar(Font font, String[] line) {
		int id = 0, x = 0, y = 0, width = 0, height = 0, xOffset = 0, yOffset = 0, xAdvance = 0;
		for( int i=1; i<line.length; i++ ) { //For each key-value pair
			String[] parts = line[i].split("="); //Split key and value
			
			switch(parts[0]) { //Switch on key
			case "id":
				id = Integer.parseInt(parts[1]);
				break;
				
			case "x":
				x = Integer.parseInt(parts[1]);
				break;
				
			case"y":
				y = Integer.parseInt(parts[1]);
				break;
				
			case "width":
				width = Integer.parseInt(parts[1]);
				break;
				
			case "height":
				height = Integer.parseInt(parts[1]);
				break;
				
			case "xoffset":
				xOffset = Integer.parseInt(parts[1]);
				break;
				
			case "yoffset":
				yOffset = Integer.parseInt(parts[1]);
				break;
				
			case "xadvance":
				xAdvance = Integer.parseInt(parts[1]);
				break;
				
			default:
				break;
			}
		}
		
		FontChar newChar = font.new FontChar((float)x/font.scaleW, (float)y/font.scaleH, 
				(float)width/font.scaleW, (float)height/font.scaleH, 
				(float)xOffset/font.scaleW, (float)yOffset/font.scaleH, 
				(float)xAdvance/font.scaleW);
		font.chars.put((char)id, newChar);
	}
	
	public float getHeight() {
		FontChar fontChar = chars.get('.');
		return fontChar.yOffset+fontChar.height;
	}

	protected class FontChar {
		final float x, y;
		final float width, height;
		final float xOffset, yOffset;
		final float xAdvance;
		
		FontChar(float x, float y, float width, float height, float xOffset, float yOffset, float xAdvance) {
			this.x = x;
			this.y = y;
			this.width = width;
			this.height = height;
			this.xOffset = xOffset;
			this.yOffset = yOffset;
			this.xAdvance = xAdvance;
		}
	}
}
