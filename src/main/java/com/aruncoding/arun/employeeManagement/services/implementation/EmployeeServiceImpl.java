package com.aruncoding.arun.employeeManagement.services.implementation;

import com.aruncoding.arun.employeeManagement.dto.EmployeeDTO;
import com.aruncoding.arun.employeeManagement.entities.Employee;
import com.aruncoding.arun.employeeManagement.exceptions.ResourceNotFoundException;
import com.aruncoding.arun.employeeManagement.exceptions.RuntimeConflictException;
import com.aruncoding.arun.employeeManagement.repositories.EmployeeRepository;
import com.aruncoding.arun.employeeManagement.services.EmployeeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.util.ReflectionUtils;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@Slf4j
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final ModelMapper modelMapper;


    @Override
    public EmployeeDTO fetchEmployeeById(Long id) {
        log.info("Fetching employee with id:{}",id);
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Employee with id:{} not found", id);
                    return new ResourceNotFoundException("Employee not found with Id:" + id);
                });
        log.info("Fetched employee with id:{}",id);
        return modelMapper.map(employee,EmployeeDTO.class);
    }

    @Override
    public List<EmployeeDTO> fetchAllEmployees() {
        log.info("Fetching all employees");
        List<Employee> allEmployees = employeeRepository.findAll();
        if(allEmployees.isEmpty()){
            throw new ResourceNotFoundException("Employees Does not Exists");
        }
        List<EmployeeDTO> employees = allEmployees
                .stream()
                .map(employee -> modelMapper.map(employee, EmployeeDTO.class))
                .collect(Collectors.toList());
        log.info("Fetched all employees");
        return employees;
    }


    @Override
    public EmployeeDTO saveEmployee(EmployeeDTO employeeDTO) {
        log.info("Checking if Employee with email already exists:{}",employeeDTO.getEmail());
        Optional<Employee> employee = employeeRepository.findByEmail(employeeDTO.getEmail());
        if(employee.isPresent()) {
            log.error("Employee with email already exists");
            throw new RuntimeConflictException("Employee already exists with Email:" + employeeDTO.getEmail());
        }
        Employee employeeToBeSaved = modelMapper.map(employeeDTO, Employee.class);
        log.info("Saving employee with email:{}",employeeDTO.getEmail());
        Employee savedEmployee = employeeRepository.save(employeeToBeSaved);
        return modelMapper.map(savedEmployee,EmployeeDTO.class);
    }

    @Override
    public EmployeeDTO updateEmployee(Long id, EmployeeDTO employeeDTO) {
        log.info("Updating employee with id:{}",id);
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Employee with id:{} not found", id);
                    return new ResourceNotFoundException("Employee not found with Id:" + id);
                });
        employee.setAge(employeeDTO.getAge());
        employee.setName(employeeDTO.getName());
        employee.setSalary(employeeDTO.getSalary());
        employee.setEmail(employeeDTO.getEmail());
        log.info("Updating employee with Id:{}:{}", id,employee);
        Employee updatedEmployee=employeeRepository.save(employee);
        log.info("Updated employee with Id:{}:{}", id,updatedEmployee);
        return modelMapper.map(updatedEmployee,EmployeeDTO.class);
    }

    @Override
    public Boolean deleteEmployee(Long id) {
        log.info("Deleting employee with id:{}",id);
        if(!doesEmployeeExist(id)) {
            log.error("Employee with id:{} not found", id);
            throw new ResourceNotFoundException("Employee not found with Id:" + id);
        }
        log.info("Deleting Employee with Id:{}",id);
        employeeRepository.deleteById(id);
        return true;
    }

    @Override
    public EmployeeDTO patchEmployeeDetails(Long employeeId, Map<String, Object> updates) {
        log.info("Updating employee with id:{}",employeeId);
        Employee employeeToBeUpdated = employeeRepository.findById(employeeId)
                .orElseThrow(() -> {
                    log.error("Employee with id:{} not found", employeeId);
                    return new ResourceNotFoundException("Employee not found with Id:" + employeeId);
                });
        updates.forEach((field,value)->{
            Field requiredField = ReflectionUtils.findRequiredField(Employee.class, field);
            requiredField.setAccessible(true);
            ReflectionUtils.setField(requiredField, employeeToBeUpdated, value);
        });
        log.info("Updating Employee with Id:{},{}",employeeId,employeeToBeUpdated);
        Employee updateEmployee = employeeRepository.save(employeeToBeUpdated);
        return modelMapper.map(updateEmployee,EmployeeDTO.class);
    }

    public Boolean doesEmployeeExist(Long employeeId) {
        return employeeRepository.existsById(employeeId);
    }
}
