package org.scapesoft.game.player.controlers.impl.quest;

import org.scapesoft.game.Entity;
import org.scapesoft.game.WorldTile;
import org.scapesoft.game.npc.others.quest.DesertTreasureNPC;
import org.scapesoft.game.npc.others.quest.LunarDiplomacyNPC;
import org.scapesoft.game.player.controlers.Controller;
import org.scapesoft.utilities.game.player.TeleportLocations;
import org.scapesoft.utilities.misc.ChatColors;

/**
 * @author Lazarus <lazarus.rs.king@gmail.com>
 * @since Jun 29, 2014
 */
public class LDQuest extends Controller {

	@Override
	public void start() {
		player.closeInterfaces();
		target = new LunarDiplomacyNPC(player.getUsername(), 4510, TARGET_TILE, -1, true);
		player.setNextWorldTile(new WorldTile(1825, 5165, 2));
		target.setTarget(player);
		player.getHintIconsManager().addHintIcon(target, 1, -1, false);
	}

	@Override
	public boolean processButtonClick(int interfaceId, int componentId, int slotId, int packetId) {
		if (interfaceId == 182 && (componentId == 6 || componentId == 13)) {
			player.sendMessage("<col=" + ChatColors.RED + ">You can't log out in here!");
			return false;
		}
		return true;
	}

	@Override
	public boolean checkWalkStep(int lastX, int lastY, int nextX, int nextY) {
		if (target != null && !player.withinDistance(target, 20)) {
			forceClose();
			removeController();
		}
		return true;
	}

	@Override
	public boolean canAttack(Entity target) {
		if (target instanceof DesertTreasureNPC && target != this.target) {
			player.getPackets().sendGameMessage("This isn't your target.");
			return false;
		}
		return true;
	}

	@Override
	public boolean logout() {
		leave();
		return true;
	}

	@Override
	public void forceClose() {
		leave();
	}

	private void leave() {
		player.setNextWorldTile(TeleportLocations.QUESTING_DOME);
		if (target != null) {
			target.finish(); // target also calls removing hint icon at remove
		}
		removeController();
	}

	private static final WorldTile TARGET_TILE = new WorldTile(1825, 5155, 2);
	private LunarDiplomacyNPC target;
}
