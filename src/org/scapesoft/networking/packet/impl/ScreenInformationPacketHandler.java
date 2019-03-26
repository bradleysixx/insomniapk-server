package org.scapesoft.networking.packet.impl;

import org.scapesoft.game.player.Player;
import org.scapesoft.networking.codec.stream.InputStream;
import org.scapesoft.networking.packet.PacketHandler;
import org.scapesoft.networking.packet.PacketInformation;

/**
 * @author Lazarus <lazarus.rs.king@gmail.com>
 * @since Jun 19, 2014
 */
@PacketInformation(listeners = "87")
public class ScreenInformationPacketHandler extends PacketHandler {

	@Override
	public void handle(Player player, Integer packetId, Integer length, InputStream stream) {
		int displayMode = stream.readUnsignedByte();
		player.setScreenWidth(stream.readUnsignedShort());
		player.setScreenHeight(stream.readUnsignedShort());

		stream.readUnsignedByte();
		if (!player.hasStarted() || player.hasFinished() || displayMode == player.getDisplayMode() || !player.getInterfaceManager().containsInterface(742)) {
			return;
		}
		player.setDisplayMode(displayMode);
		player.getInterfaceManager().removeAll();
		player.getInterfaceManager().sendInterfaces();
		player.getInterfaceManager().sendInterface(742);
	}

}
