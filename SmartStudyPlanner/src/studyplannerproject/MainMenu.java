package studyplannerproject;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.time.*;
import java.time.format.*;

public class MainMenu extends JFrame {

    // ── Dashboard labels ─────────────────────────────────────────
    private JLabel lblTotalTasks, lblPendingTasks, lblExams, lblStudyHours, lblStreak;

    private boolean darkMode = false;
    private JPanel mainPanel;
    private JPanel sidebar;
    private JPanel topBar;
    private JPanel welcomePanel;

    // Colours
    private static final Color LIGHT_BG  = new Color(245, 248, 255);
    private static final Color SIDEBAR   = new Color(63,  81, 181);
    public MainMenu() {
        super("📚 Smart Study Planner");
        FileManager.loadData();
        buildUI();
        updateDashboard();
        checkDeadlines();
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(980, 700);
        setLocationRelativeTo(null);
    }

    public void updateDashboard() {
        int total = DataStore.tasks.size();
        int pending = 0, exams = 0;
        for (Task t : DataStore.tasks) {
            if (!t.isCompleted()) pending++;
            if (t.getType().equalsIgnoreCase("Exam")) exams++;
        }
        lblTotalTasks.setText(String.valueOf(total));
        lblPendingTasks.setText(String.valueOf(pending));
        lblExams.setText(String.valueOf(exams));
        lblStudyHours.setText(String.format("%.1f h", DataStore.studyHours));
        lblStreak.setText(DataStore.studyStreakDays + " 🔥");
    }

    public void addTask(Task task) {
        DataStore.tasks.add(task);
        updateDashboard();
    }

    private void buildUI() {
        setBackground(LIGHT_BG);
        setLayout(new BorderLayout());
        topBar = new JPanel(new BorderLayout());
        topBar.setBackground(SIDEBAR);
        topBar.setBorder(new EmptyBorder(12, 20, 12, 20));

        JLabel title = new JLabel("📚  Smart Study Planner");
        title.setFont(new Font("Segoe UI Historic", Font.BOLD, 24));
        title.setForeground(new Color(255, 214, 64));
        topBar.add(title, BorderLayout.WEST);

        JLabel dateLabel = new JLabel(LocalDate.now().format(
                DateTimeFormatter.ofPattern("EEE, dd MMM yyyy")));
        dateLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        dateLabel.setForeground(new Color(200, 210, 255));
        topBar.add(dateLabel, BorderLayout.EAST);
        add(topBar, BorderLayout.NORTH);

        // ── SIDEBAR ───────────────────────────────────────────────
        sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(new Color(25, 35, 95));
        sidebar.setBorder(new EmptyBorder(14, 8, 14, 8));
        sidebar.setPreferredSize(new Dimension(240, 0));

        String[][] buttons = {
            {"➕ Add Subject",     "addSubject"},
            {"📋 Add Task",        "addTask"},
            {"👁 View Tasks",       "viewTasks"},
            {"📅 Calendar",        "calendar"},
            {"✅ Daily To-Do",     "todo"},
            {"⏱ Pomodoro Timer",  "pomodoro"},
            {"📓 Study Notes",     "notes"},
            {"🎵 Study Playlist",  "playlist"},
            {"📊 Statistics",      "stats"},
            {"📈 Progress Report", "progress"},
            {"🏃 Habit Tracker",   "habits"},
            {"🌙 Dark Mode",       "darkMode"},
            {"[] Goal Tracker",     "goaltracker"},
            {"🚪 Exit",            "exit"}
        };

        for (String[] btn : buttons) {
            sidebar.add(sidebarBtn(btn[0], btn[1]));
            sidebar.add(Box.createVerticalStrut(4));
        }

        JScrollPane sideScroll = new JScrollPane(sidebar);
        sideScroll.setBorder(null);
        sideScroll.getVerticalScrollBar().setUnitIncrement(16);
        add(sideScroll, BorderLayout.WEST);

        // ── MAIN CONTENT ──────────────────────────────────────────
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(LIGHT_BG);

        // Welcome + dashboard cards
        welcomePanel = new JPanel(new BorderLayout(0, 14));
       welcomePanel.setBackground(LIGHT_BG);
        welcomePanel.setBorder(new EmptyBorder(22, 24, 14, 24));

        JLabel lblWelcome = new JLabel("Welcome Back! 👋");
        lblWelcome.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblWelcome.setForeground(new Color(40, 50, 120));
       welcomePanel.add(lblWelcome, BorderLayout.NORTH);

        welcomePanel.add(buildDashboardCards(), BorderLayout.CENTER);
       welcomePanel.add(buildQuickTips(),       BorderLayout.SOUTH);

        mainPanel.add(welcomePanel, BorderLayout.CENTER);
        add(mainPanel, BorderLayout.CENTER);
    }

    private JPanel buildDashboardCards() {
        JPanel cards = new JPanel(new GridLayout(1, 5, 12, 0));
        cards.setBackground(LIGHT_BG);

        lblTotalTasks   = new JLabel("0", SwingConstants.CENTER);
        lblPendingTasks = new JLabel("0", SwingConstants.CENTER);
        lblExams        = new JLabel("0", SwingConstants.CENTER);
        lblStudyHours   = new JLabel("0.0 h", SwingConstants.CENTER);
        lblStreak       = new JLabel("0 🔥", SwingConstants.CENTER);

        cards.add(dashCard("📋 Total Tasks",   lblTotalTasks,   new Color(100, 150, 255)));
        cards.add(dashCard("⏳ Pending",        lblPendingTasks, new Color(255, 160,  80)));
        cards.add(dashCard("📝 Exams",          lblExams,        new Color(255,  80,  80)));
        cards.add(dashCard("⏱ Study Hours",    lblStudyHours,   new Color( 80, 200, 120)));
        cards.add(dashCard("🔥 Streak",         lblStreak,       new Color(220, 150,  40)));

        return cards;
    }

    private JPanel dashCard(String title, JLabel valueLabel, Color accent) {
        JPanel card = new JPanel(new BorderLayout(0, 6));
        card.setBackground(Color.WHITE);
        card.setBorder(new CompoundBorder(
                new LineBorder(new Color(220, 225, 245), 1, true),
                new EmptyBorder(16, 12, 16, 12)));

        JLabel lTitle = new JLabel(title, SwingConstants.CENTER);
        lTitle.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lTitle.setForeground(accent);

        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        valueLabel.setForeground(new Color(30, 30, 60));

        // coloured top stripe
        JPanel stripe = new JPanel();
        stripe.setBackground(accent);
        stripe.setPreferredSize(new Dimension(0, 5));

        card.add(stripe,     BorderLayout.NORTH);
        card.add(lTitle,     BorderLayout.CENTER);
        card.add(valueLabel, BorderLayout.SOUTH);
        return card;
    }

    private JPanel buildQuickTips() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(new Color(235, 240, 255));
        p.setBorder(new CompoundBorder(
                new LineBorder(new Color(180, 200, 240), 1, true),
                new EmptyBorder(12, 16, 12, 16)));

        JLabel lbl = new JLabel("<html><b>💡 Tip:</b> Use the Pomodoro Timer for focused 25-minute study sessions. "
                + "Check Daily To-Do every morning. Update Habit Tracker each evening!</html>");
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lbl.setForeground(new Color(60, 80, 140));
        p.add(lbl, BorderLayout.CENTER);
        return p;
    }

    private JButton sidebarBtn(String text, String action) {

    JButton b = new JButton(text);

    Color normalColor = new Color(35, 45, 120);
    Color hoverColor  = new Color(25, 135, 255);

    b.setFont(new Font("Segoe UI", Font.BOLD, 14));
    b.setBackground(normalColor);
    b.setForeground(Color.WHITE);
    b.setFocusPainted(false);
    b.setBorderPainted(false);
    b.setOpaque(true);
    b.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
    b.setPreferredSize(new Dimension(200, 45));
    b.setAlignmentX(Component.LEFT_ALIGNMENT);
    b.setBorder(BorderFactory.createEmptyBorder(
            10, 15, 10, 15));
    b.setCursor(new Cursor(Cursor.HAND_CURSOR));
    b.setHorizontalAlignment(SwingConstants.LEFT);
    b.addMouseListener(new MouseAdapter() {
        @Override
        public void mouseEntered(MouseEvent e) {
            b.setBackground(hoverColor);
        }
        @Override
        public void mouseExited(MouseEvent e) {
            b.setBackground(normalColor);
        }
    });
    b.addActionListener(e -> handleAction(action));
    return b;
}

    private void handleAction(String action) {
        switch (action) {
            case "addSubject": new AddSubjectForm().setVisible(true); this.dispose(); break;
            case "addTask":    new AddTaskForm().setVisible(true); break;
            case "viewTasks":  new ViewTasksForm(this).setVisible(true); break;
            case "calendar":   new MonthlyCalendarForm().setVisible(true); break;
            case "todo":       new DailyTodoForm().setVisible(true); break;
            case "pomodoro":   new PomodoroTimerForm(this).setVisible(true); break;
            case "notes":      new StudyNotesForm().setVisible(true); break;
            case "playlist":   new StudyPlaylistForm().setVisible(true); break;
            case "stats":      new StatisticsForm().setVisible(true); break;
            case "progress":   new ProgressReportForm().setVisible(true); break;
            case "habits":     new HabitTrackerForm().setVisible(true); break;
            case "darkMode":   toggleDarkMode(); break;
            case "goaltracker": new GoalTrackerForm().setVisible(true); break;
            case "exit":       System.exit(0); break;
        }
    }

  private void toggleDarkMode() {

    darkMode = !darkMode;

    Color bg = darkMode
            ? new Color(28, 30, 50)
            : LIGHT_BG;


    mainPanel.setBackground(bg);

    if (welcomePanel != null)
        welcomePanel.setBackground(bg);

    if (sidebar != null)
        sidebar.setBackground(
                darkMode
                ? new Color(15, 20, 45)
                : new Color(25, 35, 95));

    if (topBar != null)
        topBar.setBackground(
                darkMode
                ? new Color(20, 22, 40)
                : SIDEBAR);

    repaint();
}

    private void checkDeadlines() {
        LocalDate today = LocalDate.now();
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        for (Task task : DataStore.tasks) {
            try {
                LocalDate d = LocalDate.parse(task.getDeadline(), fmt);
                long days = java.time.temporal.ChronoUnit.DAYS.between(today, d);
                if (days <= 1 && !task.isCompleted()) {
                    SoundPlayer.playSound("alarm.wav");
                    JOptionPane.showMessageDialog(this,
                            "⚠ Upcoming Deadline:\n" + task.getTitle(),
                            "Deadline Alert", JOptionPane.WARNING_MESSAGE);
                }
            } catch (Exception ignored) {}
        }
    }

    // Override to use full path
     static long daysBetween(LocalDate a, LocalDate b) {
        return java.time.temporal.ChronoUnit.DAYS.between(a, b);
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception ignored) {}
        SwingUtilities.invokeLater(() -> new MainMenu().setVisible(true));
    }
}