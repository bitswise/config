package com.bitswise.config.delta;

import java.util.Map;

import com.bitswise.config.DeltaRootKey;
import com.bitswise.config.Entity;

public class Delta {

	private String entityId;
	private String version;
	private String priorVersion;
	
	private Map<DeltaRootKey,  Map<String, Entity>> delta;

	public Delta() {
		// complete
	}

	public String getId() {
		return getEntityId() + "@" + getVersion();
	}

	public String getEntityId() {
		return entityId;
	}

	public void setEntityId(String entityId) {
		this.entityId = entityId;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getPriorVersion() {
		return priorVersion;
	}

	public void setPriorVersion(String priorVersion) {
		this.priorVersion = priorVersion;
	}

	public Map<DeltaRootKey, Map<String, Entity>> getDelta() {
		return delta;
	}

	public void setDelta(Map<DeltaRootKey, Map<String, Entity>> delta) {
		this.delta = delta;
	}
}
