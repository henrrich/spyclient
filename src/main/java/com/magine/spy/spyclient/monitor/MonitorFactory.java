package com.magine.spy.spyclient.monitor;

import com.magine.spy.spyclient.monitor.impl.SimpleSystemCpuMonitor;

/**
 * Factory class to create monitor instances
 */
public class MonitorFactory {

	/**
	 * Create a SimpleSystemCpuMonitor instance
	 * 
	 * @param interval
	 *            int health check interval in seconds
	 * @param threshold
	 *            double threshold value for the system state to be monitored,
	 *            if exceeded, system unhealthy event will be triggered
	 * @return Monitor an instance of SimpleSystemCpuMonitor class
	 */
	public static Monitor createSimpleSystemCpuMonitor(int interval, double threshold) {
		return new SimpleSystemCpuMonitor(interval, threshold);
	}

}
