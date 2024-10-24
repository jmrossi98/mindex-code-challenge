package com.mindex.challenge.service;

import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;

public interface ReportingStructureService {
    ReportingStructure getReportingStructure(String employeeId);
    int calculateReports(Employee employee);
}
