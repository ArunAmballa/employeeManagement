package com.aruncoding.arun.employeeManagement.services.implementation;

import com.aruncoding.arun.employeeManagement.dto.EmployeeDTO;
import com.aruncoding.arun.employeeManagement.entities.Employee;
import com.aruncoding.arun.employeeManagement.exceptions.ResourceNotFoundException;
import com.aruncoding.arun.employeeManagement.exceptions.RuntimeConflictException;
import com.aruncoding.arun.employeeManagement.repositories.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;


class EmployeeServiceImplTest extends AbstractServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Spy
    private ModelMapper modelMapper;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    private Employee mockedEmployee;

    private EmployeeDTO mockedEmployeeDTO;

    @BeforeEach
    void setUp(){
        mockedEmployee=Employee.builder()
               .id(1L)
               .name("arun")
               .age(26)
               .email("arunamballa@gmail.com")
               .salary(1000.0)
               .build();
        mockedEmployeeDTO=modelMapper.map( mockedEmployee,EmployeeDTO.class);
    }


    @Test
    void testFetchEmployeeById_whenIdIsPresent_thenReturnEmployee() {
        //Assign
        Long id=mockedEmployee.getId();
        when(employeeRepository.findById(id)).thenReturn(Optional.of(mockedEmployee));
        //Act
        EmployeeDTO employeeDTO = employeeService.fetchEmployeeById(id);
        //Assert
        assertThat(employeeDTO).isNotNull();
        assertThat(employeeDTO.getEmail()).isEqualTo(mockedEmployee.getEmail());
        assertThat(employeeDTO.getName()).isEqualTo(mockedEmployee.getName());
        assertThat(employeeDTO.getAge()).isEqualTo(mockedEmployee.getAge());
        assertThat(employeeDTO.getSalary()).isEqualTo(mockedEmployee.getSalary());

        verify(employeeRepository,times(1)).findById(id);
    }

    @Test
    void testFetchEmployeeById_whenIdIsNotPresent_thenThrowException() {
        //Assign
        when(employeeRepository.findById(anyLong())).thenReturn(Optional.empty());
        //Act and Assert
        assertThatThrownBy(()->employeeService.fetchEmployeeById(2L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Employee not found with Id:2" );

        verify(employeeRepository,atLeast(1)).findById(anyLong());
    }


    @Test
    void testSaveEmployee_whenEmployeeIsNotPresent_thenCreateEmployee() {
       //Assign
        String email=mockedEmployee.getEmail();
        when(employeeRepository.findByEmail(email)).thenReturn(Optional.empty());
        when(employeeRepository.save(any(Employee.class))).thenReturn(mockedEmployee);
       //Act
        EmployeeDTO employeeDTO = employeeService.saveEmployee(mockedEmployeeDTO);
        //Assert
        assertThat(employeeDTO).isNotNull();
        assertThat(employeeDTO.getEmail()).isEqualTo(mockedEmployee.getEmail());
        assertThat(employeeDTO.getName()).isEqualTo(mockedEmployee.getName());
        assertThat(employeeDTO.getAge()).isEqualTo(mockedEmployee.getAge());
        assertThat(employeeDTO.getSalary()).isEqualTo(mockedEmployee.getSalary());

        verify(employeeRepository,atLeast(1)).findByEmail(email);
        verify(employeeRepository,atLeast(1)).save(any(Employee.class));

    }


    @Test
    void testSaveEmployee_whenEmployeeIsPresent_thenThrowException() {
        //Assign
        String email=mockedEmployee.getEmail();
        when(employeeRepository.findByEmail(email)).thenReturn(Optional.of(mockedEmployee));

        //Act and Assert
        assertThatThrownBy(()->employeeService.saveEmployee(mockedEmployeeDTO))
                .isInstanceOf(RuntimeConflictException.class)
                .hasMessage("Employee already exists with Email:"+email);

        verify(employeeRepository,atLeast(1)).findByEmail(email);
        verify(employeeRepository,times(0)).save(any(Employee.class));
    }


    @Test
    void testFetchAllEmployees_whenEmployeesPresent_thenReturnEmployees() {
        //Assign
        when(employeeRepository.findAll()).thenReturn(List.of(mockedEmployee));
        //Act
        List<EmployeeDTO> employeeDTOS = employeeService.fetchAllEmployees();
        //Assert
        assertThat(employeeDTOS).isNotNull();
        assertThat(employeeDTOS.get(0)).isEqualTo(mockedEmployeeDTO);

        verify(employeeRepository,times(1)).findAll();
    }

    @Test
    void testFetchAllEmployees_whenEmployeesNotPresent_thenThrowException() {
        //Assign
        when(employeeRepository.findAll()).thenReturn(List.of());
        //Act and Asset
        assertThatThrownBy(()->employeeService.fetchAllEmployees())
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Employees Does not Exists");

        verify(employeeRepository,atLeast(1)).findAll();
        verify(employeeRepository,never()).save(any(Employee.class));
    }

    @Test
    void testUpdateEmployee_whenEmployeeIsPresent_thenUpdateEmployee() {
        //Assign
        Long id=mockedEmployee.getId();
        when(employeeRepository.findById(id)).thenReturn(Optional.of(mockedEmployee));
        when(employeeRepository.save(any(Employee.class))).thenReturn(mockedEmployee);
        //Act
        EmployeeDTO employeeDTO = employeeService.updateEmployee(id, mockedEmployeeDTO);
        //Assert
        assertThat(employeeDTO).isNotNull();
        assertThat(employeeDTO.getEmail()).isEqualTo(mockedEmployee.getEmail());
        assertThat(employeeDTO.getName()).isEqualTo(mockedEmployee.getName());
        assertThat(employeeDTO.getAge()).isEqualTo(mockedEmployee.getAge());
        assertThat(employeeDTO.getSalary()).isEqualTo(mockedEmployee.getSalary());

        verify(employeeRepository,atLeast(1)).findById(id);
        verify(employeeRepository,atLeast(1)).save(any(Employee.class));
    }


    @Test
    void testUpdateEmployee_whenEmployeeIsNotPresent_thenThrowException() {
        //Assign
        Long id=2L;
        when(employeeRepository.findById(id)).thenReturn(Optional.empty());
        //Act and Assert
        assertThatThrownBy(()->employeeService.updateEmployee(id, mockedEmployeeDTO))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Employee not found with Id:2" );
        verify(employeeRepository,atLeast(1)).findById(id);
        verify(employeeRepository,never()).save(any(Employee.class));
    }


    @Test
    void testDeleteEmployee_whenEmployeeIsPresent_thenDeleteEmployee() {
        //Assign
        Long id=mockedEmployee.getId();
        when(employeeRepository.existsById(id)).thenReturn(true);
        doNothing().when(employeeRepository).deleteById(id);

        //Act
        Boolean b = employeeService.deleteEmployee(id);
        assertThat(b).isTrue();

        verify(employeeRepository,atLeast(1)).deleteById(id);
        verify(employeeRepository,atLeast(1)).existsById(id);
    }


    @Test
    void testDeleteEmployee_whenEmployeeIsNotPresent_thenThrowException() {
        //Assign
        Long id=2L;
        when(employeeRepository.existsById(id)).thenReturn(false);

        //Act and Assert
        assertThatThrownBy(()->employeeService.deleteEmployee(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Employee not found with Id:2" );

        verify(employeeRepository,atLeast(1)).existsById(id);
        verify(employeeRepository,never()).deleteById(id);
    }


    @Test
    void testPatchEmployeeDetails_whenEmployeeIsPresent_thenPatchEmployeeDetails() {
       //Assign
       Long id=mockedEmployee.getId();
       when(employeeRepository.findById(id)).thenReturn(Optional.of(mockedEmployee));

       Employee updatedEmployee=new Employee();
       updatedEmployee.setId(mockedEmployee.getId());
       updatedEmployee.setEmail("arunamballa24@gmail.com");
       updatedEmployee.setName(mockedEmployee.getName());
       updatedEmployee.setAge(mockedEmployee.getAge());
       updatedEmployee.setSalary(mockedEmployee.getSalary());

       when(employeeRepository.save(any(Employee.class))).thenReturn(updatedEmployee);
       Map<String, Object> updates=Map.of("email","arunamballa24@gmail.com");

       //Act
        EmployeeDTO employeeDTO = employeeService.patchEmployeeDetails(id, updates);

        //Assert
        assertThat(employeeDTO).isNotNull();
        assertThat(employeeDTO.getEmail()).isEqualTo(updatedEmployee.getEmail());

        verify(employeeRepository,atLeast(1)).findById(id);
        verify(employeeRepository,atLeast(1)).save(any(Employee.class));

    }


    @Test
    void testPatchEmployeeDetails_whenEmployeeIsNotPresent_thenThrowException() {
        //Assign
        Long id=2L;
        when(employeeRepository.findById(id)).thenReturn(Optional.empty());
        Map<String, Object> updates=Map.of("email","arunamballa24@gmail.com");
        //Act and Assert
        assertThatThrownBy(()->employeeService.patchEmployeeDetails(id,updates))
        .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Employee not found with Id:2" );

        verify(employeeRepository,atLeast(1)).findById(id);
        verify(employeeRepository,never()).save(any(Employee.class));
    }

}