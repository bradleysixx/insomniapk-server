package org.scapesoft.game.npc.others;

import org.scapesoft.game.Entity;
import org.scapesoft.game.World;
import org.scapesoft.game.WorldTile;
import org.scapesoft.game.item.Item;
import org.scapesoft.game.npc.NPC;
import org.scapesoft.game.npc.godwars.bandos.GodwarsBandosFaction;
import org.scapesoft.game.player.Player;
import org.scapesoft.game.player.controlers.impl.guilds.warriors.WarriorsGuild;
import org.scapesoft.game.tasks.WorldTask;
import org.scapesoft.game.tasks.WorldTasksManager;
import org.scapesoft.utilities.misc.Utils;

public class Cyclopse extends GodwarsBandosFaction {

	/**
	 *
	 */
	private static final long serialVersionUID = -348753458086327348L;

	public Cyclopse(int id, WorldTile tile, int mapAreaNameHash, boolean canBeAttackFromOutOfArea) {
		super(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea, false);
	}

	@Override
	public void sendDeath(Entity source) {
		super.sendDeath(source);
		if (source instanceof Player) {
			WarriorsGuild.killedCyclopses++;
			final NPC npc = this;
			final Player player = (Player) source;
			if (player.getControllerManager().getController() == null || !(player.getControllerManager().getController() instanceof WarriorsGuild) || Utils.random(15) != 0) {
				return;
			}
			WarriorsGuild controler = (WarriorsGuild) player.getControllerManager().getController();
			if (!controler.isInCyclopse()) {
				System.out.println("Player tried to receive drops butw asnt in yabish");
				return;
			}
			WorldTasksManager.schedule(new WorldTask() {

				@Override
				public void run() {
					World.addGroundItem(new Item(WarriorsGuild.getBestDefender(player)), new WorldTile(getCoordFaceX(npc.getSize()), getCoordFaceY(npc.getSize()), getPlane()), player, true, 60);
				}
			}, getCombatDefinitions().getDeathDelay());
		}
	}
}
