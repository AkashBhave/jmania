import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class PanelSettings extends JPanel {

    final private Window owner;
    private int height;
    private int width;
    private Properties props = new Properties();

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

    private void loadProps() {
        try {
            props.load(new FileInputStream(Driver.projectPath + "/app.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createGUI() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        Font mainFont = new Font(Driver.fontFamily, Font.PLAIN, 20);

        PanelBack backButtonLayout = new PanelBack();
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


        JLabel[] sliderLabels = new JLabel[3];
        JSlider[] sliders = new JSlider[3];

        sliderLabels[0] = new JLabel("Master Volume");
        c.gridx = 0; c.gridy = 0;
        controlsPanel.add(sliderLabels[0], c);
        sliders[0] = new JSlider(JSlider.VERTICAL, 0, 100, Integer.parseInt(props.getProperty("masterVolume")));
        c.gridx = 0; c.gridy = 1;
        controlsPanel.add(sliders[0], c);

        sliderLabels[1] = new JLabel("Effects Volume");
        c.gridx = 1; c.gridy = 0;
        controlsPanel.add(sliderLabels[1], c);
        sliders[1] = new JSlider(JSlider.VERTICAL, 0, 100, Integer.parseInt(props.getProperty("effectsVolume")));
        c.gridx = 1; c.gridy = 1;
        controlsPanel.add(sliders[1], c);

        sliderLabels[2] = new JLabel("Song Volume");
        c.gridx = 2; c.gridy = 0;
        controlsPanel.add(sliderLabels[2], c);
        sliders[2] = new JSlider(JSlider.VERTICAL, 0, 100, Integer.parseInt(props.getProperty("songVolume")));
        c.gridx = 2; c.gridy = 1;
        controlsPanel.add(sliders[2], c);

        for(int i = 0; i < sliders.length; i++) {
            sliders[i].setMajorTickSpacing(10);
            sliders[i].setMinorTickSpacing(1);
            sliders[i].setPaintTicks(true);
            sliders[i].setPaintLabels(true);
            sliders[i].setBackground(Driver.bgColor);
            sliders[i].setPreferredSize(new Dimension(30, 320));
            sliderLabels[i].setFont(Driver.font);
        }

        JPanel saveButtonPanel = new JPanel(new FlowLayout());
        saveButtonPanel.setBackground(Driver.bgColor);
        JButton saveButton = new JButton("Save");
        saveButton.setFont(Driver.font);
        saveButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        saveButton.setPreferredSize(new Dimension(140, 45));
        saveButtonPanel.add(saveButton);
        c.gridx = 0; c.gridy = 2; c.gridwidth = 3;
        controlsPanel.add(saveButtonPanel, c);

        add(controlsPanel);

    }

}