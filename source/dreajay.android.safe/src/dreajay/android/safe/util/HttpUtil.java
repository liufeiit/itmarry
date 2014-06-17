package dreajay.android.safe.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpUtil {

	public static int CONNECTTIMEOUT = 1000;

	public static String executeHttpGet(String url) throws IOException {
		String result = "";
		HttpURLConnection httpURLConnection = null;
		InputStream is = null;
		try {
			URL u = new URL(url);
			httpURLConnection = (HttpURLConnection) u.openConnection();
			httpURLConnection.setRequestMethod("GET");
			httpURLConnection.setConnectTimeout(CONNECTTIMEOUT);
//			httpURLConnection.getResponseCode() == 200
			is = httpURLConnection.getInputStream();
			result = readStream(is);
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (is != null) {
				is.close();
				is = null;
			}
			if (httpURLConnection != null) {
				httpURLConnection.disconnect();
				httpURLConnection = null;
			}
		}
		return result;
	}

	public static String readStream(InputStream is) throws IOException {
		byte[] buffer = new byte[1024];
		ByteArrayOutputStream bs = new ByteArrayOutputStream();
		int len = 0;
		while ((len = is.read(buffer)) != -1) {
			bs.write(buffer, 0, len);
		}
		String ret = bs.toString();
		bs.close();
		bs = null;
		return ret;
	}

}
