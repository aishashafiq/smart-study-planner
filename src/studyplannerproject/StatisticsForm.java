package studyplannerproject;

import java.awt.*;
import javax.swing.*;

public class StatisticsForm extends JFrame {

    private JLabel      valTotal, valCompleted, valPending, valPercent;
    private JProgressBar progressBar;
    private JPanel       subjectBreakdown;

    public StatisticsForm() {
        buildUI();
        loadStatistics();
    }

    private void buildUI() {
        setTitle("Statistics");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(620, 520);
        setLocationRelativeTo(null);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(UITheme.CONTENT_BG);

        // ── header ────────────────────────────────────────────────────────
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(UITheme.HEADER_BG);
        header.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0,0,1,0, UITheme.CARD_BORDER),
            BorderFactory.createEmptyBorder(14,24,14,24)));
        JLabel t = new JLabel("\uD83D\uDCCA  Statistics");
        t.setFont(UITheme.FONT_PAGE_TITLE);
        t.setForeground(UITheme.TEXT_PRIMARY);
        header.add(t);

        // ── content ───────────────────────────────────────────────────────
        JPanel content = new JPanel();
        content.setBackground(UITheme.CONTENT_BG);
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBorder(BorderFactory.createEmptyBorder(22,22,22,22));

        // 3 metric cards
        JPanel cardsRow = new JPanel(new GridLayout(1,3,14,0));
        cardsRow.setOpaque(false);
        cardsRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 95));

        Object[] c1 = UITheme.makeStatCard("Total Tasks",  "0", UITheme.TINT_BLUE,  UITheme.TINT_BLUE_T);
        Object[] c2 = UITheme.makeStatCard("Completed",    "0", UITheme.TINT_GREEN, UITheme.TINT_GREEN_T);
        Object[] c3 = UITheme.makeStatCard("Pending",      "0", UITheme.TINT_AMBER, UITheme.TINT_AMBER_T);
        valTotal     = (JLabel) c1[1];
        valCompleted = (JLabel) c2[1];
        valPending   = (JLabel) c3[1];
        cardsRow.add((JPanel) c1[0]);
        cardsRow.add((JPanel) c2[0]);
        cardsRow.add((JPanel) c3[0]);

        // progress card
        JPanel progCard = UITheme.makeCard();
        progCard.setLayout(new BorderLayout(0,10));
        progCard.setMaximumSize(new Dimension(Integer.MAX_VALUE, 90));

        JPanel progHeader = new JPanel(new BorderLayout());
        progHeader.setOpaque(false);
        JLabel progTitle = UITheme.makeSectionHeader("Completion Progress");
        valPercent = new JLabel("0%");
        valPercent.setFont(new Font("Segoe UI", Font.BOLD, 16));
        valPercent.setForeground(UITheme.ACCENT);
        progHeader.add(progTitle,  BorderLayout.WEST);
        progHeader.add(valPercent, BorderLayout.EAST);

        progressBar = new JProgressBar(0,100);
        UITheme.styleProgressBar(progressBar);
        progressBar.setPreferredSize(new Dimension(0,16));

        progCard.add(progHeader,  BorderLayout.NORTH);
        progCard.add(progressBar, BorderLayout.CENTER);

        // subject breakdown
        JPanel breakCard = UITheme.makeCard();
        breakCard.setLayout(new BorderLayout(0,8));

        JLabel breakTitle = UITheme.makeSectionHeader("Tasks by Subject");
        subjectBreakdown = new JPanel();
        subjectBreakdown.setOpaque(false);
        subjectBreakdown.setLayout(new BoxLayout(subjectBreakdown, BoxLayout.Y_AXIS));

        JScrollPane breakScroll = new JScrollPane(subjectBreakdown);
        breakScroll.setBorder(BorderFactory.createEmptyBorder());
        breakScroll.setBackground(UITheme.CARD_BG);
        breakScroll.getViewport().setBackground(UITheme.CARD_BG);

        breakCard.add(breakTitle,  BorderLayout.NORTH);
        breakCard.add(breakScroll, BorderLayout.CENTER);

        content.add(cardsRow);
        content.add(Box.createVerticalStrut(14));
        content.add(progCard);
        content.add(Box.createVerticalStrut(14));
        content.add(breakCard);

        // ── footer ────────────────────────────────────────────────────────
        JPanel foot = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        foot.setBackground(UITheme.HEADER_BG);
        foot.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(1,0,0,0, UITheme.CARD_BORDER),
            BorderFactory.createEmptyBorder(8,16,8,16)));
        JButton btnBack = UITheme.makeSecondaryButton("Back");
        btnBack.addActionListener(e -> dispose());
        foot.add(btnBack);

        root.add(header,  BorderLayout.NORTH);
        root.add(content, BorderLayout.CENTER);
        root.add(foot,    BorderLayout.SOUTH);
        setContentPane(root);
    }

    private void loadStatistics() {
        int total=0, completed=0, pending=0;
        for (Task t : DataStore.tasks) {
            total++;
            if (t.isCompleted()) completed++; else pending++;
        }
        int pct = total > 0 ? (completed*100)/total : 0;

        valTotal.setText(String.valueOf(total));
        valCompleted.setText(String.valueOf(completed));
        valPending.setText(String.valueOf(pending));
        valPercent.setText(pct + "%");

        // animate progress bar
        progressBar.setValue(0);
        final int target = pct;
        Timer anim = new Timer(12, null);
        anim.addActionListener(e -> {
            int cur = progressBar.getValue();
            if (cur >= target) { anim.stop(); return; }
            progressBar.setValue(cur+1);
            progressBar.setString(progressBar.getValue() + "%");
        });
        anim.start();

        // subject breakdown
        subjectBreakdown.removeAll();
        Color[] colors = {UITheme.TINT_BLUE_T, UITheme.TINT_GREEN_T,
                          UITheme.TINT_AMBER_T, UITheme.TINT_RED_T, UITheme.ACCENT};
        for (int si = 0; si < DataStore.subjects.size(); si++) {
            String sub = DataStore.subjects.get(si);
            int cnt = 0;
            for (Task t : DataStore.tasks)
                if (t.getSubject().equalsIgnoreCase(sub)) cnt++;

            JPanel row = new JPanel(new BorderLayout(10,0));
            row.setOpaque(false);
            row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));
            row.setBorder(BorderFactory.createEmptyBorder(4,0,4,0));

            JLabel nameLbl = new JLabel(sub);
            nameLbl.setFont(UITheme.FONT_BODY);
            nameLbl.setForeground(UITheme.TEXT_PRIMARY);

            JLabel cntLbl = new JLabel(cnt + " task" + (cnt!=1?"s":""));
            cntLbl.setFont(UITheme.FONT_BODY);
            cntLbl.setForeground(colors[si % colors.length]);

            row.add(nameLbl, BorderLayout.WEST);
            row.add(cntLbl,  BorderLayout.EAST);
            subjectBreakdown.add(row);
        }
        if (DataStore.subjects.isEmpty()) {
            JLabel e = new JLabel("No subjects added yet.");
            e.setFont(UITheme.FONT_BODY);
            e.setForeground(UITheme.TEXT_MUTED);
            subjectBreakdown.add(e);
        }
        subjectBreakdown.revalidate();
    }

    public static void main(String[] args) {
        UITheme.apply();
        EventQueue.invokeLater(() -> new StatisticsForm().setVisible(true));
    }
}
