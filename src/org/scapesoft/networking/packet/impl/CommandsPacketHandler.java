package org.scapesoft.networking.packet.impl;

import org.scapesoft.api.event.command.CommandHandler;
import org.scapesoft.game.player.Player;
import org.scapesoft.networking.codec.stream.InputStream;
import org.scapesoft.networking.packet.PacketHandler;
import org.scapesoft.networking.packet.PacketInformation;

/**
 * @author Lazarus <lazarus.rs.king@gmail.com>
 * @since Jun 19, 2014
 */
@PacketInformation(listeners = "70")
public class CommandsPacketHandler extends PacketHandler {

	@Override
	public void handle(Player player, Integer packetId, Integer length, InputStream stream) {
		if (!player.isRunning()) {
			return;
		}
		stream.readUnsignedByte();
		stream.readUnsignedByte();
		String command = stream.readString();
		CommandHandler.get().handleCommand(player, command);
	}

}
