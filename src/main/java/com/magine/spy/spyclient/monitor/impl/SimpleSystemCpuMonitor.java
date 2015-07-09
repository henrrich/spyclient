package com.magine.spy.spyclient.monitor.impl;

import java.lang.management.ManagementFactory;
import com.magine.spy.spyclient.monitor.IMonitorListener;
import com.magine.spy.spyclient.monitor.Monitor;
import com.sun.management.OperatingSystemMXBean;

/**
 * Monitor that monitors the system CPU load. If the system CPU load is above
 * the specified threshold, it will invoke the onSystemUnhealthy callback method
 * of the listener.
 */
public class SimpleSystemCpuMonitor extends Monitor {

	private double threshold;

	/**
	 * Constructor
	 * 
	 * @param int
	 *            interval health check interval in seconds
	 * @param threshold
	 *            double threshold of system CPU load, 0 <= threshold <= 1
	 */
	public SimpleSystemCpuMonitor(int interval, double threshold) {
		super(interval);
		this.threshold = threshold;
	}

	/**
	 * Constructor
	 * 
	 * @param int
	 *            interval health check interval in seconds
	 * @param threshold
	 *            double threshold of system CPU load, 0 <= threshold <= 1
	 * @param listener
	 *            IMonitorListener listener instance
	 */
	public SimpleSystemCpuMonitor(int interval, double threshold, IMonitorListener listener) {
		super(interval, listener);
		this.threshold = threshold;
	}

	@Override
	public boolean isHealthy() {

		OperatingSystemMXBean bean = (com.sun.management.OperatingSystemMXBean) ManagementFactory
				.getOperatingSystemMXBean();

		// sample system CPU load
		double cpuLoad = bean.getSystemCpuLoad();
		
		System.out.println("Current CPU load:" + cpuLoad);

		if (cpuLoad >= this.threshold) {
			return false;
		}

		return true;
	}

}
