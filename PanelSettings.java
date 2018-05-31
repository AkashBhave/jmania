import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.Properties;

@SuppressWarnings("serial")
public class PanelSettings extends JPanel {

    final private Window owner;
    private int height;
    private int width;
    private JSlider[] sliders;
    private JLabel[] sliderLabels;
    private PanelBack backButtonLayout;
    private static Properties props = new Properties();

    public PanelSettings(Window owner, int width, int height) {

        this.width = width;
        this.height = height;
        this.owner = owner;

        loadProps();

        createGUI();
    }

    public void paintComponent(Graphics g) {
        g.setColor(Driver.bgColor);
        g.fillRect(0, 0, width, height);
    }

    private static void loadProps() {
        try {
            FileInputStream in = new FileInputStream(Driver.projectPath + "/app.properties");
            props.load(in);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createGUI() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        backButtonLayout = new PanelBack();
        backButtonLayout.backButton.addActionListener(event -> {
            SwingUtilities.invokeLater(() -> owner.showView(new PanelHome(owner, Driver.width, Driver.height)));
        });
        add(backButtonLayout);


        JPanel controlsPanel = new JPanel();
        controlsPanel.setLayout(new GridBagLayout());
        controlsPanel.setBackground(Driver.bgColor);
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(10, 50, 10, 50);


        sliderLabels = new JLabel[4];
        sliders = new JSlider[4];

        sliderLabels[0] = new JLabel("Effects Volume");
        c.gridx = 0; c.gridy = 0;
        //controlsPanel.add(sliderLabels[0], c);
        sliders[0] = new JSlider(JSlider.VERTICAL, 0, 100, Integer.parseInt(props.getProperty("effectsVolume")));
        sliders[0].setMajorTickSpacing(10);
        sliders[0].setMinorTickSpacing(1);
        c.gridx = 0; c.gridy = 1;
        //controlsPanel.add(sliders[0], c);

        sliderLabels[1] = new JLabel("Song Volume");
        c.gridx = 1; c.gridy = 0;
        controlsPanel.add(sliderLabels[1], c);
        sliders[1] = new JSlider(JSlider.VERTICAL, 0, 100, Integer.parseInt(props.getProperty("songVolume")));
        c.gridx = 1; c.gridy = 1;
        controlsPanel.add(sliders[1], c);
        sliders[1].setMajorTickSpacing(10);
        sliders[1].setMinorTickSpacing(1);

        sliderLabels[2] = new JLabel("Margin of Error");
        c.gridx = 2; c.gridy = 0;
        //controlsPanel.add(sliderLabels[2], c);
        sliders[2] = new JSlider(JSlider.VERTICAL, 1, 10, Integer.parseInt(props.getProperty("marginOfError")));
        c.gridx = 2; c.gridy = 1;
        //controlsPanel.add(sliders[2], c);
        sliders[2].setMajorTickSpacing(1);

        sliderLabels[3] = new JLabel("Note Speed");
        c.gridx = 3; c.gridy = 0;
        //controlsPanel.add(sliderLabels[3], c);
        sliders[3] = new JSlider(JSlider.VERTICAL, 1, 10, Integer.parseInt(props.getProperty("noteSpeed")));
        c.gridx = 3; c.gridy = 1;
        //controlsPanel.add(sliders[3], c);
        sliders[3].setMajorTickSpacing(1);


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

        int backMap = JComponent.WHEN_IN_FOCUSED_WINDOW;
        InputMap imap = this.getInputMap(backMap);
        KeyStroke escape = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
        imap.put(escape, "return");

        ActionMap amap = this.getActionMap();
        amap.put("return", new BackAction() );

        owner.requestFocus();

    }

    @SuppressWarnings("serial")
    private class BackAction extends AbstractAction {
        public void actionPerformed (ActionEvent e) {
            backButtonLayout.backButton.doClick();
        }
    }

    private class saveProps implements ActionListener {
        public void actionPerformed (ActionEvent e) {

            try {
                FileOutputStream out = new FileOutputStream(Driver.projectPath + "/app.properties");
                props.setProperty("effectsVolume", sliders[0].getValue() + "");
                props.setProperty("songVolume", sliders[1].getValue() + "");
                props.setProperty("marginOfError", sliders[2].getValue() + "");
                props.setProperty("noteSpeed", sliders[3].getValue() + "");
                props.store(out, "Settings");
                out.close();
                System.out.println("saved.");
            } catch (IOException f) {
                f.printStackTrace();
                
            }
            
        }
    }

}