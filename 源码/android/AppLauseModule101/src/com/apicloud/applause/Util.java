package com.apicloud.applause;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class Util {

	public static byte[] getImage(String path) throws Exception {
		URL url = new URL(path);
		HttpURLConnection httpURLconnection = (HttpURLConnection) url
				.openConnection();
		httpURLconnection.setRequestMethod("GET");
		httpURLconnection.setReadTimeout(6 * 1000);
		InputStream in = null;
		if (httpURLconnection.getResponseCode() == 200) {
			in = httpURLconnection.getInputStream();
			byte[] result = readStream(in);
			in.close();
			return result;

		}
		return null;
	}

	public static byte[] readStream(InputStream in) throws Exception {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = -1;
		while ((len = in.read(buffer)) != -1) {
			outputStream.write(buffer, 0, len);
		}
		outputStream.close();
		in.close();
		return outputStream.toByteArray();
	}
	
}