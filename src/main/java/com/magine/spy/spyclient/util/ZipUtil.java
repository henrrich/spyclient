package com.magine.spy.spyclient.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Util class for achieving collector log files into a zipped file
 */
public class ZipUtil {

	/**
	 * Add a file into zipped file
	 * 
	 * @param log
	 *            File file to be added into zipped file
	 * @param out
	 *            ZipOutputStream output stream to the zipped file
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static void addToZipFile(File log, ZipOutputStream out) throws FileNotFoundException, IOException {

		String filename = log.getName();
		
		System.out.println("Writing '" + filename + "' to zip file");

		FileInputStream input = new FileInputStream(log);
		ZipEntry zipEntry = new ZipEntry(filename);
		out.putNextEntry(zipEntry);

		byte[] bytes = new byte[1024];
		int length;
		while ((length = input.read(bytes)) >= 0) {
			out.write(bytes, 0, length);
		}

		out.closeEntry();
		input.close();
	}

}
