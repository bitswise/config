package com.bitswise.config.delta;

import java.util.HashMap;
import java.util.Map;

import com.bitswise.config.Config;

public class ConfigDeltaService {
	
	/**
	 * maps config id to a set of its deltas
	 */
	private Map<String, Map<String, Delta>> configDeltas;
	
	public ConfigDeltaService() {
		configDeltas = new HashMap<String, Map<String, Delta>>();
	}

	/**
	 * Add brand new Config instance
	 * 
	 * @param config
	 * @return
	 */
	public Config addConfig(Config config) {
		return null;
	}


	public Config getConfigVersions(String id) {
		return null;
	}

	public Config getConfig(String id, String version) {
		return null;
	}
	
	public Config getLatestConfig(String id) {
		return null;
	}

}
