package bubbleswervegame;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;

/**
 * @author https://scott.rodeo/
 */
 
public class Engine {

	public Game game;
	public JFrame programWindow;	
	public PanelMenu panelMenu;
	public PanelGame panelGame;
	//public PanelAvatars panelAvatars;
	
	
	public Engine() {
        
        programWindow = new JFrame("PanelGame");
        programWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        programWindow.setSize(800, 600);
        programWindow.setExtendedState(JFrame.MAXIMIZED_BOTH);
        programWindow.setUndecorated(false); // Remove title bar and borders
        programWindow.setResizable(true);
        
        game = new Game(this);

        // Create panels
        panelMenu = new PanelMenu(this);
        panelGame = new PanelGame(this);

        // Use a CardLayout to switch between the menu and game
        JPanel container = new JPanel(new CardLayout());
        container.add(panelMenu, "Menu");
        container.add(panelGame, "Game");
        programWindow.add(container);

        // Show the menu initially
        CardLayout cl = (CardLayout) container.getLayout();
        cl.show(container, "Menu");
        cl.show(container, "Game");
        
        programWindow.setVisible(true);
    }
    
    
    public Game getGame() {
    	return game;
    }
    
    public JFrame getProgramWindow() {
    	return programWindow;
    }    
    
    public PanelMenu getPanelMenu() {
    	return panelMenu;
    }      
    
    public PanelGame getPanelGame() {
    	return panelGame;
    }          
    
    /*
    public PanelAvatars getPanelAvatars() {
    	return panelAvatars;
    }      
    */
}    












