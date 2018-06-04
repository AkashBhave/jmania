import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;

/**
 * Credits screen
 */
@SuppressWarnings("serial")
public class PanelCredits extends JPanel {

    /**
     * The JFrame this panel is drawn upon
     */
    final private Window owner;
    /**
     * Height of GUI window
     */
    private int height;
    /**
     * Width of GUI window
     */
    private int width;
    /**
     * Panel for the back button
     */
    private PanelBack backButtonLayout;

    /**
     * Creates a new PanelCredits on the JFrame owner with the given width and height.
     * @param owner JFrame window
     * @param width Width of window
     * @param height Height of window
     */
    public PanelCredits(Window owner, int width, int height) {

        this.width = width;
        this.height = height;
        this.owner = owner;

        createGUI();
    }

    /**
     * Draws background.
     */
    public void paintComponent(Graphics g) {
        g.setColor(Driver.bgColor);
        g.fillRect(0, 0, width, height);
    }

    /**
     * Sets all text and hyperlinks for Github pages.
     * Also sets key bindings.
     */
    private void createGUI() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        // Import fonts so that they can be used in HTML
        GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(Driver.fontRegular);
        GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(Driver.fontBold);

        backButtonLayout = new PanelBack();
        backButtonLayout.backButton.addActionListener(event -> {
            SwingUtilities.invokeLater(() -> owner.showView(new PanelHome(owner, Driver.width, Driver.height)));
        });
        add(backButtonLayout);

        JTextPane textPane = new JTextPane();
        textPane.setBackground(Driver.bgColor);
        String creditsContent =
                "<html>" +
                        "<body style=\"font-family: 'Rubik'; font-size: 20px;\">" +
                        "<br>" +
                        "<p style=\"font-family: 'Rubik Medium'; font-size: 30px;\">CREDITS</p>" +
                        "<p>Akash Bhave </p><a href=\"https://github.com/AkashBhave\">(@AkashBhave)</a>" +
                        "<p>Avik Rao </p><a href=\"https://github.com/acidepicice\">(@acidepicice)</a>" +
                        "<p>Nathan Stephenson </p><a href=\"https://github.com/nathanstep55\">(@nathanstep55)</a>" +
                        "</body>" +
                        "</html>";
        textPane.setContentType("text/html");
        textPane.setText(creditsContent);
        textPane.setFont(Driver.standardFont);
        textPane.setEditable(false);
        textPane.setMargin(new Insets(50, 20, 0, 20));
        textPane.addHyperlinkListener(e -> {
            if (HyperlinkEvent.EventType.ACTIVATED.equals(e.getEventType())) {
                Desktop desktop = Desktop.getDesktop();
                try {
                    desktop.browse(e.getURL().toURI());
                } catch (Exception linkException) {
                    linkException.printStackTrace();
                }
            }
        });
        StyledDocument styledDoc = textPane.getStyledDocument();
        SimpleAttributeSet centerStyle = new SimpleAttributeSet();
        StyleConstants.setAlignment(centerStyle, StyleConstants.ALIGN_CENTER);
        styledDoc.setParagraphAttributes(0, styledDoc.getLength(), centerStyle, false);

        add(textPane);

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
     * Returns to PanelHome if the escape key is pressed.
     */
    @SuppressWarnings("serial")
    private class BackAction extends AbstractAction {
        public void actionPerformed (ActionEvent e) {
            backButtonLayout.backButton.doClick();
        }
    }

}
