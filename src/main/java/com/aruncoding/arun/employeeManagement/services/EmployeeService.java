package com.aruncoding.arun.employeeManagement.services;

import com.aruncoding.arun.employeeManagement.dto.EmployeeDTO;

import java.util.List;
import java.util.Map;


public interface EmployeeService {

    EmployeeDTO fetchEmployeeById(Long id);

    List<EmployeeDTO> fetchAllEmployees();

    EmployeeDTO saveEmployee(EmployeeDTO employeeDTO);

    EmployeeDTO updateEmployee(Long id, EmployeeDTO employeeDTO);

    Boolean deleteEmployee(Long id);

    EmployeeDTO patchEmployeeDetails(Long employeeId, Map<String, Object> updates);
}
