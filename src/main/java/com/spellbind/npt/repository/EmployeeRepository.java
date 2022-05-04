package com.spellbind.npt.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.spellbind.npt.entity.Employee;

@Repository
public interface EmployeeRepository extends CrudRepository<Employee, Long> {

}