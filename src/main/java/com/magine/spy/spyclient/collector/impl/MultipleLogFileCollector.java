package com.magine.spy.spyclient.collector.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.zip.ZipOutputStream;

import com.magine.spy.spyclient.collector.ILogCollector;
import com.magine.spy.spyclient.config.ConfigManager;
import com.magine.spy.spyclient.util.ZipUtil;

/**
 * MultipleLogFileCollector log collector will achieve all specified log files
 * into a zipped file
 */
public class MultipleLogFileCollector implements ILogCollector {

	private List<String> logsToCollect;
	private String logPrefix;
	private String logFilename;

	/**
	 * Constructor
	 * 
	 * @param prefix
	 *            String prefix of the log achieve filename
	 * @param logs
	 *            List<String> list of files to be collected
	 */
	public MultipleLogFileCollector(String prefix, List<String> logs) {
		super();
		this.logsToCollect = logs;
		this.logPrefix = prefix;
	}

	@Override
	public boolean collect() {
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");

		logFilename = ConfigManager.getInstance().getWorkspace() + File.separator + this.logPrefix + "_"
				+ format.format(new Date()) + ".zip";

		try {

			ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(logFilename));

			for (String filename : logsToCollect) {
				File logFile = new File(filename);
				if (!logFile.exists()) {
					System.err.println("Log file " + filename + " not found!");
					continue;
				}

				ZipUtil.addToZipFile(logFile, zos);
			}

			zos.close();

		} catch (IOException e) {
			System.err.println("Failed to create zip file" + logFilename);
			return false;
		}

		return true;

	}

	@Override
	public String getLogFileName() {
		return logFilename;
	}

	@Override
	public String getLogPrefix() {
		return logPrefix;
	}

}
