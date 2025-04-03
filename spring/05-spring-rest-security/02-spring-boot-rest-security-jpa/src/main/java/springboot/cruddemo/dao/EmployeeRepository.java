package springboot.cruddemo.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import springboot.cruddemo.entity.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
}
