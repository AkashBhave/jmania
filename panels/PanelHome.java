package jmania.panels;
import javax.swing.*;
import java.awt.*;

public class PanelHome extends JPanel {

    
    public void paintComponent (Graphics g) {
    
        ImageIcon logo = new ImageIcon(System.getProperty("user.dir") + "/jmania/assets/logoBanner.png");
        g.setColor(new Color(240, 255, 240));
        g.fillRect(0, 0, 1280, 720);
        g.drawImage(logo.getImage(), 320, 50, 600, 300, null, null);
        
        JButton play = new JButton ("Play");
        
        JButton settings = new JButton ("Settings");
        
        JButton credits = new JButton ("Credits");
        
    }
}
