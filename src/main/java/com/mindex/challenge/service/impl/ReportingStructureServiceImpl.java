package com.mindex.challenge.service.impl;

import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.dao.EmployeeRepository;
import com.mindex.challenge.service.ReportingStructureService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReportingStructureServiceImpl implements ReportingStructureService {

    private static final Logger LOG = LoggerFactory.getLogger(ReportingStructureServiceImpl.class);

    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public ReportingStructure getReportingStructure(String employeeId){
        Employee employee = employeeRepository.findByEmployeeId(employeeId);
        int numberOfReports = calculateReports(employee);
        return new ReportingStructure(employee, numberOfReports);
    }

    public int calculateReports(Employee employee) {
        if (employee.getDirectReports() == null || employee.getDirectReports().isEmpty()) {
            return 0;
        }

        int totalReports = employee.getDirectReports().size();
        for (Employee report : employee.getDirectReports()){
            Employee directReport = employeeRepository.findByEmployeeId(report.getEmployeeId());
            if (directReport != null){
                totalReports += calculateReports(directReport);
            }
        }
        return totalReports;
    }
}
