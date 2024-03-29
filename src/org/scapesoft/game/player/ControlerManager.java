package org.scapesoft.game.player;

import java.io.Serializable;

import org.scapesoft.Constants;
import org.scapesoft.game.Entity;
import org.scapesoft.game.WorldObject;
import org.scapesoft.game.WorldTile;
import org.scapesoft.game.item.FloorItem;
import org.scapesoft.game.item.Item;
import org.scapesoft.game.minigames.games.GamesHandler;
import org.scapesoft.game.npc.NPC;
import org.scapesoft.game.player.content.Foods.Food;
import org.scapesoft.game.player.content.Pots.Pot;
import org.scapesoft.game.player.controlers.Controller;
import org.scapesoft.game.player.controlers.ControllerHandler;

public final class ControlerManager implements Serializable {

	private static final long serialVersionUID = 2084691334731830796L;

	private transient Player player;
	private transient Controller controler;
	private transient boolean inited;
	private Object[] lastControlerArguments;

	private String lastControler;

	public ControlerManager() {
		lastControler = Constants.START_CONTROLER;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public Controller getController() {
		return controler;
	}

	public void startController(String key, Object... parameters) {
		if (controler != null) {
			if (controler instanceof GamesHandler) {
				((GamesHandler) controler).leaveGame(false);
			}
			forceStop();
		}
		controler = ControllerHandler.getControler(key);
		if (controler == null) {
			return;
		}
		controler.setPlayer(player);
		lastControlerArguments = parameters;
		lastControler = key;
		controler.start();
		inited = true;
	}

	public void login() {
		if (lastControler == null) {
			return;
		}
		controler = ControllerHandler.getControler(lastControler);
		if (controler == null) {
			forceStop();
			return;
		}
		controler.setPlayer(player);
		if (controler.login()) {
			forceStop();
		} else {
			inited = true;
		}
	}

	public void logout() {
		if (controler == null) {
			return;
		}
		if (controler.logout()) {
			forceStop();
		}
	}

	public boolean canWalk() {
		if (controler == null || !inited) {
			return true;
		}
		return controler.canWalk();
	}
	
	public boolean canTrade(Player player) {
		if (controler == null || !inited) {
			return true;
		}
		return controler.canTrade(player);
	}

	public boolean canMove(int dir) {
		if (controler == null || !inited) {
			return true;
		}
		return controler.canMove(dir);
	}

	public boolean checkWalkStep(int lastX, int lastY, int nextX, int nextY) {
		if (controler == null || !inited) {
			return true;
		}
		return controler.checkWalkStep(lastX, lastY, nextX, nextY);
	}

	public boolean keepCombating(Entity target) {
		if (controler == null || !inited) {
			return true;
		}
		return controler.keepCombating(target);
	}

	public boolean canEquip(int slotId, int itemId) {
		if (controler == null || !inited) {
			return true;
		}
		return controler.canEquip(slotId, itemId);
	}

	public boolean canAddInventoryItem(int itemId, int amount) {
		player.getDialogueManager().finishDialogue();
		if (controler == null || !inited)
			return true;
		return controler.canAddInventoryItem(itemId, amount);
	}

	public void trackXP(int skillId, int addedXp) {
		if (controler == null || !inited) {
			return;
		}
		controler.trackXP(skillId, addedXp);
	}

	public boolean canDeleteInventoryItem(int itemId, int amount) {
		if (controler == null || !inited) {
			return true;
		}
		return controler.canDeleteInventoryItem(itemId, amount);
	}

	public boolean canUseItemOnItem(Item itemUsed, Item usedWith) {
		if (controler == null || !inited) {
			return true;
		}
		return controler.canUseItemOnItem(itemUsed, usedWith);
	}

	public boolean canAttack(Entity entity) {
		if (controler == null || !inited) {
			return true;
		}
		return controler.canAttack(entity);
	}

	public boolean canPlayerOption1(Player target) {
		if (controler == null || !inited) {
			return true;
		}
		return controler.canPlayerOption1(target);
	}

	public boolean canHit(Entity entity) {
		if (controler == null || !inited) {
			return true;
		}
		return controler.canHit(entity);
	}

	public void moved() {
		if (controler == null || !inited) {
			return;
		}
		controler.moved();
	}

	public void magicTeleported(int type) {
		if (controler == null || !inited) {
			return;
		}
		controler.magicTeleported(type);
	}

	public void sendInterfaces() {
		if (controler == null || !inited) {
			return;
		}
		controler.sendInterfaces();
	}

	public void process() {
		if (controler == null || !inited) {
			return;
		}
		controler.process();
	}

	public boolean sendDeath() {
		if (controler == null || !inited) {
			return true;
		}
		return controler.sendDeath();
	}

	public boolean canEat(Food food) {
		if (controler == null || !inited) {
			return true;
		}
		return controler.canEat(food);
	}

	public boolean canPot(Pot pot) {
		if (controler == null || !inited) {
			return true;
		}
		return controler.canPot(pot);
	}

	public boolean useDialogueScript(Object key) {
		if (controler == null || !inited) {
			return true;
		}
		return controler.useDialogueScript(key);
	}

	public boolean processMagicTeleport(WorldTile toTile) {
		if (controler == null || !inited) {
			return true;
		}
		return controler.processMagicTeleport(toTile);
	}

	public boolean processItemTeleport(WorldTile toTile) {
		if (controler == null || !inited) {
			return true;
		}
		return controler.processItemTeleport(toTile);
	}

	public boolean processObjectTeleport(WorldTile toTile) {
		if (controler == null || !inited) {
			return true;
		}
		return controler.processObjectTeleport(toTile);
	}

	public boolean processObjectClick1(WorldObject object) {
		if (controler == null || !inited) {
			return true;
		}
		return controler.processObjectClick1(object);
	}

	public boolean processButtonClick(int interfaceId, int componentId, int slotId, int packetId) {
		if (controler == null || !inited) {
			return true;
		}
		return controler.processButtonClick(interfaceId, componentId, slotId, packetId);
	}

	public boolean processNPCClick1(NPC npc) {
		if (controler == null || !inited) {
			return true;
		}
		return controler.processNPCClick1(npc);
	}

	public boolean processNPCClick2(NPC npc) {
		if (controler == null || !inited) {
			return true;
		}
		return controler.processNPCClick2(npc);
	}

	public boolean processNPCClick3(NPC npc) {
		if (controler == null || !inited) {
			return true;
		}
		return controler.processNPCClick3(npc);
	}

	public boolean processObjectClick2(WorldObject object) {
		if (controler == null || !inited) {
			return true;
		}
		return controler.processObjectClick2(object);
	}

	public boolean processObjectClick3(WorldObject object) {
		if (controler == null || !inited) {
			return true;
		}
		return controler.processObjectClick3(object);
	}

	public boolean processItemOnNPC(NPC npc, Item item) {
		if (controler == null || !inited) {
			return true;
		}
		return controler.processItemOnNPC(npc, item);
	}

	public void processNPCDeath(int id) {
		if (controler == null || !inited) {
			return;
		}
		controler.processNPCDeath(id);
	}

	public boolean canSummonFamiliar() {
		if (controler == null || !inited) {
			return true;
		}
		return controler.canSummonFamiliar();
	}

	public void forceStop() {
		if (controler != null) {
			controler.forceClose();
			controler = null;
		}
		lastControlerArguments = null;
		lastControler = null;
		inited = false;
	}

	public void removeControllerWithoutCheck() {
		controler = null;
		lastControlerArguments = null;
		lastControler = null;
		inited = false;
	}

	public Object[] getLastControlerArguments() {
		return lastControlerArguments;
	}

	public void setLastControlerArguments(Object[] lastControlerArguments) {
		this.lastControlerArguments = lastControlerArguments;
	}

	public boolean canDropItem(Item item) {
		if (controler == null || !inited) {
			return true;
		}
		return controler.canDropItem(item);
	}

	public boolean handleItemOnObject(WorldObject object, Item item) {
		if (controler == null || !inited) {
			return true;
		}
		return controler.handleItemOnObject(object, item);
	}

	public boolean canTakeItem(FloorItem item) {
		if (controler == null || !inited) {
			return true;
		}
		return controler.canTakeItem(item);
	}

	public boolean canPlayerOption2(Player target) {
		if (controler == null || !inited) {
			return true;
		}
		return controler.canPlayerOption2(target);
	}

	public boolean canPlayerOption3(Player target) {
		if (controler == null || !inited) {
			return true;
		}
		return controler.canPlayerOption3(target);
	}

	public boolean canPlayerOption4(Player target) {
		if (controler == null || !inited) {
			return true;
		}
		return controler.canPlayerOption4(target);
	}

	public boolean processItemOnPlayer(Player p2, Item item) {
		if (controler == null || !inited) {
			return true;
		}
		return controler.processItemOnPlayer(p2, item);
	}

	public boolean addWalkStep(int lastX, int lastY, int nextX, int nextY) {
		if (controler == null || !inited)
			return true;
		return controler.checkWalkStep(lastX, lastY, nextX, nextY);
	}

	public boolean handleItemOption1(Player playerr, int slotId, int itemId, Item item) {
		if (itemId != item.getId()) {
			return false;
		}
		switch (itemId) {
			case -1:
				return false;
		}
		return true;
	}
	
	public boolean processObjectClick4(WorldObject object) {
		if (controler == null || !inited)
			return true;
		return controler.processObjectClick4(object);
	}

	public boolean processObjectClick5(WorldObject object) {
		if (controler == null || !inited)
			return true;
		return controler.processObjectClick5(object);
	}

}
