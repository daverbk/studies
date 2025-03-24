package com.studies.sptingbootrestcrudemployee.service;

import com.studies.sptingbootrestcrudemployee.entity.Employee;

import java.util.List;

public interface EmployeeService {
    List<Employee> findAll();

    Employee findById(int id);

    Employee save(Employee employee);

    void deleteById(int id);
}
