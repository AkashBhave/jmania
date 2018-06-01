import javax.swing.SwingUtilities;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.io.File;
import java.io.IOException;

/**
 * Main Class
 */
public class Driver {

    /**
     * Width of the GUI window
     */
    public static int width = 1280;
    /**
     * Height of the GUI window
     */
    public static int height = 720;
    /**
     * RGB Value of the GUI background color
     */
    public static Color bgColor = new Color(209, 240, 255);
    /**
     * Path of files used to import assets
     */
    public static String projectPath = System.getProperty("user.dir");

    /**
     * Default font
     */
    public static Font standardFont;
    /**
     * Specialized font in default appearance
     */
    public static Font fontRegular;
    /**
     * Specialized font in bold appearance
     */
    public static Font fontBold;
    /**
     * Specialized font in light appearance
     */
    public static Font fontLight;


    public static void main(String[] args) {
        loadFonts(); // loads fonts
        SwingUtilities.invokeLater(
                () -> {
                    new Window("JMania").setVisible(true);
                });
    }


    /**
     * Loads fonts to be used across the program
     */
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