package com.magine.spy.spyclient.config;

import java.util.List;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;

import com.magine.spy.spyclient.collector.ILogCollector;
import com.magine.spy.spyclient.monitor.Monitor;

/**
 * Util class for loading xml configuration into memory. By default, the file
 * spyclient_config.xml file in classpath will be used. -Dconfig vm argument can
 * be used to specify another xml config file.
 */
public class ConfigManager {

	private static final String CONFIG_FILENAME = "spyclient_config.xml";
	private static final String PROPERTY_CONFIG = "config";

	/**
	 * Enum type of monitors
	 */
	public enum MonitorType {
		/**
		 * monitor that monitors the system cpu load
		 */
		SIMPLE_SYSTEM_CPU_MONITOR
	}

	private static ConfigManager instance;

	private String serverIp;
	private int serverPort;
	private String workspace;
	private List<Monitor> monitors;
	private ILogCollector collector;

	private ConfigManager() {
		try {
			load();
		} catch (ConfigurationException e) {
			e.printStackTrace();
			System.err.println("Failed to load configuration.");
			System.exit(1);
		}
	}

	/**
	 * Get singleton instance of ConfigManager class
	 * 
	 * @return ConfigManager singleton instance of ConfigManager class
	 */
	public static ConfigManager getInstance() {
		if (instance == null) {
			synchronized (ConfigManager.class) {
				if (instance == null) {
					instance = new ConfigManager();
				}
			}
		}

		return instance;
	}

	private void load() throws ConfigurationException {

		System.out.println("Start loading configuration file.");

		String configFilename = System.getProperty(PROPERTY_CONFIG);
		XMLConfiguration config = null;
		if (configFilename != null) {
			config = new XMLConfiguration(configFilename);
		} else {
			config = new XMLConfiguration(CONFIG_FILENAME);
		}

		this.serverIp = ConfigBuilder.getServerIp(config);
		this.serverPort = ConfigBuilder.getServerPort(config);
		this.workspace = ConfigBuilder.getWorkspace(config);
		this.collector = ConfigBuilder.constructLogCollector(config);
		this.monitors = ConfigBuilder.constructMonitors(config);

		System.out.println("Configuration is loaded.");
	}

	/**
	 * Getter for server IP
	 * 
	 * @return String server IP from xml configuration file
	 */
	public String getServerIp() {
		return this.serverIp;
	}

	/**
	 * Getter for server port
	 * 
	 * @return String server port from xml configuration file
	 */
	public int getServerPort() {
		return this.serverPort;
	}

	/**
	 * Getter for workspace path
	 * 
	 * @return String client workspace path where collected logs are stored
	 */
	public String getWorkspace() {
		return this.workspace;
	}

	/**
	 * Getter for monitors
	 * 
	 * @return List<Monitor> list of monitors constructed from the xml
	 *         configuration
	 */
	public List<Monitor> getMonitors() {
		return monitors;
	}

	/**
	 * Getter for log collector instance
	 * 
	 * @return ILogCollector log collector instance constructed from the xml
	 *         configuration
	 */
	public ILogCollector getCollector() {
		return collector;
	}

}
