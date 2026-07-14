package studyplannerproject;

import java.awt.*;
import javax.swing.*;

public class ProgressReportForm extends JFrame {

    public ProgressReportForm() {
        buildUI();
    }

    private void buildUI() {
        setTitle("Progress Report");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(540, 480);
        setLocationRelativeTo(null);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(UITheme.CONTENT_BG);

        // header
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(UITheme.HEADER_BG);
        header.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0,0,1,0,UITheme.CARD_BORDER),
            BorderFactory.createEmptyBorder(14,24,14,24)));
        JLabel t = new JLabel("\uD83D\uDCC8  Progress Report");
        t.setFont(UITheme.FONT_PAGE_TITLE);
        t.setForeground(UITheme.TEXT_PRIMARY);
        header.add(t);
        int total     = DataStore.tasks.size();
        int completed = 0;
        for (Task task : DataStore.tasks)
            if (task.isCompleted()) completed++;
        int pending  = total - completed;
        double pct   = total > 0 ? (completed * 100.0) / total : 0;
        final int iPct = (int) pct;

        // ── circle panel ──────────────────────────────────────────────────
        JPanel circle = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                    RenderingHints.VALUE_ANTIALIAS_ON);
                int cx = getWidth()/2, cy = getHeight()/2, r = 80;
                // background ring
                g2.setColor(UITheme.ACCENT_LIGHT);
                g2.setStroke(new BasicStroke(14, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                g2.drawOval(cx-r, cy-r, r*2, r*2);
                // progress arc
                g2.setColor(UITheme.ACCENT);
                g2.drawArc(cx-r, cy-r, r*2, r*2, 90, -(int)(3.6*iPct));
                // text
                g2.setFont(new Font("Segoe UI", Font.BOLD, 28));
                g2.setColor(UITheme.TEXT_PRIMARY);
                String label = iPct + "%";
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(label, cx - fm.stringWidth(label)/2,
                              cy + fm.getAscent()/2 - 2);
                g2.setFont(UITheme.FONT_CARD_LABEL);
                g2.setColor(UITheme.TEXT_SECONDARY);
                String sub = "Complete";
                g2.drawString(sub, cx - g2.getFontMetrics().stringWidth(sub)/2,
                              cy + fm.getAscent()/2 + 18);
                g2.dispose();
            }
        };
        circle.setBackground(UITheme.CONTENT_BG);
        circle.setPreferredSize(new Dimension(220, 220));

        // ── stats card ────────────────────────────────────────────────────
        JPanel statsCard = UITheme.makeCard();
        statsCard.setLayout(new BoxLayout(statsCard, BoxLayout.Y_AXIS));

        addStatRow(statsCard, "Total Tasks",     String.valueOf(total),     UITheme.TINT_BLUE_T);
        addDivider(statsCard);
        addStatRow(statsCard, "Completed",       String.valueOf(completed), UITheme.TINT_GREEN_T);
        addDivider(statsCard);
        addStatRow(statsCard, "Pending",         String.valueOf(pending),   UITheme.TINT_AMBER_T);
        addDivider(statsCard);
        addStatRow(statsCard, "Study Hours",
                   String.format("%.1f h", DataStore.studyHours),          UITheme.ACCENT);

        // ── layout ────────────────────────────────────────────────────────
        JPanel content = new JPanel(new GridBagLayout());
        content.setBackground(UITheme.CONTENT_BG);
        content.setBorder(BorderFactory.createEmptyBorder(22,22,22,22));
        GridBagConstraints gc = new GridBagConstraints();
        gc.fill    = GridBagConstraints.BOTH;
        gc.weightx = 0.4;
        gc.weighty = 1;
        content.add(circle, gc);
        gc.weightx = 0.6;
        gc.insets  = new Insets(0,14,0,0);
        content.add(statsCard, gc);
        JPanel foot = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        foot.setBackground(UITheme.HEADER_BG);
        foot.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(1,0,0,0,UITheme.CARD_BORDER),
            BorderFactory.createEmptyBorder(8,16,8,16)));
        JButton btnClose = UITheme.makePrimaryButton("Close");
        btnClose.addActionListener(e -> dispose());
        foot.add(btnClose);

        root.add(header,  BorderLayout.NORTH);
        root.add(content, BorderLayout.CENTER);
        root.add(foot,    BorderLayout.SOUTH);
        setContentPane(root);
    }

    private void addStatRow(JPanel card, String label, String value, Color valColor) {
        JPanel row = new JPanel(new BorderLayout());
        row.setOpaque(false);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        row.setBorder(BorderFactory.createEmptyBorder(4,0,4,0));
        JLabel lbl = new JLabel(label);
        lbl.setFont(UITheme.FONT_BODY);
        lbl.setForeground(UITheme.TEXT_SECONDARY);
        JLabel val = new JLabel(value);
        val.setFont(new Font("Segoe UI", Font.BOLD, 14));
        val.setForeground(valColor);
        row.add(lbl, BorderLayout.WEST);
        row.add(val, BorderLayout.EAST);
        card.add(row);
    }

    private void addDivider(JPanel card) {
        JSeparator s = new JSeparator();
        s.setForeground(UITheme.CARD_BORDER);
        s.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        card.add(s);
    }

    public static void main(String[] args) {
        UITheme.apply();
        EventQueue.invokeLater(() -> new ProgressReportForm().setVisible(true));
    }
}