import javax.swing.*;
import java.awt.*;

public class Driver {

    public static int width = 1280;
    public static int height = 720;
    public static Color bgColor = new Color(240, 255, 240);
    public static String fontFamily = "Courier New";
    public static int fontSize = 18;
    public static Font font = new Font(fontFamily, Font.PLAIN, fontSize);
    public static String projectPath = System.getProperty("user.dir");

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new Window("JMania").setVisible(true);
        });
    }
}