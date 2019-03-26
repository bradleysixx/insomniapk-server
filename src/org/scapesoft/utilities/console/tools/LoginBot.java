package org.scapesoft.utilities.console.tools;

import java.net.Socket;

import org.scapesoft.networking.codec.stream.OutputStream;

/**
 * @author Jonathan
 */
public class LoginBot {

	public static void start(final int numClients) {
		new Thread(() -> {
			for (int i = 0; i < numClients; i++) {
				try {
					OutputStream body = new OutputStream();
					body.writeByte(16);
					for (int k = 5; k < 9; k++)
						body.writeInt(k);

					body.writeString("password" + i);
					body.writeString(i + "bot");// username
				body.writeByte(1);
				body.writeShort(1920);
				body.writeShort(1080);
				final Socket sock = new Socket("localhost", 43594);
				sock.getOutputStream().write(body.getBuffer());
				new Thread() {
					@Override
					public void run() {
						while (true) {
							try {
								int avail = sock.getInputStream().available();
								if (avail == -1)
									sock.close();
								Thread.sleep(100L);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}
				}.start();
			} catch (Exception e) {
				continue;
			}
		}

	}	).start();
	}
}