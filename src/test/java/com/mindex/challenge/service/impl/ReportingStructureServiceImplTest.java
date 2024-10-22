package com.mindex.challenge.service.impl;

import java.util.*;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.service.ReportingStructureService;

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

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ReportingStructureServiceImplTest {

    private String employeeUrl;
    private String employeeIdUrl;
    private String reportingStructureUrl;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ReportingStructureService reportingStructureService;

    @Before
    public void setup() {
        employeeUrl = "http://localhost:" + port + "/employee";
        employeeIdUrl = "http://localhost:" + port + "/employee/{id}";
        reportingStructureUrl = "http://localhost:" + port + "/reportingStructure";
    }

    @Test
    public void testCreateReadUpdate() {

        // Create employees
        Employee john = new Employee();
        Employee paul = new Employee();
        Employee pete = new Employee();
        Employee ringo = new Employee();
        Employee george = new Employee();

        // Set up reports
        ringo.setDirectReports(List.of(pete, george));
        john.setDirectReports(List.of(paul, ringo));

        // Save employees
        Employee createdJohn = restTemplate.postForEntity(employeeUrl, john, Employee.class).getBody();
        Employee createdPaul = restTemplate.postForEntity(employeeUrl, paul, Employee.class).getBody();
        Employee createdPete = restTemplate.postForEntity(employeeUrl, pete, Employee.class).getBody();
        Employee createdRingo = restTemplate.postForEntity(employeeUrl, ringo, Employee.class).getBody();
        Employee createdGeorge = restTemplate.postForEntity(employeeUrl, george, Employee.class).getBody();

        assertNotNull(createdJohn.getEmployeeId());
        assertNotNull(createdPaul.getEmployeeId());
        assertNotNull(createdPete.getEmployeeId());
        assertNotNull(createdRingo.getEmployeeId());
        assertNotNull(createdGeorge.getEmployeeId());

        // Step 3: Read the reporting structure for John
        ReportingStructure reportingStructure = reportingStructureService.getReportingStructure(createdJohn.getEmployeeId());

        // Step 4: Validate reporting structure
        assertNotNull(reportingStructure);
        assertEquals(createdJohn.getEmployeeId(), reportingStructure.getEmployee());
        assertEquals(4, reportingStructure.getNumberOfReports()); // John has 4 reports under him (Paul, Ringo, George)

    }
}
