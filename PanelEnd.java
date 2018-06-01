import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.List;

@SuppressWarnings("serial")
public class PanelEnd extends JPanel {

    final private Window owner;
    private int height;
    private int width;
    private double accuracy;
    private List<Integer> scores;
    private PanelBack backButtonLayout;

    private JPanel songInfoPanel = new JPanel();
    private JPanel songGraphPanel = new JPanel();

    public PanelEnd(Window owner, int width, int height, double accuracy, List<Integer> scores) {

        this.width = width;
        this.height = height;
        this.owner = owner;
        this.accuracy = accuracy;
        this.scores = scores;

        createGUI();
    }

    public void paintComponent(Graphics g) {
        g.setColor(Driver.bgColor);
        g.fillRect(0, 0, width, height);
        g.setColor(Color.RED);
        g.fillRect(230,80,10,10);
    }

    private void createGUI() {
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        System.out.println(this.accuracy);

        backButtonLayout = new PanelBack();
        backButtonLayout.backButton.addActionListener(event -> {
            SwingUtilities.invokeLater(() -> owner.showView(new PanelHome(owner, Driver.width, Driver.height)));
        });
        add(backButtonLayout);

        setSongInfoPanel();
        setSongGraphPanel();

        int backMap = JComponent.WHEN_IN_FOCUSED_WINDOW;
        InputMap imap = this.getInputMap(backMap);
        KeyStroke escape = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
        imap.put(escape, "return");

        ActionMap amap = this.getActionMap();
        amap.put("return", new BackAction() );

        owner.requestFocus();
    }

    private void setSongInfoPanel() {
        songInfoPanel = PanelSelect.songInfoPanel;
        songInfoPanel.remove(PanelSelect.playButton);
        add(songInfoPanel);
    }

    private void setSongGraphPanel() {
        songGraphPanel.setLayout(new BoxLayout(songGraphPanel, BoxLayout.X_AXIS));
        songGraphPanel.setBackground(Driver.bgColor);

        JLabel titleLabel = new JLabel("Score Distribution:");
        songGraphPanel.add(titleLabel);
        add(songGraphPanel);
        add(new GraphComponent(100, 100, 500, PanelPlay.colorPerfect));
    }

    @SuppressWarnings("serial")
    private class BackAction extends AbstractAction {
        public void actionPerformed (ActionEvent e) {
            backButtonLayout.backButton.doClick();
        }
    }

    public class GraphComponent extends JComponent
    {
        private int x;
        private int y;
        private int height;
        private Color color;

        @Override
        public void paint(Graphics g)
        {
            int width = 20;
            g.setColor(color);
            g.drawRect(x, y, width, height);
            g.fillRect(x, y, width, height);
        }

        public GraphComponent(int x, int y, int height, Color color) {
            this.x = x;
            this.y = y;
            this.height = height;
            this.color = color;
        }
    }
}