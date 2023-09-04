package com.jpmc.service;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Set;

import com.jpmc.utils.FileConstants;

/**
 * @author Gopal Kk
 *
 */
public class FileMerge {
	private File masterFile = null;
	private File childFile = null;
	private int bucketSize = 100; //default capacity

	public FileMerge(String masterFileName, String childFileName) {
		masterFile = new File(masterFileName);
		if(!masterFile.exists()){
			throw new IllegalStateException("Master File with name doesn't exists:"+ masterFileName);			
		}

		childFile = new File(childFileName);
		if(!childFile.exists()){
			throw new IllegalStateException("Child File with name doesn't exists:"+ childFileName);			
		}
		
		bucketSize = FileConstants.BUCKETSIZE;
	}

	public void mergeFiles() throws IOException  {
		Set<String> set = FileSplitter.getFilesSet();
		Iterator<String> itr = set.iterator();
		while (itr.hasNext()) {
			compareFiles(itr.next().toString());
		}
	}

	public void compareFiles(String args) throws IOException {
		String master = FileConstants.MASTERSPLITFILESPATH + args;
		String child = FileConstants.CHILDSPLITFILESPATH + args;

		boolean success = FileCompare.compareFiles(master, child);
		if(success){
			System.out.println("File has been compared and merged successfully:" + args);
		}
	}

	public void splitFiles() throws IOException  {
		System.out.println("****************Splitting Master File");
		FileSplitter.splitFile("master", bucketSize, masterFile, 3);
		System.out.println("****************Splitting Child File");
		FileSplitter.splitFile("child", bucketSize, childFile,3);
		System.out.println("****************Started Merging File");
		mergeFiles();
	}
}