package com.jpmc.execute;

import java.io.IOException;

import com.jpmc.service.FileMerge;
import com.jpmc.utils.FileConstants;
/**
 * @author Gopal Kk
 *
 */

public class Main {
	public static void main(String s[]) {
		String masterFileName = FileConstants.MASTERFILE;
		String childFileName = FileConstants.CHILDFILE;

		FileMerge service = new FileMerge(masterFileName, childFileName);
		try {
			service.splitFiles();
		} catch (IllegalStateException e) {
			// TODO
			System.out.println(e.getMessage());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println(e.getMessage());
		}
	}
}