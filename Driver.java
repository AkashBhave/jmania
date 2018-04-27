package jmania;
import jmania.panels.PanelHome;
import javax.swing.JFrame;

public class Driver {
    public static void main(String[] args) {
        JFrame frame = new JFrame ("JMania");
      
        frame.setSize(1280, 720);
        frame.setLocation(200, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(new PanelHome());
        frame.setVisible(true);
   }
}
         
         