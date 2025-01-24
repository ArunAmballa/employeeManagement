package com.aruncoding.arun.employeeManagement.controllers;

import com.aruncoding.arun.employeeManagement.TestcontainersConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(TestcontainersConfiguration.class)
@AutoConfigureWebTestClient(timeout = "100000")
public class AbstractIntegrationTest {

    @Autowired
    WebTestClient webTestClient;

}
