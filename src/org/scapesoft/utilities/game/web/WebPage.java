package org.scapesoft.utilities.game.web;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class WebPage {

	private URL url;
	private ArrayList<String> lines;

	public WebPage(String url) throws MalformedURLException {
		if (!url.startsWith("http://")) {
			url = "http://" + url;
		}
		this.url = new URL(url);
	}

	public void load() throws IOException {
		lines = new ArrayList<String>();
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("GET");
		connection.setRequestProperty("User-Agent", "Mozilla Firefox");
		connection.setDoOutput(true);
		connection.setDoInput(true);
		
		InputStream input;
		if (connection.getResponseCode() >= 400) {
			input = connection.getErrorStream();
		} else {
			input = connection.getInputStream();
		}
		BufferedReader reader = new BufferedReader(new InputStreamReader(input));
		String line;
		while ((line = reader.readLine()) != null) {
			lines.add(line);
		}
		reader.close();
	}

	public void setLines(ArrayList<String> lines) {
		this.lines = lines;
	}

	public ArrayList<String> getLines() {
		return lines;
	}
}
