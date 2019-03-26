package org.scapesoft.game.player.controlers;

import org.scapesoft.game.Entity;
import org.scapesoft.game.WorldObject;
import org.scapesoft.game.WorldTile;
import org.scapesoft.game.item.FloorItem;
import org.scapesoft.game.item.Item;
import org.scapesoft.game.npc.NPC;
import org.scapesoft.game.player.Player;
import org.scapesoft.game.player.content.Foods.Food;
import org.scapesoft.game.player.content.Pots.Pot;

public abstract class Controller {

	protected Player player;

	public final void setPlayer(Player player) {
		this.player = player;
	}

	public Player getPlayer() {
		return player;
	}

	public final Object[] getArguments() {
		return player.getControllerManager().getLastControlerArguments();
	}

	public final void setArguments(Object[] objects) {
		player.getControllerManager().setLastControlerArguments(objects);
	}

	public final void removeController() {
		player.getControllerManager().removeControllerWithoutCheck();
	}

	public abstract void start();

	public boolean canEat(Food food) {
		return true;
	}

	public boolean canTrade(Player player) {
		return true;
	}

	public boolean handleItemOnObject(WorldObject object, Item item) {
		return true;
	}

	public boolean canPot(Pot pot) {
		return true;
	}

	/*
	 * after the normal checks, extra checks, only called when you attacking
	 */
	public boolean keepCombating(Entity target) {
		return true;
	}

	public boolean canEquip(int slotId, int itemId) {
		return true;
	}

	/*
	 * after the normal checks, extra checks, only called when you start trying
	 * to attack
	 */
	public boolean canAttack(Entity target) {
		return true;
	}

	public void trackXP(int skillId, int addedXp) {

	}

	public boolean canDeleteInventoryItem(int itemId, int amount) {
		return true;
	}

	public boolean canUseItemOnItem(Item itemUsed, Item usedWith) {
		return true;
	}

	public boolean canAddInventoryItem(int itemId, int amount) {
		return true;
	}

	public boolean canPlayerOption1(Player target) {
		return true;
	}

	/*
	 * hits as ice barrage and that on multi areas
	 */
	public boolean canHit(Entity entity) {
		return true;
	}

	/*
	 * processes every game ticket, usualy not used
	 */
	public void process() {

	}

	public void moved() {

	}

	public void magicTeleported(int type) {

	}

	public void sendInterfaces() {

	}

	/*
	 * return can use script
	 */
	public boolean useDialogueScript(Object key) {
		return true;
	}

	/*
	 * return can teleport
	 */
	public boolean processMagicTeleport(WorldTile toTile) {
		return true;
	}

	/*
	 * return can teleport
	 */
	public boolean processItemTeleport(WorldTile toTile) {
		return true;
	}

	/*
	 * return can teleport
	 */
	public boolean processObjectTeleport(WorldTile toTile) {
		return true;
	}

	/*
	 * return process normaly
	 */
	public boolean processObjectClick1(WorldObject object) {
		return true;
	}

	/*
	 * return process normaly
	 */
	public boolean processButtonClick(int interfaceId, int componentId, int slotId, int packetId) {
		return true;
	}

	/*
	 * return process normaly
	 */
	public boolean processNPCClick1(NPC npc) {
		return true;
	}

	/*
	 * return process normaly
	 */
	public boolean processNPCClick2(NPC npc) {
		return true;
	}

	public boolean canSummonFamiliar() {
		return true;
	}

	/*
	 * return process normaly
	 */
	public boolean processNPCClick3(NPC npc) {
		return true;
	}

	public boolean canDropItem(Item item) {
		return true;
	}

	/*
	 * return process normaly
	 */
	public boolean processObjectClick2(WorldObject object) {
		return true;
	}

	/*
	 * return process normaly
	 */
	public boolean processObjectClick3(WorldObject object) {
		return true;
	}

	/*
	 * return let default death
	 */
	public boolean sendDeath() {
		return true;
	}

	/*
	 * return can move that step
	 */
	public boolean canMove(int dir) {
		return true;
	}

	/*
	 * return can set that step
	 */
	public boolean checkWalkStep(int lastX, int lastY, int nextX, int nextY) {
		return true;
	}

	/*
	 * return remove controler
	 */
	public boolean login() {
		return true;
	}

	/*
	 * return remove controler
	 */
	public boolean logout() {
		return true;
	}

	public void forceClose() {
	}

	public boolean processItemOnNPC(NPC npc, Item item) {
		return true;
	}

	public boolean handleItemOption1(Player player, int slotId, int itemId, Item item) {
		return true;
	}

	public boolean canWalk() {
		return true;
	}

	public boolean canTakeItem(FloorItem item) {
		return true;
	}

	public boolean canPlayerOption2(Player target) {
		return true;
	}

	public boolean canPlayerOption3(Player target) {
		return true;
	}

	public boolean canPlayerOption4(Player target) {
		return true;
	}

	public boolean processItemOnPlayer(Player p2, Item item) {
		return true;
	}

	public boolean addWalkStep(int lastX, int lastY, int nextX, int nextY) {
		return true;
	}

	public void processNPCDeath(int id) {

	}

	public boolean processObjectClick4(WorldObject object) {//TODO implement
		return true;
	}

	public boolean processObjectClick5(WorldObject object) {
		return true;
	}
}
