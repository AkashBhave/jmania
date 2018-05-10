import javax.swing.*;
import java.awt.*;

public class Window extends JFrame {

    private JPanel view;

    public Window(String title) {

        super(title);
        createGUI();

    }

    private void createGUI() {

        ImageIcon logo = new ImageIcon(System.getProperty("user.dir") + "/assets/logoSquare.png");
        setIconImage(logo.getImage());

        setPreferredSize(new Dimension(1280, 720));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        view = new JPanel(new BorderLayout());
        add(view, BorderLayout.CENTER);

        showView(new PanelHome(this, Driver.width, Driver.height));
        pack();

    }

    public void showView(JPanel panel) {

        view.removeAll();
        view.add(panel, BorderLayout.CENTER);
        view.revalidate();
        view.repaint();

    }

}