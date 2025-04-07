package rodeo.scott.bubbleswerve;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.*;

/**
 * The Engine class serves as the main controller for the Bubble Swerve game.
 * It initializes the game window, manages panels (menu and game view), and provides access to core components.
 * 
 * Responsibilities:
 * - Creates the program window and sets its properties.
 * - Manages the transition between the menu and game panels using a CardLayout.
 * - Provides getters for accessing the game, program window, and panels.
 * 
 * @author https://scott.rodeo/
 */
public class Engine {

    public Game game;               // The Game instance managing game logic.
    public JFrame programWindow;    // The main application window.
    public PanelMenu panelMenu;     // The menu panel.
    public PanelGame panelGame;     // The game panel.

    /**
     * Constructs the Engine and initializes the game components.
     * This sets up the main program window, game logic, and panel transitions.
     */
    public Engine() {

        // Set up the main application window.
        programWindow = new JFrame("PanelGame");
        programWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        programWindow.setSize(800, 600);                // Default size.
        programWindow.setExtendedState(JFrame.MAXIMIZED_BOTH); // Maximized on start.
        programWindow.setUndecorated(false);            // Window decorations enabled.
        programWindow.setResizable(true);               // Allows resizing.

        // Initialize the game logic.
        game = new Game(this);

        // Create and initialize panels.
        panelMenu = new PanelMenu(this);                // Menu panel for navigation.
        panelGame = new PanelGame(this);                // Game panel for gameplay.

        // Use a CardLayout to manage switching between panels.
        JPanel container = new JPanel(new CardLayout());
        container.add(panelMenu, "Menu");               // Add the menu panel.
        container.add(panelGame, "Game");               // Add the game panel.
        programWindow.add(container);

        // Display the initial panel (menu).
        CardLayout cl = (CardLayout) container.getLayout();
        cl.show(container, "Menu");                     // Initially show the menu panel.
        cl.show(container, "Game");                     // This seems redundant but could ensure visibility during setup.

        // Make the application window visible.
        programWindow.setVisible(true);
    }

    /**
     * Gets the Game instance.
     * 
     * @return The current Game instance.
     */
    public Game getGame() {
        return game;
    }

    /**
     * Gets the program window.
     * 
     * @return The JFrame representing the main program window.
     */
    public JFrame getProgramWindow() {
        return programWindow;
    }

    /**
     * Gets the menu panel.
     * 
     * @return The PanelMenu instance.
     */
    public PanelMenu getPanelMenu() {
        return panelMenu;
    }

    /**
     * Gets the game panel.
     * 
     * @return The PanelGame instance.
     */
    public PanelGame getPanelGame() {
        return panelGame;
    }
}
