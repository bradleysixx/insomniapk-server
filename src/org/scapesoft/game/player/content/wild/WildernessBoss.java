package org.scapesoft.game.player.content.wild;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;

import org.scapesoft.Constants;
import org.scapesoft.game.Animation;
import org.scapesoft.game.Entity;
import org.scapesoft.game.World;
import org.scapesoft.game.WorldTile;
import org.scapesoft.game.item.Item;
import org.scapesoft.game.npc.NPC;
import org.scapesoft.game.npc.combat.NPCCombatDefinitions;
import org.scapesoft.game.npc.familiar.Familiar;
import org.scapesoft.game.player.Player;
import org.scapesoft.game.player.content.FriendChatsManager;
import org.scapesoft.game.player.content.wild.activities.WildernessBossActivity;
import org.scapesoft.game.tasks.WorldTask;
import org.scapesoft.game.tasks.WorldTasksManager;
import org.scapesoft.utilities.console.gson.impl.NPCDropManager;
import org.scapesoft.utilities.game.npc.drops.Drop;
import org.scapesoft.utilities.game.npc.drops.Drop.Chance;
import org.scapesoft.utilities.misc.ChatColors;
import org.scapesoft.utilities.misc.Utils;

/**
 * @author Tyluur<itstyluur@gmail.com>
 * @since Jan 1, 2015
 */
@SuppressWarnings("serial")
public class WildernessBoss extends NPC {

	public WildernessBoss(int id, WorldTile tile) {
		super(id, tile, -1, true);
		setSpawned(true);
		for (int i = 0; i < getBonuses().length; i++) {
			getBonuses()[i] += 120;
		}
	}
	
	@Override
	public void sendDeath(final Entity source) {
		final NPCCombatDefinitions defs = getCombatDefinitions();
		resetWalkSteps();
		getCombat().removeTarget();
		setNextAnimation(null);
		WorldTasksManager.schedule(new WorldTask() {
			int loop;

			@Override
			public void run() {
				if (loop == 0) {
					setNextAnimation(new Animation(defs.getDeathEmote()));
				} else if (loop >= defs.getDeathDelay()) {
					if (source instanceof Player) {
						((Player) source).getControllerManager().processNPCDeath(getId());
					}
					drop();
					reset();
					setLocation(getRespawnTile());
					finish();
					if (!isSpawned()) {
						setRespawnTask();
					}
					stop();
				}
				loop++;
			}
		}, 0, 1);
	}
	
	@Override
	protected List<Item> getItemsToDrop(Player killer) {
		List<Item> items = new ArrayList<Item>();
		List<Drop> drops = NPCDropManager.getDrops(getName());

		if (drops == null || drops.size() == 0) {
			if (Constants.DEBUG) {
				System.out.println(getName() + " has no drops");
			}
			return items;
		}
		Collections.shuffle(drops);
		ListIterator<Drop> it = drops.listIterator();
		Drop[] possibleDrops = new Drop[drops.size()];
		int possibleDropsCount = 0;
		boolean equippingROW = killer.getEquipment().getRingId() == 2572;

		while (it.hasNext()) {
			Drop drop = it.next();
			if (drop.getRate() == Chance.ALWAYS) {
				items.add(new Item(drop.getItemId()));
			} else {
				/** If the item is a pvp item, it has decreased chances of loot because rev tables have few items. */
				boolean pvp = drop.getItemId() >= 13858 && drop.getItemId() <= 13990;
				int chanceReduction = pvp ? 100 : 40;
				double chance = pvp ? Utils.getRandomDouble(300 - chanceReduction) : Utils.getRandomDouble(120 - chanceReduction);
				double dropChance = drop.getRate().getChance();
				/** Implementing ring of wealth drop modification */
				if (equippingROW && killer.getFacade().getRowCharges() > 0) {
					chance = chance * 0.90;
				}
				if (chance <= dropChance) {
					possibleDrops[possibleDropsCount++] = drop;
				}
			}
		}
		if (possibleDropsCount > 0) {
			Drop drop = possibleDrops[Utils.getRandom(possibleDropsCount - 1)];
			Item item = new Item(drop.getItemId(), Utils.random(drop.getMinAmount(), drop.getMaxAmount()));
			if (item.getAmount() > 1) {
				if (!item.getDefinitions().isStackable() && !item.getDefinitions().isNoted() && item.getDefinitions().getCertId() != -1) {
					item.setId(item.getDefinitions().getCertId());
				}
			}
			items.add(item);
			if (equippingROW && killer.getFacade().getRowCharges() > 0) {
				killer.getFacade().setRowCharges(killer.getFacade().getRowCharges() <= 1 ? 0 : killer.getFacade().getRowCharges() - 1);
				if (killer.getFacade().getRowCharges() > 0) {
					killer.sendMessage("You now have " + killer.getFacade().getRowCharges() + " ring of wealth charges left.");
				}
			}
			if (equippingROW && killer.getFacade().getRowCharges() <= 0) {
				killer.sendMessage("You have a ring of wealth equipped with 0 charges. Charge it with Max at the home portal!");
			}
		}
		return items;
	}

	@Override
	public void drop() {
		try {
			if (getTemporaryAttributtes().contains("droploot")) {
				return;
			}
			Entity entityKiller = getMostDamageReceivedSourceEntity();
			if (entityKiller == null) {
				entityKiller = getAttackedBy();
			}
			if (entityKiller == null) {
				return;
			}
			Player killer = null;
			if (entityKiller.isNPC()) {
				if (entityKiller instanceof Familiar) {
					Familiar familiar = (Familiar) entityKiller;
					killer = familiar.getOwner();
				}
			} else if (entityKiller.isPlayer()) {
				killer = (Player) entityKiller;
			}
			if (killer == null) {
				return;
			}
			handleDeathReward(killer);
			
			/** Wilderness boss implementation */
			int pointsToGive = killer.isDonator() ? 75 : 50;
			killer.getFacade().addWildernessPoints(pointsToGive);
			killer.sendMessage("<col=" + ChatColors.MAROON + ">You receive " + pointsToGive + " wilderness points for slaying this boss!");
			
			/** Gives players engaging in combat with the boss bonus points */
			for (Entity source : getReceivedDamage().keySet()) {
				if (!source.isPlayer()) {
					continue;
				}
				Integer damage = getReceivedDamage().get(source);
				if (damage == null || source.hasFinished()) {
					continue;
				}
				double percent = (double) ((double) damage / (double) getMaxHitpoints());
				if (percent > 0.10) {
					WildernessActivityManager.getSingleton().giveBonusPoints(source.player());
				}
			}
			
			if (WildernessActivityManager.getSingleton().isActivityCurrent(WildernessBossActivity.class)) {
				World.sendWorldMessage("<col=FF0000>Wilderness Activity:</col> The wilderness boss has been defeated! A new one is coming...", false, true);
				WildernessBossActivity activity = WildernessActivityManager.getSingleton().getWildernessActivity(WildernessBossActivity.class);
				activity.setNextSpawnTime(System.currentTimeMillis() + WildernessBossActivity.RESPAWN_DELAY);
			}
			
			List<Item> items = getItemsToDrop(killer);
			if (items.size() == 0) {
				return;
			}
			List<Player> players = FriendChatsManager.getLootSharingPeople(killer);
			if (players == null || players.size() == 1) {
				for (Item item : items) {
					applyDropEffects(killer, item);
					dropItem(item, killer);
				}
			} else {
				for (Item item : items) {
					Player luckyPlayer = players.get(Utils.random(players.size()));
					applyDropEffects(luckyPlayer, item);
					dropItem(item, luckyPlayer);
					luckyPlayer.getPackets().sendGameMessage("<col=00FF00>You received: " + item.getAmount() + " " + item.getName() + ".");
					for (Player p2 : players) {
						if (p2 == luckyPlayer)
							continue;
						p2.getPackets().sendGameMessage("<col=66FFCC>" + luckyPlayer.getDisplayName() + "</col> received: " + item.getAmount() + " " + item.getName() + ".");
						p2.getPackets().sendGameMessage("Your chance of receiving loot has improved.");
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} catch (Error e) {
			e.printStackTrace();
		}
	}

}
