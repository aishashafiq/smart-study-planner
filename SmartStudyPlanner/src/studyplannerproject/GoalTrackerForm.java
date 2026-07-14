package studyplannerproject;

import java.awt.*;
import java.io.*;
import javax.swing.*;


public class GoalTrackerForm extends JFrame {

    private double weeklyGoal   = 10.0;   // default 10 h/week
    private static final String DATA_FILE = "goal.dat";

    private GaugePanel gaugePanel;
    private JLabel     lblGoal, lblDone, lblDaily, lblMotivation;
    private JSlider    slider;

    public GoalTrackerForm() {
        loadGoal();
        buildUI();
        refreshGauge();
    }

    private void buildUI() {
        setTitle("Goal Tracker");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(480, 520);
        setResizable(false);
        setLocationRelativeTo(null);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(UITheme.CONTENT_BG);

        // header
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(UITheme.HEADER_BG);
        header.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0,0,1,0, UITheme.CARD_BORDER),
            BorderFactory.createEmptyBorder(14,24,14,24)));
        JLabel title = new JLabel("\uD83C\uDFAF  Goal Tracker");
        title.setFont(UITheme.FONT_PAGE_TITLE);
        title.setForeground(UITheme.TEXT_PRIMARY);
        header.add(title);

        // gauge
        gaugePanel = new GaugePanel();
        gaugePanel.setPreferredSize(new Dimension(220, 220));
        gaugePanel.setOpaque(false);

        JPanel gaugeWrapper = new JPanel(new GridBagLayout());
        gaugeWrapper.setBackground(UITheme.CONTENT_BG);
        gaugeWrapper.add(gaugePanel);

        // stats card
        JPanel statsCard = UITheme.makeCard();
        statsCard.setLayout(new BoxLayout(statsCard, BoxLayout.Y_AXIS));

        lblDone = makeStat("Hours Studied", "0.0 h", UITheme.ACCENT);
        lblGoal = makeStat("Weekly Goal",   "10.0 h", UITheme.TINT_BLUE_T);
        lblDaily= makeStat("Daily Target",  "1.4 h",  UITheme.TINT_GREEN_T);

        statsCard.add(lblDone.getParent());
        statsCard.add(Box.createVerticalStrut(8));
        addDivider(statsCard);
        statsCard.add(Box.createVerticalStrut(8));
        statsCard.add(lblGoal.getParent());
        statsCard.add(Box.createVerticalStrut(8));
        addDivider(statsCard);
        statsCard.add(Box.createVerticalStrut(8));
        statsCard.add(lblDaily.getParent());

        // motivation label
        lblMotivation = new JLabel("Keep going!");
        lblMotivation.setFont(new Font("Segoe UI", Font.ITALIC, 13));
        lblMotivation.setForeground(UITheme.ACCENT);
        lblMotivation.setHorizontalAlignment(SwingConstants.CENTER);

        // slider card
        JPanel sliderCard = UITheme.makeCard();
        sliderCard.setLayout(new BorderLayout(0, 6));
        sliderCard.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));

        JLabel sliderTitle = UITheme.makeSectionHeader("Set Weekly Goal (hours)");
        slider = new JSlider(1, 40, (int) weeklyGoal);
        slider.setOpaque(false);
        slider.setPaintLabels(false);
        slider.setPaintTicks(true);
        slider.setMajorTickSpacing(5);
        slider.addChangeListener(e -> {
            weeklyGoal = slider.getValue();
            saveGoal();
            refreshGauge();
        });
        sliderCard.add(sliderTitle, BorderLayout.NORTH);
        sliderCard.add(slider,      BorderLayout.CENTER);

        // assemble content
        JPanel centre = new JPanel();
        centre.setBackground(UITheme.CONTENT_BG);
        centre.setLayout(new BoxLayout(centre, BoxLayout.Y_AXIS));
        centre.setBorder(BorderFactory.createEmptyBorder(16,20,16,20));

        JPanel top = new JPanel(new GridLayout(1,2,16,0));
        top.setOpaque(false);
        top.setMaximumSize(new Dimension(Integer.MAX_VALUE, 220));
        top.add(gaugeWrapper);
        top.add(statsCard);

        top.setAlignmentX(Component.LEFT_ALIGNMENT);
        lblMotivation.setAlignmentX(Component.CENTER_ALIGNMENT);
        sliderCard.setAlignmentX(Component.LEFT_ALIGNMENT);

        centre.add(top);
        centre.add(Box.createVerticalStrut(14));
        centre.add(lblMotivation);
        centre.add(Box.createVerticalStrut(14));
        centre.add(sliderCard);

        // footer
        JPanel foot = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        foot.setBackground(UITheme.HEADER_BG);
        foot.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(1,0,0,0, UITheme.CARD_BORDER),
            BorderFactory.createEmptyBorder(8,16,8,16)));
        JButton btnClose = UITheme.makePrimaryButton("Close");
        btnClose.addActionListener(e -> dispose());
        foot.add(btnClose);

        root.add(header, BorderLayout.NORTH);
        root.add(centre, BorderLayout.CENTER);
        root.add(foot,   BorderLayout.SOUTH);
        setContentPane(root);
    }

    private void refreshGauge() {
        double done = DataStore.studyHours;
        double pct  = weeklyGoal > 0 ? Math.min(done / weeklyGoal, 1.0) : 0;
        gaugePanel.setProgress(pct, done >= weeklyGoal);

        lblDone .setText(String.format("%.1f h", done));
        lblGoal .setText(String.format("%.0f h", weeklyGoal));
        lblDaily.setText(String.format("%.1f h", weeklyGoal / 7.0));

        int p = (int)(pct * 100);
        lblMotivation.setText(
            p < 25 ? "Just getting started — every hour counts!" :
            p < 50 ? "Good progress — keep the momentum going!" :
            p < 75 ? "Over halfway there — you're doing great!" :
            p < 100 ? "Almost at your goal — final push!" :
                      "Goal achieved this week! \uD83C\uDF89");
        lblMotivation.setForeground(
            p < 50 ? UITheme.TINT_AMBER_T :
            p < 100 ? UITheme.ACCENT : UITheme.TINT_GREEN_T);
    }

    private JLabel makeStat(String labelText, String value, Color valueColor) {
        JPanel row = new JPanel(new BorderLayout());
        row.setOpaque(false);
        JLabel nameLbl = new JLabel(labelText);
        nameLbl.setFont(UITheme.FONT_BODY);
        nameLbl.setForeground(UITheme.TEXT_SECONDARY);
        JLabel valLbl = new JLabel(value);
        valLbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
        valLbl.setForeground(valueColor);
        row.add(nameLbl, BorderLayout.WEST);
        row.add(valLbl,  BorderLayout.EAST);
        return valLbl;
    }

    private void addDivider(JPanel p) {
        JSeparator s = new JSeparator();
        s.setForeground(UITheme.CARD_BORDER);
        s.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        p.add(s);
    }

    private void loadGoal() {
        try (DataInputStream in = new DataInputStream(
                new FileInputStream(DATA_FILE))) {
            weeklyGoal = in.readDouble();
        } catch (Exception e) { weeklyGoal = 10.0; }
    }
    private void saveGoal() {
        try (DataOutputStream out = new DataOutputStream(
                new FileOutputStream(DATA_FILE))) {
            out.writeDouble(weeklyGoal);
        } catch (Exception ignored) {}
    }

    private static class GaugePanel extends JPanel {
        private double  progress  = 0;
        private boolean achieved  = false;

        void setProgress(double p, boolean a) { progress=p; achieved=a; repaint(); }

        @Override protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                RenderingHints.VALUE_ANTIALIAS_ON);

            int w=getWidth(), h=getHeight();
            int pad=18, diam=Math.min(w,h)-pad*2;
            int x=(w-diam)/2, y=(h-diam)/2;

            // track
            g2.setColor(UITheme.ACCENT_LIGHT);
            g2.setStroke(new BasicStroke(16, BasicStroke.CAP_ROUND,
                                          BasicStroke.JOIN_ROUND));
            g2.drawArc(x, y, diam, diam, 225, -270);

            // progress arc
            g2.setColor(achieved ? UITheme.TINT_GREEN_T : UITheme.ACCENT);
            int sweep = (int)(-270 * progress);
            g2.drawArc(x, y, diam, diam, 225, sweep);

            // centre text
            int pct = (int)(progress*100);
            g2.setFont(new Font("Segoe UI", Font.BOLD, 26));
            g2.setColor(UITheme.TEXT_PRIMARY);
            String txt = pct + "%";
            FontMetrics fm = g2.getFontMetrics();
            g2.drawString(txt, w/2 - fm.stringWidth(txt)/2,
                               h/2 + fm.getAscent()/2 - 2);

            g2.setFont(UITheme.FONT_CARD_LABEL);
            g2.setColor(UITheme.TEXT_SECONDARY);
            String sub = "of goal";
            g2.drawString(sub, w/2 - g2.getFontMetrics().stringWidth(sub)/2,
                               h/2 + fm.getAscent()/2 + 16);
            g2.dispose();
        }
    }

    public static void main(String[] args) {
        UITheme.apply();
        EventQueue.invokeLater(() -> new GoalTrackerForm().setVisible(true));
    }
}
