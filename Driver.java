import javax.swing.*;
import java.awt.*;

public class Driver {

    public static int width = 1280;
    public static int height = 720;
    
    public static void main(String[] args) {
            
        SwingUtilities.invokeLater(() -> {
            new Window("JMania").setVisible(true);
        });
    
    }
}
         
         