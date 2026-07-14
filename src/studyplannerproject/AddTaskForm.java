package studyplannerproject;

import java.awt.*;
import java.text.SimpleDateFormat;
import javax.swing.*;
import com.toedter.calendar.JDateChooser;


public class AddTaskForm extends JFrame {

    private MainMenu   mainMenu;

   
    private JTextField     txtTaskName;
    private JComboBox<String> cmbType;
    private JComboBox<String> cmbPriority;
    private JComboBox<String> cmbSubject;
    private JDateChooser   dateDeadline;
    private JLabel         lblError;

    public AddTaskForm(MainMenu mainMenu) {
        this.mainMenu = mainMenu;
        buildUI();
        refreshSubjects();
    }
    public AddTaskForm() { this(null); }

    
    private void buildUI() {
        setTitle("Add Task");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(520, 540);
        setResizable(false);
        setLocationRelativeTo(null);

        
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(UITheme.CONTENT_BG);

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(UITheme.HEADER_BG);
        header.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0,0,1,0, UITheme.CARD_BORDER),
            BorderFactory.createEmptyBorder(14,24,14,24)));
        JLabel pageTitle = new JLabel("\u2714  Add New Task");
        pageTitle.setFont(UITheme.FONT_PAGE_TITLE);
        pageTitle.setForeground(UITheme.TEXT_PRIMARY);
        header.add(pageTitle, BorderLayout.WEST);

       
        JPanel card = UITheme.makeCard();
        card.setLayout(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();
        gc.fill   = GridBagConstraints.HORIZONTAL;
        gc.insets = new Insets(6, 0, 6, 0);

      
        Insets labelInsets = new Insets(10, 0, 2, 0);
        Insets fieldInsets = new Insets(0,  0, 0, 0);

        gc.gridx=0; gc.gridy=0; gc.insets=labelInsets;
        card.add(UITheme.makeFormLabel("Task Title"), gc);
        gc.gridy=1; gc.insets=fieldInsets;
        txtTaskName = UITheme.makeTextField("e.g. Chapter 5 revision");
        txtTaskName.setPreferredSize(new Dimension(400, 38));
        card.add(txtTaskName, gc);

        // Task Type
        gc.gridy=2; gc.insets=labelInsets;
        card.add(UITheme.makeFormLabel("Task Type"), gc);
        gc.gridy=3; gc.insets=fieldInsets;
        cmbType = UITheme.makeComboBox(new String[]{"Assignment","Quiz","Exam","Study","Other"});
        cmbType.setPreferredSize(new Dimension(400,36));
        card.add(cmbType, gc);

        // Subject
        gc.gridy=4; gc.insets=labelInsets;
        card.add(UITheme.makeFormLabel("Subject"), gc);
        gc.gridy=5; gc.insets=fieldInsets;
        cmbSubject = UITheme.makeComboBox(new String[]{});
        cmbSubject.setPreferredSize(new Dimension(400,36));
        card.add(cmbSubject, gc);

        // Deadline
        gc.gridy=6; gc.insets=labelInsets;
        card.add(UITheme.makeFormLabel("Deadline"), gc);
        gc.gridy=7; gc.insets=fieldInsets;
        dateDeadline = new JDateChooser();
        dateDeadline.setFont(UITheme.FONT_INPUT);
        dateDeadline.setPreferredSize(new Dimension(400,36));
        dateDeadline.setBackground(new Color(248,249,252));
        card.add(dateDeadline, gc);

        gc.gridy=8; gc.insets=labelInsets;
        card.add(UITheme.makeFormLabel("Priority"), gc);
        gc.gridy=9; gc.insets=fieldInsets;
        cmbPriority = UITheme.makeComboBox(new String[]{"High","Medium","Low"});
        cmbPriority.setSelectedItem("Medium");
        cmbPriority.setPreferredSize(new Dimension(400,36));
        card.add(cmbPriority, gc);

      
        gc.gridy=10; gc.insets=new Insets(4,0,0,0);
        lblError = new JLabel(" ");
        lblError.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblError.setForeground(UITheme.DANGER);
        card.add(lblError, gc);

        
        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        btnRow.setOpaque(false);
        JButton btnBack     = UITheme.makeSecondaryButton("Back");
        JButton btnAdd      = UITheme.makeSecondaryButton("Add & Stay");
        JButton btnSave     = UITheme.makePrimaryButton("Save & Close");

        btnBack.addActionListener(e -> {
            if (mainMenu != null) mainMenu.setVisible(true);
            else new MainMenu().setVisible(true);
            dispose();
        });
        btnAdd.addActionListener(e -> {
            if (saveTask()) {
                // stay open for next entry
                txtTaskName.setText("");
                dateDeadline.setDate(null);
                lblError.setText(" ");
            }
        });
        btnSave.addActionListener(e -> {
            if (saveTask()) {
                if (mainMenu != null) { mainMenu.setVisible(true); }
                else new MainMenu().setVisible(true);
                dispose();
            }
        });

        btnRow.add(btnBack);
        btnRow.add(btnAdd);
        btnRow.add(btnSave);

    
        JPanel centre = new JPanel(new GridBagLayout());
        centre.setBackground(UITheme.CONTENT_BG);
        centre.setBorder(BorderFactory.createEmptyBorder(20,30,20,30));
        centre.add(card);

        root.add(header, BorderLayout.NORTH);
        root.add(centre, BorderLayout.CENTER);
        root.add(wrapButtons(btnRow), BorderLayout.SOUTH);

        setContentPane(root);
    }

    private JPanel wrapButtons(JPanel inner) {
        JPanel foot = new JPanel(new BorderLayout());
        foot.setBackground(UITheme.HEADER_BG);
        foot.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(1,0,0,0, UITheme.CARD_BORDER),
            BorderFactory.createEmptyBorder(12,24,12,24)));
        foot.add(inner, BorderLayout.EAST);
        return foot;
    }

    private void refreshSubjects() {
        cmbSubject.removeAllItems();
        if (DataStore.subjects.isEmpty()) {
            cmbSubject.addItem("(Add a subject first)");
        } else {
            for (String s : DataStore.subjects) cmbSubject.addItem(s);
        }
    }

    private boolean saveTask() {
        // validate title
        String title = txtTaskName.getText().trim();
        if (title.isEmpty() || title.equals("e.g. Chapter 5 revision")) {
            lblError.setText("Please enter a task title.");
            return false;
        }
        // validate date
        if (dateDeadline.getDate() == null) {
            lblError.setText("Please select a deadline date.");
            return false;
        }
        // validate subject
        if (cmbSubject.getSelectedItem() == null ||
            cmbSubject.getSelectedItem().toString().startsWith("(")) {
            lblError.setText("Please add a subject first.");
            return false;
        }

        String type     = cmbType.getSelectedItem().toString();
        String priority = cmbPriority.getSelectedItem().toString();
        String subject  = cmbSubject.getSelectedItem().toString();
        String deadline = new SimpleDateFormat("dd-MM-yyyy")
                              .format(dateDeadline.getDate());

        Task task = new Task(title, type, deadline, priority, subject);
        DataStore.tasks.add(task);
        FileManager.saveData();
        SoundPlayer.playSound("notification.wav");

        if (mainMenu != null) mainMenu.updateDashboard();

        lblError.setForeground(UITheme.SUCCESS);
        lblError.setText("Task saved successfully!");
        Timer reset = new Timer(2000, e -> {
            lblError.setText(" ");
            lblError.setForeground(UITheme.DANGER);
        });
        reset.setRepeats(false);
        reset.start();
        return true;
    }

    public static void main(String[] args) {
        UITheme.apply();
        EventQueue.invokeLater(() -> new AddTaskForm().setVisible(true));
    }
}