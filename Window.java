import javax.swing.*;
import java.awt.*;

/**
* JFrame upon which GUI Panels are drawn
*/
public class Window extends JFrame {

    /**
    * Panel that is in use at any given point
    */
    private JPanel view;

    /**
    * Creates a new Window with a given Title/Header
    * @param title Title of the window
    */
    public Window(String title) {

        super(title);
        createGUI();

    }

    /**
    * Sets window properties such as size and location.
    * Also sets application icon.
    */
    private void createGUI() {

        // Set application icon
        ImageIcon logo = new ImageIcon(Driver.projectPath + "/assets/images/logoSquare.png");
        setIconImage(logo.getImage());

        // Set window size
        setPreferredSize(new Dimension(1280, 720));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Adds a center panel
        view = new JPanel(new BorderLayout());
        add(view, BorderLayout.CENTER);
        
        // Sets default panel to PanelHome
        showView(new PanelHome(this, Driver.width, Driver.height));
        pack();

    }
    
    /**
    * Displays a given panel on the Window
    * @param panel The JPanel object to display
    */
    public void showView(JPanel panel) {

        view.removeAll();
        view.add(panel, BorderLayout.CENTER);
        view.revalidate();
        view.repaint();
    }

}