package com.guru.utils;


/**
 * @author Gopal Kk
 *
 */
public interface FileConstants {

	public static final int BUCKETSIZE = 100; // Bucket size to split the input file
	
	public static final String DELIMITER = ","; // Delimiter for the file

	public static final String MASTERFILE = "./resources/file-1.csv"; //Master file name to be given as input
	public static final String CHILDFILE = "./resources/file-2.csv"; //Child file name to be given as input

	public static final String TEMPFILE = "./resources/temp.csv"; //Temp file created to verify against the empty bucket

	public static final String OUTPUTFILE = "./resources/output.txt";//Output file where the merged file contents are to be written

	public static final String MASTERSPLITFILESPATH = "./resources/master/";// Path to where the master file to split into
	public static final String CHILDSPLITFILESPATH = "./resources/child/";// Path to where the child file to split into
}
