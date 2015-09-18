package com.bitswise.config.delta;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import com.bitswise.config.Config;
import com.bitswise.config.ConfigEntry;
import com.bitswise.config.DeltaRootKey;
import com.bitswise.config.Entity;

public class DeltaWriter {
	
	public Map<DeltaRootKey,  Map<String, Entity>> createDelta(Config oldConfig, 
			Config newConfig) {
		
		Map<DeltaRootKey,  Map<String, Entity>> delta = 
			new HashMap<DeltaRootKey, Map<String, Entity>>();
		Map<String, Entity> upsertedMap = 
			new HashMap<String, Entity>();
		Map<String, Entity> deletedMap = 
			new HashMap<String, Entity>();
		Map<String, Entity> transientDeletedMap = 
			new HashMap<String, Entity>();

		delta.put(DeltaRootKey.UPSERTED, upsertedMap);
		delta.put(DeltaRootKey.DELETED, deletedMap);

		Map<String, Entity> oldMap = new HashMap<String, Entity>();
		Map<String, Entity> newMap = new HashMap<String, Entity>();
		if (oldConfig != null) {
			oldConfig.write(oldMap);
		}
		newConfig.write(newMap);
		for (Entity entity : newMap.values()) {
			Entity oldEntity = oldMap.get(entity.getId());
			if (oldEntity == null) {
				handleUpsertedEntity(entity, upsertedMap);
			} else if (!oldEntity.shallowEquals(entity)) {
				handleUpsertedEntity(entity, upsertedMap);
			}
		}
		for (Entity entity : oldMap.values()) {
			if (newMap.get(entity.getId()) == null) {
				handleDeletedEntity(entity, deletedMap, transientDeletedMap);
			}
		}
		return delta;
	}
	
	protected void handleUpsertedEntity(Entity entity, Map<String, Entity> map) {
		if (entity instanceof Config) {
			Config config = (Config)entity;
			ConfigInfo info = new ConfigInfo(config.getParentId(), config.getId(), config.getName());
			map.put(info.getId(),  info);
		} else {
			ConfigEntry entry = (ConfigEntry)entity;
			ConfigEntryInfo info = new ConfigEntryInfo(entry.getParentId(), entry.getId(), entry.getName(), entry.getValue());
			map.put(info.getId(),  info);
		}
	}

	protected void handleDeletedEntity(Entity entity, Map<String, Entity> map, Map<String, Entity> transientDeletedMap) {
		if (transientDeletedMap.get(entity.getId()) != null) {
			// this entity has its parent or transient parent already recorded as deleted
			return;
		}
		if (entity instanceof Config) {
			Config config = (Config)entity;
			ConfigInfo info = new ConfigInfo(config.getParentId(), config.getId(), config.getName());
			map.put(info.getId(), info);
			// optimization:
			// walk its entries and the subtree that this Config instance was the root of
			// and record its entites ids. If those ids are encountered again in handleDeletedEntity
			// call, then skip their processing as the fact that they are deleted does not require recording
			Stack<Config> configsStack = new Stack<Config>();
			configsStack.push(config);
			while (!configsStack.isEmpty()) {
				Config currentConfig = configsStack.pop();
				transientDeletedMap.put(currentConfig.getId(), currentConfig);
				for (ConfigEntry ce : currentConfig.getAllConfigEntries()) {
					transientDeletedMap.put(ce.getId(), ce);
				}
				for (Config c : currentConfig.getAllConfigs()) {
					configsStack.push(c);
				}
			}
		} else {
			ConfigEntry entry = (ConfigEntry)entity;
			ConfigEntryInfo info = new ConfigEntryInfo(entry.getParentId(), entry.getId(), entry.getName(), entry.getValue());
			map.put(info.getId(),  info);
		}
	}
}
