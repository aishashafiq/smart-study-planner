package studyplannerproject;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.time.*;
import java.time.format.*;
import java.util.*;

/**
 * Monthly Study Calendar – shows tasks on their deadline dates,
 * lets user navigate months, and click any day to see that day's tasks.
 */
public class MonthlyCalendarForm extends JFrame {

    private YearMonth currentMonth;
    private JLabel    lblMonthYear;
    private JPanel    calendarGrid;
    private JTextArea taskDetail;

    private static final Color BG          = new Color(245, 248, 255);
    private static final Color HEADER_BG   = new Color(63, 81, 181);
    private static final Color TODAY_BG    = new Color(255, 214,  64);
    private static final Color TASK_BG     = new Color(100, 210, 120);
    private static final Color EXAM_BG     = new Color(255, 100, 100);
    private static final Color CELL_BG     = Color.WHITE;
    private static final Color CELL_BORDER = new Color(210, 218, 240);

    public MonthlyCalendarForm() {
        super("📅 Monthly Study Calendar");
        currentMonth = YearMonth.now();
        buildUI();
        renderCalendar();
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(900, 680);
        setLocationRelativeTo(null);
    }

    private void buildUI() {
        setBackground(BG);
        setLayout(new BorderLayout(8, 8));

        // ── TOP ──────────────────────────────────────────────────
        JPanel top = new JPanel(new BorderLayout());
        top.setBackground(HEADER_BG);
        top.setBorder(new EmptyBorder(10, 16, 10, 16));

        JLabel title = new JLabel("📅  Monthly Study Calendar");
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(Color.WHITE);
        top.add(title, BorderLayout.WEST);

        JPanel navPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        navPanel.setOpaque(false);

        JButton btnPrev = navBtn("◀");
        JButton btnNext = navBtn("▶");
        lblMonthYear = new JLabel();
        lblMonthYear.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblMonthYear.setForeground(Color.WHITE);

        btnPrev.addActionListener(e -> { currentMonth = currentMonth.minusMonths(1); renderCalendar(); });
        btnNext.addActionListener(e -> { currentMonth = currentMonth.plusMonths(1);  renderCalendar(); });

        navPanel.add(btnPrev);
        navPanel.add(lblMonthYear);
        navPanel.add(btnNext);
        top.add(navPanel, BorderLayout.EAST);
        add(top, BorderLayout.NORTH);

        // ── CALENDAR GRID ─────────────────────────────────────────
        calendarGrid = new JPanel(new GridLayout(0, 7, 4, 4));
        calendarGrid.setBackground(BG);
        calendarGrid.setBorder(new EmptyBorder(6, 10, 6, 10));

        JScrollPane gridScroll = new JScrollPane(calendarGrid);
        gridScroll.setBorder(null);
        gridScroll.getViewport().setBackground(BG);
        add(gridScroll, BorderLayout.CENTER);

        // ── BOTTOM – task detail ──────────────────────────────────
        JPanel bottom = new JPanel(new BorderLayout(6, 0));
        bottom.setBackground(BG);
        bottom.setBorder(new EmptyBorder(4, 10, 8, 10));

        JLabel lblDetail = new JLabel("📋 Tasks for selected day:");
        lblDetail.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblDetail.setForeground(HEADER_BG);
        bottom.add(lblDetail, BorderLayout.NORTH);

        taskDetail = new JTextArea(4, 40);
        taskDetail.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        taskDetail.setEditable(false);
        taskDetail.setBackground(new Color(250, 252, 255));
        taskDetail.setBorder(new CompoundBorder(
                new LineBorder(CELL_BORDER, 1, true),
                new EmptyBorder(6, 10, 6, 10)));
        bottom.add(new JScrollPane(taskDetail), BorderLayout.CENTER);

        // Back button
        JButton btnBack = new JButton("⬅ Back");
        btnBack.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnBack.setBackground(new Color(120, 130, 160));
        btnBack.setForeground(Color.BLACK);
        btnBack.setFocusPainted(false);
        btnBack.setBorder(new EmptyBorder(7, 18, 7, 18));
        btnBack.setOpaque(true);
        btnBack.addActionListener(e -> dispose());

        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnRow.setBackground(BG);
        btnRow.add(btnBack);
        bottom.add(btnRow, BorderLayout.SOUTH);

        add(bottom, BorderLayout.SOUTH);
    }

    private void renderCalendar() {
        calendarGrid.removeAll();
        lblMonthYear.setText(currentMonth.format(
                DateTimeFormatter.ofPattern("MMMM yyyy")));

        // Day-of-week headers
        String[] days = {"Sun","Mon","Tue","Wed","Thu","Fri","Sat"};
        for (String d : days) {
            JLabel h = new JLabel(d, SwingConstants.CENTER);
            h.setFont(new Font("Segoe UI", Font.BOLD, 13));
            h.setOpaque(true);
            h.setBackground(new Color(100, 120, 200));
            h.setForeground(Color.WHITE);
            h.setBorder(new EmptyBorder(6, 0, 6, 0));
            calendarGrid.add(h);
        }

        // Build task-date map
        Map<LocalDate, java.util.List<Task>> tasksByDate = new HashMap<>();
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        for (Task t : DataStore.tasks) {
            try {
                LocalDate d = LocalDate.parse(t.getDeadline(), fmt);
                tasksByDate.computeIfAbsent(d, k -> new ArrayList<>()).add(t);
            } catch (Exception ignored) {}
        }

        LocalDate firstDay = currentMonth.atDay(1);
        int startDow = firstDay.getDayOfWeek().getValue() % 7; // Sun=0
        int daysInMonth = currentMonth.lengthOfMonth();
        LocalDate today = LocalDate.now();

        // Blank cells before day 1
        for (int i = 0; i < startDow; i++) calendarGrid.add(new JLabel());

        for (int day = 1; day <= daysInMonth; day++) {
            LocalDate date = currentMonth.atDay(day);
            java.util.List<Task> tasks = tasksByDate.getOrDefault(date, Collections.emptyList());

            JPanel cell = new JPanel();
            cell.setLayout(new BoxLayout(cell, BoxLayout.Y_AXIS));
            cell.setBackground(date.equals(today) ? TODAY_BG : CELL_BG);
            cell.setBorder(new LineBorder(CELL_BORDER, 1, true));
            cell.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

            JLabel dayNum = new JLabel(String.valueOf(day));
            dayNum.setFont(new Font("Segoe UI", Font.BOLD, 13));
            dayNum.setAlignmentX(LEFT_ALIGNMENT);
            dayNum.setBorder(new EmptyBorder(3, 5, 2, 0));
            cell.add(dayNum);

            for (Task t : tasks) {
                Color dot = t.getType().equalsIgnoreCase("Exam") ? EXAM_BG : TASK_BG;
                JLabel lbl = new JLabel("• " + abbreviate(t.getTitle(), 10));
                lbl.setFont(new Font("Segoe UI", Font.PLAIN, 10));
                lbl.setOpaque(true);
                lbl.setBackground(dot);
                lbl.setForeground(Color.WHITE);
                lbl.setBorder(new EmptyBorder(1, 4, 1, 4));
                lbl.setAlignmentX(LEFT_ALIGNMENT);
                cell.add(lbl);
            }

            final LocalDate fd = date;
            final java.util.List<Task> ft = tasks;
            cell.addMouseListener(new MouseAdapter() {
                @Override public void mouseClicked(MouseEvent e) { showDayDetail(fd, ft); }
                @Override public void mouseEntered(MouseEvent e) {
                    if (!fd.equals(today)) cell.setBackground(new Color(230, 235, 255));
                }
                @Override public void mouseExited(MouseEvent e) {
                    if (!fd.equals(today)) cell.setBackground(CELL_BG);
                }
            });

            calendarGrid.add(cell);
        }

        calendarGrid.revalidate();
        calendarGrid.repaint();
    }

    private void showDayDetail(LocalDate date, java.util.List<Task> tasks) {
        if (tasks.isEmpty()) {
            taskDetail.setText("No tasks on " + date.format(DateTimeFormatter.ofPattern("EEEE, dd MMMM yyyy")));
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append("📅 ").append(date.format(DateTimeFormatter.ofPattern("EEEE, dd MMMM yyyy"))).append("\n\n");
            for (Task t : tasks) {
                sb.append("  📌 ").append(t.getTitle())
                  .append("  [").append(t.getType()).append("]")
                  .append("  Priority: ").append(t.getPriority())
                  .append("  Status: ").append(t.getStatus()).append("\n");
            }
            taskDetail.setText(sb.toString());
        }
    }

    private String abbreviate(String s, int max) {
        return s.length() <= max ? s : s.substring(0, max) + "…";
    }

    private JButton navBtn(String text) {
        JButton b = new JButton(text);
        b.setFont(new Font("Segoe UI", Font.BOLD, 16));
        b.setForeground(Color.WHITE);
        b.setBackground(new Color(80, 100, 200));
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setOpaque(true);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return b;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MonthlyCalendarForm().setVisible(true));
    }
}