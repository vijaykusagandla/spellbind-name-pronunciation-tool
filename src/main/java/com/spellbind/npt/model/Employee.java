package com.spellbind.npt.model;

import java.io.Serializable;

public class Employee implements Serializable {

	private static final long serialVersionUID = 8514670279750911034L;
	private Long employeeId;

	public Long getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(Long employeeId) {
		this.employeeId = employeeId;
	}

}
