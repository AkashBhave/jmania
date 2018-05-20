import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileFilter;

@SuppressWarnings("serial")
public class PanelSelect extends JPanel {

    final private Window owner;
    private int height;
    private int width;
    private PanelBack backButtonLayout;

    private DefaultListModel<String> songs = new DefaultListModel<>();
    private String[] songArray;

    public PanelSelect(Window owner, int width, int height) {

        this.width = width;
        this.height = height;
        this.owner = owner;

        createGUI();
    }

    public void paintComponent(Graphics g) {
        g.setColor(Driver.bgColor);
        g.fillRect(0, 0, width, height);
    }

    private void createGUI() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.X_AXIS));

        Font mainFont = new Font(Driver.fontFamily, Font.PLAIN, 20);

        backButtonLayout = new PanelBack();
        backButtonLayout.backButton.addActionListener(event -> {
            SwingUtilities.invokeLater(() -> owner.showView(new PanelHome(owner, Driver.width, Driver.height)));
        });

        add(backButtonLayout);
        int backMap = JComponent.WHEN_IN_FOCUSED_WINDOW;
        InputMap imap = this.getInputMap(backMap);
        KeyStroke escape = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
        imap.put(escape, "return");

        ActionMap amap = this.getActionMap();
        amap.put("return", new BackAction() );

        initSongs();


        JButton leftButton = new JButton();
        JButton rightButton = new JButton();

        try {
            Image img = ImageIO.read(getClass().getResource("assets/images/leftButton.png"));
            img = img.getScaledInstance(50, 50, Image.SCALE_SMOOTH);
            leftButton.setIcon(new ImageIcon(img));
            img = ImageIO.read(getClass().getResource("assets/images/rightButton.png"));
            img = img.getScaledInstance(50, 50, Image.SCALE_SMOOTH);
            rightButton.setIcon(new ImageIcon(img));
        } catch (Exception ex) {
            System.out.println(ex);
        }

        leftButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(Box.createVerticalGlue());
        mainPanel.add(leftButton);
        mainPanel.add(Box.createVerticalGlue());



        JPanel songInfoPanel = new JPanel();
        songInfoPanel.setLayout(new BoxLayout(songInfoPanel, BoxLayout.Y_AXIS));
        songInfoPanel.setMinimumSize(new Dimension(500, 600));
        JLabel songTitle = new JLabel("SONG");
        songInfoPanel.add(songTitle);



        mainPanel.add(songInfoPanel);

        leftButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(Box.createVerticalGlue());
        mainPanel.add(rightButton);
        mainPanel.add(Box.createVerticalGlue());


        mainPanel.setPreferredSize(new Dimension(1280, 600));
        JList songList = new JList(songs);
        songList.setPreferredSize(new Dimension(100, 600));
        mainPanel.add(songList);

        add(mainPanel);

        owner.requestFocus();
    }

    private void initSongs() {
        File songDirectory = new File(Driver.projectPath + "/assets/songs/");
        File[] songFiles = songDirectory.listFiles(new FileFilter() {
            @Override
            public boolean accept(File file) {
                return file.isDirectory() || file.getName().toLowerCase().endsWith(".csm");
            }
        });

        songArray = new String[songFiles.length];

        for(int i = 0; i < songFiles.length; i++) {
            songs.addElement(songFiles[i].getName().replaceAll("_", " "));
            songArray[i] = songFiles[i].getName();
        }
    }

    @SuppressWarnings("serial")
    private class BackAction extends AbstractAction {
        public void actionPerformed (ActionEvent e) {
            backButtonLayout.backButton.doClick();
        }
    }


}