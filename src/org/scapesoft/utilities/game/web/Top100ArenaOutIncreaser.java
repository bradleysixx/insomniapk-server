package org.scapesoft.utilities.game.web;

import java.io.IOException;

import org.scapesoft.utilities.console.TextIO;

/**
 * @author Tyluur<itstyluur@gmail.com>
 * @since Nov 1, 2014
 */
public class Top100ArenaOutIncreaser {

	public static void main(String[] args) {
		System.out.print("Enter Amount: ");
		int amount = TextIO.getlnInt();
		System.out.println();
		for (int i = 0; i < amount; i++) {
			new Thread(new Runnable() {

				@Override
				public void run() {
					final String host = "37.58.52.41";
					final String port = "4140";
					System.setProperty("http.proxyHost", host);
					System.setProperty("http.proxyPort", port);
					System.setProperty("https.proxyHost", host);
					System.setProperty("https.proxyPort", port);
					try {
						WebPage page = new WebPage("http://topofgames.com/tracker.php?do=out&id=77176");
						page.load();
						System.out.println(page.getLines().size());
					} catch (IOException e) {
						e.printStackTrace();
					}
				}

			}).start();
		}
	}

}
