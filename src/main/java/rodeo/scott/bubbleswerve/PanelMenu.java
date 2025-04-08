package rodeo.scott.bubbleswerve;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.URI;

/**
 * The PanelMenu class represents the menu panel for the game.
 * It provides user interface buttons for actions like restarting the game and viewing the "About" dialog.
 * 
 * Key Features:
 * - Custom-styled buttons.
 * - Restart functionality.
 * - Interactive "About" dialog with a clickable link.
 * 
 * @author https://scott.rodeo/
 */
public class PanelMenu extends JPanel {

    private Engine engine; // Reference to the Engine object managing the game.

    /**
     * Constructs a PanelMenu instance and initializes the menu interface.
     * 
     * @param launcher The Engine instance managing the game.
     */
    public PanelMenu(Engine launcher) {
        this.engine = launcher; // Store reference to the Engine object.
        initializePanelMenu();  // Set up the menu panel.
    }

    /**
     * Initializes the menu panel with buttons and their respective functionality.
     */
    private void initializePanelMenu() {
        SwingUtilities.invokeLater(() -> {
            // Create the top panel for buttons.
            JPanel topPanel = new JPanel();
            topPanel.setLayout(new FlowLayout(FlowLayout.LEFT)); // Align buttons to the left.

            // Create buttons with custom styles.
            JButton restartButton = createStyledButton("Restart");
            JButton aboutButton = createStyledButton("About");

            // Add action listener for the restart button.
            restartButton.addActionListener(e -> engine.getGame().gameRestart());

            // Add action listener for the about button.
            aboutButton.addActionListener(e -> {
                engine.getGame().pauseGame(); // Pause the game.

                // Game info
                String version = "1.0.0"; // Or however you retrieve this
                String message = "<html><div style='text-align: center;'>"
                               + "<h2>Bubble Swerve</h2>"
                               + "Version: " + version + "<br><br>"
                               + "<a href='https://scott.rodeo/'>https://scott.rodeo/</a><br><br>"
                               + "Enjoy playing!<br><br>"
                               + "</div></html>";

                JLabel label = new JLabel(message);
                label.setHorizontalAlignment(SwingConstants.CENTER);
                label.setCursor(new Cursor(Cursor.HAND_CURSOR));

                label.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent me) {
                        try {
                            Desktop.getDesktop().browse(new URI("https://scott.rodeo/"));
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                });

                // Create a dialog box to show the about message
                JOptionPane.showMessageDialog(null, label, "About", JOptionPane.INFORMATION_MESSAGE);
                engine.getGame().resumeGame();
            });


            // Prevent spacebar from triggering button actions when focused.
            restartButton.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke("SPACE"), "none");
            aboutButton.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke("SPACE"), "none");

            // Add buttons to the top panel.
            topPanel.add(restartButton);
            topPanel.add(aboutButton);

            // Ensure the program window is correctly initialized.
            if (!(engine.getProgramWindow() instanceof JFrame)) {
                throw new IllegalStateException("Frame is not an instance of JFrame");
            }

            if (engine == null || engine.getProgramWindow() == null) {
                throw new IllegalStateException("Launcher or frame is not initialized");
            }

            // Add the top panel to the program window.
            engine.getProgramWindow().add(topPanel, BorderLayout.NORTH);
        });
    }

    /**
     * Helper method to create custom-styled buttons.
     * 
     * @param text The text to display on the button.
     * @return A JButton instance with custom styles and hover effects.
     */
    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);

        // Style the button.
        button.setFocusPainted(false); // Remove focus border.
        button.setBorderPainted(false); // Remove button border.
        button.setContentAreaFilled(false); // Remove background.
        button.setOpaque(false); // Ensure transparency.
        button.setForeground(Color.BLACK); // Set text color.
        button.setFont(new Font("Arial", Font.BOLD, 14)); // Set custom font.
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); // Change cursor on hover.

        // Add hover effect for the button.
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setForeground(Color.BLUE); // Change text color on hover.
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setForeground(Color.BLACK); // Reset text color.
            }
        });

        return button;
    }
}
