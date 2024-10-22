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
        reportingStructureUrl = "http://localhost:" + port + "/reportingStructure/{id}";
    }

    @Test
    public void testCreateRead() {

        // Create employees
        Employee john = new Employee();
        john.setEmployeeId(UUID.randomUUID().toString());

        Employee paul = new Employee();
        paul.setEmployeeId(UUID.randomUUID().toString());

        Employee ringo = new Employee();
        ringo.setEmployeeId(UUID.randomUUID().toString());

        Employee george = new Employee();
        george.setEmployeeId(UUID.randomUUID().toString());

        Employee pete = new Employee();
        pete.setEmployeeId(UUID.randomUUID().toString());

        ringo.setDirectReports(List.of(pete, george));
        john.setDirectReports(List.of(paul, ringo));

        // Save employees and verify
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

        createdRingo.setDirectReports(List.of(createdPete, createdGeorge));
        createdJohn.setDirectReports(List.of(createdPaul, createdRingo));

        // Verify report structure for John
        int numberOfReports = calculateReports(createdJohn);
        ReportingStructure reportingStructure = new ReportingStructure(createdJohn, numberOfReports);
        assertNotNull(reportingStructure);
        assertNotNull(reportingStructure.getEmployee());
        assertEquals(createdJohn.getEmployeeId(), reportingStructure.getEmployee().getEmployeeId());
        assertEquals(4, reportingStructure.getNumberOfReports());

        // Verify report structure for Ringo
        numberOfReports = calculateReports(createdRingo);
        reportingStructure = new ReportingStructure(createdRingo, numberOfReports);
        assertNotNull(reportingStructure);
        assertNotNull(reportingStructure.getEmployee());
        assertEquals(createdRingo.getEmployeeId(), reportingStructure.getEmployee().getEmployeeId());
        assertEquals(2, reportingStructure.getNumberOfReports());
    }

    public int calculateReports(Employee employee) {
        if (employee.getDirectReports() == null || employee.getDirectReports().isEmpty()) {
            return 0;
        }

        // Recursively traverse hierarchy structure to get total direct report count
        int totalReports = employee.getDirectReports().size();
        for (Employee report : employee.getDirectReports()){
            Employee directReport = restTemplate.getForEntity(employeeIdUrl, Employee.class, report.getEmployeeId()).getBody();
            if (directReport != null){
                totalReports += calculateReports(directReport);
            }
        }
        return totalReports;
    }

}
