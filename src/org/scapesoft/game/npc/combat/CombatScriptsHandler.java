package org.scapesoft.game.npc.combat;

import java.util.HashMap;

import org.scapesoft.game.Entity;
import org.scapesoft.game.minigames.akrisae.AkrisaeBrother;
import org.scapesoft.game.npc.NPC;
import org.scapesoft.utilities.misc.FileClassLoader;

public class CombatScriptsHandler {

	private static final HashMap<Object, CombatScript> cachedCombatScripts = new HashMap<Object, CombatScript>();
	private static final CombatScript DEFAULT_SCRIPT = new Default();

	public static final void init() {
		cachedCombatScripts.clear();
		for (Object packet : FileClassLoader.getClassesInDirectory(CombatScriptsHandler.class.getPackage().getName() + ".impl")) {
			CombatScript handler = (CombatScript) packet;
			for (Object parameter : handler.getKeys()) {
				cachedCombatScripts.put(parameter, handler);
			}
		}
	}

	public static int specialAttack(final NPC npc, final Entity target) {
		if (npc instanceof AkrisaeBrother) {
			return cachedCombatScripts.get("akrisae_brother").attack(npc, target);
		}
		CombatScript script = cachedCombatScripts.get(npc.getId());
		if (script == null) {
			script = cachedCombatScripts.get(npc.getDefinitions().getName());
			if (script == null) {
				script = DEFAULT_SCRIPT;
			}
		}
		return script.attack(npc, target);
	}
}
