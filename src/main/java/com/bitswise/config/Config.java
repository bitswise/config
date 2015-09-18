package com.bitswise.config;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.bitswise.config.delta.ConfigInfo;

public class Config extends Entity {

	private String parentId = Void.TYPE.toString();
	private Map<String, ConfigEntry> entries;
	private Map<String, Config> configs;

	/**
	 * initial state
	 * TO DO: make this a package protected constructor
	 */
	public Config() {
		this(Void.TYPE.toString());
	}
	
	public Config(String name) {
		super(name, name);
		configs = new HashMap<String, Config>();
		entries =  new HashMap<String, ConfigEntry>();
	}

	public Config(Config container, String name) {
		super(container.getId() + "/" + name, name);
		parentId = container.getId();
		configs = new HashMap<String, Config>();
		entries =  new HashMap<String, ConfigEntry>();
		container.putConfig(this);
	}
	
	/**
	 * Deserialization constructor
	 * 
	 * @param info
	 */
	public Config(ConfigInfo info) {
		super(info.getId(), info.getName());
		parentId = info.getParentId();
		configs = new HashMap<String, Config>();
		entries =  new HashMap<String, ConfigEntry>();
	}

	/**
	 * Copy constructor
	 * 
	 * @param config
	 */
	public Config(Config config) {
		super(config.getId(), config.getName());
		parentId = config.getParentId();
		configs = new HashMap<String, Config>();
		for (Config rhsConfig : config.configs.values()) {
			Config lhsConfig = new Config(rhsConfig);
			configs.put(lhsConfig.getId(), lhsConfig);
		}
		entries =  new HashMap<String, ConfigEntry>();
		for (ConfigEntry rhsConfigEntry : config.entries.values()) {
			ConfigEntry lhsConfigEntry = new ConfigEntry(rhsConfigEntry);
			entries.put(lhsConfigEntry.getId(), lhsConfigEntry);
		}
	}
	
	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	
	public Config getConfig(String id) {
		return configs.get(id);
	}

	public Config getConfigByName(String name) {
		for (Config c : configs.values()) {
			if (c.getName().equals(name)) {
				return c;
			}
		}
		return null;
	}

	public Config findConfig(String id) {
		if (getId().equals(id)) {
			return this;
		}
		Config result = configs.get(id);
		if (result == null) {
			for (Config c : configs.values()) {
				result = c.findConfig(id);
				if (result != null) {
					break;
				}
			}
		}
		return result;
	}
	
	public Collection<Config> getAllConfigs() {
		return new ArrayList<Config>(configs.values());
	}

	public Config putConfig(Config config) {
		return configs.put(config.getId(), config);
	}

	public Config removeConfig(Config config) {
		return configs.remove(config.getId());
	}

	public Config removeConfig(String configId) {
		return configs.remove(configId);
	}
	
	public Config removeConfigByName(String name) {
		for (Config c : configs.values()) {
			if (c.getName().equals(name)) {
				return configs.remove(c.getId());
			}
		}
		return null;
	}


	public ConfigEntry getConfigEntry(String id) {
		return entries.get(id);
	}
	
	public ConfigEntry getConfigEntryByName(String name) {
		for (ConfigEntry ce : entries.values()) {
			if (ce.getName().equals(name)) {
				return ce;
			}
		}
		return null;
	}

	public ConfigEntry findConfigEntry(String id) {
		ConfigEntry result = getConfigEntry(id);
		if (result == null) {
			for (Config c : configs.values()) {
				result = c.findConfigEntry(id);
				if (result != null) {
					break;
				}
			}
		}
		return result;
	}
	
	public Collection<ConfigEntry> getAllConfigEntries() {
		return new ArrayList<ConfigEntry>(entries.values());
	}
	
	public ConfigEntry putConfigEntry(ConfigEntry configEntry) {
		return entries.put(configEntry.getId(), configEntry);
	}

	public ConfigEntry removeConfigEntry(ConfigEntry configEntry) {
		return entries.remove(configEntry.getId());
	}

	public ConfigEntry removeConfigEntry(String id) {
		return entries.remove(id);
	}

	public ConfigEntry removeConfigEntryByName(String name) {
		for (ConfigEntry ce : entries.values()) {
			if (ce.getName().equals(name)) {
				return entries.remove(ce.getId());
			}
		}
		return null;	
	}

	public void write(Map<String, Entity> map) {
		map.put(getId(), this);
		for (ConfigEntry entry : entries.values()) {
			map.put(entry.getId(), entry);
		}
		for (Config config : configs.values()) {
			config.write(map);
		}
	}
		

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((configs == null) ? 0 : configs.hashCode());
		result = prime * result + ((entries == null) ? 0 : entries.hashCode());
		result = prime * result
				+ ((parentId == null) ? 0 : parentId.hashCode());
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
		Config other = (Config) obj;
		if (configs == null) {
			if (other.configs != null)
				return false;
		} else if (!configs.equals(other.configs))
			return false;
		if (entries == null) {
			if (other.entries != null)
				return false;
		} else if (!entries.equals(other.entries))
			return false;
		if (parentId == null) {
			if (other.parentId != null)
				return false;
		} else if (!parentId.equals(other.parentId))
			return false;
		return true;
	}
	
	public boolean shallowEquals(Object obj) {
		if (this == obj)
			return true;
		if (!super.shallowEquals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		Config other = (Config) obj;
		if (parentId == null) {
			if (other.parentId != null)
				return false;
		} else if (!parentId.equals(other.parentId))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Config [parentId=" + getParentId() + ", id=" + getId() + ", name=" + getName()
				+ ", entries=" + entries + ", configs=" + configs + "]";
	}
}
