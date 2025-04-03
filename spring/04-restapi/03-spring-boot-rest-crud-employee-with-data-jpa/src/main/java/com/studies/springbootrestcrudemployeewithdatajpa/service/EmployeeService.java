package com.studies.springbootrestcrudemployeewithdatajpa.service;

import com.studies.springbootrestcrudemployeewithdatajpa.entity.Employee;

import java.util.List;

public interface EmployeeService {
    List<Employee> findAll();

    Employee findById(int id);

    Employee save(Employee employee);

    void deleteById(int id);
}
