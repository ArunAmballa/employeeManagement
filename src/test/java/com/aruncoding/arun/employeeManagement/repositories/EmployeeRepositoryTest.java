package com.aruncoding.arun.employeeManagement.repositories;

import com.aruncoding.arun.employeeManagement.entities.Employee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;



class EmployeeRepositoryTest extends AbstractRepositoryTest {

    @Autowired
    private EmployeeRepository employeeRepository;

    private Employee employee;

    @BeforeEach
    void setUp(){
        employee = Employee
                        .builder()
                        .name("arunamballa")
                        .age(26)
                        .email("arunamballa@gmail.com")
                        .build();
        employeeRepository.save(employee);
    }



    @Test
    void testFindByEmail_whenEmailIsPresent_thenReturnEmployee() {
        //Arrange
        String email = "arunamballa@gmail.com";
        //Act
        Optional<Employee> employeeFound = employeeRepository.findByEmail(email);
        //Assert
        assertThat(employeeFound.get().getName()).isEqualTo(employee.getName());
        assertThat(employeeFound.get().getEmail()).isEqualTo(email);
        assertThat(employeeFound.get().getAge()).isEqualTo(employee.getAge());
    }


    @Test
    void testFindByEmail_whenEmailIsNotPresent_thenReturnEmptyOptional() {
        //Arrange
        String email = "arunamballa24@gmail.com";
        //Act
        Optional<Employee> employeeNotFound = employeeRepository.findByEmail(email);
        //Assert
        assertThat(employeeNotFound.isPresent()).isFalse();
    }
}