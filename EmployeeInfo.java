package com.jpmc.info;

import java.io.Serializable;

/**
 * @author Gopal Kk
 *
 */
@SuppressWarnings("rawtypes")
public class EmployeeInfo implements Comparable, Serializable{

	
	private static final long serialVersionUID = 1L;
	private static int count;
	private int employeeId;
	private String designation;
	private String firstName;
	private String lastName;
	
	public EmployeeInfo(int employeeId, String firstName, String lastName, String designation){
		count++;
		this.employeeId = employeeId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.designation = designation;
	}

	public int getCount() {
		return count;
	}

	public int getEmployeeId() {
		return employeeId;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public String getDesignation() {
		return designation;
	}

	@Override
	public String toString() {
		return "Employee [EmployeeId=" + employeeId + ", firstName=" + firstName
				+ ", lastName=" + lastName + ", Designation=" + designation
				+ "]";
	}

	@Override
	public int compareTo(Object o) {
		EmployeeInfo e = (EmployeeInfo) o;
		return this.employeeId-e.employeeId;
	}
}