import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import java.awt.FlowLayout;
import java.awt.Image;

/**
 * Displays the back button.
 * Allows for reusability of this button, as many panels use it.
 */
public class PanelBack extends JPanel {

    /**
     * The back button
     */
    public JButton backButton;

    /**
     * Constructor that is called when class is initialized, creates the button
     */
    public PanelBack() {
        setBackground(Driver.bgColor);
        setLayout(new FlowLayout(FlowLayout.LEFT, 20, 15));

        backButton = new JButton(" Go Back");
        backButton.setFont(Driver.fontRegular.deriveFont(20f));

        // Attemps to retrieve the back arrow icon
        try {
            Image img = ImageIO.read(getClass().getResource("assets/images/backButton.png"));
            img = img.getScaledInstance(20, 20, Image.SCALE_SMOOTH);
            backButton.setIcon(new ImageIcon(img));
        } catch (Exception ex) {
            System.out.println(ex);
        }

        // Adds the button to the back button layout
        add(backButton);
    }

}