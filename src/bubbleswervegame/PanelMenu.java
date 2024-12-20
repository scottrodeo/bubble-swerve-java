package bubbleswervegame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * @author https://scott.rodeo/
 */

public class PanelMenu extends JPanel {

	private Engine engine;
	
    public PanelMenu(Engine launcher) {
        this.engine = launcher; // Store reference to the Game object
        initializePanelMenu();
    }

    private void initializePanelMenu() {
        SwingUtilities.invokeLater(() -> {
        JPanel topPanel = new JPanel();
	    topPanel.setLayout(new FlowLayout(FlowLayout.LEFT)); // Align buttons to the left

	    // Create buttons
	    //JButton loadButton = createStyledButton("Load");
	    //JButton saveButton = createStyledButton("Save");
	    JButton restartButton = createStyledButton("Restart");
	    //JButton aboutButton = createStyledButton("About");

	    // Add action listeners to buttons
	    //loadButton.addActionListener(e -> engine.getGame().loadGame());
	    //saveButton.addActionListener(e -> engine.getGame().saveGame());
	    restartButton.addActionListener(e -> engine.getGame().gameRestart());
	    /*
	    aboutButton.addActionListener(e -> {
	    	engine.getGame().pauseGame();
	        //JOptionPane.showMessageDialog(frame, "PanelGame Game v1.0\nCreated by https://scott.rodeo/\nEnjoy playing!", "About PanelGame", JOptionPane.INFORMATION_MESSAGE);
	    	engine.getGame().togglePause();
	    });
	    */
	    //loadButton.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke("SPACE"), "none");
	    //saveButton.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke("SPACE"), "none");
	    restartButton.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke("SPACE"), "none");
	    //aboutButton.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke("SPACE"), "none");

	    
	    // Add buttons to the top panel
	    //topPanel.add(loadButton);
	    //topPanel.add(saveButton);
	    topPanel.add(restartButton);
	    //topPanel.add(aboutButton);

	    if (!(engine.getProgramWindow() instanceof JFrame)) {
	        throw new IllegalStateException("Frame is not an instance of JFrame");
	    }
	    
	    if (engine == null || engine.getProgramWindow() == null) {
	        throw new IllegalStateException("Launcher or frame is not initialized");
	    }
	    
	    // Add the top panel to the frame
	    engine.getProgramWindow().add(topPanel, BorderLayout.NORTH);
	    /*
	    // Ensure the game panel receives focus
	    frame.addWindowListener(new WindowAdapter() {
	        @Override
	        public void windowOpened(WindowEvent e) {
	            PanelGame.this.requestFocusInWindow(); // Request focus on the game panel
	        }
	    });
	     */
        });
    }

    
    
 // Helper method to create styled buttons
 	private JButton createStyledButton(String text) {
 	    JButton button = new JButton(text);

 	    // Style the button
 	    button.setFocusPainted(false); // Remove focus border
 	    button.setBorderPainted(false); // Remove button border
 	    button.setContentAreaFilled(false); // Remove background
 	    button.setOpaque(false); // Ensure transparency
 	    button.setForeground(Color.BLACK); // Set text color
 	    button.setFont(new Font("Arial", Font.BOLD, 14)); // Set custom font
 	    button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); // Change cursor on hover

 	    // Add hover effect (optional)
 	    button.addMouseListener(new java.awt.event.MouseAdapter() {
 	        @Override
 	        public void mouseEntered(java.awt.event.MouseEvent evt) {
 	            button.setForeground(Color.BLUE); // Change text color on hover
 	        }

 	        @Override
 	        public void mouseExited(java.awt.event.MouseEvent evt) {
 	            button.setForeground(Color.BLACK); // Reset text color
 	        }
 	    });

 	    return button;
 	}

}

