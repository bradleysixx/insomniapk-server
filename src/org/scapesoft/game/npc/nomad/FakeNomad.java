package org.scapesoft.game.npc.nomad;

import org.scapesoft.game.Hit;
import org.scapesoft.game.WorldTile;
import org.scapesoft.game.npc.NPC;

public class FakeNomad extends NPC {

	/**
	 *
	 */
	private static final long serialVersionUID = 1439177820906728935L;
	private Nomad nomad;

	public FakeNomad(WorldTile tile, Nomad nomad) {
		super(8529, tile, -1, true, true);
		this.nomad = nomad;
		setForceMultiArea(true);
	}

	@Override
	public void handleIngoingHit(Hit hit) {
		nomad.destroyCopy(this);
	}

}
