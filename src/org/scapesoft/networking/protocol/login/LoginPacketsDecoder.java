package org.scapesoft.networking.protocol.login;

import org.Server;
import org.scapesoft.Constants;
import org.scapesoft.cache.Cache;
import org.scapesoft.engine.service.login.LoginService;
import org.scapesoft.engine.service.login.LoginServiceTask;
import org.scapesoft.game.World;
import org.scapesoft.networking.Session;
import org.scapesoft.networking.codec.Decoder;
import org.scapesoft.networking.codec.stream.InputStream;
import org.scapesoft.utilities.game.player.ReturnCode;
import org.scapesoft.utilities.misc.Utils;
import org.scapesoft.utilities.security.IsaacKeyPair;

public final class LoginPacketsDecoder extends Decoder {

	public LoginPacketsDecoder(Session session) {
		super(session);
	}

	@Override
	public void decode(InputStream stream) {
		session.setDecoder(-1);
		if (Server.STARTUP_TIME == -1 && World.updateIn < 60) {
			session.getLoginPackets().sendClientPacket(ReturnCode.SERVER_IS_UPDATING);
			return;
		}
		int packetId = stream.readUnsignedByte();
		if (packetId == 16) {
			decodeWorldLogin(stream);
		} else {
			if (Constants.DEBUG) {
				System.out.println("PacketId " + packetId);
			}
			session.getChannel().close();
			System.out.println("Didnt receive login packet of 16[" + packetId + "] so closed session");
		}
	}

	public void decodeWorldLogin(InputStream stream) {
		if (World.exiting_start != 0 && World.updateIn < 60) {
			session.getLoginPackets().sendClientPacket(ReturnCode.SERVER_IS_UPDATING);
			return;
		}
		int packetSize = stream.readUnsignedShort();
		if (packetSize != stream.getRemaining()) {
			System.err.println("Packet size is whacko! " + packetSize);
			session.getChannel().close();
			return;
		}
		if (stream.readInt() != Constants.REVISION || stream.readInt() != Constants.CUSTOM_CLIENT_BUILD) {
			session.getLoginPackets().sendClientPacket(ReturnCode.NOVITE_HAS_BEEN_UPDATED);
			return;
		}
		stream.readUnsignedByte();
		if (stream.readUnsignedByte() != 10) { // rsa block check
			session.getLoginPackets().sendClientPacket(ReturnCode.INVALID_SESSION_ID);
			return;
		}
		int[] isaacKeys = new int[4];
		for (int i = 0; i < isaacKeys.length; i++) {
			isaacKeys[i] = stream.readInt();
		}
		if (stream.readLong() != 0L) { // rsa block check, pass part
			session.getLoginPackets().sendClientPacket(ReturnCode.INVALID_SESSION_ID);
			return;
		}
		String password = stream.readString();
		Utils.longToString(stream.readLong());
		stream.readLong(); // random value
		stream.decodeXTEA(isaacKeys, stream.getOffset(), stream.getLength());
		String username = Utils.formatPlayerNameForProtocol(stream.readString());
		String macAddress = stream.readString();
		stream.readUnsignedByte(); // unknown
		int displayMode = stream.readUnsignedByte();
		int screenWidth = stream.readUnsignedShort();
		int screenHeight = stream.readUnsignedShort();
		stream.readUnsignedByte();
		stream.skip(24); // 24bytes directly from a file, no idea whats there
		stream.readString();
		stream.readInt();
		stream.skip(stream.readUnsignedByte()); // useless settings
		if (stream.readUnsignedByte() != 5) {
			session.getLoginPackets().sendClientPacket(ReturnCode.INVALID_SESSION_ID);
			return;
		}
		stream.readUnsignedByte();
		stream.readUnsignedByte();
		stream.readUnsignedByte();
		stream.readUnsignedByte();
		stream.readUnsignedByte();
		stream.readUnsignedByte();
		stream.readUnsignedByte();
		stream.readUnsignedByte();
		stream.readUnsignedShort();
		stream.readUnsignedByte();
		stream.read24BitInt();
		stream.readUnsignedShort();
		stream.readUnsignedByte();
		stream.readUnsignedByte();
		stream.readUnsignedByte();
		stream.readJagString();
		stream.readJagString();
		stream.readJagString();
		stream.readJagString();
		stream.readUnsignedByte();
		stream.readUnsignedShort();
		stream.readInt();
		stream.readLong();
		boolean hasAdditionalInformation = stream.readUnsignedByte() == 1;
		if (hasAdditionalInformation) {
			stream.readString(); // aditionalInformation
		}
		stream.readUnsignedByte();
		stream.readUnsignedByte();
		stream.readUnsignedByte();
		for (int index = 0; index < Cache.STORE.getIndexes().length; index++) {
			int crc = Cache.STORE.getIndexes()[index] == null ? 0 : Cache.STORE.getIndexes()[index].getCRC();
			int receivedCRC = stream.readInt();
			if (crc != receivedCRC && index < 32) {
				session.getLoginPackets().sendClientPacket(ReturnCode.NOVITE_HAS_BEEN_UPDATED);
				return;
			}
		}
		LoginService.getSingleton().submit(new LoginServiceTask(username, password, macAddress, session, new IsaacKeyPair(isaacKeys), new int[] { displayMode, screenWidth, screenHeight }));
	}

}
