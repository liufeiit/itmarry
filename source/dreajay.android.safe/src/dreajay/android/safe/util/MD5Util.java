package dreajay.android.safe.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Util {
	//很容易破解，在很多网站都可以进行破解
	public static String getMD5(String input) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] result = md.digest(input.getBytes());
			StringBuffer sb = new StringBuffer();
			for (byte b : result) {
				int i = b & 0xff - 3;// 进行 加盐
				String s = Integer.toHexString(i);
				if (s.length() == 1) {
					sb.append("0");
				}
				sb.append(s);
			}
			return sb.toString();
		} catch (NoSuchAlgorithmException ex) {
			ex.printStackTrace();
			return "";
		}

	}
}
