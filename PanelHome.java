import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

@SuppressWarnings("serial")
public class PanelHome extends JPanel {

    private int width;
    private int height;
    final private Window owner;
    private JButton play;
    private JButton settings;
    private JButton credits;

    public PanelHome(Window owner, int width, int height) {
        super();
        this.width = width;
        this.height = height;
        this.owner = owner;

        createGUI();
    }

    private void createGUI() {

        setLayout(new FlowLayout(FlowLayout.CENTER, 0, 450));

        JPanel buttons = new JPanel();
        buttons.setOpaque(false);
        buttons.setLayout(new FlowLayout(FlowLayout.CENTER, 75, 0));
        add(buttons);

        Dimension buttonDimension = new Dimension(140, 45);
        Font buttonFont = Driver.fontBold.deriveFont(18f);

        settings = new JButton("Settings");
        settings.setFocusPainted(false);
        settings.addActionListener(event -> {
            SwingUtilities.invokeLater(() -> owner.showView(new PanelSettings(owner, Driver.width, Driver.height)));
        });
        settings.setPreferredSize(buttonDimension);
        settings.setFont(buttonFont);
        buttons.add(settings);

        play = new JButton("Play");
        play.setFocusPainted(false);
        play.addActionListener(event -> {
            SwingUtilities.invokeLater(() -> owner.showView(new PanelSelect(owner, Driver.width, Driver.height)));
        });
        play.setPreferredSize(buttonDimension);
        play.setFont(buttonFont);
        buttons.add(play);

        credits = new JButton("Credits");
        credits.setFocusPainted(false);
        credits.addActionListener(event -> {
            SwingUtilities.invokeLater(() -> owner.showView(new PanelCredits(owner, Driver.width, Driver.height)));
        });
        credits.setPreferredSize(buttonDimension);
        credits.setFont(buttonFont);
        buttons.add(credits);

        int arrowMap = JComponent.WHEN_IN_FOCUSED_WINDOW;
        InputMap imap = this.getInputMap(arrowMap);
        KeyStroke leftKey = KeyStroke.getKeyStroke("LEFT");
        KeyStroke rightKey = KeyStroke.getKeyStroke("RIGHT");
        KeyStroke upKey = KeyStroke.getKeyStroke("UP");
        imap.put(leftKey, "settings");
        imap.put(rightKey, "credits");
        imap.put(upKey, "select");

        ActionMap amap = this.getActionMap();
        amap.put("settings", new LeftArrowAction());
        amap.put("credits", new RightArrowAction());
        amap.put("select", new UpArrowAction());

        owner.requestFocus();

    }

    @SuppressWarnings("serial")
    private class LeftArrowAction extends AbstractAction {
        public void actionPerformed(ActionEvent e) {
            settings.doClick();
        }
    }

    @SuppressWarnings("serial")
    private class RightArrowAction extends AbstractAction {
        public void actionPerformed(ActionEvent e) {
            credits.doClick();
        }
    }

    @SuppressWarnings("serial")
    private class UpArrowAction extends AbstractAction {
        public void actionPerformed(ActionEvent e) {
            play.doClick();
        }
    }

    public void paintComponent(Graphics g) {
        ImageIcon logo = new ImageIcon(Driver.projectPath + "/assets/images/logoBanner.png");
        g.setColor(Driver.bgColor);
        g.fillRect(0, 0, width, height);
        g.drawImage(logo.getImage(), 320, 100, 600, 300, null, null);
    }
}