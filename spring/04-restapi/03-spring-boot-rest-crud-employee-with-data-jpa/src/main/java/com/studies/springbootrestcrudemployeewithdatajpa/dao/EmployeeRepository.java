package com.studies.springbootrestcrudemployeewithdatajpa.dao;

import com.studies.springbootrestcrudemployeewithdatajpa.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Integer> { }
