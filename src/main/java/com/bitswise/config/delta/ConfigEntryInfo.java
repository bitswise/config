package com.bitswise.config.delta;

import com.bitswise.config.Entity;

public class ConfigEntryInfo extends Entity {

	private String parentId;
	private String value;
	
	public ConfigEntryInfo(String parentId, String id, String name, String value) {
		super(id, name);
		this.parentId = parentId;
		this.value = value;
	}

	public String getParentId() {
		return parentId;
	}

	public String getValue() {
		return value;
	}

	@Override
	public String toString() {
		return "ConfigEntyInfo [parentId=" + parentId + ", id=" + 
				getId() + ", name=" + getName() + ", value=" + value + "]";
	}

}
