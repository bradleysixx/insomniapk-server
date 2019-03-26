package org.scapesoft.utilities.game.npc;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.scapesoft.cache.Cache;
import org.scapesoft.cache.loaders.NPCDefinitions;
import org.scapesoft.utilities.console.gson.GsonHandler;
import org.scapesoft.utilities.console.gson.impl.NPCBonuses;
import org.scapesoft.utilities.misc.Utils;

/**
 * @author Lazarus <lazarus.rs.king@gmail.com>
 * @since Jun 29, 2014
 */
public class NPCCombatChecker {

	public static void main(String... args) throws IOException {
		Cache.init();
		GsonHandler.initialize();
		NPCCombatDefinitionsL.init();
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File("defs_missing.txt")));
		for (int i = 0; i < Utils.getNPCDefinitionsSize(); i++) {
			NPCDefinitions definitions = NPCDefinitions.getNPCDefinitions(i);
			if (!definitions.hasAttackOption()) {
				continue;
			}
			boolean missingBonuses = false;
			if (NPCBonuses.getBonuses(i) == null)
				missingBonuses = true;
			
			boolean missingDefinitions = false;
			if (NPCCombatDefinitionsL.getNPCCombatDefinitions(i) == NPCCombatDefinitionsL.DEFAULT_DEFINITION)
				missingDefinitions = true;
			
			if (!missingBonuses && !missingDefinitions)
				continue;
			
			writer.write("[name=" + definitions.getName() + ", id=" + i + "");
			writer.newLine();
			writer.write("\tbonuses=" + (missingBonuses ? "MISSING" : "SAVED") + ", combatDefinitions=" + (missingDefinitions ? "MISSING" : "SAVED") + "]");
			writer.newLine();
		}
		writer.close();
	}
}
