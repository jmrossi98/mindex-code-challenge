package com.mindex.challenge.data;

import java.util.List;
import com.mindex.challenge.data.Employee;

public class ReportingStructure {
    private Employee employee;
    private int numberOfReports;

    public ReportingStructure(Employee employee, int numberOfReports) {
        this.employee = employee;
        this.numberOfReports = numberOfReports;
    }

    public void getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Integer getNumberOfReports() {
        return numberOfReports;
    }

    public Integer setNumberOfReports(int numberOfReports) {
        this.numberOfReports = numberOfReports;
    }
}
