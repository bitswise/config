package com.bitswise.config;

import com.bitswise.config.delta.ConfigEntryInfo;

public class ConfigEntry extends Entity {

	private String parentId;
	private String value;
	
	public ConfigEntry(Config container, String name, String value) {
		super(container.getId() + "/" + name, name);
		this.parentId = container.getId();
		this.value = value;
	}

	
	/**
	 * Deserialization constructor
	 * 
	 * @param entryInfo
	 */
	public ConfigEntry(ConfigEntryInfo entryInfo) {
		super(entryInfo.getId(), entryInfo.getName());
		this.parentId = entryInfo.getParentId();
		this.value = entryInfo.getValue();
	}

	/**
	 * Copy constructor
	 * 
	 * @param container
	 */
	public ConfigEntry(ConfigEntry entry) {
		super(entry.getId(), entry.getName());
		this.parentId = entry.getParentId();
		this.value = entry.getValue();
	}
	
	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "ConfigEntry [parentId=" + getParentId() + 
				", id=" + getId() + ", name=" + getName() + 
				", value=" + getValue()  + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ ((parentId == null) ? 0 : parentId.hashCode());
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		ConfigEntry other = (ConfigEntry) obj;
		if (parentId == null) {
			if (other.parentId != null)
				return false;
		} else if (!parentId.equals(other.parentId))
			return false;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}
	
	public boolean shallowEquals(Object obj) {
		return equals(obj);
	}

}
