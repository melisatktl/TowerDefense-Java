package ui;

import manager.UserManager;
import manager.ScoreManager;
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class MainMenu extends JPanel {
    
    private GameWindow gameWindow;
    private JButton startButton;
    private JButton loginButton;
    private JButton creditsButton;
    private JButton highScoresButton;
    
    public MainMenu(GameWindow gameWindow) {
        this.gameWindow = gameWindow;
        setPreferredSize(new Dimension(GameWindow.WIDTH, GameWindow.HEIGHT));
        setLayout(null);
        
        createButtons();
    }
    
    private void createButtons() {
        int buttonWidth = 150;
        int buttonHeight = 40;
        int centerX = GameWindow.WIDTH / 2 - buttonWidth / 2;
        int startY = 350;
        int gap = 60;
        
        startButton = new JButton("START");
        startButton.setBounds(centerX, startY, buttonWidth, buttonHeight);
        startButton.setBackground(new Color(76, 175, 80));
        startButton.setForeground(Color.WHITE);
        startButton.setFocusPainted(false);
        add(startButton);
        
        loginButton = new JButton("LOGIN");
        loginButton.setBounds(centerX, startY + gap, buttonWidth, buttonHeight);
        loginButton.setBackground(new Color(33, 150, 243));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);
        add(loginButton);
        
        creditsButton = new JButton("CREDITS");
        creditsButton.setBounds(centerX, startY + gap * 2, buttonWidth, buttonHeight);
        creditsButton.setBackground(new Color(255, 152, 0));
        creditsButton.setForeground(Color.WHITE);
        creditsButton.setFocusPainted(false);
        add(creditsButton);
        
        highScoresButton = new JButton("High Scores");
        highScoresButton.setBounds(centerX, startY + gap * 3, buttonWidth, buttonHeight);
        highScoresButton.setBackground(new Color(244, 67, 54));
        highScoresButton.setForeground(Color.WHITE);
        highScoresButton.setFocusPainted(false);
        add(highScoresButton);
        
        loginButton.addActionListener(e -> {
            LoginDialog dialog = new LoginDialog(gameWindow);
            dialog.setVisible(true);
        });
        
        startButton.addActionListener(e -> {
            if (!UserManager.isLoggedIn()) {
                LoginDialog dialog = new LoginDialog(gameWindow);
                dialog.setVisible(true);
            }
            
            if (UserManager.isLoggedIn()) {
                showMapSelection();
            }
        });
        
        highScoresButton.addActionListener(e -> {
            showHighScores();
        });
        
        creditsButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, 
                "Tower Defense Game\n\nDeveloped for CSE212\nYeditepe University\nby Melisa Tokatlı\nAssets: Kenney", 
                "Credits", 
                JOptionPane.INFORMATION_MESSAGE);
        });
    }
    
    private void showMapSelection() {
        JDialog dialog = new JDialog(gameWindow, "Select Map", true);
        dialog.setSize(350, 300);
        dialog.setLocationRelativeTo(gameWindow);
        dialog.setLayout(new BorderLayout());
        
        JLabel title = new JLabel("Choose Difficulty", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        title.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        dialog.add(title, BorderLayout.NORTH);
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(3, 1, 10, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 50, 30, 50));

        JButton easyBtn = new JButton("EASY");
        easyBtn.setFont(new Font("Arial", Font.BOLD, 18));
        easyBtn.setBackground(Color.GREEN);
        easyBtn.setForeground(Color.WHITE);
        easyBtn.setPreferredSize(new Dimension(200, 50));
        easyBtn.addActionListener(e -> {
            dialog.dispose();
            gameWindow.startGame(1);
        });
        buttonPanel.add(easyBtn);
        
        JButton mediumBtn = new JButton("MEDIUM");
        mediumBtn.setFont(new Font("Arial", Font.BOLD, 18));
        mediumBtn.setBackground(Color.ORANGE);
        mediumBtn.setForeground(Color.WHITE);
        mediumBtn.setPreferredSize(new Dimension(200, 50));
        mediumBtn.addActionListener(e -> {
            dialog.dispose();
            gameWindow.startGame(2);
        });
        buttonPanel.add(mediumBtn);
        
        JButton hardBtn = new JButton("HARD");
        hardBtn.setFont(new Font("Arial", Font.BOLD, 18));
        hardBtn.setBackground(Color.RED);
        hardBtn.setForeground(Color.WHITE);
        hardBtn.setPreferredSize(new Dimension(200, 50));
        hardBtn.addActionListener(e -> {
            dialog.dispose();
            gameWindow.startGame(3);
        });
        buttonPanel.add(hardBtn);
        
        dialog.add(buttonPanel, BorderLayout.CENTER);
        dialog.setVisible(true);
    }
    
    private void showHighScores() {
    	List<String[]> scores = ScoreManager.getHighScores(10);
    
    	StringBuilder sb = new StringBuilder();
    	sb.append("=== TOP 10 HIGH SCORES ===\n\n");
    
    	if (scores.isEmpty()) {
    		sb.append("No scores yet!");
    	} else {
    		int rank = 1;
    		for (String[] score : scores) {
    			sb.append(rank).append(". ");
    			sb.append(score[0]).append(" - ");    
    			sb.append(score[1]).append(" pts");    
            
    			if (score.length >= 4) {
    				sb.append(" [").append(score[3]).append("]");
    			}
            
    			sb.append(" - ").append(score[2]).append("\n");  
    			rank++;
    		}
    	}
    
    	JOptionPane.showMessageDialog(this, sb.toString(), "High Scores", JOptionPane.INFORMATION_MESSAGE);
    }    
    
    @Override
    protected void paintComponent(Graphics g) {
    	super.paintComponent(g);
    
    	g.setColor(new Color(34, 139, 34));
    	g.fillRect(0, 0, getWidth(), getHeight());
    
    	g.setColor(Color.WHITE);
    	g.setFont(new Font("Arial", Font.BOLD, 72));
    	String title = "TOWER DEFENSE";
    	FontMetrics fm = g.getFontMetrics();
    	int titleX = (getWidth() - fm.stringWidth(title)) / 2;
    	g.drawString(title, titleX, 200);
    
    	if (UserManager.isLoggedIn()) {
    		g.setFont(new Font("Arial", Font.PLAIN, 20));
    		g.drawString("Logged in as: " + UserManager.getCurrentUser(), 10, 30);
    	}
    }
}