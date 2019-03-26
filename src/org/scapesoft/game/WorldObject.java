package org.scapesoft.game;

import java.util.concurrent.ConcurrentHashMap;

import org.scapesoft.cache.loaders.ObjectDefinitions;

public class WorldObject extends WorldTile {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3376600248127097230L;
	private int id;
	private int type;
	private int rotation;
	private int life;
	private transient ConcurrentHashMap<Object, Object> attributes;

	public WorldObject(int id, int type, int rotation, WorldTile tile) {
		super(tile.getX(), tile.getY(), tile.getPlane());
		this.id = id;
		this.type = type;
		this.rotation = rotation;
		this.life = 1;
		this.attributes = new ConcurrentHashMap<Object, Object>();
	}

	public WorldObject(int id, int type, int rotation, int x, int y, int plane) {
		super(x, y, plane);
		this.id = id;
		this.type = type;
		this.rotation = rotation;
		this.life = 1;
		this.attributes = new ConcurrentHashMap<Object, Object>();
	}

	public WorldObject(int id, int type, int rotation, int x, int y, int plane, int life) {
		super(x, y, plane);
		this.id = id;
		this.type = type;
		this.rotation = rotation;
		this.life = life;
		this.attributes = new ConcurrentHashMap<Object, Object>();
	}

	public WorldObject(WorldObject object) {
		super(object.getX(), object.getY(), object.getPlane());
		this.id = object.id;
		this.type = object.type;
		this.rotation = object.rotation;
		this.life = object.life;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getType() {
		return type;
	}

	public int getRotation() {
		return rotation;
	}

	public void setRotation(int rotation) {
		this.rotation = rotation;
	}

	public int getLife() {
		return life;
	}

	public void setLife(int life) {
		this.life = life;
	}

	public void decrementObjectLife() {
		this.life--;
	}

	public ObjectDefinitions getDefinitions() {
		return ObjectDefinitions.getObjectDefinitions(id);
	}

	public ConcurrentHashMap<Object, Object> getAttributes() {
		return attributes;
	}

}
