package studyplannerproject;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;


public class StudyNotesForm extends JFrame {

    // --- UI components ---
    private JList<String>   subjectList;
    private JTextArea       editor;          // initialized in buildUI() BEFORE list selection
    private JLabel          lblWordCount;
    private JLabel          lblStatus;
    private JButton         btnSave, btnClear, btnBack;
    private JLabel          lblTitle;

    // --- Data ---
    private final Map<String, String> notesMap = new HashMap<>();
    private String currentSubject = null;

    public StudyNotesForm() {
        super("📓 Study Notes");
        buildUI();
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(820, 580);
        setLocationRelativeTo(null);
        setResizable(true);
    }

    private void buildUI() {
        getContentPane().setBackground(new Color(245, 248, 255));
        setLayout(new BorderLayout(10, 10));

        // ── TOP BAR ──────────────────────────────────────────────
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(new Color(63, 81, 181));
        topBar.setBorder(new EmptyBorder(12, 18, 12, 18));

        lblTitle = new JLabel("📓  Study Notes");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitle.setForeground(Color.WHITE);
        topBar.add(lblTitle, BorderLayout.WEST);

        lblStatus = new JLabel("Select a subject to begin");
        lblStatus.setFont(new Font("Segoe UI", Font.ITALIC, 13));
        lblStatus.setForeground(new Color(200, 210, 255));
        topBar.add(lblStatus, BorderLayout.EAST);

        add(topBar, BorderLayout.NORTH);

        // ── LEFT PANEL – subject list ─────────────────────────────
        JPanel leftPanel = new JPanel(new BorderLayout(0, 8));
        leftPanel.setBackground(new Color(235, 240, 255));
        leftPanel.setBorder(new CompoundBorder(
                new EmptyBorder(10, 10, 10, 0),
                new LineBorder(new Color(180, 190, 230), 1, true)));
        leftPanel.setPreferredSize(new Dimension(190, 0));

        JLabel lblSubjects = new JLabel("  📚 Subjects");
        lblSubjects.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblSubjects.setForeground(new Color(63, 81, 181));
        lblSubjects.setBorder(new EmptyBorder(8, 6, 8, 6));
        leftPanel.add(lblSubjects, BorderLayout.NORTH);

        DefaultListModel<String> listModel = new DefaultListModel<>();
        if (DataStore.subjects.isEmpty()) {
            listModel.addElement("General");
        } else {
            DataStore.subjects.forEach(listModel::addElement);
        }

        subjectList = new JList<>(listModel);
        subjectList.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subjectList.setBackground(new Color(245, 248, 255));
        subjectList.setSelectionBackground(new Color(63, 81, 181));
        subjectList.setSelectionForeground(Color.WHITE);
        subjectList.setFixedCellHeight(36);
        subjectList.setBorder(new EmptyBorder(4, 8, 4, 8));

        leftPanel.add(new JScrollPane(subjectList), BorderLayout.CENTER);
        add(leftPanel, BorderLayout.WEST);

        // ── CENTER – editor ───────────────────────────────────────
        // CRITICAL: create editor BEFORE attaching the list selection listener
        editor = new JTextArea();
        editor.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        editor.setBackground(new Color(255, 255, 253));
        editor.setForeground(new Color(30, 30, 50));
        editor.setCaretColor(new Color(63, 81, 181));
        editor.setLineWrap(true);
        editor.setWrapStyleWord(true);
        editor.setBorder(new EmptyBorder(12, 14, 12, 14));
        editor.setEnabled(false);

        editor.addKeyListener(new KeyAdapter() {
            @Override public void keyReleased(KeyEvent e) { updateWordCount(); }
        });

        JScrollPane editorScroll = new JScrollPane(editor);
        editorScroll.setBorder(new CompoundBorder(
                new EmptyBorder(10, 6, 0, 10),
                new LineBorder(new Color(180, 190, 230), 1, true)));

        JPanel centerPanel = new JPanel(new BorderLayout(0, 6));
        centerPanel.setBackground(new Color(245, 248, 255));
        centerPanel.add(editorScroll, BorderLayout.CENTER);

        // word count bar
        lblWordCount = new JLabel("Words: 0  |  Characters: 0");
        lblWordCount.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblWordCount.setForeground(new Color(120, 120, 160));
        lblWordCount.setBorder(new EmptyBorder(2, 16, 2, 0));
        centerPanel.add(lblWordCount, BorderLayout.SOUTH);

        add(centerPanel, BorderLayout.CENTER);

        // ── BOTTOM BUTTONS ────────────────────────────────────────
        JPanel bottomBar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 8));
        bottomBar.setBackground(new Color(245, 248, 255));
        bottomBar.setBorder(new EmptyBorder(0, 10, 8, 10));

        btnBack  = makeButton("⬅ Back",  new Color(120, 130, 160));
        btnClear = makeButton("🗑 Clear", new Color(220, 80,  80));
        btnSave  = makeButton("💾 Save",  new Color(63,  180, 100));

        bottomBar.add(btnBack);
        bottomBar.add(btnClear);
        bottomBar.add(btnSave);
        add(bottomBar, BorderLayout.SOUTH);

        // ── LISTENERS (editor is already non-null here) ───────────
        subjectList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                switchSubject(subjectList.getSelectedValue());
            }
        });

        btnSave.addActionListener(e -> saveCurrentNotes());
        btnClear.addActionListener(e -> {
            if (currentSubject != null) {
                int ok = JOptionPane.showConfirmDialog(this,
                     "Clear notes for " + currentSubject + "?",
                        "Confirm", JOptionPane.YES_NO_OPTION);
                if (ok == JOptionPane.YES_OPTION) {
                    editor.setText("");
                    updateWordCount();
                }
            }
        });
        btnBack.addActionListener(e -> { saveCurrentNotes(); dispose(); });

        // Select first item AFTER everything is wired up
        if (listModel.size() > 0) {
            subjectList.setSelectedIndex(0);
        }
    }

    /** Called when user picks a different subject in the list. */
    private void switchSubject(String subject) {
        if (subject == null) return;

        // Save previous subject's notes first
        if (currentSubject != null) {
            notesMap.put(currentSubject, editor.getText());
        }

        currentSubject = subject;
        // editor is guaranteed non-null here
        editor.setText(notesMap.getOrDefault(subject, ""));
        editor.setEnabled(true);
        editor.requestFocusInWindow();
        lblStatus.setText("Editing: " + subject);
        updateWordCount();
    }

    private void saveCurrentNotes() {
        if (currentSubject != null) {
            notesMap.put(currentSubject, editor.getText());
            lblStatus.setText("✅ Saved: " + currentSubject);
            javax.swing.Timer t =
    new javax.swing.Timer(2000, e ->
                    lblStatus.setText("Editing: " + currentSubject));
            t.setRepeats(false);
            t.start();
        }
    }

    private void updateWordCount() {
        String text = editor.getText().trim();
        int words = text.isEmpty() ? 0 : text.split("\\s+").length;
        int chars = editor.getText().length();
        lblWordCount.setText("Words: " + words + "  |  Characters: " + chars);
    }

    private JButton makeButton(String text, Color bg) {
        JButton b = new JButton(text);
        b.setFont(new Font("Segoe UI", Font.BOLD, 13));
        b.setBackground(bg);
        b.setForeground(Color.BLACK);
        b.setFocusPainted(false);
        b.setBorder(new EmptyBorder(8, 18, 8, 18));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setOpaque(true);
        return b;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new StudyNotesForm().setVisible(true));
    }
}