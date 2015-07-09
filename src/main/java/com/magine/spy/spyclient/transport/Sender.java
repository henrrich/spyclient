package com.magine.spy.spyclient.transport;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Sender is responsible for transferring collected log achieve file to server
 * via TCP socket.
 */
public class Sender {

	private String filename;
	private String destIp;
	private int destPort;
	private Socket connection;

	/**
	 * Constructor
	 * 
	 * @param filename
	 *            String filename of the collected log achieve
	 * @param destIp
	 *            String server IP
	 * @param destPort
	 *            int server port
	 */
	public Sender(String filename, String destIp, int destPort) {
		super();
		this.filename = filename;
		this.destIp = destIp;
		this.destPort = destPort;
	}

	public void send() throws UnknownHostException, IOException {

		File f = new File(filename);

		connection = new Socket(this.destIp, this.destPort);

		DataOutputStream out = new DataOutputStream(new BufferedOutputStream(connection.getOutputStream()));
		FileInputStream in = new FileInputStream(f);

		System.out.println("Start sending filename " + f.getName());

		out.writeUTF(f.getName());

		System.out.println("Start sending the content of file " + f.getName());

		byte[] bytes = new byte[1024];

		int count;
		while ((count = in.read(bytes)) > 0) {
			out.write(bytes, 0, count);
		}

		out.flush();

		in.close();
		out.close();
		connection.close();

		System.out.println("End sending file " + f.getName());

	}

	/**
	 * Getter of collector log achieve filename
	 * 
	 * @return String achieved log filename
	 */
	public String getFilename() {
		return filename;
	}

	/**
	 * Setter of collector log achieve filename
	 * 
	 * @param filename
	 *            String achieved log filename
	 */
	public void setFilename(String filename) {
		this.filename = filename;
	}

	/**
	 * Getter of server IP
	 * 
	 * @return String server IP
	 */
	public String getDestIp() {
		return destIp;
	}

	/**
	 * Setter of server IP
	 * 
	 * @param destIp
	 *            String server IP
	 */
	public void setDestIp(String destIp) {
		this.destIp = destIp;
	}

	/**
	 * Getter of server port
	 * 
	 * @return int server port
	 */
	public int getDestPort() {
		return destPort;
	}

	/**
	 * Setter of server port
	 * 
	 * @param destPort
	 *            int server port
	 */
	public void setDestPort(int destPort) {
		this.destPort = destPort;
	}

}
