package studyplannerproject;

import java.awt.*;
import javax.swing.*;

/**
 * LoginForm  —  first window the user sees.
 */
public class LoginForm extends JFrame {

    // ── UI fields
    private JTextField     txtUsername;
    private JPasswordField txtPassword;
    private JLabel         lblError;
    public LoginForm() {
        buildUI();
    }
    //  BUILD UI
    private void buildUI() {
        setTitle("Smart Study Planner — Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        JPanel root = new JPanel(new GridLayout(1, 2));
        root.setPreferredSize(new Dimension(780, 480));

        root.add(buildBrandPanel());
        root.add(buildLoginCard());

        setContentPane(root);
        pack();
        setLocationRelativeTo(null);   // centre on screen
    }

    private JPanel buildBrandPanel() {
        JPanel brand = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                    RenderingHints.VALUE_ANTIALIAS_ON);
                // solid dark background
                g2.setColor(UITheme.SIDEBAR_BG);
                g2.fillRect(0, 0, getWidth(), getHeight());
                // decorative accent circles
                g2.setColor(new Color(99, 91, 255, 30));
                g2.fillOval(-60, -60, 280, 280);
                g2.setColor(new Color(99, 91, 255, 18));
                g2.fillOval(getWidth() - 180, getHeight() - 180, 320, 320);
                g2.dispose();
            }
        };
        brand.setLayout(new GridBagLayout());

        JPanel inner = new JPanel();
        inner.setOpaque(false);
        inner.setLayout(new BoxLayout(inner, BoxLayout.Y_AXIS));

        // book emoji
        JLabel logo = new JLabel("\uD83D\uDCDA");
        logo.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 52));
        logo.setAlignmentX(Component.CENTER_ALIGNMENT);

        // app name
        JLabel line1 = new JLabel("Smart Study");
        line1.setFont(new Font("Segoe UI", Font.BOLD, 26));
        line1.setForeground(Color.WHITE);
        line1.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel line2 = new JLabel("Planner");
        line2.setFont(new Font("Segoe UI", Font.BOLD, 26));
        line2.setForeground(UITheme.ACCENT_LIGHT);
        line2.setAlignmentX(Component.CENTER_ALIGNMENT);

        // tagline
        JLabel tagline = new JLabel(
            "<html><center>Stay organised.<br>Study smarter.</center></html>");
        tagline.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tagline.setForeground(new Color(175, 182, 220));
        tagline.setAlignmentX(Component.CENTER_ALIGNMENT);
        tagline.setHorizontalAlignment(SwingConstants.CENTER);

        // version badge
        JLabel version = new JLabel("v2.0");
        version.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        version.setForeground(new Color(120, 128, 165));
        version.setAlignmentX(Component.CENTER_ALIGNMENT);

        inner.add(logo);
        inner.add(Box.createVerticalStrut(14));
        inner.add(line1);
        inner.add(line2);
        inner.add(Box.createVerticalStrut(16));
        inner.add(tagline);
        inner.add(Box.createVerticalStrut(20));
        inner.add(version);

        brand.add(inner);
        return brand;
    }

    // ── RIGHT: white login card 
    private JPanel buildLoginCard() {
        // outer wrapper centres the card
        JPanel right = new JPanel(new GridBagLayout());
        right.setBackground(UITheme.CONTENT_BG);

        JPanel card = UITheme.makeCard();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setPreferredSize(new Dimension(320, 360));

        // ── welcome text 
        JLabel welcome = new JLabel("Welcome back \uD83D\uDC4B");
        welcome.setFont(new Font("Segoe UI", Font.BOLD, 20));
        welcome.setForeground(UITheme.TEXT_PRIMARY);
        welcome.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel sub = new JLabel("Sign in to your planner");
        sub.setFont(UITheme.FONT_BODY);
        sub.setForeground(UITheme.TEXT_SECONDARY);
        sub.setAlignmentX(Component.LEFT_ALIGNMENT);

        // ── username field 
        JLabel lblUser = UITheme.makeFormLabel("Username");
        lblUser.setAlignmentX(Component.LEFT_ALIGNMENT);

        txtUsername = UITheme.makeTextField("Enter username");
        txtUsername.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        txtUsername.setAlignmentX(Component.LEFT_ALIGNMENT);
        // pressing Enter moves focus to password
        txtUsername.addActionListener(e -> txtPassword.requestFocus());

        // ── password field 
        JLabel lblPass = UITheme.makeFormLabel("Password");
        lblPass.setAlignmentX(Component.LEFT_ALIGNMENT);

        txtPassword = UITheme.makePasswordField("Enter password");
        txtPassword.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        txtPassword.setAlignmentX(Component.LEFT_ALIGNMENT);
        // pressing Enter = login
        txtPassword.addActionListener(e -> handleLogin());

        // ── error label (hidden until needed) 
        lblError = new JLabel(" ");
        lblError.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblError.setForeground(UITheme.DANGER);
        lblError.setAlignmentX(Component.LEFT_ALIGNMENT);

        // ── buttons 
        JButton btnLogin = UITheme.makePrimaryButton("  Sign In  ");
        JButton btnExit  = UITheme.makeSecondaryButton("Exit");
        btnLogin.addActionListener(e -> handleLogin());
        btnExit .addActionListener(e -> System.exit(0));

        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        btnRow.setOpaque(false);
        btnRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnRow.add(btnLogin);
        btnRow.add(btnExit);

        // ── assemble card 
        card.add(welcome);
        card.add(Box.createVerticalStrut(4));
        card.add(sub);
        card.add(Box.createVerticalStrut(22));
        card.add(lblUser);
        card.add(Box.createVerticalStrut(5));
        card.add(txtUsername);
        card.add(Box.createVerticalStrut(14));
        card.add(lblPass);
        card.add(Box.createVerticalStrut(5));
        card.add(txtPassword);
        card.add(Box.createVerticalStrut(8));
        card.add(lblError);
        card.add(Box.createVerticalStrut(12));
        card.add(btnRow);
        card.add(Box.createVerticalStrut(16));

        right.add(card);
        return right;
    }
    private void handleLogin() {

        String user = txtUsername.getText().trim();
        String pass = new String(txtPassword.getPassword());

        if (user.equals("admin") && pass.equals("1234")) {
            // ── SUCCESS ───────────────────────────────────────────────────
            lblError.setText(" ");
            dispose();                        // close login window
            new MainMenu().setVisible(true);  // open dashboard

        } else {
            // ── FAILURE ───────────────────────────────────────────────────
            lblError.setText("Incorrect username or password.");
            txtPassword.setText("");
            txtPassword.requestFocus();
            shakeWindow();
        }
    }

    private void shakeWindow() {
        final int originX = getX();
        final int[] count = {0};
        Timer shake = new Timer(28, null);
        shake.addActionListener(e -> {
            count[0]++;
            setLocation(originX + (count[0] % 2 == 0 ? 9 : -9), getY());
            if (count[0] >= 8) {
                setLocation(originX, getY());
                shake.stop();
            }
        });
        shake.start();
    }

       public static void main(String[] args) {
        UITheme.apply();
        EventQueue.invokeLater(() -> new LoginForm().setVisible(true));
    }
}
