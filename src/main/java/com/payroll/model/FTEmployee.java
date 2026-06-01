package com.payroll.model;

public class FTEmployee extends Employee {
    private double monthlySalary;
    private double healthBenefitsPremium;

    /**
     * Constructor to initialize a Full-Time Employee.
     *
     * @param employeeId the employee ID (cannot be null or empty)
     * @param name the employee name (cannot be null or empty)
     * @param bankAccount the bank account number (cannot be null or empty)
     * @param monthlySalary the monthly salary (must be >= 15000)
     * @param healthBenefitsPremium the health benefits premium (must be >= 0)
     * @throws IllegalArgumentException if validation fails
     */
    public FTEmployee(String employeeId, String name, String bankAccount,
                      double monthlySalary, double healthBenefitsPremium) {
        super(employeeId, name, bankAccount);
        setMonthlySalary(monthlySalary);
        setHealthBenefitsPremium(healthBenefitsPremium);
    }

    // Getters
    public double getMonthlySalary() {
        return monthlySalary;
    }

    public double getHealthBenefitsPremium() {
        return healthBenefitsPremium;
    }

    // Setters with validation
    public void setMonthlySalary(double monthlySalary) {
        if (monthlySalary < 15000) {
            throw new IllegalArgumentException("Monthly salary must be at least 15000");
        }
        this.monthlySalary = monthlySalary;
    }

    public void setHealthBenefitsPremium(double healthBenefitsPremium) {
        if (healthBenefitsPremium < 0) {
            throw new IllegalArgumentException("Health benefits premium cannot be negative");
        }
        this.healthBenefitsPremium = healthBenefitsPremium;
    }

    /**
     * Calculate net pay for a full-time employee.
     * Net pay = Monthly Salary - Health Benefits Premium
     *
     * @return the calculated net pay
     */
    @Override
    public double calculateNetPay() {
        return monthlySalary - healthBenefitsPremium;
    }
}
