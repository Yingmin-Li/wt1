package com.dataiku.wt1;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class Utils {
	public static Map<String, String[]> decodeQueryString(String value) {
		Map<String, String[]> out = new HashMap<String, String[]>();
		if (value == null) return out;
			
		String[] chunks = value.split("&");
		for (String c : chunks) {
			try {
				String[] subChunks = c.split("=");
				if (subChunks.length != 2) continue; // Bad value, ignore it
				String k = URLDecoder.decode(subChunks[0], "utf8");
				String v = URLDecoder.decode(subChunks[1], "utf8");
				String[] arr = out.get(k);
				if (arr == null) {
					arr = new String[]{v};
					out.put(k, arr);
				} else {
					String[] arr2 = new String[arr.length +1];
					System.arraycopy(arr, 0, arr2, 0, arr.length);
					arr2[arr2.length-1] = v;
					out.put(k, arr2);
				}
			} catch (IOException e) {
				// Bad value, ignore it
			}
		}
		return out;
	}

	public static String encodeQueryString(Map<String, String[]> in) {
		StringBuilder sb = new StringBuilder();
		try {
			for (Map.Entry<String, String[]> e : in.entrySet()) {
				for (String v : e.getValue()) {

					if (sb.length() > 0) sb.append('&');
					sb.append(URLEncoder.encode(e.getKey(), "utf8"));
					sb.append('=');
					sb.append(URLEncoder.encode(v, "utf8"));
				}
			}
		} catch (UnsupportedEncodingException e) {
			throw new Error("The impossible happened", e);
		}
		return sb.toString();
	}
}
