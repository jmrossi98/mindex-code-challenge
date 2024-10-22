package com.mindex.challenge.service.impl;

import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.Compensation;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.time.LocalDate;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CompensationImplTest {

    private String employeeUrl;
    private String compensationUrl;
    private String compensationIdUrl;

    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    private int port;

    @Before
    public void setup() {
        employeeUrl = "http://localhost:" + port + "/employee";
        compensationUrl = "http://localhost:" + port + "/compensation";
        compensationIdUrl = "http://localhost:" + port + "/compensation/{id}";
    }

    @Test
    public void testCreateRead() {

        // Create an employee
        Employee testEmployee = new Employee();
        Employee createdEmployee = restTemplate.postForEntity(employeeUrl, testEmployee, Employee.class).getBody();
        assertNotNull(createdEmployee.getEmployeeId());

        // Create compensation
        Compensation testCompensation = new Compensation();
        testCompensation.setEmployeeId(createdEmployee.getEmployeeId());
        testCompensation.setSalary(100000);
        testCompensation.setEffectiveDate(LocalDate.of(2024, 10, 21));

        // Save compensation
        Compensation createdCompensation = restTemplate.postForEntity(compensationUrl, testCompensation, Compensation.class).getBody();
        assertNotNull(createdCompensation);
        assertEquals(testCompensation.getSalary(), createdCompensation.getSalary(), 0);
        assertEquals(testCompensation.getEffectiveDate(), createdCompensation.getEffectiveDate());
        assertEquals(createdEmployee.getEmployeeId(), createdCompensation.getEmployeeId());

        // Verify compensation
        Compensation readCompensation = restTemplate.getForEntity(compensationIdUrl, Compensation.class, createdEmployee.getEmployeeId()).getBody();
        assertNotNull(readCompensation);
        assertEquals(createdCompensation.getSalary(), readCompensation.getSalary(), 0);
        assertEquals(createdCompensation.getEffectiveDate(), readCompensation.getEffectiveDate());
        assertEquals(createdEmployee.getEmployeeId(), readCompensation.getEmployeeId());
    }
}
