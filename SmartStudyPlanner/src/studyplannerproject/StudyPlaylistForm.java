package studyplannerproject;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

public class StudyPlaylistForm extends JFrame {

    private DefaultListModel<PlaylistItem> listModel;
    private JList<PlaylistItem>            trackList;
    private JLabel                         lblNowPlaying;
    private JLabel                         lblNote;
    private javax.swing.Timer pulseTimer;
    private boolean                        pulseBright = true;

    private static final Color BG      = new Color(18,  18,  36);
    private static final Color CARD    = new Color(30,  30,  55);
    private static final Color ACCENT  = new Color(138, 43, 226);
    private static final Color GREEN   = new Color(29, 185, 84);
    private static final Color TEXT    = new Color(240, 240, 255);
    private static final Color SUBTEXT = new Color(160, 160, 200);

    public StudyPlaylistForm() {
        super("🎵 Study Playlist");
        buildUI();
        loadDefaultTracks();
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(620, 640);
        setLocationRelativeTo(null);
    }

    private void buildUI() {
        setBackground(BG);
        setLayout(new BorderLayout(0, 0));

        // ── HEADER ───────────────────────────────────────────────
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(ACCENT);
        header.setBorder(new EmptyBorder(14, 20, 14, 20));

        JLabel title = new JLabel("🎵  Study Playlist");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(Color.BLACK);
        header.add(title, BorderLayout.WEST);

        lblNote = new JLabel("🎧 Focus music for deep study");
        lblNote.setFont(new Font("Segoe UI", Font.ITALIC, 13));
        lblNote.setForeground(new Color(220, 200, 255));
        header.add(lblNote, BorderLayout.EAST);
        add(header, BorderLayout.NORTH);

        // ── NOW PLAYING bar ───────────────────────────────────────
        JPanel nowPlaying = new JPanel(new BorderLayout(10, 0));
        nowPlaying.setBackground(CARD);
        nowPlaying.setBorder(new EmptyBorder(10, 18, 10, 18));

        JLabel icon = new JLabel("♫");
        icon.setFont(new Font("Segoe UI", Font.BOLD, 22));
        icon.setForeground(GREEN);

        lblNowPlaying = new JLabel("No track selected");
        lblNowPlaying.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblNowPlaying.setForeground(GREEN);

        nowPlaying.add(icon,          BorderLayout.WEST);
        nowPlaying.add(lblNowPlaying, BorderLayout.CENTER);
        add(nowPlaying, BorderLayout.NORTH); // wrapped below

        // pulse animation
        pulseTimer = new javax.swing.Timer(700, e -> {
            pulseBright = !pulseBright;
            lblNowPlaying.setForeground(pulseBright ? GREEN : new Color(20, 130, 50));
        });

        // ── ADD TRACK INPUT ───────────────────────────────────────
        JPanel inputRow = new JPanel(new BorderLayout(8, 0));
        inputRow.setBackground(BG);
        inputRow.setBorder(new EmptyBorder(10, 14, 6, 14));

        JTextField tfTrack = new JTextField();
        tfTrack.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tfTrack.setBackground(CARD);
        tfTrack.setForeground(TEXT);
        tfTrack.setCaretColor(TEXT);
        tfTrack.setBorder(new CompoundBorder(
                new LineBorder(ACCENT, 1, true),
                new EmptyBorder(8, 12, 8, 12)));
        tfTrack.setToolTipText("Enter track name or paste URL");

        JTextField tfUrl = new JTextField("https://");
        tfUrl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tfUrl.setBackground(CARD);
        tfUrl.setForeground(SUBTEXT);
        tfUrl.setCaretColor(TEXT);
        tfUrl.setPreferredSize(new Dimension(160, 0));
        tfUrl.setBorder(new CompoundBorder(
                new LineBorder(new Color(80, 60, 120), 1, true),
                new EmptyBorder(8, 8, 8, 8)));

        JButton btnAdd = styledBtn("＋ Add", new Color(76, 175, 80));
        btnAdd.addActionListener(e -> {
            String name = tfTrack.getText().trim();
            String url  = tfUrl.getText().trim();
            if (!name.isEmpty()) {
                listModel.addElement(new PlaylistItem(name, url, false));
                tfTrack.setText("");
                tfUrl.setText("https://");
            }
        });
        tfTrack.addActionListener(e -> btnAdd.doClick());

        JPanel urlRow = new JPanel(new BorderLayout(4, 0));
        urlRow.setBackground(BG);
        urlRow.add(tfTrack, BorderLayout.CENTER);
        urlRow.add(tfUrl,   BorderLayout.EAST);

        inputRow.add(urlRow, BorderLayout.CENTER);
        inputRow.add(btnAdd, BorderLayout.EAST);

        // ── TRACK LIST ────────────────────────────────────────────
        listModel = new DefaultListModel<>();
        trackList = new JList<>(listModel);
        trackList.setBackground(CARD);
        trackList.setForeground(TEXT);
        trackList.setSelectionBackground(ACCENT);
        trackList.setSelectionForeground(Color.WHITE);
        trackList.setCellRenderer(new TrackCellRenderer());
        trackList.setFixedCellHeight(52);

        JScrollPane scroll = new JScrollPane(trackList);
        scroll.setBorder(new EmptyBorder(0, 14, 0, 14));
        scroll.getViewport().setBackground(CARD);

        // ── ACTION BUTTONS ────────────────────────────────────────
        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 8));
        btnRow.setBackground(BG);
        btnRow.setBorder(new EmptyBorder(0, 14, 0, 0));

        JButton btnPlay  = styledBtn("▶ Play",       new Color(29, 185, 84));
        JButton btnFav   = styledBtn("★ Favourite",  new Color(255, 180, 0));
        JButton btnDel   = styledBtn("🗑 Remove",    new Color(220, 80, 80));
        JButton btnCopy  = styledBtn("🔗 Copy URL",  new Color(63, 81, 181));
        JButton btnBack  = styledBtn("⬅ Back",       new Color(100, 110, 140));

        btnPlay.addActionListener(e -> {
            PlaylistItem sel = trackList.getSelectedValue();
            if (sel != null) {
                lblNowPlaying.setText("▶  " + sel.getName());
                pulseTimer.start();
                if (!sel.getUrl().equals("https://") && sel.getUrl().startsWith("http")) {
                    try { Desktop.getDesktop().browse(new java.net.URI(sel.getUrl())); }
                    catch (Exception ex) { /* no browser */ }
                }
            }
        });

        btnFav.addActionListener(e -> {
            int idx = trackList.getSelectedIndex();
            if (idx >= 0) {
                PlaylistItem it = listModel.get(idx);
                it.setFav(!it.isFav());
                trackList.repaint();
            }
        });

        btnDel.addActionListener(e -> {
            int idx = trackList.getSelectedIndex();
            if (idx >= 0) listModel.remove(idx);
        });

        btnCopy.addActionListener(e -> {
            PlaylistItem sel = trackList.getSelectedValue();
            if (sel != null) {
                Toolkit.getDefaultToolkit().getSystemClipboard()
                       .setContents(new java.awt.datatransfer.StringSelection(sel.getUrl()), null);
                JOptionPane.showMessageDialog(this, "URL copied to clipboard!");
            }
        });

        btnBack.addActionListener(e -> { pulseTimer.stop(); dispose(); });

        btnRow.add(btnPlay); btnRow.add(btnFav);
        btnRow.add(btnDel);  btnRow.add(btnCopy); btnRow.add(btnBack);

        // ── ASSEMBLE ─────────────────────────────────────────────
        JPanel center = new JPanel(new BorderLayout(0, 0));
        center.setBackground(BG);
        center.add(inputRow, BorderLayout.NORTH);
        center.add(scroll,   BorderLayout.CENTER);
        center.add(btnRow,   BorderLayout.SOUTH);

        JPanel wrap = new JPanel(new BorderLayout());
        wrap.setBackground(BG);
        wrap.add(nowPlaying, BorderLayout.NORTH);
        wrap.add(center,     BorderLayout.CENTER);
        add(wrap, BorderLayout.CENTER);
    }

    private void loadDefaultTracks() {
        listModel.addElement(new PlaylistItem("Lo-Fi Hip Hop – Study Beats",
                "https://www.youtube.com/watch?v=jfKfPfyJRdk", true));
        listModel.addElement(new PlaylistItem("Brain.fm – Focus Music",
                "https://www.brain.fm", false));
        listModel.addElement(new PlaylistItem("Classical Study Music – Mozart",
                "https://www.youtube.com/watch?v=Fmgp_Lfzums", false));
        listModel.addElement(new PlaylistItem("Deep Focus – White Noise",
                "https://www.youtube.com/watch?v=nMfPqeZjc2c", false));
        listModel.addElement(new PlaylistItem("Synthwave Study Session",
                "https://www.youtube.com/watch?v=4xDzrJKXOOY", false));
    }

    private JButton styledBtn(String text, Color bg) {
        JButton b = new JButton(text);
        b.setFont(new Font("Segoe UI", Font.BOLD, 12));
        b.setBackground(bg);
        b.setForeground(Color.BLACK);
        b.setFocusPainted(false);
        b.setOpaque(true);
        b.setBorder(new EmptyBorder(7, 14, 7, 14));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return b;
    }

    // ── Model ─────────────────────────────────────────────────────
    static class PlaylistItem {
        private final String name, url;
        private boolean fav;
        PlaylistItem(String n, String u, boolean f) { name=n; url=u; fav=f; }
        String  getName() { return name; }
        String  getUrl()  { return url; }
        boolean isFav()   { return fav; }
        void    setFav(boolean f) { fav = f; }
        @Override public String toString() { return name; }
    }

    // ── Cell Renderer ─────────────────────────────────────────────
    private class TrackCellRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value,
                int index, boolean selected, boolean focused) {
            JPanel cell = new JPanel(new BorderLayout(10, 0));
            PlaylistItem item = (PlaylistItem) value;
            cell.setBackground(selected ? ACCENT : (index % 2 == 0 ? CARD : new Color(35, 35, 60)));
            cell.setBorder(new EmptyBorder(8, 14, 8, 14));

            JLabel icon = new JLabel(item.isFav() ? "★" : "♪");
            icon.setFont(new Font("Segoe UI", Font.BOLD, 20));
            icon.setForeground(item.isFav() ? new Color(255, 200, 0) : new Color(138, 43, 226));

            JPanel info = new JPanel(new GridLayout(2, 1, 0, 2));
            info.setOpaque(false);

            JLabel lName = new JLabel(item.getName());
            lName.setFont(new Font("Segoe UI", Font.BOLD, 13));
            lName.setForeground(selected ? Color.WHITE : TEXT);

            JLabel lUrl = new JLabel(item.getUrl().length() > 50
                    ? item.getUrl().substring(0, 50) + "…" : item.getUrl());
            lUrl.setFont(new Font("Segoe UI", Font.PLAIN, 11));
            lUrl.setForeground(selected ? new Color(220, 220, 255) : SUBTEXT);

            info.add(lName); info.add(lUrl);
            cell.add(icon, BorderLayout.WEST);
            cell.add(info, BorderLayout.CENTER);
            return cell;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new StudyPlaylistForm().setVisible(true));
    }
}