import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.Properties;

/**
* Settings Screen
*/
@SuppressWarnings("serial")
public class PanelSettings extends JPanel {
   
    /** 
    * Main JFrame this panel is drawn upon
    */
    final private Window owner;
    /** 
    * Height of GUI window
    */
    private int height;
    /** 
    * Main JFrame this panel is drawn upon
    */
    private int width;
    /** 
    * Main JFrame this panel is drawn upon
    */
    private JSlider[] sliders;
    /** 
    * Main JFrame this panel is drawn upon
    */
    private JLabel[] sliderLabels;
    /** 
    * Main JFrame this panel is drawn upon
    */
    private PanelBack backButtonLayout;
    /** 
    * Main JFrame this panel is drawn upon
    */
    private static Properties props = new Properties();

    /**
    * Creates a new PanelSettings on the Window owner with the given width and height
    * @param owner JFrame window 
    * @param width Width of window
    * @param height Height of window
    */ 
    public PanelSettings(Window owner, int width, int height) {

        this.width = width;
        this.height = height;
        this.owner = owner;

        loadProps(); // loads settings file

        createGUI(); // draws sliders and buttons
    }

    /** 
    * Draws background
    */
    public void paintComponent(Graphics g) {
        g.setColor(Driver.bgColor);
        g.fillRect(0, 0, width, height);
    }

    /**
    * Loads app.properties.
    * Used for reading and saving settings.
    */
    private static void loadProps() {
        try {
            FileInputStream in = new FileInputStream(Driver.projectPath + "/app.properties");
            props.load(in);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
    * Creates layout and draws Sliders and Buttons.
    * Also sets key bindings.
    */
    private void createGUI() {
    
        // creates a layout
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        
        // creates the back button
        backButtonLayout = new PanelBack();
        backButtonLayout.backButton.addActionListener(event -> {
            SwingUtilities.invokeLater(() -> owner.showView(new PanelHome(owner, Driver.width, Driver.height)));
        });
        add(backButtonLayout);

        // panel for the sliders
        JPanel controlsPanel = new JPanel();
        controlsPanel.setLayout(new GridBagLayout());
        controlsPanel.setBackground(Driver.bgColor);
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(10, 50, 10, 50);

         
        sliderLabels = new JLabel[1];
        sliders = new JSlider[4];

        // adds a settings slider for song volume
        sliderLabels[1] = new JLabel("Song Volume");
        c.gridx = 1; c.gridy = 0;
        controlsPanel.add(sliderLabels[1], c);
        sliders[1] = new JSlider(JSlider.VERTICAL, 0, 100, Integer.parseInt(props.getProperty("songVolume")));
        c.gridx = 1; c.gridy = 1;
        controlsPanel.add(sliders[1], c);
        sliders[1].setMajorTickSpacing(10);
        sliders[1].setMinorTickSpacing(1);

        for(int i = 0; i < sliders.length; i++) {
            sliders[i].setPaintTicks(true);
            sliders[i].setPaintLabels(true);
            sliders[i].setBackground(Driver.bgColor);
            sliders[i].setPreferredSize(new Dimension(30, 320));
            sliderLabels[i].setFont(Driver.standardFont);
        }

        JPanel saveButtonPanel = new JPanel(new FlowLayout());
        saveButtonPanel.setBackground(Driver.bgColor);
        JButton saveButton = new JButton("Save");
        saveButton.setFont(Driver.standardFont);
        saveButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        saveButton.setPreferredSize( new Dimension(140, 45) );
        saveButton.addActionListener( new saveProps() );
        saveButtonPanel.add(saveButton);
        c.gridx = 0; c.gridy = 2; c.gridwidth = 4;
        controlsPanel.add(saveButtonPanel, c);

        add(controlsPanel);

        // key bindings
        int backMap = JComponent.WHEN_IN_FOCUSED_WINDOW;
        InputMap imap = this.getInputMap(backMap);
        KeyStroke escape = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
        imap.put(escape, "return");

        ActionMap amap = this.getActionMap();
        amap.put("return", new BackAction() );

        owner.requestFocus();

    }

    /** 
    * Goes back to PanelHome if the back button is pressed
    */
    @SuppressWarnings("serial")
    private class BackAction extends AbstractAction {
        public void actionPerformed (ActionEvent e) {
            backButtonLayout.backButton.doClick();
        }
    }

    /**
    * Saves settings from JSliders to app.properties
    */
    private class saveProps implements ActionListener {
        public void actionPerformed (ActionEvent e) {

            try {
                FileOutputStream out = new FileOutputStream(Driver.projectPath + "/app.properties");
                props.setProperty("effectsVolume", sliders[0].getValue() + "");
                props.store(out, "Settings");
                out.close();
                System.out.println("saved.");
            } catch (IOException f) {
                f.printStackTrace();
                
            }
            
        }
    }

}