import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class Driver {

    public static int width = 1280;
    public static int height = 720;
    public static Color bgColor = new Color(209, 240, 255);
    public static String projectPath = System.getProperty("user.dir");

    public static Font standardFont;
    public static Font fontRegular;
    public static Font fontBold;
    public static Font fontLight;


    public static void main(String[] args) {
        loadFonts();
        SwingUtilities.invokeLater(() -> {
            new Window("JMania").setVisible(true);
        });
    }

    private static void loadFonts() {
        try {
            //create the font to use. Specify the size!
            fontRegular = Font.createFont(Font.TRUETYPE_FONT, new File("assets/fonts/Rubik-Regular.ttf"));
            fontBold = Font.createFont(Font.TRUETYPE_FONT, new File("assets/fonts/Rubik-Medium.ttf"));
            fontLight = Font.createFont(Font.TRUETYPE_FONT, new File("assets/fonts/Rubik-Light.ttf"));
            standardFont = fontRegular.deriveFont(14f);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            //register the font
            ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("assets/fonts/Rubik-Regular.ttf")));
            fontRegular = Font.createFont(Font.TRUETYPE_FONT, new File("assets/fonts/Rubik-Medium.ttf"));
            fontRegular = Font.createFont(Font.TRUETYPE_FONT, new File("assets/fonts/Rubik-Light.ttf"));
        } catch (IOException | FontFormatException e) {
            e.printStackTrace();
        }
    }
}