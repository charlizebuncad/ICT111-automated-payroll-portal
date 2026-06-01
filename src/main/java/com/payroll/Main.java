package com.payroll;

import java.util.ArrayList;
import com.payroll.model.*;

public class Main {
    public static void main(String[] args) {
        // Create an ArrayList of Employee type
        ArrayList<Employee> workforce = new ArrayList<>();

        // Add sample employees to the workforce
        FTEmployee ftEmployee = new FTEmployee(
                "EMP001",
                "John Smith",
                "1234567890",
                25000.0,  // monthlySalary
                2000.0    // healthBenefitsPremium
        );
        workforce.add(ftEmployee);

        HourlyContractor hourlyContractor = new HourlyContractor(
                "EMP002",
                "Jane Doe",
                "0987654321",
                50.0,     // hourlyRate
                45.0      // hoursWorked
        );
        workforce.add(hourlyContractor);

        CommdSalesperson salesperson = new CommdSalesperson(
                "EMP003",
                "Robert Johnson",
                "5555555555",
                5000.0,   // basePay
                50000.0,  // totalSales
                0.10      // commissionRate
        );
        workforce.add(salesperson);

        // Calculate total company payout
        double totalPayout = 0;

        // Iterate through workforce and print employee information
        System.out.println("===== PAYROLL REPORT =====");
        System.out.println();
        for (Employee employee : workforce) {
            double netPay = employee.calculateNetPay();
            totalPayout += netPay;

            System.out.println("Employee ID: " + employee.getEmployeeId());
            System.out.println("Name: " + employee.getName());
            System.out.println("Net Pay: $" + String.format("%.2f", netPay));
            System.out.println("---");
        }

        // Print grand total company payout
        System.out.println();
        System.out.println("===== SUMMARY =====");
        System.out.println("Grand Total Company Payout: $" + String.format("%.2f", totalPayout));
    }
}
