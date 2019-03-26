package org.scapesoft.networking.codec;

import org.scapesoft.networking.Session;
import org.scapesoft.networking.codec.stream.InputStream;

public abstract class Decoder {

	protected Session session;

	public Decoder(Session session) {
		this.session = session;
	}

	public abstract void decode(InputStream stream);

}
