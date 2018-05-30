import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;

public class PanelBack extends JPanel {

    public JButton backButton;

    public PanelBack() {
        setBackground(Driver.bgColor);
        setLayout(new FlowLayout(FlowLayout.LEFT, 20, 15));

        backButton = new JButton(" Go Back");
        backButton.setFont(Driver.fontRegular.deriveFont(20f));

        try {
            Image img = ImageIO.read(getClass().getResource("assets/images/backButton.png"));
            img = img.getScaledInstance(20, 20, Image.SCALE_SMOOTH);
            backButton.setIcon(new ImageIcon(img));
        } catch (Exception ex) {
            System.out.println(ex);
        }

        add(backButton);
    }

}