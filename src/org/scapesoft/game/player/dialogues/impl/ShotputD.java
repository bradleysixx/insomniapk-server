package org.scapesoft.game.player.dialogues.impl;

import org.scapesoft.game.Animation;
import org.scapesoft.game.player.controlers.Controller;
import org.scapesoft.game.player.controlers.impl.guilds.warriors.WarriorsGuild;
import org.scapesoft.game.player.dialogues.Dialogue;
import org.scapesoft.game.tasks.WorldTask;
import org.scapesoft.game.tasks.WorldTasksManager;

public class ShotputD extends Dialogue {

	private boolean is18LB;

	@Override
	public void start() {
		is18LB = (boolean) this.parameters[0];
		player.setNextAnimation(new Animation(827));
		WorldTasksManager.schedule(new WorldTask() {

			@Override
			public void run() {
				sendOptionsDialogue(DEFAULT_OPTIONS_TI, "Standing Throw.", "Step and throw.", "Spin and throw.");
			}
		});
	}

	@Override
	public void run(int interfaceId, int componentId) {
		Controller controler = player.getControllerManager().getController();
		if (controler == null || !(controler instanceof WarriorsGuild)) {
			end();
			return;
		}
		WarriorsGuild currentGuild = (WarriorsGuild) controler;
		if (componentId == OPTION_1) {
			currentGuild.prepareShotput((byte) 0, is18LB);
			player.setNextAnimation(new Animation(15079));
		} else if (componentId == OPTION_2) {
			currentGuild.prepareShotput((byte) 1, is18LB);
			player.setNextAnimation(new Animation(15080));
		} else {
			currentGuild.prepareShotput((byte) 2, is18LB);
			player.setNextAnimation(new Animation(15078));
		}
		end();
	}

	@Override
	public void finish() {

	}
}
