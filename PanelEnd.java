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
    private JPanel songStatsPanel = new JPanel();

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
    }

    private void createGUI() {
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

        backButtonLayout = new PanelBack();
        backButtonLayout.backButton.addActionListener(event -> {
            SwingUtilities.invokeLater(() -> owner.showView(new PanelHome(owner, Driver.width, Driver.height)));
        });
        backButtonLayout.backButton.setText(" Home");
        add(backButtonLayout);

        setSongInfoPanel();
        setSongStatsPanel();

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
        songStatsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        add(songInfoPanel);
    }

    private void setSongStatsPanel() {
        songStatsPanel.setLayout(new BoxLayout(songStatsPanel, BoxLayout.Y_AXIS));
        songStatsPanel.setBackground(Driver.bgColor);

        JLabel titleLabel = new JLabel("Score Distribution:");
        titleLabel.setFont(Driver.fontRegular.deriveFont(26f));
        songStatsPanel.add(titleLabel);
        add(songStatsPanel);

        int totalScores = scores.size();
        int totalScore = 0;
        System.out.println(totalScores);
        int[] scoresArray = new int[4];
        String[] typesArray = new String[] {"MISS", "GOOD", "GREAT", "PERFECT"};
        Color[] colorsArray = new Color[] {PanelPlay.colorMiss, PanelPlay.colorGood, PanelPlay.colorGreat, PanelPlay.colorPerfect};
        for (Integer score : scores) {
            scoresArray[score]++;
            totalScore += score;
        }
        for(int s = 0; s < scoresArray.length; s++) {
            double percentage = (double) scoresArray[s] / (double) totalScores;
            percentage = Math.round(percentage * 10000)/100;
            JLabel tempLabel = new JLabel();
            tempLabel.setFont(Driver.fontBold.deriveFont(24f));
            tempLabel.setText(typesArray[s] + ": " + String.valueOf(percentage) + "%" + " (" + scoresArray[s] + " times)");
            tempLabel.setForeground(colorsArray[s]);
            songStatsPanel.add(tempLabel);
        }
        songStatsPanel.add(Box.createHorizontalGlue());

        JLabel scoreLabel = new JLabel(" Your Total Score: " + String.valueOf(totalScore));
        scoreLabel.setFont(Driver.fontBold.deriveFont(24f));
        songStatsPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        songStatsPanel.add(scoreLabel);

        this.accuracy = Math.round(this.accuracy * 10000)/100;
        JLabel accuracyLabel = new JLabel(" Your Accuracy: " + String.valueOf(this.accuracy) + "%");
        accuracyLabel.setFont(Driver.fontBold.deriveFont(24f));
        songStatsPanel.add(Box.createRigidArea(new Dimension(0, 50)));
        songStatsPanel.add(accuracyLabel);
    }

    @SuppressWarnings("serial")
    private class BackAction extends AbstractAction {
        public void actionPerformed (ActionEvent e) {
            backButtonLayout.backButton.doClick();
        }
    }

    // Experimental graph class
    public class GraphComponent extends JComponent
    {
        private int x;
        private int y;
        private double percentage;
        private Color color;
        private int width = 20;
        private int maxHeight = 300;

        @Override
        public void paint(Graphics g)
        {
            g.setColor(color);
            int height = (int) (percentage*maxHeight);
            g.drawRect(x, y, this.width, height);
            g.fillRect(x, y, this.width, height);
        }

        public GraphComponent(int x, int y, double percentage, Color color) {
            this.x = x;
            this.y = y;
            this.percentage = percentage;
            this.color = color;
        }
    }
}