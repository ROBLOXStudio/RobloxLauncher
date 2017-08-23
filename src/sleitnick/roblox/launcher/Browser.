package sleitnick.roblox.launcher;

import java.awt.Desktop;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

public final class Browser {
	
	private static final Desktop DESKTOP = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
	
	/**
	 * Check if Browser support is available
	 * @return isSupported
	 */
	public static boolean isSupported() {
		return DESKTOP.isSupported(Desktop.Action.BROWSE);
	}
	
	/**
	 * Browse to the given URI
	 * @param uri {@link URI}
	 */
	public static void browse(URI uri) {
		try {
			DESKTOP.browse(uri);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Browse to the given URL
	 * @param url {@link URL}
	 */
	public static void browse(URL url) {
		try {
			browse(url.toURI());
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Browse to the given URL
	 * @param url {@link String} URL
	 */
	public static void browse(String url) {
		try {
			browse(new URL(url));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}

}
