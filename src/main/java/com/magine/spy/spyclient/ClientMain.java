package com.magine.spy.spyclient;

/**
 * Main method for client
 */
public class ClientMain {

	public static void main(String[] args) {
		
		System.out.println("Client is started.");
		
		Agent agent = new Agent();
		
		// start all configured monitors
		agent.startMonitors();
		
		agent.waitForAllMonitorsStop();

	}

}
