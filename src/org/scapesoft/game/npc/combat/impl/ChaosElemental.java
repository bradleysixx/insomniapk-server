package org.scapesoft.game.npc.combat.impl;

import java.util.ArrayList;
import java.util.List;

import org.scapesoft.game.Animation;
import org.scapesoft.game.Entity;
import org.scapesoft.game.World;
import org.scapesoft.game.item.Item;
import org.scapesoft.game.npc.NPC;
import org.scapesoft.game.npc.combat.CombatScript;
import org.scapesoft.game.npc.combat.NPCCombatDefinitions;
import org.scapesoft.game.player.Equipment;
import org.scapesoft.game.player.Player;
import org.scapesoft.utilities.misc.Utils;

/**
 * @author Lazarus <lazarus.rs.king@gmail.com>
 * @since Jul 5, 2014
 */
public class ChaosElemental extends CombatScript {

	@Override
	public Object[] getKeys() {
		return new Object[]  { 3200 };
	}

	@Override
	public int attack(NPC npc, Entity entityTarget) {
		final NPCCombatDefinitions defs = npc.getCombatDefinitions();		
		
		for (Entity target : getPossibleTargets(npc)) {
			if (Utils.percentageChance(10)) {
				if (target.isPlayer() && target.player().getEquipment().getWeaponId() != -1) {
					Item wep = target.player().getEquipment().getItem(Equipment.SLOT_WEAPON);
					target.player().getEquipment().deleteItem(wep.getId(), wep.getAmount());
					if (target.player().getInventory().addItem(wep)) {
						target.player().sendMessage("The chaos elemental removes your weapon and places it in your inventory.");
					} else {
						World.addGroundItem(wep, target.player(), target.player(), true, 60);
						target.player().sendMessage("The chaos elemental removes your weapon and drops it below you.");
					}
					target.player().getAppearence().generateAppearenceData();
					target.player().getEquipment().refresh();
				}
			}
			delayHit(npc, 2, target, getRangeHit(npc, getRandomMaxHit(npc, 400, NPCCombatDefinitions.RANGE, target)));
			World.sendProjectile(npc, target, 551, 18, 18, 50, 50, 0, 0);

			npc.setNextAnimation(ATTACK_ANIMATION);
		}
		
		return defs.getAttackDelay();
	}
	public List<Entity> getPossibleTargets(NPC npc) {
		List<Entity> targets = new ArrayList<Entity>();
		List<Integer> indexes = npc.getRegion().getPlayerIndexes();
		if (indexes != null) {
			for (Integer index : indexes) {
				Player player = World.getPlayers().get(index);
				if (player == null || !player.withinDistance(npc, 15)) {
					continue;
				}
				targets.add(player);
			}
		}
		return targets;
	}
	
	private static final Animation ATTACK_ANIMATION = new Animation(5443);
}
