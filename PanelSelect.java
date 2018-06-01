import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.net.URL;
import java.util.Arrays;

@SuppressWarnings("serial")
public class PanelSelect extends JPanel {

    final private Window owner;
    private int height;
    private int width;
    private PanelBack backButtonLayout;

    private DefaultListModel<String> songs = new DefaultListModel<>();
    private String[] songNameArray;
    private String[] songDirectoryArray;

    private JList songList;
    public static JPanel songInfoPanel = new JPanel();

    private JButton leftButton;
    private JButton rightButton;
    public static JButton playButton;

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
        mainPanel.setBackground(Driver.bgColor);

        backButtonLayout = new PanelBack();
        backButtonLayout.backButton.addActionListener(event -> {
            SwingUtilities.invokeLater(() -> owner.showView(new PanelHome(owner, Driver.width, Driver.height)));
        });

        add(backButtonLayout);

        initSongs();

        leftButton = new JButton();
        rightButton = new JButton();

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
        mainPanel.add(leftButton);
        mainPanel.add(Box.createHorizontalGlue());


        songInfoPanel = new JPanel();
        songInfoPanel.setLayout(new BoxLayout(songInfoPanel, BoxLayout.Y_AXIS));
        songInfoPanel.setPreferredSize(new Dimension(500, 600));
        songInfoPanel.setBackground(Driver.bgColor);
        songInfoPanel.setFont(Driver.standardFont);


        mainPanel.add(songInfoPanel);
        mainPanel.add(Box.createHorizontalGlue());

        leftButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(rightButton);
        mainPanel.add(Box.createHorizontalGlue());


        mainPanel.setPreferredSize(new Dimension(1280, 600));
        songList = new JList(songs);
        songList.setPreferredSize(new Dimension(200, 600));
        try {
            String songAbsolutePath = songDirectoryArray[0] + File.separator + songNameArray[0];
            setSongInfoPanel(songAbsolutePath, songDirectoryArray[0]);
        } catch (Exception e) {
            e.printStackTrace();
        }
        songList.setSelectedIndex(0);
        songList.setFont(Driver.fontRegular.deriveFont(16f));
        songList.setBackground(Driver.bgColor);


        // Action listeners
        leftButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                changeSongListener(-1);
            }
        });

        songList.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent event) {
                if (!event.getValueIsAdjusting()){
                    JList source = (JList)event.getSource();
                    int selectedIndex = source.getSelectedIndex();
                    try {
                        String songAbsolutePath = songDirectoryArray[selectedIndex] + File.separator + songNameArray[selectedIndex];
                        setSongInfoPanel(songAbsolutePath, songDirectoryArray[selectedIndex]);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        rightButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                changeSongListener(1);
            }
        } );

        mainPanel.add(songList);

        add(mainPanel);

        int backMap = JComponent.WHEN_IN_FOCUSED_WINDOW;
        InputMap imap = this.getInputMap(backMap);
        KeyStroke escape = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
        KeyStroke leftKey = KeyStroke.getKeyStroke("LEFT");
        KeyStroke rightKey = KeyStroke.getKeyStroke("RIGHT");
        KeyStroke spaceKey = KeyStroke.getKeyStroke("SPACE");
        KeyStroke enterKey = KeyStroke.getKeyStroke("ENTER");

        imap.put(escape, "return");
        imap.put(leftKey, "left");
        imap.put(rightKey, "right");
        imap.put(spaceKey, "play");
        imap.put(enterKey, "play");

        ActionMap amap = this.getActionMap();
        amap.put("return", new KeyAction("return"));
        amap.put("left", new KeyAction("left"));
        amap.put("right", new KeyAction("right"));
        amap.put("play", new KeyAction("play"));

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

        songNameArray = new String[songFiles.length];
        songDirectoryArray = new String[songFiles.length];

        for(int i = 0; i < songFiles.length; i++) {
            songs.addElement(songFiles[i].getName().replaceAll("_", " "));
            songNameArray[i] = songFiles[i].getName() + ".csm";
            songDirectoryArray[i] = songFiles[i].getAbsolutePath();
        }
    }

    private void changeSongListener(int value) {
        int currentIndex = songList.getSelectedIndex();
        int newIndex = currentIndex + value;
        if(newIndex < 0) {
            newIndex = songNameArray.length + value;
        } else if(newIndex >= songNameArray.length) {
            newIndex = newIndex - songNameArray.length;
        }

        songList.setSelectedIndex(newIndex);
    }

    private void setSongInfoPanel(String songFilename, String songDirectory) throws Exception {
        songInfoPanel.removeAll();

        Simfile currentSong = new Simfile(songFilename, 0.0);

        // your directory
        File songDirectoryPath = new File(songDirectory);
        File songCoverFile;

        try {
            songCoverFile = songDirectoryPath.listFiles(new FilenameFilter() {
                public boolean accept(File dir, String name) {
                    return name.startsWith("cover");
                }
            })[0];
        } catch (Exception e) {
            String s = File.separator;
            songCoverFile = new File(Driver.projectPath+"/assets/images/genericCover.jpg");
        }

        Image songCoverImage = ImageIO.read(songCoverFile);
        songCoverImage = songCoverImage.getScaledInstance(300, 300, Image.SCALE_SMOOTH);

        playButton = new JButton("PLAY");
        playButton.setPreferredSize(new Dimension(140, 55));
        playButton.setFont(Driver.fontBold.deriveFont(18f));
        playButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SwingUtilities.invokeLater(() -> owner.showView(new PanelPlay(owner, Driver.width, Driver.height, currentSong)));
            }
        });

        JLabel songCover = new JLabel(new ImageIcon(songCoverImage));
        String songTitle = "TITLE: " + currentSong.Title();
        String songArtist = "ARTIST: " + currentSong.Artist();
        String songYear = "YEAR: " + currentSong.Year();
        String songGenre = "GENRE: " + currentSong.Genre();
        String songBPM = "BPM: " + (int) currentSong.BPM()[0][1];

        JLabel songTitleLabel = new JLabel(songTitle); songTitleLabel.setFont(Driver.standardFont);
        JLabel songArtistLabel = new JLabel(songArtist); songArtistLabel.setFont(Driver.standardFont);
        JLabel songYearLabel = new JLabel(songYear); songYearLabel.setFont(Driver.standardFont);
        JLabel songGenreLabel = new JLabel(songGenre); songGenreLabel.setFont(Driver.standardFont);
        JLabel songBPMLabel = new JLabel(songBPM); songBPMLabel.setFont(Driver.standardFont);

        songInfoPanel.add(songCover);
        songInfoPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        songInfoPanel.add(songTitleLabel);
        songInfoPanel.add(songArtistLabel);
        songInfoPanel.add(songYearLabel);
        songInfoPanel.add(songGenreLabel);
        songInfoPanel.add(songBPMLabel);
        songInfoPanel.add(Box.createRigidArea(new Dimension(0, 50)));
        songInfoPanel.add(playButton);

        songInfoPanel.revalidate();
        songInfoPanel.repaint();
    }

    @SuppressWarnings("serial")
    private class KeyAction extends AbstractAction {

        private String key;

        public KeyAction (String key) {
            this.key = key;
        }

        public void actionPerformed (ActionEvent e) {
            switch (key) {
                case "return" :
                    backButtonLayout.backButton.doClick();
                    break;
                case "left" :
                    leftButton.doClick();
                    break;
                case "right" :
                    rightButton.doClick();
                    break;
                case "play" :
                    playButton.doClick();
                    break;
            }
            
        }

    }


}