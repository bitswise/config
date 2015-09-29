package com.bitswise.config.delta;

import java.util.Map;

import com.bitswise.config.Config;
import com.bitswise.config.ConfigEntry;
import com.bitswise.config.DeltaRootKey;
import com.bitswise.config.Entity;

public class DeltaReader {
	
	public Config applyDelta(Map<DeltaRootKey,  Map<String, Entity>> delta, Config oldConfig) {
		
		Map<String, Entity> upsertedMap = delta.get(DeltaRootKey.UPSERTED);
		Map<String, Entity> deletedMap = delta.get(DeltaRootKey.DELETED);
		
		TreeToMapVisitor visitor = new TreeToMapVisitor();
		Config newConfig;
		if (oldConfig != null) {
			oldConfig.accept(visitor);
			// create a new config instance, which starts as a clone of the old config
			newConfig = new Config(oldConfig);
		} else {
			// create a new config instance, which starts off in its initial state
			newConfig = new Config();
		}
		Map<String, Entity> oldMap = visitor.getMap();
		
		for (Entity entity : upsertedMap.values()) {
			Entity oldEntity = oldMap.get(entity.getId());
			if (oldEntity == null) {
				handleUpsertedEntity(entity, newConfig, upsertedMap);
			} else if (!oldEntity.shallowEquals(entity)) {
				handleUpsertedEntity(entity, newConfig, upsertedMap);
			} else {
				throw new IllegalStateException("Config to which the delta is being " +
						"applied has a entity that equals to a delta entity declared as upserted, " +
						"wchich must not happen");
			}
		}
		for (Entity entity : deletedMap.values()) {
			if (oldMap.get(entity.getId()) != null) {
				handleDeletedEntity(entity, newConfig);
			} else {
				throw new IllegalStateException("Config to which the delta is being " +
						"applied does not have a entity which is declated deleted entity in delta, " +
						"wchich must not happen");
			}
		}
		return newConfig;
	}
	
	protected void handleUpsertedEntity(Entity entity, Config newConfig, Map<String, Entity> delta) {
		if (entity instanceof ConfigInfo) {
			ConfigInfo configInfo = (ConfigInfo)entity;
			if (configInfo.getParentId().equals(Void.TYPE.toString())) {
				// special case - root Config instance
				newConfig.setId(configInfo.getId());
				newConfig.setName(configInfo.getName());
				return;
			} 
			Config parentConfig = newConfig.findConfig(configInfo.getParentId());
			if (parentConfig == null) {
				// old config does not have the parent config,
				// so walk up the parent path resolving parents 
				// (create or get from delta config info) recursively
				Entity parentEntity = delta.get(configInfo.getParentId());
				assert parentEntity != null;
				assert parentEntity instanceof ConfigInfo;
				handleUpsertedEntity(parentEntity, newConfig, delta);
				// by now new config must have parent instance 
				// so just find it again, then construct new config and let parent now about it
				parentConfig = newConfig.findConfig(configInfo.getParentId());
				assert parentConfig != null;
				parentConfig.putConfig(new Config(configInfo));
			} else {
				// parent config already exists, 
				// find if it already has config instance for this ConfigInfo
				Config config = parentConfig.getConfig(configInfo.getId());
				if (config != null) {
					// if so handle its potential name chance, its id is the same
					// and its children may has already been modified by this code
					config.setName(configInfo.getName());
				} else {
					// just construct a branch new Config instance
					parentConfig.putConfig(new Config(configInfo));
				}
			}
		} else {
			ConfigEntryInfo configEntryInfo = (ConfigEntryInfo)entity;
			Config parentConfig = newConfig.findConfig(configEntryInfo.getParentId());
			if (parentConfig == null) {
				// old config does not have the parent config,
				// so walk up the parent path resolving parents 
				// (create or get from old config) recursively
				Entity parentEntity = delta.get(configEntryInfo.getParentId());
				assert parentEntity != null;
				assert parentEntity instanceof ConfigInfo;
				handleUpsertedEntity(parentEntity, newConfig, delta);
			}
			// by now old config must have parent instance 
			// so just find it again if need be, then construct new config entry and let parent now about it
			if (parentConfig == null) {
				parentConfig = newConfig.findConfig(configEntryInfo.getParentId());
			}
			assert parentConfig != null;
			parentConfig.putConfigEntry(new ConfigEntry(configEntryInfo));
		}
	}
	
	protected void handleDeletedEntity(Entity entity, Config newConfig) {
		if (entity instanceof ConfigInfo) {
			ConfigInfo configInfo = (ConfigInfo)entity;
			newConfig.removeConfig(configInfo.getId());
		} else {
			ConfigEntryInfo configEntryInfo = (ConfigEntryInfo)entity;
			newConfig.removeConfigEntry(configEntryInfo.getId());
		}
	}
}
