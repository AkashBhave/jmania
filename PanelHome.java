import javax.swing.*;
import java.awt.*;

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

        Dimension buttonDimension = new Dimension(140, 45);
        Font buttonFont = Driver.font;

        JButton settings = new JButton("Settings");
        settings.setFocusPainted(false);
        settings.addActionListener(event -> {
            SwingUtilities.invokeLater(() -> owner.showView(new PanelSettings(owner, Driver.width, Driver.height)));
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
        ImageIcon logo = new ImageIcon(Driver.projectPath + "/assets/logoBanner.png");
        g.setColor(Driver.bgColor);
        g.fillRect(0, 0, width, height);
        g.drawImage(logo.getImage(), 320, 100, 600, 300, null, null);
    }
}