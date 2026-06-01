package com.payroll.model;

public class CommdSalesperson extends Employee {
    private double basePay;
    private double totalSales;
    private double commissionRate;

    /**
     * Constructor to initialize a Commission-Based Salesperson.
     *
     * @param employeeId the employee ID (cannot be null or empty)
     * @param name the employee name (cannot be null or empty)
     * @param bankAccount the bank account number (cannot be null or empty)
     * @param basePay the base pay (cannot be negative)
     * @param totalSales the total sales (cannot be negative)
     * @param commissionRate the commission rate (must be between 0.0 and 1.0)
     * @throws IllegalArgumentException if validation fails
     */
    public CommdSalesperson(String employeeId, String name, String bankAccount,
                            double basePay, double totalSales, double commissionRate) {
        super(employeeId, name, bankAccount);
        setBasePay(basePay);
        setTotalSales(totalSales);
        setCommissionRate(commissionRate);
    }

    // Getters
    public double getBasePay() {
        return basePay;
    }

    public double getTotalSales() {
        return totalSales;
    }

    public double getCommissionRate() {
        return commissionRate;
    }

    // Setters with validation
    public void setBasePay(double basePay) {
        if (basePay < 0) {
            throw new IllegalArgumentException("Base pay cannot be negative");
        }
        this.basePay = basePay;
    }

    public void setTotalSales(double totalSales) {
        if (totalSales < 0) {
            throw new IllegalArgumentException("Total sales cannot be negative");
        }
        this.totalSales = totalSales;
    }

    public void setCommissionRate(double commissionRate) {
        if (commissionRate < 0 || commissionRate > 1.0) {
            throw new IllegalArgumentException("Commission rate must be between 0.0 and 1.0");
        }
        this.commissionRate = commissionRate;
    }

    /**
     * Calculate net pay for a commission-based salesperson.
     * Net pay = Base Pay + (Total Sales * Commission Rate)
     *
     * @return the calculated net pay
     */
    @Override
    public double calculateNetPay() {
        return basePay + (totalSales * commissionRate);
    }
}
