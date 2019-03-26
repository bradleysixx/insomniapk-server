package org.scapesoft.networking.codec;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.scapesoft.networking.Session;

public abstract class Encoder {
	
	protected Session session;
	protected Map<Integer, Integer> requests;

	public Encoder(Session session) {
		this.session = session;
		this.requests = Collections.synchronizedMap(new HashMap<Integer, Integer>());
	}

	public Map<Integer, Integer> getRequests() {
		if (requests == null)
			this.requests = Collections.synchronizedMap(new HashMap<Integer, Integer>());
		return requests;
	}
}
