package studyplannerproject;

import java.awt.EventQueue;

/**
 * Main  —  the ONE and ONLY entry point for the entire application.
 *
 */
public class Main {

    public static void main(String[] args) {
        UITheme.apply();

        // 2. Launch the LoginForm on the Swing Event Dispatch Thread (EDT).
        //    Always create Swing components on the EDT — never on the main thread.
        EventQueue.invokeLater(() -> {
            LoginForm login = new LoginForm();
            login.setVisible(true);
        });
    }
}