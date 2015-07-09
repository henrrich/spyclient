package com.magine.spy.spyclient.monitor;

/**
 * Interface defining the listener for events triggered from monitor threads
 */
public interface IMonitorListener {

	/**
	 * Callback method to be invoked by the monitor thread which detects the
	 * system in an unhealthy state
	 * 
	 * @param monitor
	 *            Monitor instance of monitor thread
	 */
	void onSystemNotHealthy(Monitor monitor);

}
