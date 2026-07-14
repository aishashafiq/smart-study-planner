package studyplannerproject;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;

public class UITheme {

    // ── palette ───────────────────────────────────────────────────────────
    public static final Color SIDEBAR_BG     = new Color(28,  32,  57);
    public static final Color SIDEBAR_HOVER  = new Color(48,  54,  92);
    public static final Color ACCENT         = new Color(99,  91, 255);
    public static final Color ACCENT_DARK    = new Color(75,  68, 210);
    public static final Color ACCENT_LIGHT   = new Color(232,231,255);

    public static final Color CONTENT_BG     = new Color(244,245,250);
    public static final Color CARD_BG        = Color.WHITE;
    public static final Color CARD_BORDER    = new Color(222,222,232);

    public static final Color HEADER_BG      = Color.WHITE;

    // stat-card tints
    public static final Color TINT_BLUE      = new Color(219,234,254);
    public static final Color TINT_BLUE_T    = new Color( 29, 78,216);
    public static final Color TINT_GREEN     = new Color(220,252,231);
    public static final Color TINT_GREEN_T   = new Color( 21,128, 61);
    public static final Color TINT_AMBER     = new Color(254,243,199);
    public static final Color TINT_AMBER_T   = new Color(180, 83,  9);
    public static final Color TINT_RED       = new Color(254,226,226);
    public static final Color TINT_RED_T     = new Color(185, 28, 28);

    // text
    public static final Color TEXT_PRIMARY   = new Color( 15, 23, 42);
    public static final Color TEXT_SECONDARY = new Color(100,116,139);
    public static final Color TEXT_MUTED     = new Color(148,163,184);
    public static final Color TEXT_WHITE     = Color.WHITE;

    // danger
    public static final Color DANGER         = new Color(220, 38, 38);

    // success / warning
    public static final Color SUCCESS        = new Color( 22,163, 74);
    public static final Color WARNING        = new Color(217,119,  6);

    // ── fonts ─────────────────────────────────────────────────────────────
    public static final Font FONT_APP_TITLE  = new Font("Segoe UI", Font.BOLD,  14);
    public static final Font FONT_PAGE_TITLE = new Font("Segoe UI", Font.BOLD,  20);
    public static final Font FONT_SECTION    = new Font("Segoe UI", Font.BOLD,  13);
    public static final Font FONT_NAV        = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font FONT_NAV_ACTIVE = new Font("Segoe UI", Font.BOLD,  13);
    public static final Font FONT_CARD_VALUE = new Font("Segoe UI", Font.BOLD,  26);
    public static final Font FONT_CARD_LABEL = new Font("Segoe UI", Font.PLAIN, 11);
    public static final Font FONT_BODY       = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font FONT_LABEL      = new Font("Segoe UI", Font.BOLD,  12);
    public static final Font FONT_INPUT      = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font FONT_BTN        = new Font("Segoe UI", Font.BOLD,  12);
    public static final Font FONT_TABLE_HEAD = new Font("Segoe UI", Font.BOLD,  12);
    public static final Font FONT_TABLE_BODY = new Font("Segoe UI", Font.PLAIN, 12);

    // ═════════════════════════════════════════════════════════════════════
    //  GLOBAL L&F  — call once in main()
    // ═════════════════════════════════════════════════════════════════════
   public static void apply() {
    try {
        UIManager.setLookAndFeel(
                UIManager.getSystemLookAndFeelClassName());
    } catch (Exception ignored) { }

    UIManager.put("OptionPane.background", CARD_BG);
    UIManager.put("Panel.background", CONTENT_BG);
    UIManager.put("OptionPane.messageFont", FONT_BODY);
    UIManager.put("OptionPane.buttonFont", FONT_BTN);

    UIManager.put("Table.font", FONT_TABLE_BODY);
    UIManager.put("TableHeader.font", FONT_TABLE_HEAD);
    UIManager.put("TableHeader.background", new Color(248,248,252));
    UIManager.put("TableHeader.foreground", TEXT_PRIMARY);
    UIManager.put("Table.gridColor", CARD_BORDER);
    UIManager.put("Table.selectionBackground", ACCENT_LIGHT);
    UIManager.put("Table.selectionForeground", TEXT_PRIMARY);

    UIManager.put("ScrollPane.border",
            BorderFactory.createLineBorder(CARD_BORDER,1));

    UIManager.put("ComboBox.font", FONT_INPUT);
    UIManager.put("CheckBox.font", FONT_BODY);

    UIManager.put("ProgressBar.font", FONT_BODY);
    UIManager.put("ProgressBar.selectionBackground", ACCENT);
    UIManager.put("ProgressBar.selectionForeground", Color.WHITE);

    // ===== BUTTON STYLING =====
    UIManager.put("Button.font", FONT_BTN);
    UIManager.put("Button.background", ACCENT);
    UIManager.put("Button.foreground", Color.WHITE);

    UIManager.put("ToggleButton.font", FONT_BTN);
    UIManager.put("ToggleButton.background", ACCENT);
    UIManager.put("ToggleButton.foreground", Color.WHITE);

    UIManager.put("Label.font", FONT_BODY);
    UIManager.put("TextField.font", FONT_INPUT);
    UIManager.put("TextArea.font", FONT_BODY);
    UIManager.put("TabbedPane.font", FONT_BODY);
    UIManager.put("Menu.font", FONT_BODY);
    UIManager.put("MenuItem.font", FONT_BODY);
    UIManager.put("List.font", FONT_BODY);
}

    /**
     * Full-width sidebar button.
     * @param emoji  small unicode symbol, e.g. "+"  or use a plain letter
     * @param label  text shown next to the symbol
     */
    public static JButton makeNavButton(String emoji, String label) {
        JButton btn = new JButton(emoji + "   " + label) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                    RenderingHints.VALUE_ANTIALIAS_ON);
                if (getModel().isRollover() || getModel().isPressed()) {
                    g2.setColor(SIDEBAR_HOVER);
                    g2.fillRoundRect(4, 2, getWidth()-8, getHeight()-4, 8, 8);
                }
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setFont(FONT_NAV);
        btn.setForeground(new Color(175,182,220));
        btn.setBackground(new Color(0,0,0,0));
        btn.setOpaque(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setBorder(BorderFactory.createEmptyBorder(11,18,11,18));
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));
        return btn;
    }

    /** Mark a nav button as the currently active page */
    public static void setNavActive(JButton btn) {
        btn.setFont(FONT_NAV_ACTIVE);
        btn.setForeground(Color.WHITE);
        btn.setOpaque(true);
        btn.setBackground(ACCENT);
        btn.setContentAreaFilled(true);
    }

    /** Reset a nav button to the default inactive appearance */
    public static void setNavInactive(JButton btn) {
        btn.setFont(FONT_NAV);
        btn.setForeground(new Color(175,182,220));
        btn.setOpaque(false);
        btn.setContentAreaFilled(false);
    }

    // ═════════════════════════════════════════════════════════════════════
    //  ACTION  BUTTONS
    // ═════════════════════════════════════════════════════════════════════
    /** Accent-filled primary button — "Save", "Add", "Login" */
    public static JButton makePrimaryButton(String text) {
        final JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                    RenderingHints.VALUE_ANTIALIAS_ON);
                Color bg = getModel().isPressed()  ? ACCENT_DARK
                         : getModel().isRollover() ? new Color(85,78,235)
                         : ACCENT;
                g2.setColor(bg);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setFont(FONT_BTN);
        btn.setForeground(Color.WHITE);
        btn.setOpaque(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createEmptyBorder(10, 22, 10, 22));
        return btn;
    }

    /** Outlined secondary button — "Back", "Cancel" */
    public static JButton makeSecondaryButton(String text) {
        final JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                    RenderingHints.VALUE_ANTIALIAS_ON);
                if (getModel().isRollover() || getModel().isPressed()) {
                    g2.setColor(new Color(238,238,248));
                    g2.fillRoundRect(0,0,getWidth(),getHeight(),10,10);
                }
                g2.setColor(CARD_BORDER);
                g2.drawRoundRect(0,0,getWidth()-1,getHeight()-1,10,10);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setFont(FONT_BTN);
        btn.setForeground(TEXT_PRIMARY);
        btn.setOpaque(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createEmptyBorder(10, 22, 10, 22));
        return btn;
    }

    /** Red danger button — "Delete", "Exit" */
    public static JButton makeDangerButton(String text) {
        final JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                    RenderingHints.VALUE_ANTIALIAS_ON);
                Color bg = getModel().isPressed()  ? new Color(185,28,28)
                         : getModel().isRollover() ? new Color(200,30,30)
                         : DANGER;
                g2.setColor(bg);
                g2.fillRoundRect(0,0,getWidth(),getHeight(),10,10);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setFont(FONT_BTN);
        btn.setForeground(Color.WHITE);
        btn.setOpaque(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createEmptyBorder(10, 22, 10, 22));
        return btn;
    }

    // ═════════════════════════════════════════════════════════════════════
    //  STAT  CARD
    // ═════════════════════════════════════════════════════════════════════
    /**
     * Dashboard metric card.
     * Returns a JPanel[].  Index 0 = the card panel.  Index 1 = cast to
     * JLabel to update the value from updateDashboard().
     *
     * Usage:
     *   Object[] card = UITheme.makeStatCard("Tasks", "0",
     *                        UITheme.TINT_BLUE, UITheme.TINT_BLUE_T);
     *   JPanel  cardPanel  = (JPanel)  card[0];
     *   JLabel  valueLabel = (JLabel)  card[1];
     */
    public static Object[] makeStatCard(String label, String initialValue,
                                        Color tintBg, Color tintFg) {
        // ── outer card ────────────────────────────────────────────────────
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                    RenderingHints.VALUE_ANTIALIAS_ON);
                // shadow hint (paint slightly darker rect behind)
                g2.setColor(new Color(0,0,0,12));
                g2.fillRoundRect(2,3,getWidth()-2,getHeight()-2,14,14);
                g2.setColor(CARD_BG);
                g2.fillRoundRect(0,0,getWidth()-2,getHeight()-3,14,14);
                g2.setColor(CARD_BORDER);
                g2.drawRoundRect(0,0,getWidth()-3,getHeight()-3,14,14);
                g2.dispose();
            }
        };
        card.setOpaque(false);
        card.setLayout(new BorderLayout(12,0));
        card.setBorder(BorderFactory.createEmptyBorder(16,16,16,16));

        // ── coloured icon circle ──────────────────────────────────────────
        JPanel circle = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                    RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(tintBg);
                g2.fillOval(0,0,42,42);
                g2.dispose();
            }
        };
        circle.setOpaque(false);
        circle.setPreferredSize(new Dimension(42,42));
        circle.setMinimumSize(new Dimension(42,42));
        circle.setMaximumSize(new Dimension(42,42));

        // icon initial letter (first char of label)
        JLabel iconLbl = new JLabel(label.substring(0,1));
        iconLbl.setFont(new Font("Segoe UI", Font.BOLD, 16));
        iconLbl.setForeground(tintFg);
        circle.add(iconLbl);

        // ── text block ────────────────────────────────────────────────────
        JPanel text = new JPanel();
        text.setOpaque(false);
        text.setLayout(new BoxLayout(text, BoxLayout.Y_AXIS));

        JLabel valueLbl = new JLabel(initialValue);
        valueLbl.setFont(FONT_CARD_VALUE);
        valueLbl.setForeground(TEXT_PRIMARY);

        JLabel nameLbl = new JLabel(label);
        nameLbl.setFont(FONT_CARD_LABEL);
        nameLbl.setForeground(TEXT_SECONDARY);

        text.add(valueLbl);
        text.add(Box.createVerticalStrut(2));
        text.add(nameLbl);

        card.add(circle, BorderLayout.WEST);
        card.add(text,   BorderLayout.CENTER);

        return new Object[]{ card, valueLbl };
    }

    // ═════════════════════════════════════════════════════════════════════
    //  FORM  HELPERS
    // ═════════════════════════════════════════════════════════════════════
    /** Styled text field with placeholder behaviour */
    public static JTextField makeTextField(final String placeholder) {
        final JTextField tf = new JTextField(placeholder);
        tf.setFont(FONT_INPUT);
        tf.setForeground(TEXT_MUTED);
        tf.setBackground(new Color(248,249,252));
        tf.setBorder(fieldBorder(false));
        tf.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent e) {
                if (tf.getText().equals(placeholder)) {
                    tf.setText("");
                    tf.setForeground(TEXT_PRIMARY);
                }
                tf.setBorder(fieldBorder(true));
            }
            public void focusLost(java.awt.event.FocusEvent e) {
                if (tf.getText().trim().isEmpty()) {
                    tf.setText(placeholder);
                    tf.setForeground(TEXT_MUTED);
                }
                tf.setBorder(fieldBorder(false));
            }
        });
        return tf;
    }

    /** Styled password field */
    public static JPasswordField makePasswordField(final String placeholder) {
        final JPasswordField pf = new JPasswordField();
        pf.setFont(FONT_INPUT);
        pf.setForeground(TEXT_MUTED);
        pf.setBackground(new Color(248,249,252));
        pf.setBorder(fieldBorder(false));
        pf.setEchoChar((char)0);
        pf.setText(placeholder);
        pf.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent e) {
                if (String.valueOf(pf.getPassword()).equals(placeholder)) {
                    pf.setText("");
                    pf.setEchoChar('\u2022');
                    pf.setForeground(TEXT_PRIMARY);
                }
                pf.setBorder(fieldBorder(true));
            }
            public void focusLost(java.awt.event.FocusEvent e) {
                if (pf.getPassword().length == 0) {
                    pf.setEchoChar((char)0);
                    pf.setText(placeholder);
                    pf.setForeground(TEXT_MUTED);
                }
                pf.setBorder(fieldBorder(false));
            }
        });
        return pf;
    }

    private static Border fieldBorder(boolean focused) {
        Color line = focused ? ACCENT : CARD_BORDER;
        int w      = focused ? 2     : 1;
        return BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(line, w, true),
            BorderFactory.createEmptyBorder(8, 12, 8, 12));
    }

    /** Styled combobox */
    public static JComboBox<String> makeComboBox(String[] items) {
        JComboBox<String> cb = new JComboBox<>(items);
        cb.setFont(FONT_INPUT);
        cb.setBackground(new Color(248,249,252));
        cb.setBorder(BorderFactory.createLineBorder(CARD_BORDER,1,true));
        return cb;
    }

    /** Bold section header label */
    public static JLabel makeSectionHeader(String text) {
        JLabel l = new JLabel(text);
        l.setFont(FONT_SECTION);
        l.setForeground(TEXT_PRIMARY);
        return l;
    }

    /** Form row label (bold, small) */
    public static JLabel makeFormLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(FONT_LABEL);
        l.setForeground(TEXT_PRIMARY);
        return l;
    }

    // ═════════════════════════════════════════════════════════════════════
    //  WHITE  CARD  PANEL
    // ═════════════════════════════════════════════════════════════════════
    /** Generic white rounded card panel for grouping form content */
    public static JPanel makeCard() {
        JPanel p = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                    RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(0,0,0,10));
                g2.fillRoundRect(2,3,getWidth()-2,getHeight()-2,14,14);
                g2.setColor(CARD_BG);
                g2.fillRoundRect(0,0,getWidth()-2,getHeight()-3,14,14);
                g2.setColor(CARD_BORDER);
                g2.drawRoundRect(0,0,getWidth()-3,getHeight()-3,14,14);
                g2.dispose();
            }
        };
        p.setOpaque(false);
        p.setBorder(BorderFactory.createEmptyBorder(18,18,18,18));
        return p;
    }

    // ═════════════════════════════════════════════════════════════════════
    //  TABLE  STYLING
    // ═════════════════════════════════════════════════════════════════════
    /** Apply modern styling to a JTable */
    public static void styleTable(JTable table) {
        table.setFont(FONT_TABLE_BODY);
        table.setRowHeight(34);
        table.setShowHorizontalLines(true);
        table.setShowVerticalLines(false);
        table.setGridColor(new Color(235,235,242));
        table.setBackground(CARD_BG);
        table.setSelectionBackground(new Color(232,231,255));
        table.setSelectionForeground(TEXT_PRIMARY);
        table.setFillsViewportHeight(true);
        table.setIntercellSpacing(new Dimension(0,0));
        // header
        table.getTableHeader().setFont(FONT_TABLE_HEAD);
        table.getTableHeader().setBackground(new Color(248,248,252));
        table.getTableHeader().setForeground(TEXT_SECONDARY);
        table.getTableHeader().setBorder(
            BorderFactory.createMatteBorder(0,0,1,0, CARD_BORDER));
        table.getTableHeader().setReorderingAllowed(false);
    }

    // ═════════════════════════════════════════════════════════════════════
    //  PROGRESS  BAR  STYLING
    // ═════════════════════════════════════════════════════════════════════
    public static void styleProgressBar(JProgressBar bar) {
        bar.setFont(FONT_BTN);
        bar.setForeground(ACCENT);
        bar.setBackground(ACCENT_LIGHT);
        bar.setBorder(BorderFactory.createEmptyBorder());
        bar.setStringPainted(true);
    }
}