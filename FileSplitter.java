package com.guru.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import com.jpmc.utils.FileConstants;



/**
 * @author Gopal Kk
 *
 */
public class FileSplitter {
	private static long internalBufferSize = 1;
	
	// private static Map<Long, BufferedWriter> writerCache = new HashMap<Long, BufferedWriter>();
	private static Map<Long, List<String>> internalCache = new HashMap<Long, List<String>>();

	public static void splitFile(String prefix, int bucketSize, File file, int splitIndex) {
		System.out.println("{splitFile}[" + prefix + "][" + bucketSize + "][" + file + "][" + splitIndex + "]");
		try (BufferedReader fileReader = new BufferedReader(new FileReader(file))) {
			String record = null;
			while ((record = fileReader.readLine()) != null) {
				String[] fields = record.split(",");
				if (fields.length > splitIndex) {
					placeRecordInBucket(prefix, record, fields[0], bucketSize);
				}
			}
			// --Writing the remaining records to buckets
			writeRecordsIntoBuckets(prefix, bucketSize);
		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		System.out.println("{splitFile}:EXIT");
	}

	private static long tempBufferSize = 0;

	private static void placeRecordInBucket(String prefix, String record, String field, int bucketSize) {
		System.out.println("{placeRecordInBucket}[" + prefix + "][" + record + "][" + field + "][" + bucketSize + "]");
		try {
			long bucketNo = Long.parseLong(field) % bucketSize;
			List<String> records = internalCache.get(bucketNo);
			if (records == null) {
				records = new ArrayList<String>();
			}
			records.add(record);
			if (++tempBufferSize == internalBufferSize) {
				writeRecordsIntoBuckets(prefix, bucketSize);
				tempBufferSize = 0;
				internalCache = new HashMap<Long, List<String>>();
			}
			internalCache.put(bucketNo, records);
		} catch (NumberFormatException ex) {
			System.out.println(field + " is not a valid empid so skipping the record " + record);
		}
		System.out.println("{placeRecordInBucket}:EXIT");
	}
	
	private static void writeRecordsIntoBuckets(String prefix, int bucketSize) {
		System.out.println("{writeRecordsIntoBuckets}[" + prefix + "][" + bucketSize + "]" + "");
		for (int bucketNo = 0; bucketNo < bucketSize; bucketNo++) {
			List<String> records = internalCache.get((long) bucketNo);
			if (records != null && records.size() > 0) {
				writeRecordsIntoFiles(prefix, records, bucketNo); // System.out.println(73);
			}
		}
		System.out.println("{writeRecordsIntoBuckets:Exit}");
	}

	private static void writeRecordsIntoFiles(String prefix, List<String> records, int bucketNo) {
		System.out.println("{writeRecordsIntoFiles}[" + prefix + "][" + records + "][" + bucketNo + "]");
		try {
			String dir = "./resources/" + prefix + "/";
			String fileName = bucketNo+"";
			File file = new File(dir);
			if (!file.exists()) {
				file.mkdirs();
			}
			BufferedWriter writer = new BufferedWriter(new FileWriter(dir + fileName, true));
			for (String record : records) {
				writer.write(record);
				writer.newLine();
			}
			writer.close();
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		System.out.println("{writeRecordsIntoFiles}:Exit");
	}

	public static Set<String> getFilesSet() {
		System.out.println("{getFilesSet:[]}");
		File masterDirectory = new File(FileConstants.MASTERSPLITFILESPATH);
		File childDirectory = new File(FileConstants.CHILDSPLITFILESPATH);
		Set<String> files = new TreeSet<String>();
		File[] masterFileList = masterDirectory.listFiles();
		File[] childFileList = childDirectory.listFiles();
		for (File file : masterFileList) {
			if (file.isFile()) {
				files.add(file.getName());
			}
		}
		for (File file : childFileList) {
			if (file.isFile()) {
				files.add(file.getName());
			}
		}
		//Collections.sort(files);
		System.out.println("getFilesSet:Exit");
		return files;
	}
}
