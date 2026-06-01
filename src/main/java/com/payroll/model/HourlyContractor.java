package com.payroll.model;

public class HourlyContractor extends Employee {
    private double hourlyRate;
    private double hoursWorked;

    /**
     * Constructor to initialize an Hourly Contractor.
     *
     * @param employeeId the employee ID (cannot be null or empty)
     * @param name the employee name (cannot be null or empty)
     * @param bankAccount the bank account number (cannot be null or empty)
     * @param hourlyRate the hourly rate (cannot be negative)
     * @param hoursWorked the hours worked (cannot be negative)
     * @throws IllegalArgumentException if validation fails
     */
    public HourlyContractor(String employeeId, String name, String bankAccount,
                            double hourlyRate, double hoursWorked) {
        super(employeeId, name, bankAccount);
        setHourlyRate(hourlyRate);
        setHoursWorked(hoursWorked);
    }

    // Getters
    public double getHourlyRate() {
        return hourlyRate;
    }

    public double getHoursWorked() {
        return hoursWorked;
    }

    // Setters with validation
    public void setHourlyRate(double hourlyRate) {
        if (hourlyRate < 0) {
            throw new IllegalArgumentException("Hourly rate cannot be negative");
        }
        this.hourlyRate = hourlyRate;
    }

    public void setHoursWorked(double hoursWorked) {
        if (hoursWorked < 0) {
            throw new IllegalArgumentException("Hours worked cannot be negative");
        }
        this.hoursWorked = hoursWorked;
    }

    /**
     * Calculate net pay for an hourly contractor.
     * - If hoursWorked <= 40: pay = hourlyRate * hoursWorked
     * - If hoursWorked > 40: pay = (hourlyRate * 40) + (hourlyRate * 1.5 * (hoursWorked - 40))
     *
     * @return the calculated net pay
     */
    @Override
    public double calculateNetPay() {
        if (hoursWorked <= 40) {
            return hourlyRate * hoursWorked;
        } else {
            double standardPay = hourlyRate * 40;
            double overtimePay = hourlyRate * 1.5 * (hoursWorked - 40);
            return standardPay + overtimePay;
        }
    }
}
