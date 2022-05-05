package com.spellbind.npt.dao;

import java.io.IOException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.spellbind.npt.entity.Employee;
import com.spellbind.npt.exception.NamePronunciationToolException;
import com.spellbind.npt.repository.EmployeeRepository;

public class EmployeeDAO {

	@Autowired
	EmployeeRepository employeeRepository;

	@Transactional
	public void storeAudioFile(MultipartFile multipartFile) {
		Long employeeId = Long.parseLong(multipartFile.getOriginalFilename().replaceAll("\\.\\w+$", ""));
		Employee employee = findEmployeeById(employeeId);
		try {
			employee.setAudioContent(multipartFile.getBytes());
		} catch (IOException e) {
			throw new NamePronunciationToolException("Internal Error Occured while reading file content" + e);
		}
		employeeRepository.save(employee);
	}

	public Employee findEmployeeById(Long employeeId) {
		Optional<Employee> employee = employeeRepository.findById(employeeId);
		return employee.orElseThrow(() -> new NamePronunciationToolException("Employee Id not found"));
	}

}
