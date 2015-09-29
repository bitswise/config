package com.bitswise.config.delta;

import java.util.HashMap;
import java.util.Map;

import com.bitswise.config.Config;
import com.bitswise.config.ConfigEntry;
import com.bitswise.config.ConfigVisitor;
import com.bitswise.config.Entity;

public class TreeToMapVisitor implements ConfigVisitor {
	private Map<String, Entity> map;
	
	public TreeToMapVisitor() {
		map = new HashMap<String, Entity>();
	}
	
	@Override
	public boolean visit(Config config) {
		map.put(config.getId(), config);
		for (ConfigEntry entry : config.getAllConfigEntries()) {
			map.put(entry.getId(), entry);
		}
		return true; 
	}
	
	public Map<String, Entity> getMap() {
		return map;
	}

}
