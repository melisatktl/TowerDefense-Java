package ui;

import javax.swing.JFrame;

public class GameWindow extends JFrame {
    
    public static final int WIDTH = 1280;
    public static final int HEIGHT = 960;
    
    private MainMenu mainMenu;
    private GamePanel gamePanel;
    
    public GameWindow() {
        setTitle("Tower Defense Game");
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        
        mainMenu = new MainMenu(this);
        add(mainMenu);
    }
    
    public void startGame(int level) {
        if (mainMenu != null) {
            remove(mainMenu);
        }
        if (gamePanel != null) {
            remove(gamePanel);
        }
        gamePanel = new GamePanel(this, level);
        add(gamePanel);
        revalidate();
        repaint();
        gamePanel.requestFocusInWindow();
    }
    
    public void showMainMenu() {
        if (gamePanel != null) {
            remove(gamePanel);
        }
        add(mainMenu);
        revalidate();
        repaint();
    }
}