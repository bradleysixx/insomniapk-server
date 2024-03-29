package org.scapesoft.game.npc.combat;

public class NPCCombatDefinitions {

	public static final int MELEE = 0;
	public static final int RANGE = 1;
	public static final int MAGE = 2;
	public static final int SPECIAL = 3;
	public static final int SPECIAL2 = 4; // follows no distance
	public static final int PASSIVE = 0;
	public static final int AGRESSIVE = 1;

	private int hitpoints;
	private int attackAnim;
	private int defenceAnim;
	private int deathAnim;
	private int attackDelay;
	private int deathDelay;
	private int respawnDelay;
	private int maxHit;
	private int attackStyle;
	private int attackGfx;
	private int attackProjectile;
	private int agressivenessType;

	public NPCCombatDefinitions(int hitpoints, int attackAnim, int defenceAnim, int deathAnim, int attackDelay, int deathDelay, int respawnDelay, int maxHit, int attackStyle, int attackGfx, int attackProjectile, int agressivenessType) {
		this.hitpoints = hitpoints;
		this.attackAnim = attackAnim;
		this.defenceAnim = defenceAnim;
		this.deathAnim = deathAnim;
		this.attackDelay = attackDelay;
		this.deathDelay = deathDelay;
		this.respawnDelay = respawnDelay;
		this.maxHit = maxHit;
		this.attackStyle = attackStyle;
		this.attackGfx = attackGfx;
		this.attackProjectile = attackProjectile;
		this.agressivenessType = agressivenessType;
	}

	public int getRespawnDelay() {
		return respawnDelay;
	}

	public int getDeathEmote() {
		return deathAnim;
	}

	public int getDefenceEmote() {
		return defenceAnim;
	}

	public int getAttackEmote() {
		return attackAnim;
	}

	public int getAttackGfx() {
		return attackGfx;
	}

	public int getAgressivenessType() {
		return agressivenessType;
	}

	public int getAttackProjectile() {
		return attackProjectile;
	}

	public int getAttackStyle() {
		return attackStyle;
	}

	public int getAttackDelay() {
		return attackDelay;
	}

	public int getMaxHit() {
		return maxHit;
	}

	public void setMaxHit(int j) {
		this.maxHit = j;
	}

	public int getHitpoints() {
		return hitpoints;
	}

	public int getDeathDelay() {
		return deathDelay;
	}

	public void setRespawnDelay(int j) {
		this.respawnDelay = j;
	}

	public void setDeathEmote(int j) {
		this.deathAnim = j;
	}

	public void setDefenceEmote(int j) {
		this.defenceAnim = j;
	}

	public void setAttackEmote(int j) {
		this.attackAnim = j;
	}

	public void setAttackGfx(int j) {
		this.attackGfx = j;
	}

	public void setAgressivenessType(int j) {
		this.agressivenessType = j;
	}

	public void setAttackProjectile(int j) {
		this.attackProjectile = j;
	}

	public void setAttackStyle(int j) {
		this.attackStyle = j;
	}

	public void setAttackDelay(int j) {
		this.attackDelay = j;
	}

	public void setHitpoints(int j) {
		this.hitpoints = j;
	}

	public void setDeathDelay(int j) {
		this.deathDelay = j;
	}
}
