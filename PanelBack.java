import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;

public class PanelBack extends JPanel {

    public JButton backButton;

    public PanelBack() {
        Font mainFont = new Font(Driver.fontFamily, Font.PLAIN, 20);

        setBackground(Driver.bgColor);
        setLayout(new FlowLayout(FlowLayout.LEFT, 20, 15));

        backButton = new JButton(" Go Back");
        backButton.setFont(mainFont);

        try {
            Image img = ImageIO.read(getClass().getResource("assets/backButton.png"));
            img = img.getScaledInstance(20, 20, Image.SCALE_SMOOTH);
            backButton.setIcon(new ImageIcon(img));
        } catch (Exception ex) {
            System.out.println(ex);
        }

        add(backButton);
    }

}