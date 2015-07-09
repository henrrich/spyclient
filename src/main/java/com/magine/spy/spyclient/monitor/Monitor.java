package com.magine.spy.spyclient.monitor;

import java.util.concurrent.TimeUnit;

/**
 * Abstract base class for all monitor threads. All sub classes need to
 * implement the isHealthy method for detecting system unhealthy state. Default
 * implementation for monitor thread is to periodically check system health and
 * invoke the callback method of IMonitorListener instance in case of unhealthy
 * system state detected.
 */
public abstract class Monitor implements Runnable {

	private int heathCheckInterval = 2;
	private boolean isStop = false;

	private IMonitorListener listener;

	/**
	 * Constructor
	 * 
	 * @param healthCheckInterval
	 *            int health check interval in seconds
	 */
	public Monitor(int healthCheckInterval) {
		super();
		this.heathCheckInterval = healthCheckInterval;
	}

	/**
	 * Constructor
	 * 
	 * @param healthCheckInterval
	 *            int health check interval in seconds
	 * @param IMonitorListener
	 *            listener instance
	 */
	public Monitor(int heathCheckInterval, IMonitorListener listener) {
		this(heathCheckInterval);
		this.listener = listener;
	}

	/**
	 * Getter for health check interval
	 * 
	 * @return healthCheckInterval int health check interval in seconds
	 */
	public int getHeathCheckInterval() {
		return heathCheckInterval;
	}

	/**
	 * Setter for health check interval
	 * 
	 * @param healthCheckInterval
	 *            int health check interval in seconds
	 */
	public void setHeathCheckInterval(int heathCheckInterval) {
		this.heathCheckInterval = heathCheckInterval;
	}

	/**
	 * Setter for listener
	 * 
	 * @param listener
	 *            IMonitorListener listener instance
	 */
	public void setListener(IMonitorListener listener) {
		this.listener = listener;
	}

	/**
	 * Stop the monitor thread
	 */
	public void stop() {
		this.isStop = true;
	}

	/**
	 * Abstract method to be implemented by sub-classes to check system health
	 * state
	 */
	public abstract boolean isHealthy();

	/**
	 * Get for monitor class name
	 * 
	 * @return String class name of the monitor instance
	 */
	public String getMonitorName() {
		return this.getClass().getName();
	}

	@Override
	public void run() {

		System.out.println("Monitor " + getMonitorName() + " started.");

		while (!isStop) {

			boolean isHealthy = isHealthy();

			if (!isHealthy) {
				System.out.println("Monitor " + getMonitorName() + " detected unhealthy state.");
				listener.onSystemNotHealthy(this);
			}

			try {
				TimeUnit.SECONDS.sleep(heathCheckInterval);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		System.out.println("Monitor " + getMonitorName() + " stopped.");
	}

}
