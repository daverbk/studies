package com.studies.springbootrestcrudemployeewithspringdatarest.dao;

import com.studies.springbootrestcrudemployeewithspringdatarest.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "members")
public interface EmployeeRepository extends JpaRepository<Employee, Integer> { }
