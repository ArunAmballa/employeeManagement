package com.aruncoding.arun.employeeManagement.controllers;

import com.aruncoding.arun.employeeManagement.advice.ApiResponse;
import com.aruncoding.arun.employeeManagement.dto.EmployeeDTO;
import com.aruncoding.arun.employeeManagement.entities.Employee;
import com.aruncoding.arun.employeeManagement.repositories.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;



class EmployeeControllerTestIT extends AbstractIntegrationTest{

    @Autowired
    private EmployeeRepository employeeRepository;

    private Employee testEmployee;

    private EmployeeDTO testEmployeeDTO;

    @BeforeEach
    void setUp() {
        testEmployee=Employee.builder()
                .name("arun")
                .age(26)
                .email("arunamballa@gmail.com")
                .salary(1000.0)
                .build();
        testEmployeeDTO=EmployeeDTO.builder()
                        .name("arun")
                        .age(26)
                        .email("arunamballa@gmail.com")
                        .salary(1000.0)
                        .build();
        employeeRepository.deleteAll();
    }
    @Test
    void testGetEmployeeById_success() {
        Employee savedEmployee = employeeRepository.save(testEmployee);
        webTestClient.get()
                .uri("/employee/{id}",savedEmployee.getId())
                .exchange()
                .expectStatus().isOk()
                .expectBody(new ParameterizedTypeReference<ApiResponse<EmployeeDTO>>() {})
                //.isEqualTo(testEmployeeDTO)
                .value(response -> {
                    EmployeeDTO employeeDTO=response.getData();
                    assertThat(employeeDTO.getEmail()).isEqualTo(savedEmployee.getEmail());
                    assertThat(employeeDTO.getName()).isEqualTo(savedEmployee.getName());
                    assertThat(employeeDTO.getAge()).isEqualTo(savedEmployee.getAge());
                });
    }


    @Test
    void testGetEmployeeById_failure() {
        webTestClient.get()
                .uri("/employee/1")
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void testCreatEmployee_whenEmployeeAlreadyExists_thenThrowException() {
        Employee savedEmployee = employeeRepository.save(testEmployee);

        webTestClient.post()
                .uri("/employee")
                .bodyValue(testEmployeeDTO)
                .exchange()
                .expectStatus().is4xxClientError();
    }

    @Test
    void testCreateEmployee_whenEmployeeDoesNotExists_thenCreateEmployee() {

        webTestClient.post()
                .uri("/employee")
                .bodyValue(testEmployeeDTO)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(new ParameterizedTypeReference<ApiResponse<EmployeeDTO>>() {
                })
                .value(response -> {
                    EmployeeDTO employeeDTO = response.getData();
                    assertThat(employeeDTO.getName()).isEqualTo(testEmployeeDTO.getName());
                    assertThat(employeeDTO.getEmail()).isEqualTo(testEmployeeDTO.getEmail());
                });
    }

    @Test
    void testUpdateEmployee_whenEmployeeDoesNotExists_thenThrowException() {
        webTestClient.put()
                .uri("/employee/1")
                .bodyValue(testEmployeeDTO)
                .exchange()
                .expectStatus().isNotFound();
    }


    @Test
    void testDeleteEmployee_whenEmployeeDoesNotExists_thenThrowException() {
        webTestClient.delete()
                .uri("/employee/1")
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void testDeleteEmployee_whenEmployeeExists_thenDeleteEmployee() {
        Employee savedEmployee = employeeRepository.save(testEmployee);

        webTestClient.delete()
                .uri("/employee/{id}",savedEmployee.getId())
                .exchange()
                .expectStatus().isOk();
    }


    @Test
    void testUpdateEmployee_whenEmployeeExists_thenUpdateEmployee() {
        Employee savedEmployee = employeeRepository.save(testEmployee);
        webTestClient.put()
                .uri("/employee/{id}",savedEmployee.getId())
                .bodyValue(testEmployeeDTO)
                .exchange()
                .expectStatus().isOk()
                .expectBody(new ParameterizedTypeReference<ApiResponse<EmployeeDTO>>() {})
                .value(response -> {
                    EmployeeDTO employeeDTO=response.getData();
                    assertThat(employeeDTO.getName()).isEqualTo(testEmployeeDTO.getName());
                    assertThat(employeeDTO.getEmail()).isEqualTo(testEmployeeDTO.getEmail());
                });
    }


    @Test
    void testPatchEmployee_whenEmployeeDoesNotExists_thenThrowException() {
        Map<String,Object> updates=Map.of("email","arunamballa24@gmail.com");
        webTestClient.patch()
                .uri("/employee/1")
                .bodyValue(updates)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void testPatchEmployee_whenEmployeeExists_thenPatchEmployee() {
        Employee savedEmployee = employeeRepository.save(testEmployee);
        Map<String,Object> updates=Map.of("email","arunamballa24@gmail.com");
        webTestClient.patch()
                .uri("/employee/{id}",savedEmployee.getId())
                .bodyValue(updates)
                .exchange()
                .expectStatus().isOk()
                .expectBody(new ParameterizedTypeReference<ApiResponse<EmployeeDTO>>() {})
                .value(response -> {
                    EmployeeDTO employeeDTO=response.getData();
                    assertThat(employeeDTO.getEmail()).isEqualTo("arunamballa24@gmail.com");
                });
    }

    @Test
    void testGetAllEmployees_whenEmployeeDoesNotExists_thenThrowException() {
        webTestClient.get()
                .uri("/employee")
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void testGetAllEmployees_whenEmployeeExists_thenGetAllEmployees() {
        Employee savedEmployee = employeeRepository.save(testEmployee);
        webTestClient.get()
                .uri("/employee")
                .exchange()
                .expectStatus().isOk()
                .expectBody(new ParameterizedTypeReference<ApiResponse<List<EmployeeDTO>>>() {})
                .value(response -> {
                    List<EmployeeDTO> employeeDTOList=response.getData();
                    assertThat(employeeDTOList).isNotEmpty();
                });
    }



}