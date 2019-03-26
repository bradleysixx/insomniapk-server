package org.scapesoft.networking.packet.impl;

import org.scapesoft.game.WorldTile;
import org.scapesoft.game.player.Player;
import org.scapesoft.game.route.RouteFinder;
import org.scapesoft.game.route.strategy.FixedTileStrategy;
import org.scapesoft.networking.codec.stream.InputStream;
import org.scapesoft.networking.packet.PacketHandler;
import org.scapesoft.networking.packet.PacketInformation;
import org.scapesoft.utilities.misc.Utils;

/**
 * @author Lazarus <lazarus.rs.king@gmail.com>
 * @since Jun 19, 2014
 */
@PacketInformation(listeners = "12,83")
public class WalkingPacketHandler extends PacketHandler {

	@Override
	public void handle(final Player player, Integer packetId, Integer length, InputStream stream) {
		if (!player.hasStarted() || !player.clientHasLoadedMapRegion() || player.isDead() || !player.getControllerManager().canWalk()) {
			return;
		}
		long currentTime = Utils.currentTimeMillis();
		if (player.getLockDelay() > currentTime) {
			return;
		}
		if (player.isFrozen()) {
			player.getPackets().sendGameMessage("A magical force prevents you from moving.");
			return;
		}
		int x = stream.readUnsignedShortLE128();
		int y = stream.readUnsignedShortLE128();
		boolean forceRun = stream.readUnsignedByte() == 1;
		player.stopAll();
		if (forceRun) {
			player.setRun(forceRun);
		}

		int steps = RouteFinder.findRoute(RouteFinder.WALK_ROUTEFINDER, player.getX(), player.getY(), player.getPlane(), player.getSize(), new FixedTileStrategy(x, y), true);
		int[] bufferX = RouteFinder.getLastPathBufferX();
		int[] bufferY = RouteFinder.getLastPathBufferY();
		int last = -1;
		for (int i = steps - 1; i >= 0; i--) {
			if (!player.addWalkSteps(bufferX[i], bufferY[i], 25, true))
				break;
			last = i;
		}

		if (last != -1) {
			WorldTile tile = new WorldTile(bufferX[last], bufferY[last], player.getPlane());
			player.getPackets().sendMinimapFlag(tile.getLocalX(player.getLastLoadedMapRegionTile(), player.getMapSize()), tile.getLocalY(player.getLastLoadedMapRegionTile(), player.getMapSize()));
		} else {
			player.getPackets().sendResetMinimapFlag();
		}
		/*
		 * 
		length = stream.getLength();

		if (packetId == 83)
			length -= 13;
		int baseX = stream.readUnsignedShortLE128();
		int baseY = stream.readUnsignedShortLE128();
		stream.readByte();
		int steps = (length - 5) / 2;
		if (steps > 25)
			steps = 25;
		player.stopAll();
		for (int step = 0; step < steps; step++)
			// dynamic part temporary fix
			if (!player.addWalkSteps(baseX + stream.readUnsignedByte(), baseY + stream.readUnsignedByte(), -1, !player.isAtDynamicRegion()))
				break;
		 */
	}

}