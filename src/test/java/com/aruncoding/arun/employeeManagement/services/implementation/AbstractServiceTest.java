package com.aruncoding.arun.employeeManagement.services.implementation;

import com.aruncoding.arun.employeeManagement.TestcontainersConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;

@Slf4j
@Import(TestcontainersConfiguration.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class AbstractServiceTest {
}
