package org.scapesoft.game.minigames.games;

import org.scapesoft.game.Entity;
import org.scapesoft.game.Hit;
import org.scapesoft.game.WorldTile;
import org.scapesoft.game.npc.NPC;
import org.scapesoft.game.player.Player;

/**
 * @author Tyluur<itstyluur@gmail.com>
 * @since Aug 19, 2014
 */
public class NoviteGamesNPC extends NPC {

	public NoviteGamesNPC(int id, WorldTile tile) {
		super(id, tile, -1, true);
		setForceAgressive(true);
		setSpawned(true);
		setForceMultiArea(true);
		setStats();
	}

	private void setStats() {
		setBonuses(new int[13]);
		
		getCombatDefinitions().setMaxHit(200);
		for (int i = 0; i <= 5; i++) {
			getBonuses()[i] = 200;
		}
		for (int i = 5; i <= 10; i++) {
			getBonuses()[i] = 50;
		}
	}

	@Override
	public void handleIngoingHit(final Hit hit) {
		super.handleIngoingHit(hit);
		Entity source = hit.getSource();
		if (source.isPlayer()) {
			Player p = source.player();
			if (p.getControllerManager().getController() instanceof GamesHandler) {
				GamesHandler handler = (GamesHandler) p.getControllerManager().getController();
				handler.setDamageDealt(handler.getDamageDealt() + hit.getDamage());
			}
		}
	}
	
	@Override
	public void drop() {
		MainGameHandler.get().removeMonster(this);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -4691087739451782456L;

}
