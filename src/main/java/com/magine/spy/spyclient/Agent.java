package com.magine.spy.spyclient;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import com.magine.spy.spyclient.collector.ILogCollector;
import com.magine.spy.spyclient.config.ConfigManager;
import com.magine.spy.spyclient.monitor.IMonitorListener;
import com.magine.spy.spyclient.monitor.Monitor;
import com.magine.spy.spyclient.monitor.MonitorManager;
import com.magine.spy.spyclient.transport.Sender;
import com.magine.spy.spyclient.transport.SenderFactory;

/**
 * Agent class is the core engine of the client program. It starts all
 * configured monitor threads for listening on different system status. It
 * implements the IMonitorListener interface and provides a callback method
 * onSystemNotHealthy to all monitor threads. In case of any monitor threads
 * detect system in a unhealthy status, the monitor thread will invoke the
 * callback method, so that Agent class can trigger log collection.
 */
public class Agent implements IMonitorListener {

	private boolean hasLogCollectionStarted = false;
	List<Future<?>> futures;

	public void startMonitors() {
		// get all configured monitor instances
		List<Monitor> monitors = ConfigManager.getInstance().getMonitors();

		MonitorManager monitorManager = MonitorManager.getInstance();
		for (Monitor monitor : monitors) {
			// for each monitor instance, add Agent as the listener for "system
			// not healthy" event
			monitor.setListener(this);
			monitorManager.addMonitor(monitor);
		}

		System.out.println("Going to start all monitor threads.");

		futures = MonitorManager.getInstance().startMonitors();
	}
	
	public void waitForAllMonitorsStop() {
		
		// Wait until all monitor threads finish computation
		for (Future<?> f : futures) {
			try {
				f.get();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		// shutdown monitor thread pool
		MonitorManager.getInstance().shutdown();
	}

	@Override
	public void onSystemNotHealthy(Monitor monitor) {

		// critical section for all monitor threads, only the first one detects
		// the error will trigger log collection
		synchronized (this) {
			if (hasLogCollectionStarted) {
				System.out.println("Log collection has already been triggered by other monitor failure.");
				return;
			}

			hasLogCollectionStarted = true;
		}

		System.out.println("Monitor " + monitor.getMonitorName() + " detects not healthy system!");

		// on system not healthy event, start log collection
		System.out.println("Going to start log collection.");

		ILogCollector logCollector = ConfigManager.getInstance().getCollector();
		boolean isLogCollected = logCollector.collect();

		if (isLogCollected) {

			System.out.println("Log collection is done.");

			String logFilename = logCollector.getLogFileName();

			// After log has been collected into specified workspace, send the
			// collected log achieve to server via TCP socket
			boolean isSendingOk = this.sendLogToServer(logFilename);
			if (isSendingOk) {
				
				// if log has been sent to server, shutdown client
				System.out.println("Going to shutdown all monitor threads.");
				try {
					MonitorManager.getInstance().stopMonitors();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} else {
			System.err.println("Failed to collect logs.");
		}
		
		// reset the flag so other monitor thread can trigger log collection later
		hasLogCollectionStarted = false;
	}

	private boolean sendLogToServer(String filename) {

		ConfigManager configManager = ConfigManager.getInstance();

		System.out.println("Going to transfer achieved log " + filename + " to server.");

		Sender sender = SenderFactory.createSender(filename, configManager.getServerIp(),
				configManager.getServerPort());
		try {
			sender.send();
		} catch (UnknownHostException e) {
			System.err.println("Unknown host when trying to send log to server: " + e.getMessage());
			return false;
		} catch (IOException e) {
			System.err.println("Failed to send log to server: " + e.getMessage());
			return false;
		}

		System.out.println("Log transferring is done.");
		return true;
	}

}
