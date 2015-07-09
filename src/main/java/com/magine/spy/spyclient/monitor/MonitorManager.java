package com.magine.spy.spyclient.monitor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Class that controls all the monitor threads as a thread pool.
 */
public class MonitorManager {

	private static MonitorManager instance;

	private ExecutorService monitorExecutor;

	private List<Monitor> monitors;
	private List<Future<?>> futures;

	private MonitorManager() {
		this.futures = new ArrayList<Future<?>>();
		this.monitors = new ArrayList<Monitor>();
		this.monitorExecutor = Executors.newCachedThreadPool();
	}

	/**
	 * Get the singleton instance of the MonitorManager class
	 * 
	 * @return MonitorManager global instance of MonitorManager class
	 */
	public static MonitorManager getInstance() {
		if (instance == null) {
			synchronized (MonitorManager.class) {
				if (instance == null) {
					instance = new MonitorManager();
				}
			}
		}

		return instance;
	}

	/**
	 * Add a monitor to be managed
	 * 
	 * @param monitor
	 *            Monitor a monitor thread to be managed
	 */
	public void addMonitor(Monitor monitor) {
		this.monitors.add(monitor);
	}

	/**
	 * Start all monitor threads in the thread pool
	 * 
	 * @return List<Future<?>> future result of monitor threads
	 */
	public List<Future<?>> startMonitors() {
		for (Monitor m : monitors) {
			futures.add(monitorExecutor.submit(m));
			System.out.println("Starting monitor " + m.getMonitorName() + ".");
		}
		return futures;
	}

	/**
	 * Stop all monitor threads in the thread pool. First, it will try to
	 * shutdown the pool gracefully. If it failed, then it will force all
	 * monitor threads to stop.
	 * 
	 * @throws InterruptedException
	 */
	public void stopMonitors() throws InterruptedException {
		for (Monitor m : monitors) {
			m.stop();
			System.out.println("Stopping monitor " + m.getMonitorName() + ".");
		}
	}

	/**
	 * Gracefully shutdown the monitor thread pool
	 */
	public void shutdown() {
		this.monitorExecutor.shutdown();
	}

}
