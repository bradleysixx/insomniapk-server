package org.scapesoft.networking.protocol;

import org.scapesoft.Constants;
import org.scapesoft.game.player.Player;
import org.scapesoft.networking.Session;
import org.scapesoft.networking.codec.Decoder;
import org.scapesoft.networking.codec.stream.InputStream;
import org.scapesoft.utilities.misc.Utils;
import org.scapesoft.utilities.security.IsaacKeyPair;

public final class ClientPacketsDecoder extends Decoder {

	public ClientPacketsDecoder(Session connection) {
		super(connection);
	}

	@Override
	public final void decode(InputStream stream) {
		session.setDecoder(-1);
		int packetId = stream.readUnsignedByte();
		switch (packetId) {
		case 16:
			if (!Constants.isVPS) {
				decodeBotLogin(stream);
			}
			break;
		case 14:
			decodeLogin(stream);
			break;
		case 15:
			decodeGrab(stream);
			break;
		default:
			session.getChannel().close();
			System.out.println("Received packetId " + packetId + " so closed session");
			break;
		}
	}

	public void decodeBotLogin(InputStream stream) {
		session.setEncoder(1);
		int[] isaacKeys = new int[4];
		for (int i = 0; i < isaacKeys.length; i++) {
			isaacKeys[i] = stream.readInt();
		}
		if (stream.readLong() != 0L) { // rsa block check, pass part
			session.getLoginPackets().sendClientPacket(10);
			// return;
		}
		String password = stream.readString();
		String username = Utils.formatPlayerNameForProtocol(stream.readString());
		int displayMode = stream.readUnsignedByte();
		int screenWidth = stream.readUnsignedShort();
		int screenHeight = stream.readUnsignedShort();

		Player player = new Player(password);

		player.init(session, username, displayMode, screenWidth, screenHeight, new IsaacKeyPair(isaacKeys), "mac");
		session.getLoginPackets().sendLoginDetails(player);
		session.setDecoder(3, player);
		session.setEncoder(2, player);
		player.setPassword(password);
		player.start(System.currentTimeMillis());
	}

	private final void decodeLogin(InputStream stream) {
		if (stream.getRemaining() != 0) {
			session.getChannel().close();
			System.err.println("Remaining from decoding login wasnt 0 so closed session");
			return;
		}
		session.setDecoder(2);
		session.setEncoder(1);
		session.getLoginPackets().sendStartUpPacket();
	}

	private final void decodeGrab(InputStream stream) {
		if (stream.getRemaining() != 8) {
			System.err.println("Invalid remaining amount: " + stream.getRemaining());
			session.getChannel().close();
			return;
		}
		session.setEncoder(0);
		int build = stream.readInt();
		boolean readsub = true;
		if (readsub) {
			int sub = stream.readInt();
			if (build != Constants.REVISION || sub != Constants.CUSTOM_CLIENT_BUILD) {
				session.setDecoder(-1);
				session.getGrabPackets().sendOutdatedClientPacket();
				return;
			}
		}
		session.setDecoder(1);
		session.getGrabPackets().sendStartUpPacket();
	}
}
