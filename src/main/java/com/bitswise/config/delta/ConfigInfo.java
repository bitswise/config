package com.bitswise.config.delta;

import com.bitswise.config.Entity;

public class ConfigInfo extends Entity {
	
	private String parentId;
	
	public ConfigInfo(String parentId, String id, String name) {
		super(id, name);
		this.parentId = parentId;
	}

	public String getParentId() {
		return parentId;
	}

	@Override
	public String toString() {
		return "ConfigInfo [parentId=" + parentId + ", id=" + getId()
				+ ", name=" + getName() + "]";
	}

}
