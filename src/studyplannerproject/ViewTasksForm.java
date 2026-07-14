package studyplannerproject;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.*;

/**
 * ViewTasksForm  —  modern redesign with:
 *   • Full-height styled JTable with alternating row colours
 *   • Colour-coded status column (green = done, amber = pending)
 *   • Priority badge column (coloured chips)
 *   • Confirm dialog before delete
 *   • All Step-1 bug fixes retained
 */
public class ViewTasksForm extends JFrame {

    private MainMenu mainMenu;
    private JTable   tblTasks;
    private DefaultTableModel tableModel;

    public ViewTasksForm(MainMenu mainMenu) {
        this.mainMenu = mainMenu;
        buildUI();
        loadTasks();
    }

    // ═════════════════════════════════════════════════════════════════════
    //  BUILD UI
    // ═════════════════════════════════════════════════════════════════════
    private void buildUI() {
        setTitle("View Tasks");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(820, 560);
        setLocationRelativeTo(null);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(UITheme.CONTENT_BG);

        // ── header ────────────────────────────────────────────────────────
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(UITheme.HEADER_BG);
        header.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0,0,1,0, UITheme.CARD_BORDER),
            BorderFactory.createEmptyBorder(14,24,14,24)));

        JLabel title = new JLabel("\uD83D\uDCCB  All Tasks");
        title.setFont(UITheme.FONT_PAGE_TITLE);
        title.setForeground(UITheme.TEXT_PRIMARY);

        // search / filter hint
        JLabel sub = new JLabel("Select a row, then use the buttons below.");
        sub.setFont(UITheme.FONT_BODY);
        sub.setForeground(UITheme.TEXT_SECONDARY);

        header.add(title, BorderLayout.WEST);
        header.add(sub,   BorderLayout.EAST);

        // ── table ─────────────────────────────────────────────────────────
        String[] cols = {"Title","Status","Deadline","Priority","Subject"};
        tableModel = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tblTasks = new JTable(tableModel);
        UITheme.styleTable(tblTasks);

        // column widths
        tblTasks.getColumnModel().getColumn(0).setPreferredWidth(200);
        tblTasks.getColumnModel().getColumn(1).setPreferredWidth(90);
        tblTasks.getColumnModel().getColumn(2).setPreferredWidth(100);
        tblTasks.getColumnModel().getColumn(3).setPreferredWidth(80);
        tblTasks.getColumnModel().getColumn(4).setPreferredWidth(130);

        // custom cell renderer — colours status + priority cells
        tblTasks.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object val,
                    boolean sel, boolean foc, int row, int col) {
                JLabel lbl = (JLabel) super.getTableCellRendererComponent(
                        t, val, sel, foc, row, col);
                lbl.setBorder(BorderFactory.createEmptyBorder(0,10,0,10));

                if (sel) {
                    lbl.setBackground(UITheme.ACCENT_LIGHT);
                    lbl.setForeground(UITheme.TEXT_PRIMARY);
                    return lbl;
                }

                // base alternating row colour
                Color base = (row % 2 == 0) ? UITheme.CARD_BG
                                             : new Color(250,250,253);
                lbl.setBackground(base);
                lbl.setForeground(UITheme.TEXT_PRIMARY);

                if (col == 1 && val != null) {          // Status column
                    String s = val.toString();
                    if (s.equalsIgnoreCase("Completed")) {
                        lbl.setForeground(UITheme.TINT_GREEN_T);
                        lbl.setFont(UITheme.FONT_TABLE_HEAD);
                    } else {
                        lbl.setForeground(UITheme.TINT_AMBER_T);
                        lbl.setFont(UITheme.FONT_TABLE_HEAD);
                    }
                } else if (col == 3 && val != null) {   // Priority column
                    String p = val.toString();
                    Color fg = p.equalsIgnoreCase("High")   ? UITheme.TINT_RED_T
                             : p.equalsIgnoreCase("Medium") ? UITheme.TINT_AMBER_T
                             : UITheme.TINT_GREEN_T;
                    lbl.setForeground(fg);
                    lbl.setFont(UITheme.FONT_TABLE_HEAD);
                }
                return lbl;
            }
        });

        JScrollPane scroll = new JScrollPane(tblTasks);
        scroll.setBorder(BorderFactory.createLineBorder(UITheme.CARD_BORDER,1,true));

        JPanel tableCard = UITheme.makeCard();
        tableCard.setLayout(new BorderLayout());
        tableCard.add(scroll, BorderLayout.CENTER);

        JPanel centre = new JPanel(new BorderLayout());
        centre.setBackground(UITheme.CONTENT_BG);
        centre.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
        centre.add(tableCard, BorderLayout.CENTER);

        // ── footer buttons ────────────────────────────────────────────────
        JPanel foot = new JPanel(new BorderLayout());
        foot.setBackground(UITheme.HEADER_BG);
        foot.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(1,0,0,0, UITheme.CARD_BORDER),
            BorderFactory.createEmptyBorder(12,24,12,24)));

        JButton btnDelete   = UITheme.makeDangerButton("Delete Task");
        JButton btnComplete = UITheme.makePrimaryButton("Mark Complete");
        JButton btnBack     = UITheme.makeSecondaryButton("Back");

        btnDelete  .addActionListener(e -> deleteTask());
        btnComplete.addActionListener(e -> markComplete());
        btnBack    .addActionListener(e -> goBack());

        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        btnRow.setOpaque(false);
        btnRow.add(btnBack);
        btnRow.add(btnDelete);
        btnRow.add(btnComplete);

        // task count label on the left
        JLabel countLbl = new JLabel();
        countLbl.setFont(UITheme.FONT_BODY);
        countLbl.setForeground(UITheme.TEXT_SECONDARY);
        countLbl.setText(DataStore.tasks.size() + " tasks total");
        foot.add(countLbl, BorderLayout.WEST);
        foot.add(btnRow,   BorderLayout.EAST);

        root.add(header,  BorderLayout.NORTH);
        root.add(centre,  BorderLayout.CENTER);
        root.add(foot,    BorderLayout.SOUTH);

        setContentPane(root);
    }

    // ═════════════════════════════════════════════════════════════════════
    //  LOGIC
    // ═════════════════════════════════════════════════════════════════════
    private void loadTasks() {
        tableModel.setRowCount(0);
        for (Task t : DataStore.tasks) {
            tableModel.addRow(new Object[]{
                t.getTitle(), t.getStatus(),
                t.getDeadline(), t.getPriority(), t.getSubject()
            });
        }
    }

    private void deleteTask() {
        int row = tblTasks.getSelectedRow();
        if (row < 0) {
            showWarning("Please select a task to delete.");
            return;
        }
        int ok = JOptionPane.showConfirmDialog(this,
            "Delete \"" + DataStore.tasks.get(row).getTitle() + "\"?",
            "Confirm Delete", JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);
        if (ok != JOptionPane.YES_OPTION) return;

        DataStore.tasks.remove(row);
        FileManager.saveData();
        if (mainMenu != null) mainMenu.updateDashboard();
        loadTasks();
        showSuccess("Task deleted.");
    }

    private void markComplete() {
        int row = tblTasks.getSelectedRow();
        if (row < 0) {
            showWarning("Please select a task to mark as complete.");
            return;
        }
        Task task = DataStore.tasks.get(row);
        if (task.isCompleted()) {
            showInfo("This task is already completed.");
            return;
        }
        task.setCompleted(true);
        task.setStatus("Completed");
        FileManager.saveData();
        if (mainMenu != null) mainMenu.updateDashboard();
        loadTasks();
        showSuccess("Task marked as complete!");
    }

    private void goBack() {
        if (mainMenu != null) mainMenu.setVisible(true);
        else new MainMenu().setVisible(true);
        dispose();
    }

    // small feedback helpers
    private void showWarning(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Note",
            JOptionPane.WARNING_MESSAGE);
    }
    private void showInfo(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Info",
            JOptionPane.INFORMATION_MESSAGE);
    }
    private void showSuccess(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Done",
            JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {
        UITheme.apply();
        EventQueue.invokeLater(() -> new ViewTasksForm(null).setVisible(true));
    }
}