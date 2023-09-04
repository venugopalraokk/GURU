package com.guru.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import com.jpmc.info.EmployeeInfo;
import com.jpmc.utils.FileConstants;

/**
 * @author Gopal Kk
 *
 */
public class FileCompare {
	
	private static int index = 0;
	private static final String CONT_COMMA = ",";

	public static boolean compareFiles(String master, String child) throws IOException {
		
		String delimiter = FileConstants.DELIMITER;
		BufferedReader masterReader = null;
		BufferedReader childReader = null;

		try {
			masterReader = new BufferedReader(new FileReader(master));
		} catch(FileNotFoundException fnfe) {
			System.out.println("MasterFileNotFound_Writing from temp file");
			masterReader = new BufferedReader(new FileReader(FileConstants.TEMPFILE));
		}
		try {
			childReader = new BufferedReader(new FileReader(child));
		} catch(FileNotFoundException fnfe) {
			System.out.println("ChildFileNotFound_Writing from temp file");
			childReader = new BufferedReader(new FileReader(FileConstants.TEMPFILE));
		}

		Map<Integer, EmployeeInfo> masterData = new TreeMap<Integer, EmployeeInfo>();
		Map<Integer, EmployeeInfo> childData = new TreeMap<Integer, EmployeeInfo>();
		Set<Integer> allKeys = new TreeSet<Integer>();

		String currentLine = null;
		currentLine = masterReader.readLine();
		while (currentLine != null) {
			String[] employeeDetail = currentLine.split(delimiter);
			int employeeId = Integer.valueOf(employeeDetail[0]);
			String firstName = employeeDetail[1];
			String lastName = employeeDetail[2];
			String designation = employeeDetail[3];
			allKeys.add(employeeId);
			masterData.put(employeeId, new EmployeeInfo(employeeId, firstName, lastName, designation));
			currentLine = masterReader.readLine();
		}
		try {
			if(masterReader != null) {
				masterReader.close();
				masterReader = null;
			}
		} catch(IOException ioe) {
			masterReader = null;
		}

		currentLine = childReader.readLine();
		while (currentLine != null) {
			String[] employeeDetail = currentLine.split(delimiter);
			int employeeId = Integer.valueOf(employeeDetail[0]);
			String firstName = employeeDetail[1];
			String lastName = employeeDetail[2];
			String designation = employeeDetail[3];

			allKeys.add(employeeId);
			childData.put(employeeId, new EmployeeInfo(employeeId, firstName, lastName, designation));

			currentLine = childReader.readLine();
		}
		try {
			if(childReader != null) {
				childReader.close();
				childReader = null;
			}
		} catch(IOException ioe) {
			childReader = null;
		}
		StringBuilder sb = new StringBuilder();
		if(index == 0) {
			sb.append("EmpId").append(CONT_COMMA);
			sb.append("F1_FName").append(CONT_COMMA).append("F2_FName").append(CONT_COMMA).append("Status_FName").append(CONT_COMMA);
			sb.append("F1_LName").append(CONT_COMMA).append("F2_LName").append(CONT_COMMA).append("Status_LName").append(CONT_COMMA);
			sb.append("F1_Designation").append(CONT_COMMA).append("F2_Designation").append(CONT_COMMA).append("Status_Designation").append(CONT_COMMA);
			sb.append("RecordStatus").append("\n");
		} 
		for(Integer key: allKeys){
			String result = "Match";
			if(masterData.get(key) != null && childData.get(key) != null){
				sb.append(key).append(CONT_COMMA);
				EmployeeInfo masterEmp = masterData.get(key);
				EmployeeInfo childEmp = childData.get(key);
 		
				sb.append(masterEmp.getFirstName()).append(CONT_COMMA).append(childEmp.getFirstName());
				if (masterEmp.getFirstName().equals(childEmp.getFirstName())) {
					sb.append(",Match,");
				} else {
					sb.append(",MisMatch,");
					result = "MisMatch";
				}

				sb.append(masterEmp.getLastName()).append(CONT_COMMA).append(childEmp.getLastName());
				if (masterEmp.getLastName().equals(childEmp.getLastName())) {
					sb.append(",Match,");
				} else {
					sb.append(",MisMatch,");
					result = "MisMatch";
				}
				sb.append(masterEmp.getDesignation()).append(CONT_COMMA).append(childEmp.getDesignation());
				if (masterEmp.getDesignation().equals(childEmp.getDesignation())) {
					sb.append(",Match,");
				} else {
					sb.append(",MisMatch,");
					result = "MisMatch";
				}

			} else if(masterData.get(key) != null){
				sb.append(key).append(CONT_COMMA);
				EmployeeInfo masterEmp = masterData.get(key);
				result = "Missing";
	
				sb.append(masterEmp.getFirstName()).append(CONT_COMMA);
				sb.append(CONT_COMMA).append(result).append(CONT_COMMA);
	
		 		sb.append(masterEmp.getLastName()).append(CONT_COMMA);
		 		sb.append(CONT_COMMA).append(result).append(CONT_COMMA);
	
		 		sb.append(masterEmp.getDesignation()).append(CONT_COMMA);
		 		sb.append(CONT_COMMA).append(result).append(CONT_COMMA);
	
	 		} else if(childData.get(key) != null){
				sb.append(key).append(CONT_COMMA);
				EmployeeInfo childEmp = childData.get(key);
				result = "Extra";
				sb.append(CONT_COMMA).append(childEmp.getFirstName());
				sb.append(CONT_COMMA).append(result).append(CONT_COMMA);
				sb.append(CONT_COMMA).append(childEmp.getLastName());
				sb.append(CONT_COMMA).append(result).append(CONT_COMMA);
				sb.append(CONT_COMMA).append(childEmp.getDesignation());
				sb.append(CONT_COMMA).append(result).append(CONT_COMMA);
			}
			sb.append(" ").append(result).append("\n");
			System.out.println(sb.toString() + "::Record Inserted..");
		}
		//String outputFile = "./resources/output.txt";
		BufferedWriter buffWriter = null;
		if(index == 0) {
			buffWriter = new BufferedWriter(new FileWriter(FileConstants.OUTPUTFILE));
		} else {
			buffWriter = new BufferedWriter(new FileWriter(FileConstants.OUTPUTFILE, true));
		}
		buffWriter.write(sb.toString());
		//buffWriter.newLine();
		
		try {
			if(buffWriter != null) {
				buffWriter.close();
				buffWriter = null;
			}
		} catch(IOException ioe) {
			buffWriter = null;
		}
		
		index = 1;
		return true;
	}
}
