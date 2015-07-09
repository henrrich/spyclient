package com.magine.spy.spyclient.collector;

import java.util.List;

import com.magine.spy.spyclient.collector.impl.MultipleLogFileCollector;

/**
 * Factory class to create log collector instances
 */
public class LogCollectorFactory {

	/**
	 * Create a MultipleLogFileCollector instance
	 * 
	 * @param logPrefix
	 *            String prefix of log achieve
	 * @param logs
	 *            List<String> a list of log file path to be collected
	 * @return ILogCollector an instance of MultipleLogFileCollector log
	 *         collector
	 */
	public static ILogCollector createMultipleLogFileCollector(String logPrefix, List<String> logs) {
		return new MultipleLogFileCollector(logPrefix, logs);
	}

}
