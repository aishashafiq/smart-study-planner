package studyplannerproject;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class DailyTodoForm extends JFrame {

    private JPanel         todoPanel;
    private JTextField     inputField;
    private JProgressBar   progressBar;
    private JLabel         lblProgress;
    private JLabel         lblDate;

    private final java.util.List<TodoItem> items = new ArrayList<>();

    private static final Color BG       = new Color(245, 248, 255);
    private static final Color HDR      = new Color(63, 81, 181);
    private static final Color ADD_BTN  = new Color(76, 175, 80);
    private static final Color DONE_BG  = new Color(232, 255, 237);
    private static final Color ITEM_BG  = Color.WHITE;

    public DailyTodoForm() {
        super("✅ Daily To-Do List");
        buildUI();
        loadTodaysTasks();
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(560, 650);
        setLocationRelativeTo(null);
    }

    private void buildUI() {
        setBackground(BG);
        setLayout(new BorderLayout(8, 8));

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(HDR);
        header.setBorder(new EmptyBorder(14, 18, 14, 18));

        JLabel title = new JLabel("✅  Daily To-Do");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(Color.WHITE);
        header.add(title, BorderLayout.WEST);

        lblDate = new JLabel(LocalDate.now().format(
                DateTimeFormatter.ofPattern("EEE, dd MMM yyyy")));
        lblDate.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblDate.setForeground(new Color(200, 210, 255));
        header.add(lblDate, BorderLayout.EAST);
        add(header, BorderLayout.NORTH);

        JPanel inputRow = new JPanel(new BorderLayout(8, 0));
        inputRow.setBackground(BG);
        inputRow.setBorder(new EmptyBorder(10, 14, 6, 14));

        inputField = new JTextField();
        inputField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        inputField.setBorder(new CompoundBorder(
                new LineBorder(new Color(180, 190, 230), 1, true),
                new EmptyBorder(8, 12, 8, 12)));
        inputField.addActionListener(e -> addItem());

        JButton btnAdd = new JButton("＋ Add");
        btnAdd.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnAdd.setBackground(ADD_BTN);
        btnAdd.setForeground(Color.BLACK);
        btnAdd.setFocusPainted(false);
        btnAdd.setOpaque(true);
        btnAdd.setBorder(new EmptyBorder(8, 18, 8, 18));
        btnAdd.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnAdd.addActionListener(e -> addItem());

        inputRow.add(inputField, BorderLayout.CENTER);
        inputRow.add(btnAdd, BorderLayout.EAST);
        add(inputRow, BorderLayout.NORTH); // will overlay – see below

        JPanel center = new JPanel(new BorderLayout(0, 0));
        center.setBackground(BG);

        JPanel progPanel = new JPanel(new BorderLayout(6, 0));
        progPanel.setBackground(BG);
        progPanel.setBorder(new EmptyBorder(0, 14, 4, 14));

        progressBar = new JProgressBar(0, 100);
        progressBar.setStringPainted(false);
        progressBar.setForeground(ADD_BTN);
        progressBar.setBackground(new Color(220, 235, 220));
        progressBar.setPreferredSize(new Dimension(0, 10));
        progressBar.setBorderPainted(false);

        lblProgress = new JLabel("0 / 0 tasks done");
        lblProgress.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblProgress.setForeground(new Color(100, 120, 160));
        lblProgress.setHorizontalAlignment(SwingConstants.RIGHT);

        progPanel.add(progressBar, BorderLayout.CENTER);
        progPanel.add(lblProgress, BorderLayout.EAST);

        todoPanel = new JPanel();
        todoPanel.setLayout(new BoxLayout(todoPanel, BoxLayout.Y_AXIS));
        todoPanel.setBackground(BG);
        todoPanel.setBorder(new EmptyBorder(6, 10, 6, 10));

        JScrollPane scroll = new JScrollPane(todoPanel);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(BG);

        center.add(progPanel, BorderLayout.NORTH);
        center.add(scroll,   BorderLayout.CENTER);

        JPanel wrapper = new JPanel(new BorderLayout(0, 0));
        wrapper.setBackground(BG);
        wrapper.add(inputRow, BorderLayout.NORTH);
        wrapper.add(center,   BorderLayout.CENTER);
        add(wrapper, BorderLayout.CENTER);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 8));
        bottom.setBackground(BG);

        JButton btnClearDone = new JButton("🗑 Clear Done");
        btnClearDone.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnClearDone.setBackground(new Color(220, 80, 80));
        btnClearDone.setForeground(Color.BLACK);
        btnClearDone.setFocusPainted(false);
        btnClearDone.setOpaque(true);
        btnClearDone.setBorder(new EmptyBorder(7, 14, 7, 14));
        btnClearDone.addActionListener(e -> clearDone());

        JButton btnBack = new JButton("⬅ Back");
        btnBack.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnBack.setBackground(new Color(120, 130, 160));
        btnBack.setForeground(Color.BLACK);
        btnBack.setFocusPainted(false);
        btnBack.setOpaque(true);
        btnBack.setBorder(new EmptyBorder(7, 14, 7, 14));
        btnBack.addActionListener(e -> dispose());

        bottom.add(btnClearDone);
        bottom.add(btnBack);
        add(bottom, BorderLayout.SOUTH);
    }

    private void loadTodaysTasks() {
        String today = LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        for (Task t : DataStore.tasks) {
            if (today.equals(t.getDeadline())) {
                TodoItem item = new TodoItem(t.getTitle() + " [" + t.getType() + "]");
                item.setDone(t.isCompleted());
                items.add(item);
                todoPanel.add(createRow(item));
            }
        }
        todoPanel.revalidate();
        updateProgress();
    }

    private void addItem() {
        String text = inputField.getText().trim();
        if (text.isEmpty()) return;
        TodoItem item = new TodoItem(text);
        items.add(item);
        todoPanel.add(createRow(item));
        todoPanel.revalidate();
        todoPanel.repaint();
        inputField.setText("");
        updateProgress();
    }

    private JPanel createRow(TodoItem item) {
        JPanel row = new JPanel(new BorderLayout(8, 0));
        row.setBackground(item.isDone() ? DONE_BG : ITEM_BG);
        row.setBorder(new CompoundBorder(
                new EmptyBorder(4, 0, 4, 0),
                new CompoundBorder(
                        new LineBorder(new Color(220, 225, 245), 1, true),
                        new EmptyBorder(10, 14, 10, 14))));
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 52));

        JCheckBox cb = new JCheckBox();
        cb.setSelected(item.isDone());
        cb.setBackground(item.isDone() ? DONE_BG : ITEM_BG);
        cb.setFocusPainted(false);

        JLabel lbl = new JLabel(item.isDone() ? "<html><strike>" + item.getText() + "</strike></html>"
                                              : item.getText());
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lbl.setForeground(item.isDone() ? new Color(160, 170, 180) : new Color(30, 30, 50));

        JButton btnDel = new JButton("✕");
        btnDel.setFont(new Font("Segoe UI", Font.BOLD, 11));
        btnDel.setForeground(new Color(200, 80, 80));
        btnDel.setBackground(ITEM_BG);
        btnDel.setBorderPainted(false);
        btnDel.setFocusPainted(false);
        btnDel.setOpaque(false);
        btnDel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        cb.addActionListener(e -> {
            item.setDone(cb.isSelected());
            if (item.isDone()) {
                lbl.setText("<html><strike>" + item.getText() + "</strike></html>");
                lbl.setForeground(new Color(160, 170, 180));
                row.setBackground(DONE_BG);
                cb.setBackground(DONE_BG);
            } else {
                lbl.setText(item.getText());
                lbl.setForeground(new Color(30, 30, 50));
                row.setBackground(ITEM_BG);
                cb.setBackground(ITEM_BG);
            }
            updateProgress();
        });

        btnDel.addActionListener(e -> {
            items.remove(item);
            todoPanel.remove(row);
            todoPanel.revalidate();
            todoPanel.repaint();
            updateProgress();
        });

        row.add(cb,     BorderLayout.WEST);
        row.add(lbl,    BorderLayout.CENTER);
        row.add(btnDel, BorderLayout.EAST);
        return row;
    }

    private void clearDone() {
        items.removeIf(TodoItem::isDone);
        todoPanel.removeAll();
        for (TodoItem it : items) todoPanel.add(createRow(it));
        todoPanel.revalidate();
        todoPanel.repaint();
        updateProgress();
    }

    private void updateProgress() {
        int total = items.size();
        int done  = (int) items.stream().filter(TodoItem::isDone).count();
        int pct   = total == 0 ? 0 : (done * 100) / total;
        progressBar.setValue(pct);
        lblProgress.setText(done + " / " + total + " tasks done  " + pct + "%");
    }

    private static class TodoItem {
        private final String text;
        private boolean done;
        TodoItem(String text) { this.text = text; }
        String  getText()          { return text; }
        boolean isDone()           { return done; }
        void    setDone(boolean d) { done = d; }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new DailyTodoForm().setVisible(true));
    }
}