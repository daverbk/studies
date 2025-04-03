package com.studies.cruddemo;

import com.studies.cruddemo.dao.StudentDAO;
import com.studies.cruddemo.entity.Student;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;

@SpringBootApplication
public class CruddemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(CruddemoApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(StudentDAO studentDAO) {

        return runner -> {
            createStudent(studentDAO);
            createMultipleStudents(studentDAO);
            readStudent(studentDAO);
            queryForStudents(studentDAO);
            queryForStudentsByLastName(studentDAO);
            updateStudent(studentDAO);
            deleteStudent(studentDAO);
            deleteAllStudents(studentDAO);
        };
    }

    private void createStudent(StudentDAO studentDAO) {
        System.out.println("Creating new student object ...");
        Student student = new Student("Paul", "Doe", "paul@uni.com");

        System.out.println("Saving the student ...");
        studentDAO.save(student);

        System.out.println("Saved student. Generated id: " + student.getId());
    }

    private void createMultipleStudents(StudentDAO studentDAO) {
        System.out.println("Creating new student objects ...");
        Student student = new Student("John", "Doe", "john@uni.com");
        Student student2 = new Student("Mary", "Public", "mary@uni.com");
        Student student3 = new Student("Bonita", "Applebum", "bonita@uni.com");

        System.out.println("Saving the students ...");
        studentDAO.save(student);
        studentDAO.save(student2);
        studentDAO.save(student3);
    }

    private void readStudent(StudentDAO studentDAO) {
        System.out.println("Creating new student object ...");
        Student student = new Student("Daffy", "Duck", "daffy@uni.com");

        System.out.println("Saving the student ...");
        studentDAO.save(student);

        int id = student.getId();
        System.out.println("Saved student. Generated id: " + id);

        System.out.println("Retrieving student with id: " + id);
        Student foundStudent = studentDAO.findById(id);

        System.out.println("Found student: " + foundStudent);
    }

    private void queryForStudents(StudentDAO studentDAO) {
        List<Student> students = studentDAO.findAll();
        for (Student student : students) {
            System.out.println(student);
        }
    }

    private void queryForStudentsByLastName(StudentDAO studentDAO) {
        List<Student> students = studentDAO.findByLastName("Doe");
        for (Student student : students) {
            System.out.println(student);
        }
    }

    private void updateStudent(StudentDAO studentDAO) {
        int studentId = 1;
        System.out.println("Getting student with id: " + studentId);
        Student student = studentDAO.findById(studentId);

        System.out.println("Updating student ...");
        student.setFirstName("John");

        studentDAO.update(student);

        System.out.println("Updated student: " + student);
    }

    private void deleteStudent(StudentDAO studentDAO) {
        int studentId = 3;
        System.out.println("Deleting student id: " + studentId);
        studentDAO.delete(studentId);
    }

    private void deleteAllStudents(StudentDAO studentDAO) {
        System.out.println("Deleting all students ...");
        int numberOfRowsDeleted = studentDAO.deleteAll();
        System.out.println("Deleted row count: " + numberOfRowsDeleted);
    }
}
