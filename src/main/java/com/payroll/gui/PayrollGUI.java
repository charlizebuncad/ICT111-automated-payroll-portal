package com.payroll.gui;

import com.payroll.model.*;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.ArrayList;

public class PayrollGUI extends JFrame {

    // ── Data ────────────────────────────────────────────────────────────────
    private final ArrayList<Employee> workforce = new ArrayList<>();

    // ── Table ───────────────────────────────────────────────────────────────
    private DefaultTableModel tableModel;
    private JTable employeeTable;

    // ── Footer ──────────────────────────────────────────────────────────────
    private JLabel totalPayoutLabel;
    private JLabel employeeCountLabel;

    // ── Colours ─────────────────────────────────────────────────────────────
    private static final Color C_DARK_BLUE   = new Color(30,  60,  114);
    private static final Color C_LIGHT_BLUE  = new Color(173, 216, 230);
    private static final Color C_GREEN       = new Color(40,  167, 69);
    private static final Color C_RED         = new Color(220, 53,  69);
    private static final Color C_BLUE        = new Color(0,   123, 255);
    private static final Color C_ROW_ALT     = new Color(240, 248, 255);

    private static final String[] COLUMNS =
        {"Employee ID", "Name", "Bank Account", "Type", "Net Pay"};

    // ════════════════════════════════════════════════════════════════════════
    //  Construction
    // ════════════════════════════════════════════════════════════════════════
    public PayrollGUI() {
        super("Automated Payroll System");
        initComponents();
        loadSampleData();
        refreshTable();
    }

    // ════════════════════════════════════════════════════════════════════════
    //  UI Layout
    // ════════════════════════════════════════════════════════════════════════
    private void initComponents() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(950, 600);
        setMinimumSize(new Dimension(750, 450));
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        add(buildHeader(),  BorderLayout.NORTH);
        add(buildCenter(),  BorderLayout.CENTER);
        add(buildFooter(),  BorderLayout.SOUTH);
    }

    // ── Header ──────────────────────────────────────────────────────────────
    private JPanel buildHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(C_DARK_BLUE);
        header.setBorder(new EmptyBorder(15, 22, 15, 22));

        JLabel title = new JLabel("Automated Payroll System");
        title.setFont(new Font("SansSerif", Font.BOLD, 22));
        title.setForeground(Color.WHITE);

        JLabel subtitle = new JLabel("Manage your workforce and payroll calculations");
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 13));
        subtitle.setForeground(new Color(180, 210, 255));

        JPanel titles = new JPanel(new GridLayout(2, 1, 0, 2));
        titles.setOpaque(false);
        titles.add(title);
        titles.add(subtitle);
        header.add(titles, BorderLayout.CENTER);
        return header;
    }

    // ── Centre (toolbar + table) ─────────────────────────────────────────────
    private JPanel buildCenter() {
        JPanel center = new JPanel(new BorderLayout());
        center.add(buildToolBar(), BorderLayout.NORTH);
        center.add(buildTable(),   BorderLayout.CENTER);
        return center;
    }

    private JToolBar buildToolBar() {
        JToolBar bar = new JToolBar();
        bar.setFloatable(false);
        bar.setBorder(new EmptyBorder(8, 12, 8, 12));
        bar.setBackground(new Color(245, 245, 245));

        JButton addBtn    = styledBtn("+ Add Employee",   C_GREEN,               e -> showAddDialog());
        JButton removeBtn = styledBtn("Remove Selected",  C_RED,                 e -> removeSelected());
        JButton editBtn   = styledBtn("Edit Selected",    new Color(255, 140, 0), e -> editSelected());
        JButton reportBtn = styledBtn("Generate Report",  C_BLUE,                e -> showReport());

        bar.add(addBtn);
        bar.addSeparator(new Dimension(10, 0));
        bar.add(removeBtn);
        bar.addSeparator(new Dimension(10, 0));
        bar.add(editBtn);
        bar.addSeparator(new Dimension(10, 0));
        bar.add(reportBtn);
        return bar;
    }

    private JScrollPane buildTable() {
        tableModel = new DefaultTableModel(COLUMNS, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        employeeTable = new JTable(tableModel);
        employeeTable.setRowHeight(30);
        employeeTable.setFont(new Font("SansSerif", Font.PLAIN, 13));
        employeeTable.setSelectionBackground(C_LIGHT_BLUE);
        employeeTable.setGridColor(new Color(220, 220, 220));
        employeeTable.setShowGrid(true);
        employeeTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Double-click to edit
        employeeTable.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2 && employeeTable.getSelectedRow() != -1) {
                    editSelected();
                }
            }
        });

        // Header styling
        JTableHeader th = employeeTable.getTableHeader();
        th.setBackground(C_DARK_BLUE);
        th.setForeground(Color.WHITE);
        th.setFont(new Font("SansSerif", Font.BOLD, 13));
        th.setPreferredSize(new Dimension(th.getWidth(), 36));
        th.setReorderingAllowed(false);

        // Alternating row renderer
        employeeTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object v,
                    boolean sel, boolean foc, int row, int col) {
                Component c = super.getTableCellRendererComponent(t, v, sel, foc, row, col);
                if (!sel) {
                    c.setBackground(row % 2 == 0 ? Color.WHITE : C_ROW_ALT);
                    c.setForeground(Color.BLACK);
                }
                // Right-align Net Pay column
                if (col == 4) ((JLabel) c).setHorizontalAlignment(SwingConstants.RIGHT);
                else          ((JLabel) c).setHorizontalAlignment(SwingConstants.LEFT);
                return c;
            }
        });

        // Column widths
        int[] widths = {100, 180, 140, 160, 110};
        for (int i = 0; i < widths.length; i++) {
            employeeTable.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);
        }

        JScrollPane sp = new JScrollPane(employeeTable);
        sp.setBorder(new EmptyBorder(0, 10, 0, 10));
        return sp;
    }

    // ── Footer ──────────────────────────────────────────────────────────────
    private JPanel buildFooter() {
        JPanel footer = new JPanel(new BorderLayout());
        footer.setBackground(C_DARK_BLUE);
        footer.setBorder(new EmptyBorder(8, 20, 8, 20));

        employeeCountLabel = new JLabel("Employees: 0");
        employeeCountLabel.setFont(new Font("SansSerif", Font.PLAIN, 13));
        employeeCountLabel.setForeground(new Color(180, 210, 255));

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        right.setOpaque(false);

        JLabel lbl = new JLabel("Total Company Payout:");
        lbl.setFont(new Font("SansSerif", Font.BOLD, 15));
        lbl.setForeground(Color.WHITE);

        totalPayoutLabel = new JLabel("$0.00");
        totalPayoutLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        totalPayoutLabel.setForeground(new Color(144, 238, 144));

        right.add(lbl);
        right.add(totalPayoutLabel);

        footer.add(employeeCountLabel, BorderLayout.WEST);
        footer.add(right,             BorderLayout.EAST);
        return footer;
    }

    // ════════════════════════════════════════════════════════════════════════
    //  Data Helpers
    // ════════════════════════════════════════════════════════════════════════
    private void loadSampleData() {
        workforce.add(new FTEmployee(
                "EMP001", "John Smith", "1234567890", 25000.0, 2000.0));
        workforce.add(new HourlyContractor(
                "EMP002", "Jane Doe", "0987654321", 50.0, 45.0));
        workforce.add(new CommdSalesperson(
                "EMP003", "Robert Johnson", "5555555555", 5000.0, 50000.0, 0.10));
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        double total = 0;
        for (Employee emp : workforce) {
            double net = emp.calculateNetPay();
            total += net;
            tableModel.addRow(new Object[]{
                emp.getEmployeeId(),
                emp.getName(),
                emp.getBankAccount(),
                typeLabel(emp),
                String.format("$%.2f", net)
            });
        }
        totalPayoutLabel.setText(String.format("$%.2f", total));
        employeeCountLabel.setText("Employees: " + workforce.size());
    }

    private static String typeLabel(Employee e) {
        if (e instanceof FTEmployee)       return "Full-Time";
        if (e instanceof HourlyContractor) return "Hourly Contractor";
        if (e instanceof CommdSalesperson) return "Comm. Salesperson";
        return "Unknown";
    }

    // ════════════════════════════════════════════════════════════════════════
    //  Actions
    // ════════════════════════════════════════════════════════════════════════
    private void removeSelected() {
        int row = employeeTable.getSelectedRow();
        if (row == -1) {
            warn("Please select an employee to remove.", "No Selection");
            return;
        }
        String id   = (String) tableModel.getValueAt(row, 0);
        String name = (String) tableModel.getValueAt(row, 1);
        int ok = JOptionPane.showConfirmDialog(this,
                "Remove " + name + " (" + id + ")?",
                "Confirm Remove", JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);
        if (ok == JOptionPane.YES_OPTION) {
            workforce.removeIf(e -> e.getEmployeeId().equals(id));
            refreshTable();
        }
    }

    private void editSelected() {
        int row = employeeTable.getSelectedRow();
        if (row == -1) {
            warn("Please select an employee to edit.", "No Selection");
            return;
        }
        String id = (String) tableModel.getValueAt(row, 0);
        for (int i = 0; i < workforce.size(); i++) {
            if (workforce.get(i).getEmployeeId().equals(id)) {
                showEditDialog(workforce.get(i), i);
                return;
            }
        }
    }

    // ════════════════════════════════════════════════════════════════════════
    //  Add Employee Dialog
    // ════════════════════════════════════════════════════════════════════════
    private void showAddDialog() {
        JDialog dlg = new JDialog(this, "Add New Employee", true);
        dlg.setSize(460, 500);
        dlg.setLocationRelativeTo(this);
        dlg.setLayout(new BorderLayout());
        dlg.setResizable(false);

        // Dialog header
        JPanel dh = new JPanel();
        dh.setBackground(C_DARK_BLUE);
        dh.setBorder(new EmptyBorder(12, 16, 12, 16));
        JLabel dht = new JLabel("Add New Employee");
        dht.setFont(new Font("SansSerif", Font.BOLD, 16));
        dht.setForeground(Color.WHITE);
        dh.add(dht);
        dlg.add(dh, BorderLayout.NORTH);

        // ── Common fields ──
        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(new EmptyBorder(16, 20, 0, 20));
        GridBagConstraints g = new GridBagConstraints();
        g.fill = GridBagConstraints.HORIZONTAL;
        g.insets = new Insets(5, 5, 5, 5);
        g.weightx = 1.0;

        String[] typeOptions = {
            "Full-Time Employee",
            "Hourly Contractor",
            "Commission Salesperson"
        };
        JComboBox<String> typeCombo = new JComboBox<>(typeOptions);

        JTextField idField   = new JTextField();
        JTextField nameField = new JTextField();
        JTextField bankField = new JTextField();

        addRow(form, g, 0, "Employee Type:", typeCombo);
        addRow(form, g, 1, "Employee ID:",   idField);
        addRow(form, g, 2, "Full Name:",     nameField);
        addRow(form, g, 3, "Bank Account:",  bankField);

        // ── Dynamic pay-detail fields ──
        JPanel payPanel = new JPanel(new GridBagLayout());
        payPanel.setBorder(new TitledBorder(
                new LineBorder(C_DARK_BLUE), "Pay Details",
                TitledBorder.LEFT, TitledBorder.TOP,
                new Font("SansSerif", Font.BOLD, 12), C_DARK_BLUE));

        JLabel   l1 = new JLabel(), l2 = new JLabel(), l3 = new JLabel();
        JTextField f1 = new JTextField(), f2 = new JTextField(), f3 = new JTextField();

        GridBagConstraints pg = new GridBagConstraints();
        pg.fill = GridBagConstraints.HORIZONTAL;
        pg.insets = new Insets(5, 5, 5, 5);
        pg.weightx = 1.0;
        addRow(payPanel, pg, 0, l1, f1);
        addRow(payPanel, pg, 1, l2, f2);
        addRow(payPanel, pg, 2, l3, f3);

        Runnable updatePay = () -> {
            int idx = typeCombo.getSelectedIndex();
            if (idx == 0) {
                l1.setText("Monthly Salary ($):"); l1.setVisible(true);  f1.setVisible(true);
                l2.setText("Health Benefits Premium ($):"); l2.setVisible(true);  f2.setVisible(true);
                l3.setText(""); l3.setVisible(false); f3.setVisible(false); f3.setText("");
            } else if (idx == 1) {
                l1.setText("Hourly Rate ($):"); l1.setVisible(true);  f1.setVisible(true);
                l2.setText("Hours Worked:"); l2.setVisible(true);  f2.setVisible(true);
                l3.setText(""); l3.setVisible(false); f3.setVisible(false); f3.setText("");
            } else {
                l1.setText("Base Pay ($):"); l1.setVisible(true);  f1.setVisible(true);
                l2.setText("Total Sales ($):"); l2.setVisible(true);  f2.setVisible(true);
                l3.setText("Commission Rate (0.00 – 1.00):"); l3.setVisible(true); f3.setVisible(true);
            }
            payPanel.revalidate(); payPanel.repaint();
        };
        updatePay.run();
        typeCombo.addActionListener(e -> { f1.setText(""); f2.setText(""); f3.setText(""); updatePay.run(); });

        g.gridx = 0; g.gridy = 4; g.gridwidth = 2;
        g.insets = new Insets(12, 5, 5, 5);
        form.add(payPanel, g);

        dlg.add(form, BorderLayout.CENTER);

        // ── Buttons ──
        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 12));
        JButton cancelBtn = new JButton("Cancel");
        cancelBtn.setFocusPainted(false);
        cancelBtn.addActionListener(e -> dlg.dispose());

        JButton saveBtn = styledBtn("Add Employee", C_GREEN, e -> {
            try {
                String id   = idField.getText().trim();
                String nm   = nameField.getText().trim();
                String bank = bankField.getText().trim();

                if (id.isEmpty() || nm.isEmpty() || bank.isEmpty()) {
                    error(dlg, "Please fill in all common fields.", "Validation Error");
                    return;
                }
                for (Employee ex : workforce) {
                    if (ex.getEmployeeId().equalsIgnoreCase(id)) {
                        error(dlg, "Employee ID '" + id + "' already exists.", "Duplicate ID");
                        return;
                    }
                }

                int t = typeCombo.getSelectedIndex();
                if ((t == 0 || t == 1) && (f1.getText().trim().isEmpty() || f2.getText().trim().isEmpty())) {
                    error(dlg, "Please fill in all pay detail fields.", "Validation Error");
                    return;
                }
                if (t == 2 && (f1.getText().trim().isEmpty() || f2.getText().trim().isEmpty() || f3.getText().trim().isEmpty())) {
                    error(dlg, "Please fill in all pay detail fields.", "Validation Error");
                    return;
                }

                Employee emp;
                if (t == 0) {
                    emp = new FTEmployee(id, nm, bank,
                            Double.parseDouble(f1.getText().trim()),
                            Double.parseDouble(f2.getText().trim()));
                } else if (t == 1) {
                    emp = new HourlyContractor(id, nm, bank,
                            Double.parseDouble(f1.getText().trim()),
                            Double.parseDouble(f2.getText().trim()));
                } else {
                    emp = new CommdSalesperson(id, nm, bank,
                            Double.parseDouble(f1.getText().trim()),
                            Double.parseDouble(f2.getText().trim()),
                            Double.parseDouble(f3.getText().trim()));
                }

                workforce.add(emp);
                refreshTable();
                dlg.dispose();
                info("Employee added successfully!", "Success");

            } catch (NumberFormatException ex) {
                error(dlg, "Please enter valid numeric values for pay fields.", "Input Error");
            } catch (IllegalArgumentException ex) {
                error(dlg, ex.getMessage(), "Validation Error");
            }
        });

        btnRow.add(cancelBtn);
        btnRow.add(saveBtn);
        dlg.add(btnRow, BorderLayout.SOUTH);

        dlg.setVisible(true);
    }

    // ════════════════════════════════════════════════════════════════════════
    //  Edit Employee Dialog
    // ════════════════════════════════════════════════════════════════════════
    private void showEditDialog(Employee emp, int index) {
        JDialog dlg = new JDialog(this, "Edit Employee", true);
        dlg.setSize(460, 500);
        dlg.setLocationRelativeTo(this);
        dlg.setLayout(new BorderLayout());
        dlg.setResizable(false);

        // Dialog header
        JPanel dh = new JPanel();
        dh.setBackground(new Color(255, 140, 0));
        dh.setBorder(new EmptyBorder(12, 16, 12, 16));
        JLabel dht = new JLabel("Edit Employee");
        dht.setFont(new Font("SansSerif", Font.BOLD, 16));
        dht.setForeground(Color.WHITE);
        dh.add(dht);
        dlg.add(dh, BorderLayout.NORTH);

        // Determine type index (locked — type cannot change)
        final int typeIdx = emp instanceof FTEmployee ? 0 :
                            emp instanceof HourlyContractor ? 1 : 2;
        String[] typeOptions = {"Full-Time Employee", "Hourly Contractor", "Commission Salesperson"};
        JComboBox<String> typeCombo = new JComboBox<>(typeOptions);
        typeCombo.setSelectedIndex(typeIdx);
        typeCombo.setEnabled(false);

        JTextField idField   = new JTextField(emp.getEmployeeId());
        idField.setEnabled(false);
        idField.setBackground(new Color(230, 230, 230));
        JTextField nameField = new JTextField(emp.getName());
        JTextField bankField = new JTextField(emp.getBankAccount());

        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(new EmptyBorder(16, 20, 0, 20));
        GridBagConstraints g = new GridBagConstraints();
        g.fill = GridBagConstraints.HORIZONTAL;
        g.insets = new Insets(5, 5, 5, 5);
        g.weightx = 1.0;

        addRow(form, g, 0, "Employee Type:", typeCombo);
        addRow(form, g, 1, "Employee ID:",   idField);
        addRow(form, g, 2, "Full Name:",     nameField);
        addRow(form, g, 3, "Bank Account:",  bankField);

        // Dynamic pay-detail fields
        JPanel payPanel = new JPanel(new GridBagLayout());
        payPanel.setBorder(new TitledBorder(
                new LineBorder(C_DARK_BLUE), "Pay Details",
                TitledBorder.LEFT, TitledBorder.TOP,
                new Font("SansSerif", Font.BOLD, 12), C_DARK_BLUE));

        JLabel    l1 = new JLabel(), l2 = new JLabel(), l3 = new JLabel();
        JTextField f1 = new JTextField(), f2 = new JTextField(), f3 = new JTextField();

        GridBagConstraints pg = new GridBagConstraints();
        pg.fill = GridBagConstraints.HORIZONTAL;
        pg.insets = new Insets(5, 5, 5, 5);
        pg.weightx = 1.0;
        addRow(payPanel, pg, 0, l1, f1);
        addRow(payPanel, pg, 1, l2, f2);
        addRow(payPanel, pg, 2, l3, f3);

        // Pre-fill pay fields
        if (emp instanceof FTEmployee) {
            FTEmployee ft = (FTEmployee) emp;
            l1.setText("Monthly Salary ($):");         f1.setText(String.valueOf(ft.getMonthlySalary()));
            l2.setText("Health Benefits Premium ($):"); f2.setText(String.valueOf(ft.getHealthBenefitsPremium()));
            l3.setVisible(false); f3.setVisible(false);
        } else if (emp instanceof HourlyContractor) {
            HourlyContractor hc = (HourlyContractor) emp;
            l1.setText("Hourly Rate ($):"); f1.setText(String.valueOf(hc.getHourlyRate()));
            l2.setText("Hours Worked:");    f2.setText(String.valueOf(hc.getHoursWorked()));
            l3.setVisible(false); f3.setVisible(false);
        } else {
            CommdSalesperson cs = (CommdSalesperson) emp;
            l1.setText("Base Pay ($):");                   f1.setText(String.valueOf(cs.getBasePay()));
            l2.setText("Total Sales ($):");                f2.setText(String.valueOf(cs.getTotalSales()));
            l3.setText("Commission Rate (0.00 \u2013 1.00):"); f3.setText(String.valueOf(cs.getCommissionRate()));
        }

        g.gridx = 0; g.gridy = 4; g.gridwidth = 2;
        g.insets = new Insets(12, 5, 5, 5);
        form.add(payPanel, g);
        dlg.add(form, BorderLayout.CENTER);

        // Buttons
        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 12));
        JButton cancelBtn = new JButton("Cancel");
        cancelBtn.setFocusPainted(false);
        cancelBtn.addActionListener(e -> dlg.dispose());

        JButton saveBtn = styledBtn("Save Changes", new Color(255, 140, 0), e -> {
            try {
                String nm   = nameField.getText().trim();
                String bank = bankField.getText().trim();
                if (nm.isEmpty() || bank.isEmpty()) {
                    error(dlg, "Please fill in all common fields.", "Validation Error");
                    return;
                }
                if (f1.getText().trim().isEmpty() || f2.getText().trim().isEmpty()) {
                    error(dlg, "Please fill in all pay detail fields.", "Validation Error");
                    return;
                }
                if (typeIdx == 2 && f3.getText().trim().isEmpty()) {
                    error(dlg, "Please fill in all pay detail fields.", "Validation Error");
                    return;
                }

                String id = emp.getEmployeeId();
                Employee updated;
                if (typeIdx == 0) {
                    updated = new FTEmployee(id, nm, bank,
                            Double.parseDouble(f1.getText().trim()),
                            Double.parseDouble(f2.getText().trim()));
                } else if (typeIdx == 1) {
                    updated = new HourlyContractor(id, nm, bank,
                            Double.parseDouble(f1.getText().trim()),
                            Double.parseDouble(f2.getText().trim()));
                } else {
                    updated = new CommdSalesperson(id, nm, bank,
                            Double.parseDouble(f1.getText().trim()),
                            Double.parseDouble(f2.getText().trim()),
                            Double.parseDouble(f3.getText().trim()));
                }

                workforce.set(index, updated);
                refreshTable();
                dlg.dispose();
                info("Employee updated successfully!", "Success");

            } catch (NumberFormatException ex) {
                error(dlg, "Please enter valid numeric values for pay fields.", "Input Error");
            } catch (IllegalArgumentException ex) {
                error(dlg, ex.getMessage(), "Validation Error");
            }
        });

        btnRow.add(cancelBtn);
        btnRow.add(saveBtn);
        dlg.add(btnRow, BorderLayout.SOUTH);
        dlg.setVisible(true);
    }

    // ════════════════════════════════════════════════════════════════════════
    //  Payroll Report Dialog
    // ════════════════════════════════════════════════════════════════════════
    private void showReport() {
        if (workforce.isEmpty()) {
            info("No employees in the system.", "Empty Workforce");
            return;
        }

        JDialog dlg = new JDialog(this, "Payroll Report", true);
        dlg.setSize(520, 520);
        dlg.setLocationRelativeTo(this);
        dlg.setLayout(new BorderLayout());

        // Header
        JPanel rh = new JPanel();
        rh.setBackground(C_DARK_BLUE);
        rh.setBorder(new EmptyBorder(12, 16, 12, 16));
        JLabel rht = new JLabel("Payroll Report");
        rht.setFont(new Font("SansSerif", Font.BOLD, 18));
        rht.setForeground(Color.WHITE);
        rh.add(rht);
        dlg.add(rh, BorderLayout.NORTH);

        // Report body
        StringBuilder sb = new StringBuilder();
        sb.append("===== PAYROLL REPORT =====\n\n");
        double total = 0;
        for (Employee emp : workforce) {
            double net = emp.calculateNetPay();
            total += net;
            sb.append(String.format("%-16s: %s%n",  "Employee ID",  emp.getEmployeeId()));
            sb.append(String.format("%-16s: %s%n",  "Name",         emp.getName()));
            sb.append(String.format("%-16s: %s%n",  "Bank Account", emp.getBankAccount()));
            sb.append(String.format("%-16s: %s%n",  "Type",         typeLabel(emp)));

            if (emp instanceof FTEmployee) {
                FTEmployee ft = (FTEmployee) emp;
                sb.append(String.format("%-16s: $%.2f%n", "Monthly Salary",   ft.getMonthlySalary()));
                sb.append(String.format("%-16s: $%.2f%n", "Health Premium",   ft.getHealthBenefitsPremium()));
            } else if (emp instanceof HourlyContractor) {
                HourlyContractor hc = (HourlyContractor) emp;
                sb.append(String.format("%-16s: $%.2f%n", "Hourly Rate",  hc.getHourlyRate()));
                sb.append(String.format("%-16s: %.1f hrs%n", "Hours Worked", hc.getHoursWorked()));
            } else if (emp instanceof CommdSalesperson) {
                CommdSalesperson cs = (CommdSalesperson) emp;
                sb.append(String.format("%-16s: $%.2f%n",  "Base Pay",       cs.getBasePay()));
                sb.append(String.format("%-16s: $%.2f%n",  "Total Sales",    cs.getTotalSales()));
                sb.append(String.format("%-16s: %.0f%%%n", "Commission Rate", cs.getCommissionRate() * 100));
            }

            sb.append(String.format("%-16s: $%.2f%n", "Net Pay", net));
            sb.append("------------------------------------------\n");
        }
        sb.append(String.format("%nTotal Employees    : %d%n", workforce.size()));
        sb.append(String.format("Total Payout       : $%.2f%n", total));

        JTextArea ta = new JTextArea(sb.toString());
        ta.setFont(new Font("Monospaced", Font.PLAIN, 13));
        ta.setEditable(false);
        ta.setBackground(new Color(250, 250, 250));
        ta.setCaretPosition(0);
        ta.setBorder(new EmptyBorder(10, 15, 10, 15));

        dlg.add(new JScrollPane(ta), BorderLayout.CENTER);

        JPanel rf = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton close = new JButton("Close");
        close.setFocusPainted(false);
        close.addActionListener(e -> dlg.dispose());
        rf.add(close);
        dlg.add(rf, BorderLayout.SOUTH);

        dlg.setVisible(true);
    }

    // ════════════════════════════════════════════════════════════════════════
    //  Utility
    // ════════════════════════════════════════════════════════════════════════
    private JButton styledBtn(String text, Color bg, java.awt.event.ActionListener al) {
        JButton b = new JButton(text);
        b.setFocusPainted(false);
        b.setBackground(bg);
        b.setForeground(Color.WHITE);
        b.setFont(new Font("SansSerif", Font.BOLD, 13));
        b.setBorder(new EmptyBorder(8, 16, 8, 16));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.addActionListener(al);
        return b;
    }

    // GridBagConstraints row helper – component overload
    private void addRow(JPanel p, GridBagConstraints g, int row, String label, JComponent field) {
        g.gridx = 0; g.gridy = row; g.gridwidth = 1;
        p.add(new JLabel(label), g);
        g.gridx = 1;
        p.add(field, g);
    }

    // GridBagConstraints row helper – JLabel overload (for dynamic panels)
    private void addRow(JPanel p, GridBagConstraints g, int row, JLabel label, JComponent field) {
        g.gridx = 0; g.gridy = row; g.gridwidth = 1;
        p.add(label, g);
        g.gridx = 1;
        p.add(field, g);
    }

    private void warn(String msg, String title) {
        JOptionPane.showMessageDialog(this, msg, title, JOptionPane.WARNING_MESSAGE);
    }

    private void info(String msg, String title) {
        JOptionPane.showMessageDialog(this, msg, title, JOptionPane.INFORMATION_MESSAGE);
    }

    private void error(Component parent, String msg, String title) {
        JOptionPane.showMessageDialog(parent, msg, title, JOptionPane.ERROR_MESSAGE);
    }

    // ════════════════════════════════════════════════════════════════════════
    //  Entry Point
    // ════════════════════════════════════════════════════════════════════════
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {}
            new PayrollGUI().setVisible(true);
        });
    }
}
