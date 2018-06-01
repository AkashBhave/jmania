import javax.swing.*;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;

/* 
* Home Screen/ Main Menu
*/
@SuppressWarnings("serial")
public class PanelHome extends JPanel {

    /**
    * Width of GUI window 
    */
    private int width;
    /**
    * Height of GUI window 
    */
    private int height;
    /**
    * Main JFrame this panel is drawn upon 
    */
    final private Window owner;
    /**
    * Play button 
    */
    private JButton play;
    /**
    * Settings button 
    */
    private JButton settings;
    /**
    * Credits button 
    */
    private JButton credits;
    
    
    /**
    * Creates a new PanelHome on the JFrame owner with the given width and height
    * @param owner JFrame window 
    * @param width Width of window
    * @param height Height of window
    */
    public PanelHome(Window owner, int width, int height) {
        super();
        this.width = width;
        this.height = height;
        this.owner = owner;

        createGUI();
    }
    
    /**
    * Creates a layout and places buttons.
    * Also sets keybindings.
    */
    private void createGUI() {
         
        setLayout(new FlowLayout(FlowLayout.CENTER, 0, 450)); // new layout

        // Creates a panel for buttons
        JPanel buttons = new JPanel();
        buttons.setOpaque(false);
        buttons.setLayout(new FlowLayout(FlowLayout.CENTER, 75, 0));
        add(buttons);
         
        // Sets a default dimension for the buttons
        Dimension buttonDimension = new Dimension(140, 45);
        Font buttonFont = Driver.fontBold.deriveFont(18f);
        
        // Adds a settings button
        settings = new JButton("Settings");
        settings.setFocusPainted(false);
        settings.addActionListener(event -> {
            SwingUtilities.invokeLater(() -> owner.showView(new PanelSettings(owner, Driver.width, Driver.height)));
        });
        settings.setPreferredSize(buttonDimension);
        settings.setFont(buttonFont);
        buttons.add(settings);

        // Adds a play button
        play = new JButton("Play");
        play.setFocusPainted(false);
        play.addActionListener(event -> {
            SwingUtilities.invokeLater(() -> owner.showView(new PanelSelect(owner, Driver.width, Driver.height)));
        });
        play.setPreferredSize(buttonDimension);
        play.setFont(buttonFont);
        buttons.add(play);
        
        // Adds a credits button
        credits = new JButton("Credits");
        credits.setFocusPainted(false);
        credits.addActionListener(event -> {
            SwingUtilities.invokeLater(() -> owner.showView(new PanelCredits(owner, Driver.width, Driver.height)));
        });
        credits.setPreferredSize(buttonDimension);
        credits.setFont(buttonFont);
        buttons.add(credits);

        // Key bindings
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
    
    /**
    * Enters PanelSettings if left arrow key is pressed
    */
    @SuppressWarnings("serial")
    private class LeftArrowAction extends AbstractAction {
        public void actionPerformed(ActionEvent e) {
            settings.doClick();
        }
    }

    /**
    * Enters PanelCredits if right arrow key is pressed
    */
    @SuppressWarnings("serial")
    private class RightArrowAction extends AbstractAction {
        public void actionPerformed(ActionEvent e) {
            credits.doClick();
        }
    }

    /**
    * Enters PanelSelect if up arrow key is pressed
    */
    @SuppressWarnings("serial")
    private class UpArrowAction extends AbstractAction {
        public void actionPerformed(ActionEvent e) {
            play.doClick();
        }
    }
    
    /**
    * Draws background and logo
    */
    public void paintComponent(Graphics g) {
        ImageIcon logo = new ImageIcon(Driver.projectPath + "/assets/images/logoBanner.png");
        g.setColor(Driver.bgColor);
        g.fillRect(0, 0, width, height);
        g.drawImage(logo.getImage(), 320, 100, 600, 300, null, null);
    }
}