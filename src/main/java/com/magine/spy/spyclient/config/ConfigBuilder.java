package com.magine.spy.spyclient.config;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.HierarchicalConfiguration;
import org.apache.commons.configuration.SubnodeConfiguration;
import org.apache.commons.configuration.XMLConfiguration;

import com.magine.spy.spyclient.collector.ILogCollector;
import com.magine.spy.spyclient.collector.LogCollectorFactory;
import com.magine.spy.spyclient.config.ConfigManager.MonitorType;
import com.magine.spy.spyclient.monitor.Monitor;
import com.magine.spy.spyclient.monitor.MonitorFactory;

/**
 * Util class to construct corresponding monitor and log collector instances
 * from xml configuration loaded in the memory
 */
public class ConfigBuilder {

	private static final String CONFIG_SERVER_IP = "server_ip";
	private static final String CONFIG_SERVER_PORT = "server_port";
	private static final String CONFIG_WORKSPACE = "workspace";

	private static final String CONFIG_MONITORS = "monitors.monitor";
	private static final String CONFIG_MONITOR_TYPE = "type";
	private static final String CONFIG_MONITOR_INTERVAL = "interval";
	private static final String CONFIG_MONITOR_THRESHOLD = "threshold";

	private static final String CONFIG_COLLECTOR = "collector";
	private static final String CONFIG_COLLECTOR_LOG_PREFIX = "log_prefix";
	private static final String CONFIG_COLLECTOR_FILES = "files.file";
	private static final String CONFIG_COLLECTOR_FILE = "file";

	/**
	 * Get server IP from xml configuration
	 * 
	 * @param root
	 *            XMLConfiguration xml root configuration
	 * @return String server IP address specified in xml configuration file
	 */
	public static String getServerIp(XMLConfiguration root) {
		return root.getString(CONFIG_SERVER_IP);
	}

	/**
	 * Get server port from xml configuration
	 * 
	 * @param root
	 *            XMLConfiguration xml root configuration
	 * @return String server port specified in xml configuration file
	 */
	public static int getServerPort(XMLConfiguration root) {
		return root.getInt(CONFIG_SERVER_PORT);
	}

	/**
	 * Get client workspace from xml configuration, where collected log file
	 * will be stored
	 * 
	 * @param root
	 *            XMLConfiguration xml root configuration
	 * @return String workspace path
	 */
	public static String getWorkspace(XMLConfiguration root) {
		return root.getString(CONFIG_WORKSPACE);
	}

	/**
	 * Construct all monitor instances from xml configuration
	 * 
	 * @param root
	 *            XMLConfiguration xml root configuration
	 * @return List<Monitor> list of monitor instances
	 * @throws ConfigurationException
	 */
	public static List<Monitor> constructMonitors(XMLConfiguration root) throws ConfigurationException {
		List<Monitor> monitors = new ArrayList<Monitor>();
		List<HierarchicalConfiguration> monitorsConfig = root.configurationsAt(CONFIG_MONITORS);
		for (HierarchicalConfiguration monitorConfig : monitorsConfig) {
			monitors.add(constructMonitor(monitorConfig));
		}

		return monitors;
	}

	private static Monitor constructMonitor(HierarchicalConfiguration monitorConfig) throws ConfigurationException {
		Monitor monitor = null;

		String typeStr = monitorConfig.getString(CONFIG_MONITOR_TYPE);
		MonitorType type = MonitorType.valueOf(typeStr);
		switch (type) {
		case SIMPLE_SYSTEM_CPU_MONITOR:
			int interval = monitorConfig.getInt(CONFIG_MONITOR_INTERVAL);
			double threshold = monitorConfig.getDouble(CONFIG_MONITOR_THRESHOLD);
			monitor = MonitorFactory.createSimpleSystemCpuMonitor(interval, threshold);
			break;
		default:
			throw new ConfigurationException("Invalid monitor type " + typeStr);
		}

		return monitor;
	}

	/**
	 * Construct a log collector instance from xml configuration
	 * 
	 * @param root
	 *            XMLConfiguration xml root configuration
	 * @return ILogCollector log collector instance
	 */
	public static ILogCollector constructLogCollector(XMLConfiguration root) {
		SubnodeConfiguration collectorConfig = root.configurationAt(CONFIG_COLLECTOR);

		String logPrefix = collectorConfig.getString(CONFIG_COLLECTOR_LOG_PREFIX);

		List<HierarchicalConfiguration> logFilesConfig = collectorConfig.configurationsAt(CONFIG_COLLECTOR_FILES);
		List<String> files = new ArrayList<String>();
		for (HierarchicalConfiguration fileConfig : logFilesConfig) {
			files.add(fileConfig.getRootNode().getValue().toString());
		}

		return LogCollectorFactory.createMultipleLogFileCollector(logPrefix, files);
	}

}
