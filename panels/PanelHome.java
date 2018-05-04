package jmania.panels;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class PanelHome extends JPanel {
        
        private int width;
        private int height;

        public PanelHome (int width, int height) {
        
            this.width = width;
            this.height = height;
                
            setLayout(new FlowLayout(FlowLayout.CENTER, 0, 450));
            JPanel buttons = new JPanel();
            buttons.setOpaque(false);
            buttons.setLayout(new FlowLayout(FlowLayout.CENTER, 75, 0));
            add(buttons);
            
            Dimension buttonDimension = new Dimension(120, 45);
            Font buttonFont = new Font("Courier New", Font.PLAIN, 17);

            JButton settings = new JButton ("Settings");
            settings.setFocusPainted(false);
            settings.addActionListener(new Settings());
            settings.setPreferredSize(buttonDimension);
            settings.setFont(buttonFont);
            buttons.add(settings);
            
            JButton play = new JButton ("Play");
            play.setFocusPainted(false);
            play.addActionListener(new Play());
            play.setPreferredSize(buttonDimension);
            play.setFont(buttonFont);
            buttons.add(play);
            
            JButton credits = new JButton ("Credits");
            credits.setFocusPainted(false);
            credits.addActionListener(new Credits());
            credits.setPreferredSize(buttonDimension);
            credits.setFont(buttonFont);
            buttons.add(credits);

        }
        
        public void paintComponent(Graphics g) {
        
            ImageIcon logo = new ImageIcon(System.getProperty("user.dir") + "/jmania/assets/logoBanner.png");
            g.setColor(new Color(240, 255, 240));
            g.fillRect(0, 0, width, height);
            g.drawImage(logo.getImage(), 320, 100, 600, 300, null, null);
            
        }
        
        private class Play implements ActionListener {
        
            public void actionPerformed (ActionEvent e) {
               
                System.out.println("Play");
                                
            }
        
        }
        
        private class Settings implements ActionListener {
        
            public void actionPerformed (ActionEvent e) {
            
                System.out.println("Settings");
                
            
            }

        }
        
        private class Credits implements ActionListener {
        
            public void actionPerformed (ActionEvent e) {
            
                System.out.println("Credits");    
            
            }

        }
}