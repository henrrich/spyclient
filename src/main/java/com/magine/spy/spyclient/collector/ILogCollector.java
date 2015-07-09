package com.magine.spy.spyclient.collector;

/**
 * Interface for log collectors. Subclass needs to implement collect method to
 * collect logs
 */
public interface ILogCollector {

	/**
	 * Method to collect logs
	 * 
	 * @return boolean true if log collection is successful, otherwise false
	 */
	boolean collect();

	/**
	 * Method to get the log achieve file name which contains the collected logs
	 * 
	 * @return String filename of achieved log container
	 */
	String getLogFileName();

	/**
	 * Method to get the specified prefix of log achieve which contains the
	 * collected logs
	 * 
	 * @return String filename prefix of the achieved log container
	 */
	String getLogPrefix();

}
