package com.spellbind.npt.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spellbind.npt.entity.Employee;
import com.spellbind.npt.repository.EmployeeRepository;

@RestController
public class EmployeeController {

	@Autowired
	EmployeeRepository employeeRepository;

	@GetMapping(path = "/listEmployees", produces = "application/json")
	public ResponseEntity<List<Employee>> listEmployees() {
		Iterable<Employee> allEmployees = employeeRepository.findAll();
		List<Employee> employeeList = new ArrayList<>();
		for (Employee employee : allEmployees) {
			employeeList.add(employee);
		}
		return new ResponseEntity<>(employeeList, HttpStatus.OK);

	}

}
