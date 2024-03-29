package org.scapesoft.networking.packet.impl;

import org.scapesoft.Constants;
import org.scapesoft.api.event.command.CommandHandler;
import org.scapesoft.game.player.Player;
import org.scapesoft.game.player.PublicChatMessage;
import org.scapesoft.game.player.QuickChatMessage;
import org.scapesoft.networking.codec.stream.InputStream;
import org.scapesoft.networking.packet.PacketHandler;
import org.scapesoft.networking.packet.PacketInformation;
import org.scapesoft.utilities.console.gson.extra.Punishment.PunishmentType;
import org.scapesoft.utilities.console.gson.impl.PunishmentLoader;
import org.scapesoft.utilities.console.logging.FileLogger;
import org.scapesoft.utilities.misc.ChatMessage;
import org.scapesoft.utilities.misc.Utils;
import org.scapesoft.utilities.security.Huffman;

/**
 * @author Lazarus <lazarus.rs.king@gmail.com>
 * @since Jun 19, 2014
 */
@PacketInformation(listeners = "23,36,30")
public class ChatPacketHandler extends PacketHandler {

	private final static int CHAT_TYPE_PACKET = 23;
	private final static int CHAT_PACKET = 36;
	private final static int PUBLIC_QUICK_CHAT_PACKET = 30;

	@Override
	public void handle(Player player, Integer packetId, Integer length, InputStream stream) {
		switch (packetId) {
		case CHAT_TYPE_PACKET:
			int type = stream.readUnsignedByte();
			player.setChatType(type);
			break;
		case CHAT_PACKET:
			if (!player.hasStarted()) {
				return;
			}
			if (player.getLastPublicMessage() > Utils.currentTimeMillis()) {
				return;
			}
			player.setLastPublicMessage(Utils.currentTimeMillis() + 300);
			int colorEffect = stream.readUnsignedByte();
			int moveEffect = stream.readUnsignedByte();
			String message = Huffman.readEncryptedMessage(250, stream);
			if (message == null || message.replaceAll(" ", "").equals("")) {
				return;
			}
			if (message.startsWith("::") || message.startsWith(";;")) {
				CommandHandler.get().handleCommand(player, message.replace("::", "").replace(";;", ""));
				return;
			}
			if (PunishmentLoader.isPunished(player.getUsername(), PunishmentType.MUTE) || PunishmentLoader.isPunished(player.getSession().getIP(), PunishmentType.IPMUTE)) {
				player.sendMessage("You are muted. Check back in 48 hours.");
				return;
			}
			FileLogger.getFileLogger().writeLog("chats/", player.getDisplayName() + " said:\t[" + message + "]", true);
			int effects = (colorEffect << 8) | (moveEffect & 0xff);
			if (player.getChatType() == 1) {
				player.sendFriendsChannelMessage(new ChatMessage(message));
			} else if (player.getChatType() == 2) {
				player.sendClanChannelMessage(new ChatMessage(message));
			} else if (player.getChatType() == 3) {
				player.sendGuestClanChannelMessage(new ChatMessage(message));
			} else {
				player.sendPublicChatMessage(new PublicChatMessage(message, effects));
			}
			if (Constants.DEBUG) {
				System.out.println("Chat type: " + player.getChatType());
			}
			break;
		case PUBLIC_QUICK_CHAT_PACKET:
			if (!player.hasStarted()) {
				return;
			}
			if (player.getLastPublicMessage() > Utils.currentTimeMillis()) {
				return;
			}
			player.setLastPublicMessage(Utils.currentTimeMillis() + 300);
			stream.readByte();
			int fileId = stream.readUnsignedShort();
			if (!Utils.isQCValid(fileId))
				return;
			byte[] data = null;
			if (length > 3) {
				data = new byte[length - 3];
				stream.readBytes(data);
			}
			data = Utils.completeQuickMessage(player, fileId, data);
			if (player.getChatType() == 0) {
				player.sendPublicChatMessage(new QuickChatMessage(fileId, data));
			} else if (player.getChatType() == 1) {
				player.sendFriendsChannelQuickMessage(new QuickChatMessage(fileId, data));
			} else if (player.getChatType() == 2) {
				player.sendClanChannelQuickMessage(new QuickChatMessage(fileId, data));
			} else {
				System.out.println("Unknown chat type: " + player.getChatType());
			}
			break;
		}
	}

}
