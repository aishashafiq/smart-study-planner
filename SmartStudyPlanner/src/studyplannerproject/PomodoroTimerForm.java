package studyplannerproject;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.geom.*;


public class PomodoroTimerForm extends JFrame {

    private static final int WORK_SECS        = 25 * 60;
    private static final int SHORT_BREAK_SECS =  5 * 60;
    private static final int LONG_BREAK_SECS  = 15 * 60;

    private javax.swing.Timer swingTimer;
    private int     secondsLeft  = WORK_SECS;
    private int     totalSeconds = WORK_SECS;
    private boolean running      = false;
    private int     sessionsCompleted = 0;
    private String  mode         = "Work";

    private TimerRingPanel ringPanel;
    private JLabel         lblMode, lblSessions;
    private JButton        btnStartPause, btnReset;
    private JPanel         modeBar;
    private MainMenu       mainMenu;

    private static final Color BG     = new Color(28,  30,  50);
    private static final Color RING_W = new Color(255, 87,  87);   // work
    private static final Color RING_B = new Color(80, 200, 140);   // break
    private static final Color RING_L = new Color(100, 160, 255);  // long break
    private static final Color TEXT   = Color.BLACK;

    public PomodoroTimerForm(MainMenu mainMenu) {
        super("⏱ Pomodoro Timer");
        this.mainMenu = mainMenu;
        buildUI();
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(440, 560);
        setLocationRelativeTo(null);
        setResizable(false);
    }

    private void buildUI() {
        setBackground(BG);
        setLayout(new BorderLayout());

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(40, 40, 70));
        header.setBorder(new EmptyBorder(12, 18, 12, 18));

        JLabel title = new JLabel("⏱  Pomodoro Timer");
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(TEXT);
        header.add(title, BorderLayout.WEST);
        add(header, BorderLayout.NORTH);

        modeBar = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 8));
        modeBar.setBackground(new Color(35, 35, 60));
        addModeBtn("Work",        WORK_SECS);
        addModeBtn("Short Break", SHORT_BREAK_SECS);
        addModeBtn("Long Break",  LONG_BREAK_SECS);

        JPanel center = new JPanel();
        center.setBackground(BG);
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
        center.add(modeBar);

        ringPanel = new TimerRingPanel();
        ringPanel.setAlignmentX(CENTER_ALIGNMENT);
        center.add(ringPanel);

        lblMode = new JLabel("Work");
        lblMode.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblMode.setForeground(RING_W);
        lblMode.setAlignmentX(CENTER_ALIGNMENT);
        center.add(lblMode);

        center.add(Box.createVerticalStrut(6));

        lblSessions = new JLabel("Sessions: 0  🍅");
        lblSessions.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblSessions.setForeground(new Color(180, 180, 220));
        lblSessions.setAlignmentX(CENTER_ALIGNMENT);
        center.add(lblSessions);

        add(center, BorderLayout.CENTER);

        JPanel controls = new JPanel(new FlowLayout(FlowLayout.CENTER, 16, 14));
        controls.setBackground(BG);

        btnStartPause = ctrlBtn("▶  Start", new Color(70, 180, 100));
        btnReset      = ctrlBtn("↺  Reset", new Color(100, 110, 150));

        JButton btnBack = ctrlBtn("⬅  Back", new Color(80, 90, 120));
        btnBack.addActionListener(e -> {
            if (swingTimer != null) swingTimer.stop();
            dispose();
        });

        btnStartPause.addActionListener(e -> toggleTimer());
        btnReset.addActionListener(e -> resetTimer());

        controls.add(btnStartPause);
        controls.add(btnReset);
        controls.add(btnBack);
        add(controls, BorderLayout.SOUTH);

        updateDisplay();
    }

    private void addModeBtn(String label, int secs) {
        JButton b = new JButton(label);
        b.setFont(new Font("Segoe UI", Font.BOLD, 12));
        b.setBackground(new Color(60, 60, 100));
        b.setForeground(TEXT);
        b.setFocusPainted(false);
        b.setOpaque(true);
        b.setBorder(new EmptyBorder(6, 14, 6, 14));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.addActionListener(e -> switchMode(label, secs));
        modeBar.add(b);
    }

    private void switchMode(String newMode, int secs) {
        if (swingTimer != null) swingTimer.stop();
        running = false;
        mode = newMode;
        secondsLeft = secs;
        totalSeconds = secs;
        btnStartPause.setText("▶  Start");

        Color c = newMode.equals("Work") ? RING_W
                : newMode.equals("Short Break") ? RING_B : RING_L;
        lblMode.setText(newMode);
        lblMode.setForeground(c);
        ringPanel.setRingColor(c);
        updateDisplay();
    }

    private void toggleTimer() {
        if (running) {
            swingTimer.stop();
            running = false;
            btnStartPause.setText("▶  Resume");
        } else {
            running = true;
            btnStartPause.setText("⏸  Pause");
            swingTimer = new javax.swing.Timer(1000, e -> tick());
            swingTimer.start();
        }
    }

    private void tick() {
        if (secondsLeft > 0) {
            secondsLeft--;
            updateDisplay();
        } else {
            swingTimer.stop();
            running = false;
            btnStartPause.setText("▶  Start");
            onSessionComplete();
        }
    }

    private void onSessionComplete() {
        SoundPlayer.playSound("bell.wav");
        if (mode.equals("Work")) {
            sessionsCompleted++;
            DataStore.studyHours += 25.0 / 60.0;   // 25 min
            FileManager.saveData();
            if (mainMenu != null) mainMenu.updateDashboard();
            lblSessions.setText("Sessions: " + sessionsCompleted + "  🍅");
            JOptionPane.showMessageDialog(this,
                    "🍅 Work session done! Take a break.",
                    "Session Complete", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this,
                    "Break over! Back to work 💪",
                    "Break Complete", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void resetTimer() {
        if (swingTimer != null) swingTimer.stop();
        running = false;
        secondsLeft = totalSeconds;
        btnStartPause.setText("▶  Start");
        updateDisplay();
    }

    private void updateDisplay() {
        int m = secondsLeft / 60, s = secondsLeft % 60;
        ringPanel.setProgress((double) secondsLeft / totalSeconds);
        ringPanel.setTimeText(String.format("%02d:%02d", m, s));
        ringPanel.repaint();
    }

    private JButton ctrlBtn(String text, Color bg) {
        JButton b = new JButton(text);
        b.setFont(new Font("Segoe UI", Font.BOLD, 14));
        b.setBackground(bg);
        b.setForeground(Color.BLACK);
        b.setFocusPainted(false);
        b.setOpaque(true);
        b.setBorder(new EmptyBorder(10, 22, 10, 22));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return b;
    }

    private static class TimerRingPanel extends JPanel {
        private double progress   = 1.0;
        private Color  ringColor  = RING_W;
        private String timeText   = "25:00";

        TimerRingPanel() {
            setPreferredSize(new Dimension(260, 260));
            setOpaque(false);
        }

        void setProgress(double p)  { progress = p; }
        void setRingColor(Color c)  { ringColor = c; }
        void setTimeText(String t)  { timeText = t; }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int w = getWidth(), h = getHeight();
            int pad = 20;
            int d = Math.min(w, h) - 2 * pad;
            int x = (w - d) / 2, y = (h - d) / 2;

            // Background ring
            g2.setStroke(new BasicStroke(16, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g2.setColor(new Color(60, 60, 90));
            g2.draw(new Arc2D.Double(x, y, d, d, 0, 360, Arc2D.OPEN));

            // Progress arc
            g2.setColor(ringColor);
            double angle = 360 * progress;
            g2.draw(new Arc2D.Double(x, y, d, d, 90, -angle, Arc2D.OPEN));

            // Glow effect (soft shadow of arc)
            Composite old = g2.getComposite();
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.18f));
            g2.setStroke(new BasicStroke(28, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g2.setColor(ringColor);
            g2.draw(new Arc2D.Double(x, y, d, d, 90, -angle, Arc2D.OPEN));
            g2.setComposite(old);

            // Time text
            g2.setFont(new Font("Segoe UI", Font.BOLD, 44));
            g2.setColor(Color.WHITE);
            FontMetrics fm = g2.getFontMetrics();
            int tx = (w - fm.stringWidth(timeText)) / 2;
            int ty = h / 2 + fm.getAscent() / 2 - 4;
            g2.drawString(timeText, tx, ty);

            g2.dispose();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new PomodoroTimerForm(null).setVisible(true));
    }
}