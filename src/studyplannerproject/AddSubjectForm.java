package studyplannerproject;

import java.awt.*;
import javax.swing.*;


public class AddSubjectForm extends JFrame {

    private MainMenu   mainMenu;
    private JTextField txtSubject;
    private JPanel     chipsPanel;
    private JLabel     lblStatus;

    public AddSubjectForm(MainMenu mainMenu) {
        this.mainMenu = mainMenu;
        buildUI();
        refreshChips();
    }
    public AddSubjectForm() { this(null); }

    private void buildUI() {
        setTitle("Add Subject");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(480, 420);
        setResizable(false);
        setLocationRelativeTo(null);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(UITheme.CONTENT_BG);

        
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(UITheme.HEADER_BG);
        header.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0,0,1,0, UITheme.CARD_BORDER),
            BorderFactory.createEmptyBorder(14,24,14,24)));
        JLabel pageTitle = new JLabel("\uD83D\uDCDA  Add Subject");
        pageTitle.setFont(UITheme.FONT_PAGE_TITLE);
        pageTitle.setForeground(UITheme.TEXT_PRIMARY);
        header.add(pageTitle, BorderLayout.WEST);

        
        JPanel card = UITheme.makeCard();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));

        JLabel fieldLabel = UITheme.makeFormLabel("Subject Name");
        fieldLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        txtSubject = UITheme.makeTextField("e.g. Mathematics");
        txtSubject.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        txtSubject.setAlignmentX(Component.LEFT_ALIGNMENT);
        // pressing Enter = add
        txtSubject.addActionListener(e -> addSubject());

        // status message
        lblStatus = new JLabel(" ");
        lblStatus.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblStatus.setForeground(UITheme.DANGER);
        lblStatus.setAlignmentX(Component.LEFT_ALIGNMENT);

        // existing chips label
        JLabel existLabel = UITheme.makeSectionHeader("Saved Subjects");
        existLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        existLabel.setBorder(BorderFactory.createEmptyBorder(14,0,6,0));

        chipsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        chipsPanel.setOpaque(false);
        chipsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        card.add(fieldLabel);
        card.add(Box.createVerticalStrut(6));
        card.add(txtSubject);
        card.add(Box.createVerticalStrut(4));
        card.add(lblStatus);
        card.add(existLabel);
        card.add(chipsPanel);

        JPanel centre = new JPanel(new GridBagLayout());
        centre.setBackground(UITheme.CONTENT_BG);
        centre.setBorder(BorderFactory.createEmptyBorder(20,28,20,28));
        centre.add(card);

       
        JPanel foot = new JPanel(new BorderLayout());
        foot.setBackground(UITheme.HEADER_BG);
        foot.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(1,0,0,0, UITheme.CARD_BORDER),
            BorderFactory.createEmptyBorder(12,24,12,24)));

        JButton btnBack = UITheme.makeSecondaryButton("Back");
        JButton btnAdd  = UITheme.makePrimaryButton("Add Subject");
        btnBack.addActionListener(e -> {
            if (mainMenu != null) mainMenu.setVisible(true);
            else new MainMenu().setVisible(true);
            dispose();
        });
        btnAdd.addActionListener(e -> addSubject());

        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        btnRow.setOpaque(false);
        btnRow.add(btnBack);
        btnRow.add(btnAdd);
        foot.add(btnRow, BorderLayout.EAST);

        root.add(header, BorderLayout.NORTH);
        root.add(centre, BorderLayout.CENTER);
        root.add(foot,   BorderLayout.SOUTH);

        setContentPane(root);
    }

    private void addSubject() {
        String name = txtSubject.getText().trim();

        if (name.isEmpty() || name.equals("e.g. Mathematics")) {
            showStatus("Please enter a subject name.", false);
            return;
        }
        for (String ex : DataStore.subjects) {
            if (ex.equalsIgnoreCase(name)) {
                showStatus("\"" + name + "\" is already in the list.", false);
                return;
            }
        }
        DataStore.subjects.add(name);
        FileManager.saveData();
        txtSubject.setText("");
        refreshChips();
        showStatus("Subject added: " + name, true);
    }

    private void showStatus(String msg, boolean success) {
        lblStatus.setForeground(success ? UITheme.SUCCESS : UITheme.DANGER);
        lblStatus.setText(msg);
        Timer t = new Timer(3000, e -> lblStatus.setText(" "));
        t.setRepeats(false);
        t.start();
    }

    private void refreshChips() {
        chipsPanel.removeAll();
        Color[] bg  = {UITheme.TINT_BLUE, UITheme.TINT_GREEN,
                        UITheme.TINT_AMBER, UITheme.TINT_RED, UITheme.ACCENT_LIGHT};
        Color[] fg  = {UITheme.TINT_BLUE_T, UITheme.TINT_GREEN_T,
                        UITheme.TINT_AMBER_T, UITheme.TINT_RED_T, UITheme.ACCENT};
        if (DataStore.subjects.isEmpty()) {
            JLabel e = new JLabel("No subjects yet.");
            e.setFont(UITheme.FONT_BODY);
            e.setForeground(UITheme.TEXT_MUTED);
            chipsPanel.add(e);
        } else {
            for (int i = 0; i < DataStore.subjects.size(); i++) {
                final Color cbg = bg[i % bg.length];
                final Color cfg = fg[i % fg.length];
                final String s  = DataStore.subjects.get(i);
                JLabel chip = new JLabel(s) {
                    @Override protected void paintComponent(Graphics g) {
                        Graphics2D g2 = (Graphics2D) g.create();
                        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                            RenderingHints.VALUE_ANTIALIAS_ON);
                        g2.setColor(cbg);
                        g2.fillRoundRect(0,0,getWidth(),getHeight(),20,20);
                        g2.dispose();
                        super.paintComponent(g);
                    }
                };
                chip.setFont(new Font("Segoe UI", Font.BOLD, 11));
                chip.setForeground(cfg);
                chip.setOpaque(false);
                chip.setBorder(BorderFactory.createEmptyBorder(5,12,5,12));
                chipsPanel.add(chip);
            }
        }
        chipsPanel.revalidate();
        chipsPanel.repaint();
    }

    public static void main(String[] args) {
        UITheme.apply();
        EventQueue.invokeLater(() -> new AddSubjectForm().setVisible(true));
    }
}
