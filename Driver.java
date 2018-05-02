package jmania;
import jmania.panels.*; 
import javax.swing.*;
import java.awt.*;

public class Driver {

    public static int width = 1280;
    public static int height = 720;
    
    public static void main(String[] args) {
            
        JFrame frame = new JFrame ("JMania");
        ImageIcon logo = new ImageIcon(System.getProperty("user.dir") + "/jmania/assets/logoSquare.png");
        frame.setIconImage(logo.getImage());
      
        frame.setSize(width, height);
        frame.setLocation(300, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(new PanelHome(width, height));
        frame.setVisible(true);
    
    }
}
         
         