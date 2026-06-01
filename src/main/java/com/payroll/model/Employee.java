package com.payroll.model;

public abstract class Employee {
    private String employeeId;
    private String name;
    private String bankAccount;

    /**
     * Constructor to initialize an Employee with required fields.
     *
     * @param employeeId the employee ID (cannot be null or empty)
     * @param name the employee name (cannot be null or empty)
     * @param bankAccount the bank account number (cannot be null or empty)
     * @throws IllegalArgumentException if any parameter is null or empty
     */
    public Employee(String employeeId, String name, String bankAccount) {
        setEmployeeId(employeeId);
        setName(name);
        setBankAccount(bankAccount);
    }

    // Getters
    public String getEmployeeId() {
        return employeeId;
    }

    public String getName() {
        return name;
    }

    public String getBankAccount() {
        return bankAccount;
    }

    // Setters with validation
    public void setEmployeeId(String employeeId) {
        if (employeeId == null || employeeId.trim().isEmpty()) {
            throw new IllegalArgumentException("Employee ID cannot be null or empty");
        }
        this.employeeId = employeeId;
    }

    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }
        this.name = name;
    }

    public void setBankAccount(String bankAccount) {
        if (bankAccount == null || bankAccount.trim().isEmpty()) {
            throw new IllegalArgumentException("Bank account cannot be null or empty");
        }
        this.bankAccount = bankAccount;
    }

    /**
     * Abstract method to calculate net pay for the employee.
     * Implementation depends on the employee type (full-time, hourly contractor, etc.)
     *
     * @return the calculated net pay
     */
    public abstract double calculateNetPay();
}
