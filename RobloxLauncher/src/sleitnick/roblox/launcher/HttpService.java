package sleitnick.roblox.launcher;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public final class HttpService {
	
	private static final Map<String, String> RESPONSE_CACHE = new HashMap<String, String>();
	
	/**
	 * Get the HTTP response from the given URL.
	 * @param urlStr {@link String} URL
	 * @param cacheResponse Whether or not to cache the response
	 * @return {@link String} response
	 */
	public static final String get(String urlStr, boolean cacheResponse) {
		if (cacheResponse) {
			String fromCache = RESPONSE_CACHE.get(urlStr);
			if (fromCache != null) {
				return fromCache;
			}
		}
		URL url;
		HttpURLConnection conn;
		BufferedReader rd;
		String line;
		String result = "";
		try {
			url = new URL(urlStr);
			conn = (HttpURLConnection)url.openConnection();
			conn.setRequestMethod("GET");
			rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			while ((line = rd.readLine()) != null) {
				result += line;
			}
			rd.close();
		} catch(IOException e) {
			result = "";
		} catch(Exception e) {
			result = "";
		}
		if (cacheResponse) {
			RESPONSE_CACHE.put(urlStr, result);
		}
		return result;
	}
	

}
