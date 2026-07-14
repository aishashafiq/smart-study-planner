package studyplannerproject;

import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javax.swing.*;


public class HabitTrackerForm extends JFrame {

    // ── habit names (first 4 match original DataStore fields) ────────────
    private static final String[] DEFAULT_HABITS = {
        "Daily Study", "Revision", "Exercise", "Read Notes"
    };

    private boolean[][] grid;
    private String[]    habitNames;
    private int         habitCount;

    private int todayIndex;

    // UI grid rows (one JCheckBox[] per habit, 7 per row)
    private JCheckBox[][] checkBoxes;
    private JLabel[]      streakLabels;
    private JPanel        gridPanel;

    public HabitTrackerForm() {
        initData();
        buildUI();
    }

    private void initData() {
        todayIndex  = LocalDate.now().getDayOfWeek().getValue() - 1; // 0=Mon
        habitCount  = DEFAULT_HABITS.length;
        habitNames  = DEFAULT_HABITS.clone();

        grid = new boolean[habitCount][7];
        // pre-populate from DataStore fields for the 4 built-in habits
        if (DataStore.studyHabit)    grid[0][todayIndex] = true;
        if (DataStore.revisionHabit) grid[1][todayIndex] = true;
        if (DataStore.exerciseHabit) grid[2][todayIndex] = true;
        if (DataStore.notesHabit)    grid[3][todayIndex] = true;
    }

    private void buildUI() {
        setTitle("Habit Tracker");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(700, 520);
        setLocationRelativeTo(null);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(UITheme.CONTENT_BG);

        // ── header ────────────────────────────────────────────────────────
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(UITheme.HEADER_BG);
        header.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0,0,1,0, UITheme.CARD_BORDER),
            BorderFactory.createEmptyBorder(14,24,14,24)));

        JLabel title = new JLabel("\u2714\uFE0F  Habit Tracker");
        title.setFont(UITheme.FONT_PAGE_TITLE);
        title.setForeground(UITheme.TEXT_PRIMARY);

        String todayStr = LocalDate.now()
            .format(DateTimeFormatter.ofPattern("EEEE, d MMM"));
        JLabel dateLabel = new JLabel(todayStr);
        dateLabel.setFont(UITheme.FONT_BODY);
        dateLabel.setForeground(UITheme.TEXT_SECONDARY);

        header.add(title,     BorderLayout.WEST);
        header.add(dateLabel, BorderLayout.EAST);

        // ── scroll content ────────────────────────────────────────────────
        JPanel content = new JPanel();
        content.setBackground(UITheme.CONTENT_BG);
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));

        content.add(buildSummaryRow());
        content.add(Box.createVerticalStrut(16));
        content.add(buildGridCard());
        content.add(Box.createVerticalStrut(16));
        content.add(buildAddHabitCard());

        JScrollPane scroll = new JScrollPane(content);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getVerticalScrollBar().setUnitIncrement(12);

        JPanel foot = new JPanel(new BorderLayout());
        foot.setBackground(UITheme.HEADER_BG);
        foot.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(1,0,0,0, UITheme.CARD_BORDER),
            BorderFactory.createEmptyBorder(10,20,10,20)));

        JButton btnMarkToday = UITheme.makePrimaryButton("\u2714  Mark All Done Today");
        JButton btnSave      = UITheme.makePrimaryButton("Save Habits");
        JButton btnClose     = UITheme.makeSecondaryButton("Close");

        btnMarkToday.addActionListener(e -> markAllToday());
        btnSave     .addActionListener(e -> saveHabits());
        btnClose    .addActionListener(e -> dispose());

        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        btnRow.setOpaque(false);
        btnRow.add(btnMarkToday);
        btnRow.add(btnSave);
        btnRow.add(btnClose);
        foot.add(btnRow, BorderLayout.EAST);

        root.add(header, BorderLayout.NORTH);
        root.add(scroll, BorderLayout.CENTER);
        root.add(foot,   BorderLayout.SOUTH);
        setContentPane(root);
    }

    private JPanel buildSummaryRow() {
        JPanel row = new JPanel(new GridLayout(1, 4, 12, 0));
        row.setOpaque(false);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 85));

        streakLabels = new JLabel[habitCount];
        Color[] colors = {UITheme.TINT_BLUE_T, UITheme.TINT_GREEN_T,
                          UITheme.TINT_AMBER_T, UITheme.TINT_RED_T};

        for (int i = 0; i < habitCount && i < 4; i++) {
            int streak = calcStreak(i);
            Object[] card = UITheme.makeStatCard(
                habitNames[i], streak + " day" + (streak!=1?"s":""),
                i==0 ? UITheme.TINT_BLUE  : i==1 ? UITheme.TINT_GREEN
              : i==2 ? UITheme.TINT_AMBER : UITheme.TINT_RED,
                colors[i]);
            streakLabels[i] = (JLabel) card[1];
            row.add((JPanel) card[0]);
        }
        return row;
    }

    private JPanel buildGridCard() {
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setOpaque(false);

        JPanel card = UITheme.makeCard();
        card.setLayout(new BorderLayout(0, 10));

        JLabel cardTitle = UITheme.makeSectionHeader("\uD83D\uDDD3  This Week");
        card.add(cardTitle, BorderLayout.NORTH);

        // grid: rows = habits, cols = Mon–Sun
        String[] days = {"Mon","Tue","Wed","Thu","Fri","Sat","Sun"};
        int cols = days.length + 1;   // +1 for habit name column

        gridPanel = new JPanel(new GridLayout(habitCount + 1, cols, 6, 6));
        gridPanel.setOpaque(false);

        // header row
        gridPanel.add(makeGridLabel("Habit", true));
        for (int d = 0; d < 7; d++) {
            JLabel dl = makeGridLabel(days[d], d == todayIndex);
            gridPanel.add(dl);
        }

        // habit rows
        checkBoxes = new JCheckBox[habitCount][7];
        for (int h = 0; h < habitCount; h++) {
            gridPanel.add(makeGridLabel(habitNames[h], false));
            for (int d = 0; d < 7; d++) {
                JCheckBox cb = makeGridCheckBox(grid[h][d], d == todayIndex);
                final int fh = h, fd = d;
                cb.addActionListener(e -> {
                    grid[fh][fd] = cb.isSelected();
                    refreshStreaks();
                });
                checkBoxes[h][d] = cb;
                gridPanel.add(cb);
            }
        }

        card.add(gridPanel, BorderLayout.CENTER);
        wrapper.add(card);
        return wrapper;
    }

    private JPanel buildAddHabitCard() {
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setOpaque(false);
        wrapper.setMaximumSize(new Dimension(Integer.MAX_VALUE, 82));

        JPanel card = UITheme.makeCard();
        card.setLayout(new BorderLayout(12, 0));

        JLabel label = UITheme.makeFormLabel("Add a custom habit:");
        JTextField tf = UITheme.makeTextField("e.g. Drink 8 glasses of water");
        tf.setPreferredSize(new Dimension(300, 36));
        JButton btn = UITheme.makePrimaryButton("+ Add");

        btn.addActionListener(e -> {
            String name = tf.getText().trim();
            if (name.isEmpty() || name.startsWith("e.g.")) return;
            addHabit(name);
            tf.setText("");
        });

        card.add(label, BorderLayout.WEST);
        card.add(tf,    BorderLayout.CENTER);
        card.add(btn,   BorderLayout.EAST);
        wrapper.add(card);
        return wrapper;
    }

    private JLabel makeGridLabel(String text, boolean highlight) {
        JLabel l = new JLabel(text, SwingConstants.CENTER);
        l.setFont(highlight
                ? new Font("Segoe UI", Font.BOLD, 12)
                : UITheme.FONT_CARD_LABEL);
        l.setForeground(highlight ? UITheme.ACCENT : UITheme.TEXT_SECONDARY);
        l.setOpaque(highlight);
        if (highlight) {
            l.setBackground(UITheme.ACCENT_LIGHT);
            l.setBorder(BorderFactory.createEmptyBorder(4,4,4,4));
        }
        return l;
    }

    private JCheckBox makeGridCheckBox(boolean selected, boolean today) {
        JCheckBox cb = new JCheckBox();
        cb.setSelected(selected);
        cb.setHorizontalAlignment(SwingConstants.CENTER);
        cb.setOpaque(today);
        cb.setBackground(today ? UITheme.ACCENT_LIGHT : UITheme.CONTENT_BG);
        cb.setFocusPainted(false);
        return cb;
    }

    private int calcStreak(int habitIndex) {
        // Count consecutive days from today going backwards
        int streak = 0;
        for (int d = todayIndex; d >= 0; d--) {
            if (grid[habitIndex][d]) streak++;
            else break;
        }
        return streak;
    }

    private void refreshStreaks() {
        if (streakLabels == null) return;
        for (int i = 0; i < habitCount && i < streakLabels.length; i++) {
            int s = calcStreak(i);
            if (streakLabels[i] != null)
                streakLabels[i].setText(s + " day" + (s!=1?"s":""));
        }
    }

    private void markAllToday() {
        for (int h = 0; h < habitCount; h++) {
            grid[h][todayIndex] = true;
            if (checkBoxes != null && checkBoxes[h][todayIndex] != null)
                checkBoxes[h][todayIndex].setSelected(true);
        }
        refreshStreaks();
    }

    private void addHabit(String name) {
        // grow arrays
        habitCount++;
        boolean[][] newGrid = new boolean[habitCount][7];
        String[]    newNames = new String[habitCount];
        for (int i = 0; i < habitCount-1; i++) {
            newGrid[i]  = grid[i];
            newNames[i] = habitNames[i];
        }
        newGrid[habitCount-1]  = new boolean[7];
        newNames[habitCount-1] = name;
        grid       = newGrid;
        habitNames = newNames;

        // rebuild grid panel
        gridPanel.removeAll();
        String[] days = {"Mon","Tue","Wed","Thu","Fri","Sat","Sun"};
        int cols = days.length + 1;
        gridPanel.setLayout(new GridLayout(habitCount+1, cols, 6, 6));

        gridPanel.add(makeGridLabel("Habit", true));
        for (int d = 0; d < 7; d++)
            gridPanel.add(makeGridLabel(days[d], d == todayIndex));

        checkBoxes = new JCheckBox[habitCount][7];
        for (int h = 0; h < habitCount; h++) {
            gridPanel.add(makeGridLabel(habitNames[h], false));
            for (int d = 0; d < 7; d++) {
                JCheckBox cb = makeGridCheckBox(grid[h][d], d == todayIndex);
                final int fh=h, fd=d;
                cb.addActionListener(e -> { grid[fh][fd]=cb.isSelected(); refreshStreaks(); });
                checkBoxes[h][d] = cb;
                gridPanel.add(cb);
            }
        }
        gridPanel.revalidate();
        gridPanel.repaint();
    }

    private void saveHabits() {
        // sync back first 4 built-in habits to DataStore
        DataStore.studyHabit    = habitCount>0 && calcStreak(0)>0;
        DataStore.revisionHabit = habitCount>1 && calcStreak(1)>0;
        DataStore.exerciseHabit = habitCount>2 && calcStreak(2)>0;
        DataStore.notesHabit    = habitCount>3 && calcStreak(3)>0;
        FileManager.saveData();
        JOptionPane.showMessageDialog(this, "Habits saved!",
                "Saved", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {
        UITheme.apply();
        EventQueue.invokeLater(() -> new HabitTrackerForm().setVisible(true));
    }
}
