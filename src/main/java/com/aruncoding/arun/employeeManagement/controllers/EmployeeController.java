package com.aruncoding.arun.employeeManagement.controllers;

import com.aruncoding.arun.employeeManagement.dto.EmployeeDTO;
import com.aruncoding.arun.employeeManagement.services.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping(path = "/employee")
@Slf4j
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping
    public ResponseEntity<List<EmployeeDTO>> getAllEmployees() {
        log.info("Fetching All Employees");
        return new ResponseEntity<>(employeeService.fetchAllEmployees(),HttpStatus.OK);

    }

    @GetMapping(path = "/{employeeId}")
    public ResponseEntity<EmployeeDTO> getEmployeeById(@PathVariable Long employeeId) {
        log.info("Fetching Employee By Id:{}",employeeId);
        return new ResponseEntity<>(employeeService.fetchEmployeeById(employeeId),HttpStatus.OK);

    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<EmployeeDTO> createEmployee(@RequestBody EmployeeDTO employeeDTO) {
        log.info("Creating Employee:{}",employeeDTO);
        return new ResponseEntity<>(employeeService.saveEmployee(employeeDTO),HttpStatus.CREATED);
    }

    @PutMapping(path = "/{employeeId}")
    public ResponseEntity<EmployeeDTO> updateEmployee(@RequestBody EmployeeDTO employeeDTO, @PathVariable Long employeeId) {
        log.info("Updating Employee:{}",employeeDTO);
        return new ResponseEntity<>(employeeService.updateEmployee(employeeId,employeeDTO),HttpStatus.OK);
    }

    @DeleteMapping(path = "/{employeeId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Boolean> deleteEmployee(@PathVariable Long employeeId) {
        log.info("Deleting Employee:{}",employeeId);
        Boolean flag = employeeService.deleteEmployee(employeeId);
        return ResponseEntity.ok(flag);
    }

    @PatchMapping(path = "/{employeeId}")
    public ResponseEntity<EmployeeDTO> patchEmployee(@RequestBody Map<String,Object> updates, @PathVariable Long employeeId) {
        log.info("Updating Employee Partially:{}",updates);
        //return employeeService.patchEmployeeDetails(employeeId,updates);
        return new ResponseEntity<>(employeeService.patchEmployeeDetails(employeeId,updates),HttpStatus.OK);
    }
}
