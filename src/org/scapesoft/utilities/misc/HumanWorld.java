package org.scapesoft.utilities.misc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @author Tyluur<itstyluur@gmail.com
 * @since Oct 26, 2014
 */
public class HumanWorld {

	/**
	 * Opens a connection to the google news page and reads the first line and
	 * returns it.
	 * 
	 * @return
	 */
	private static String getNewsData() {
		try {
			String line = null;
			try {
				URL url = new URL("http://news.google.com/news?pz=1&cf=all&ned=us&hl=en&topic=w&output=rss");
				HttpURLConnection con = (HttpURLConnection) url.openConnection();
				con.addRequestProperty("User-Agent", "Mozilla/4.76");
				BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
				line = in.readLine();
				in.close();
			} catch (final IOException e) {
				e.printStackTrace();
			}
			return line;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Generates an array of the most recent news headlines from google. Uses
	 * the {@link getNewsData()} string for information
	 * 
	 * @return
	 */
	public static String[] getNewsHeadlines() {
		String p = getNewsData();
		String[] data = null;
		if (p != null) {
			data = p.split(">");
			for (int i = 0; i < data.length; i++) {
				data[i] = data[i].replaceAll("<", "");
				data[i] = data[i].replaceAll(">", "");
				data[i] = data[i].replaceAll("/title", "");
				data[i] = data[i].replaceAll("&apos;", "'");
			}
			String[] headlines = { data[28], data[42], data[56], data[70], data[84], data[98], data[112], data[126], data[140], data[154] };
			return headlines;
		}
		return null;
	}

}
