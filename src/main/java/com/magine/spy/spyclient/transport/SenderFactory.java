package com.magine.spy.spyclient.transport;

/**
 * Factory class to create sender instance
 */
public class SenderFactory {

	/**
	 * Create a sender instance to send collected log achieve to server via TCP
	 * socket
	 * 
	 * @param filename
	 *            String filename of the collected log achieve to be sent
	 * @param destIp
	 *            String server IP
	 * @param destPort
	 *            int server port
	 * @return Sender sender instance
	 */
	public static Sender createSender(final String filename, String destIp, int destPort) {
		return new Sender(filename, destIp, destPort);
	}

}
