package org.scapesoft.engine.process.impl;

import java.util.concurrent.TimeUnit;

import org.scapesoft.Constants;
import org.scapesoft.api.input.StringInputAction;
import org.scapesoft.engine.process.TimedProcess;
import org.scapesoft.game.World;
import org.scapesoft.game.player.Player;
import org.scapesoft.game.player.content.randoms.RandomEventManager;
import org.scapesoft.game.player.controlers.impl.StartTutorial;
import org.scapesoft.networking.Session;
import org.scapesoft.networking.codec.stream.InputStream;
import org.scapesoft.networking.packet.PacketHandler;
import org.scapesoft.networking.packet.PacketSystem;
import org.scapesoft.networking.packet.impl.ButtonPacketHandler;
import org.scapesoft.networking.packet.impl.ChatPacketHandler;
import org.scapesoft.networking.packet.impl.WalkingPacketHandler;
import org.scapesoft.networking.protocol.game.DefaultGameDecoder;
import org.scapesoft.utilities.misc.ChatColors;
import org.scapesoft.utilities.misc.Utils;

/**
 * @author Tyluur<itstyluur@gmail.com>
 * @since Nov 19, 2014
 */
public class PacketQueueProcessor implements TimedProcess {

	@Override
	public Timer getTimer() {
		return new Timer(1, TimeUnit.MILLISECONDS);
	}

	@Override
	public void execute() {
		synchronized (lock) {
			try {
				for (Player player : World.getPlayers()) {
					if (player == null || !player.hasStarted())
						continue;
					player.getSession().getQueuedPackets().removeIf(e -> {
						processStream(player, player.getSession(), e);
						return true;
					});
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void processStream(Player player, Session session, InputStream stream) {
		while (stream.getRemaining() > 0 && session.getChannel().isConnected() && !player.hasFinished()) {
			int packetId = stream.readUnsignedByte();
			if (packetId >= DefaultGameDecoder.PACKET_SIZES.length && Constants.DEBUG) {
				System.out.println("PacketId " + packetId + " has fake packet id.");
				break;
			}
			int length = DefaultGameDecoder.PACKET_SIZES[packetId];
			if (length == -1) {
				length = stream.readUnsignedByte();
			} else if (length == -2) {
				length = stream.readUnsignedShort();
			} else if (length == -3) {
				length = stream.readInt();
			} else if (length == -4) {
				length = stream.getRemaining();
				if (Constants.DEBUG) {
					System.out.println("Invalid size for PacketId " + packetId + ". Size guessed to be " + length);
				}
			}
			if (length > stream.getRemaining()) {
				length = stream.getRemaining();
				if (Constants.DEBUG) {
					System.out.println("PacketId " + packetId + " has fake size. - expected size " + length);
				}
			}
			int startOffset = stream.getOffset();
			if (player.controlerAvailable() && World.containsPlayer(player.getUsername()) && ((TimeUnit.MILLISECONDS.toMinutes(player.getActionTime()) > 20) || player.getTemporaryAttributtes().remove("random_event_requested") != null)) {
				RandomEventManager.get().startRandomEvent(player);
				player.setActionTime(0);
			}
			if (player.getInterfaceManager().isClientActive() && packetId == 16) {
				player.setLoyaltyTicks(player.getLoyaltyTicks() + 1);
				if (TimeUnit.SECONDS.toMinutes(player.getLoyaltyTicks()) >= 30) {
					player.setLoyaltyTicks(0);
					player.getLoyaltyManager().addPoints(500);
					player.sendMessage("You receive <col=" + ChatColors.BLUE + ">500</col> loyalty points for <col=" + ChatColors.BLUE + ">30</col> minutes of ingame activity.");
				}
			}
			PacketHandler handler = PacketSystem.getHandler(packetId);
			player.setPacketsDecoderPing(Utils.currentTimeMillis());
			if (handler != null) {
				if (Constants.SQL_ENABLED && player.requiresEmailSet() && !(player.getControllerManager().getController() instanceof StartTutorial)) {
					if (handler.getClass() == WalkingPacketHandler.class || handler.getClass() == ChatPacketHandler.class || handler.getClass() == ButtonPacketHandler.class) {
						player.getPackets().sendInputLongTextScript("Enter Email:", new StringInputAction() {

							@Override
							public void handle(String input) {
								player.getFacade().setEmail(input);
							}
						});
						return;
					}
				}
				try {
					switch (packetId) {
					case 84:
					case 68:
						player.getAttributes().put("last_real_packet_time", System.currentTimeMillis());
						break;
					}
					handler.handle(player, packetId, length, stream);
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				if (Constants.DEBUG) {
					System.out.println("No packet handler for opcode: " + packetId);
				}
			}
			stream.setOffset(startOffset + length);
		}
	}

	private Object lock = new Object();
}
