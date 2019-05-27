package arenashooter.engine.xmlReaders;

import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import arenashooter.engine.audio.SoundBuffer;

public abstract class XmlReader {
	private final static DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

	// document
	private static DocumentBuilder builder = null;
	protected static Document document;
	protected static Element root;

	static {
		try {
			builder = factory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			System.err.println("Xml builder construction has failed");
			e.printStackTrace();
		}
	}

	protected XmlReader() {
		// Can not be instantiate
	}

	protected static void parse(String path) {
		try {
			document = builder.parse(path);
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		root = document.getDocumentElement();
	}
	
	/**
	 * Preload a sound from a file path
	 * @param path
	 * @return path (unchanged)
	 */
	protected static String preloadSound(String path) {
		SoundBuffer.loadSound(path);
		return path;
	}
}
