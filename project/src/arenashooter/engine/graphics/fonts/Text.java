package arenashooter.engine.graphics.fonts;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsonable;

import arenashooter.engine.graphics.Model;
import arenashooter.engine.graphics.fonts.Font.FontChar;
import arenashooter.game.Main;

public class Text implements Jsonable {
	
	public static enum TextAlignH {	LEFT, RIGHT, CENTER; }
	public static enum TextAlignV {	TOP, BOTTOM, CENTER; }
	
	private TextAlignH alignH;
	private TextAlignV alignV;
	private Font font;
	private String text;
	private float width;
	private Model model;

	public Text(Font font, TextAlignH alignH, TextAlignV alignV, String text) {
		this.font = font;
		this.alignH = alignH;
		this.alignV = alignV;
		this.text = text;
		genModel();
	}
	
	public TextAlignH getAlignH() { return alignH; }
	
	public TextAlignV getAlignV() {
		return alignV;
	}

	public Model getModel() { return model; }
	
	public Font getFont() { return font; }
	
	public String getText() { return text; }
	
	public float getWidth() { return width; }
	
	public float getHeight() { return font.getHeight(); }

	private void genModel() {
		List<FontChar> chars = new ArrayList<>(text.length());
		
		width = 0;
		
		for( int i=0; i<text.length(); i++ ) {
			char c = text.charAt(i);
			FontChar fontChar = font.chars.get(c);
			if(fontChar != null) {
				chars.add(fontChar);
				width += fontChar.xAdvance;
			}
		}
		
		List<Float> data = new ArrayList<>();
		List<Integer> indices = new ArrayList<>();
		
		int current = 0;
		float currentX = 0;
		float currentY;
		switch(alignV) {
		case BOTTOM:
			currentY = -getHeight();
			break;
		case TOP:
			currentY = 0;
			break;
		default: //Center
			currentY = -getHeight()/2;
			break;
		}
		
		if(alignH == TextAlignH.CENTER)
			currentX = -width/2;
		else if(alignH == TextAlignH.RIGHT)
			currentX = -width;
		
		for( FontChar fontChar : chars ) {
			if(fontChar.width != 0 && fontChar.height != 0) { //Don't generate geometry for empty glyphs
				float[] charData = genQuad(fontChar, currentX, currentY);

				int[] charIndices = genIndices(current);

				for(float f : charData)
					data.add(f);
				for(int index : charIndices)
					indices.add(index);

				current++;
			}

			//Advance in text
			currentX+=fontChar.xAdvance;
		}
		
		float[] dataArray = new float[data.size()];
		for(int i=0; i<data.size(); i++)
			dataArray[i] = data.get(i);
		
		int[] indicesArray = new int[indices.size()];
		for(int i=0; i<indices.size(); i++)
			indicesArray[i] = indices.get(i);
		
		model = Main.getRenderer().createModel(dataArray, indicesArray);
	}
	
	/**
	 * 
	 * @param character
	 * @param startX
	 * @param startY
	 * @return
	 */
	private static float[] genQuad(FontChar character, float startX, float startY) {
		float x1 = startX+character.xOffset;
		float y1 = startY+character.yOffset;
		float x2 = x1+character.width;
		float y2 = y1+character.height;
		
		float u1 = character.x;
		float v1 = character.y;
		float u2 = character.x+character.width;
		float v2 = character.y+character.height;
		
		//Vertices positions, texture coordinates and normals
		//		x,   y,  z,    u,  v,   nx, ny, nz
		return new float[] {
				x1, y1, 0f,   u1, v1,   0f, 0f, 1f, //0
				x2, y1, 0f,   u2, v1,   0f, 0f, 1f, //1
				x2, y2, 0f,   u2, v2,   0f, 0f, 1f, //2
				x1, y2, 0f,   u1, v2,   0f, 0f, 1f  //3
		};
	}
	
	/**
	 * 
	 * @param character number of characters before this one
	 * @return vertex indices array
	 */
	private static int[] genIndices(int character) {
		int offset = 4*character;
		return new int[] {
				offset+0, offset+1, offset+2, //Top triangle
				offset+2, offset+3, offset+0  //Bot triangle
		};
	}
	
	/**
	 * Create a copy of this text
	 */
	@Override
	public Text clone() {
		return new Text(font, alignH, alignV, text);
	}
	
	private JsonObject getJsonObject() {
		JsonObject json = new JsonObject();
		json.put("text", text);
		json.put("align H", alignH.name());
		json.put("align V", alignV.name());
		json.put("font path", font.getPath());
		return json;
	}

	@Override
	public String toJson() {
		return getJsonObject().toJson();
	}

	@Override
	public void toJson(Writer writable) throws IOException {
		getJsonObject().toJson(writable);
	}
}
