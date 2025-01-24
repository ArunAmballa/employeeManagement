package com.aruncoding.arun.employeeManagement.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmployeeDTO {

    private String name;
    private String email;
    private int age;
    private Double salary;
}
