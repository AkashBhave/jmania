import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class PanelHome extends JPanel {

    private int width;
    private int height;
    final private Window owner;

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

        Dimension buttonDimension = new Dimension(120, 45);
        Font buttonFont = new Font("Courier New", Font.PLAIN, 17);

        JButton settings = new JButton("Settings");
        settings.setFocusPainted(false);
        settings.addActionListener(event -> {
            SwingUtilities.invokeLater(() -> owner.showView(new PanelCredits(owner, Driver.width, Driver.height)));
        });

        settings.setPreferredSize(buttonDimension);
        settings.setFont(buttonFont);
        buttons.add(settings);

        JButton play = new JButton("Play");
        play.setFocusPainted(false);
        play.addActionListener(event -> {
            SwingUtilities.invokeLater(() -> owner.showView(new PanelCredits(owner, Driver.width, Driver.height)));
        });

        play.setPreferredSize(buttonDimension);
        play.setFont(buttonFont);
        buttons.add(play);

        JButton credits = new JButton("Credits");
        credits.setFocusPainted(false);
        credits.addActionListener(event -> {
            SwingUtilities.invokeLater(() -> owner.showView(new PanelCredits(owner, Driver.width, Driver.height)));
        });
        credits.setPreferredSize(buttonDimension);
        credits.setFont(buttonFont);
        buttons.add(credits);

    }

    public void paintComponent(Graphics g) {

        ImageIcon logo = new ImageIcon(System.getProperty("user.dir") + "/assets/logoBanner.png");
        g.setColor(new Color(240, 255, 240));
        g.fillRect(0, 0, width, height);
        g.drawImage(logo.getImage(), 320, 100, 600, 300, null, null);

    }

}