package ui;

import game.Map;
import game.Tower;
import game.Tower.TowerType;
import game.Enemy;
import game.Bullet;
import game.TankBullet;
import manager.ScoreManager;
import manager.UserManager;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import javax.imageio.ImageIO;

public class GamePanel extends JPanel implements MouseListener, MouseMotionListener {
    
    private BufferedImage[] planeImages;
    private ArrayList<double[]> planes;
    private String mapName;
    private Map map;
    private int money = 500;
    private int health = 100;
    private GameWindow gameWindow;
    private ArrayList<Tower> towers;
    private ArrayList<Enemy> enemies;
    private ArrayList<Bullet> bullets;
    private ArrayList<TankBullet> tankBullets;
    
    private TowerType selectedTowerType = null;
    private int mouseX, mouseY;
    
    private BufferedImage[] towerImages;
    
    private Tower selectedTower = null;
    private boolean isPaused = false;
    private JButton pauseButton;
    
    private Timer gameTimer;
    
    private int currentWave = 1;
    private int enemiesInWave = 5;        
    private int enemiesSpawned = 0;        
    private int spawnCounter = 0;
    private int spawnInterval = 150;     
    private double baseEnemySpeed = 1.0;
    private int baseEnemyHealth = 50;
    private boolean waveInProgress = false;
    
    private int score = 0;
    private int enemiesKilled = 0;
    private int moneySpent = 0;
    private boolean gameOver = false;
    
    private static final int BAR_HEIGHT = 80;
    
    public GamePanel(GameWindow gameWindow, int level) {
        this.gameWindow = gameWindow;
        
        switch (level) {
        	case 1: mapName = "EASY"; break;
        	case 2: mapName = "MEDIUM"; break;
        	case 3: mapName = "HARD"; break;
        	default: mapName = "UNKNOWN";
        }
        
        
        setPreferredSize(new Dimension(GameWindow.WIDTH, GameWindow.HEIGHT));
        setBackground(Color.BLACK);
        setLayout(null);
        
        map = new Map(level);
        towers = new ArrayList<>();
        enemies = new ArrayList<>();
        bullets = new ArrayList<>();
        tankBullets = new ArrayList<>();
        
        setDifficulty(level);
        loadAssets();
        createTowerBar();
        
        addMouseListener(this);
        addMouseMotionListener(this);
        createPlanes();
        startGameLoop();
    }
    
    private void createPlanes() {
        planes = new ArrayList<>();
        java.util.Random rand = new java.util.Random();
        
        for (int i = 0; i < 3; i++) {
            double x = rand.nextInt(GameWindow.WIDTH);
            double y = rand.nextInt(400) + BAR_HEIGHT;
            double speedX = (rand.nextDouble() * 2 - 1) * 1.5;
            double speedY = (rand.nextDouble() * 2 - 1) * 1.5;
            int type = rand.nextInt(2);
            
            planes.add(new double[]{x, y, speedX, speedY, type});
        }
    }
    
    private void setDifficulty(int level) {
    	switch (level) {
        	case 1: 
        		money = 800;
        		enemiesInWave = 5;
        		baseEnemySpeed = 3.0;
        		baseEnemyHealth = 50;
        		spawnInterval = 150;
        		break;
        	case 2: 
        		money = 700;
        		enemiesInWave = 7;
        		baseEnemySpeed = 4.0;
        		baseEnemyHealth = 70;
        		spawnInterval = 120;
        		break;
        	case 3: 
        		money = 600;
        		enemiesInWave = 10;
        		baseEnemySpeed = 5.0;
        		baseEnemyHealth = 90;
        		spawnInterval = 100;
        		break;
    	}
    	waveInProgress = true;
    }  
    
    private void loadAssets() {
        towerImages = new BufferedImage[4];
        String[] imageNames = {"towerDefense_tile249.png", "towerDefense_tile250.png", 
                               "towerDefense_tile226.png", "towerDefense_tile203.png"};
        
        for (int i = 0; i < 4; i++) {
            try {
                towerImages[i] = ImageIO.read(new File("assets/" + imageNames[i]));
            } catch (Exception e) {
                towerImages[i] = null;
            }
        }
        
        planeImages = new BufferedImage[2];
        try {
            planeImages[0] = ImageIO.read(new File("assets/towerDefense_tile270.png"));
            planeImages[1] = ImageIO.read(new File("assets/towerDefense_tile271.png"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void createTowerBar() {
    	int buttonSize = 60;
    	int startX = 10;
    	int y = 5;
    
    	TowerType[] types = TowerType.values();
    
    	for (int i = 0; i < types.length; i++) {
    		TowerType type = types[i];
    		JButton btn = new JButton();
    		btn.setBounds(startX + (i * 70), y, buttonSize, buttonSize);
        
    		if (towerImages[i] != null) {
    			Image scaled = towerImages[i].getScaledInstance(50, 50, Image.SCALE_SMOOTH);
    			btn.setIcon(new ImageIcon(scaled));
    		} else {
    			btn.setText(type.name().substring(0, 1));
    		}
        
    		btn.setFocusable(false);
        
    		final TowerType selectedType = type;
    		btn.addActionListener(e -> {
    			selectedTowerType = selectedType;
    			selectedTower = null;
    		});
        
    		add(btn);
        
    		JLabel priceLabel = new JLabel("$" + type.getCost(), SwingConstants.CENTER);
    		priceLabel.setBounds(startX + (i * 70), y + buttonSize, buttonSize, 15);
    		priceLabel.setFont(new Font("Arial", Font.BOLD, 11));
    		priceLabel.setForeground(Color.BLACK);
    		add(priceLabel);
    	}
    
  
    	JButton sellBtn = new JButton();
    	sellBtn.setBounds(startX + (types.length * 70), y, buttonSize, buttonSize);
    	sellBtn.setBackground(Color.ORANGE);
    	sellBtn.setFocusable(false);


    	try {
    		BufferedImage sellIcon = ImageIO.read(new File("assets/towerDefense_tile287.png"));
    		Image scaled = sellIcon.getScaledInstance(50, 50, Image.SCALE_SMOOTH);
    		sellBtn.setIcon(new ImageIcon(scaled));
    	} catch (Exception ex) {
    		sellBtn.setText("SELL");
    	}

    	sellBtn.addActionListener(e -> {
    		if (isPaused) return;
    		
    		if (selectedTower != null) {
    			int refund = selectedTower.getType().getCost() / 2;
    			money += refund;
    			towers.remove(selectedTower);
    			selectedTower = null;
    			selectedTowerType = null;
    			repaint();
    		}
    	});
    	add(sellBtn);

    	JLabel sellLabel = new JLabel("SELL", SwingConstants.CENTER);
    	sellLabel.setBounds(startX + (types.length * 70), y + buttonSize, buttonSize, 15);
    	sellLabel.setFont(new Font("Arial", Font.BOLD, 11));
    	sellLabel.setForeground(Color.BLACK);
    	add(sellLabel);
    

    	pauseButton = new JButton("PAUSE");
    	pauseButton.setBounds(startX + (types.length * 70) + 80, y, 80, buttonSize);
    	pauseButton.setBackground(Color.CYAN);
    	pauseButton.setFocusable(false);
    	pauseButton.addActionListener(e -> {
    		isPaused = !isPaused;
    		pauseButton.setText(isPaused ? "PLAY" : "PAUSE");
    	});
    	add(pauseButton);
    
    	JButton leaveBtn = new JButton("LEAVE");
    	leaveBtn.setBounds(startX + (types.length * 70) + 170, y, 80, buttonSize);
    	leaveBtn.setBackground(Color.RED);
    	leaveBtn.setForeground(Color.WHITE);
    	leaveBtn.setFocusable(false);
    	leaveBtn.addActionListener(e -> {
    		int choice = JOptionPane.showConfirmDialog(
    				this,
    				"Are you sure you want to leave?\nYour score will be saved!",
    				"Leave Game",
    				JOptionPane.YES_NO_OPTION,
    				JOptionPane.WARNING_MESSAGE
    	    );
    		if (choice == JOptionPane.YES_OPTION) {
    			endGame();
    		}
    	});
    	add(leaveBtn);
    }    
    private void startGameLoop() {
        gameTimer = new Timer(16, e -> {
            updateGame();
            repaint();
        });
        gameTimer.start();
    }
    
    private void updateGame() {
    	if (isPaused || gameOver) return;
    
    	if (waveInProgress && enemiesSpawned < enemiesInWave + (currentWave - 1) * 2) {
    		spawnCounter++;
    		if (spawnCounter >= spawnInterval) {
    			spawnEnemy();
    			spawnCounter = 0;
    		}
    	}
    
    	Iterator<Enemy> enemyIterator = enemies.iterator();
    	while (enemyIterator.hasNext()) {
    		Enemy enemy = enemyIterator.next();
    		enemy.update();
        
    		if (enemy.hasReachedEnd()) {
    			int damage = enemy.isTank() ? 20 : 10; 
    		    health -= damage;
    			enemyIterator.remove();
    		} else if (!enemy.isAlive()) {
    			money += enemy.isTank() ? 50 : 20;
    			enemiesKilled++;
    			enemyIterator.remove();
    		}
    	}
    

    	int totalEnemiesThisWave = enemiesInWave + (currentWave - 1) * 2;
    	if (enemiesSpawned >= totalEnemiesThisWave && enemies.isEmpty() && waveInProgress) {
    		startNextWave();
    	}
    
    	for (Tower tower : towers) {
    		if (tower.canFire()) {
    			Enemy target = tower.findTarget(enemies);
    			if (target != null) {
    				Bullet bullet = new Bullet(tower.getCenterX(), tower.getCenterY(), target, tower.getDamage());
    				bullets.add(bullet);
    				tower.resetFireTime();
    			}
    		}
    	}
    
    	Iterator<Bullet> bulletIterator = bullets.iterator();
    	while (bulletIterator.hasNext()) {
    		Bullet bullet = bulletIterator.next();
    		bullet.update();
    		if (!bullet.isActive()) {
    			bulletIterator.remove();
    		}
    	}
    
    	for (Enemy enemy : enemies) {
    		if (enemy.isTank() && enemy.isAlive()) {
    			TankBullet bullet = enemy.attackTower(towers);
    			if (bullet != null) {
    				tankBullets.add(bullet);
    			}
    		}
    	}
    
    	Iterator<TankBullet> tankBulletIterator = tankBullets.iterator();
    	while (tankBulletIterator.hasNext()) {
    		TankBullet bullet = tankBulletIterator.next();
    		bullet.update();
    		if (!bullet.isActive()) {
    			tankBulletIterator.remove();
    		}
    	}
    
    	Iterator<Tower> towerIterator = towers.iterator();
    	while (towerIterator.hasNext()) {
    		Tower tower = towerIterator.next();
    		if (tower.isDestroyed()) {
    			towerIterator.remove();
    		}
    	}
    
    	for (double[] plane : planes) {
    		plane[0] += plane[2];
    		plane[1] += plane[3];
        
    		if (plane[0] < -64) plane[0] = GameWindow.WIDTH;
    		if (plane[0] > GameWindow.WIDTH) plane[0] = -64;
    		if (plane[1] < BAR_HEIGHT) plane[1] = GameWindow.HEIGHT - 64;
    		if (plane[1] > GameWindow.HEIGHT - 64) plane[1] = BAR_HEIGHT;
    	}
    
    	if (health <= 0) {
    		health = 0;
    		gameOver = true;
    		endGame();
    	}
    }
    
    private void startNextWave() {
    	currentWave++;
    	enemiesSpawned = 0;
    	spawnCounter = 0;
    
    	int waveBonus = 50 + (currentWave * 10);
    	money += waveBonus;
    
    	spawnInterval = Math.max(60, spawnInterval - 5);
    
    	waveInProgress = true;
    }   
    
    private void spawnEnemy() {
    	int[][] path = map.getPath();
    	double waveMultiplier = 1 + (currentWave - 1) * 0.2;  
    
    	int enemyHealth = (int) (baseEnemyHealth * waveMultiplier);
    	double enemySpeed = baseEnemySpeed + (currentWave - 1) * 0.1;  
    
    	boolean isTank = (currentWave >= 2 && enemiesSpawned > 0 && enemiesSpawned % 5 == 0);
    
    	Enemy enemy = new Enemy(path, enemyHealth, enemySpeed, isTank);
    	enemies.add(enemy);
    	enemiesSpawned++;
    }    
    
    private void endGame() {
    	gameTimer.stop();
    
    	int remainingTowerHealth = 0;
    	for (Tower tower : towers) {
    		remainingTowerHealth += tower.getHealth();
    	}
    
    	score = (enemiesKilled * 20) + (currentWave * 100) + moneySpent + remainingTowerHealth;
    
    	String date = java.time.LocalDateTime.now().format(
    			java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
    			);
    	ScoreManager.saveScore(UserManager.getCurrentUser(), score, date, mapName);
    
    	String message = "GAME OVER!";
    	message += "\n\nMap: " + mapName;
    	message += "\nWaves Survived: " + (currentWave - 1);
    	message += "\nEnemies Killed: " + enemiesKilled;
    	message += "\nMoney Spent: " + moneySpent + "$";
    	message += "\nTowers Remaining: " + towers.size();
    	message += "\nTower Health Bonus: " + remainingTowerHealth;
    	message += "\n\nFinal Score: " + score;
    
    	JOptionPane.showMessageDialog(this, message, "Game Over", JOptionPane.INFORMATION_MESSAGE);
    
    	gameWindow.showMainMenu();
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        g.setColor(new Color(200, 200, 200));
        g.fillRect(0, 0, GameWindow.WIDTH, BAR_HEIGHT);
        
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.translate(0, BAR_HEIGHT);
        
        map.draw(g2d);
        
        for (Tower tower : towers) {
            tower.draw(g2d);
        }
        
        for (Enemy enemy : enemies) {
            enemy.draw(g2d);
        }
        
        for (Bullet bullet : bullets) {
            bullet.draw(g2d);
        }
        
        for (TankBullet bullet : tankBullets) {
            bullet.draw(g2d);
        }
        
        for (double[] plane : planes) {
            int type = (int) plane[4];
            if (planeImages != null && planeImages[type] != null) {
                int px = (int) plane[0];
                int py = (int) plane[1] - BAR_HEIGHT;
                g2d.drawImage(planeImages[type], px, py, 48, 48, null);
            }
        }
        
        if (!isPaused && selectedTowerType != null && mouseY > BAR_HEIGHT) {
            drawTowerPreview(g2d);
        }
        
        if (selectedTower != null) {
            g2d.setColor(new Color(255, 165, 0, 150));
            g2d.setStroke(new BasicStroke(3));
            g2d.drawRect(
                selectedTower.getCol() * Map.TILE_SIZE,
                selectedTower.getRow() * Map.TILE_SIZE,
                Map.TILE_SIZE,
                Map.TILE_SIZE
            );
        }
        
        g2d.dispose();
        
        drawUI(g);
    }
    
    private void drawTowerPreview(Graphics g) {
        int col = mouseX / Map.TILE_SIZE;
        int row = (mouseY - BAR_HEIGHT) / Map.TILE_SIZE;
        
        int previewX = col * Map.TILE_SIZE;
        int previewY = row * Map.TILE_SIZE;
        
        boolean canPlace = canPlaceTower(col, row);
        
        Graphics2D g2d = (Graphics2D) g;
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
        
        g.setColor(canPlace ? Color.GREEN : Color.RED);
        g.fillRect(previewX, previewY, Map.TILE_SIZE, Map.TILE_SIZE);
        
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
    }
    
    private boolean canPlaceTower(int col, int row) {
        if (!map.canPlaceTower(col, row)) {
            return false;
        }
        
        for (Tower tower : towers) {
            if (tower.getCol() == col && tower.getRow() == row) {
                return false;
            }
        }
        
        if (selectedTowerType != null && money < selectedTowerType.getCost()) {
            return false;
        }
        
        return true;
    }
    
    private void drawUI(Graphics g) {
    	g.setColor(Color.BLACK);
    	g.setFont(new Font("Arial", Font.BOLD, 20));
    	g.drawString(money + "$", 580, 35);
    	g.drawString(health + "%", 580, 60);
    	g.drawString("Wave: " + currentWave, 680, 35);
    	g.drawString("Kills: " + enemiesKilled, 680, 60);
    }    
    @Override
    public void mouseClicked(MouseEvent e) {
        int clickX = e.getX();
        int clickY = e.getY() - BAR_HEIGHT;
        
        if (clickY < 0) return;
        
        int col = clickX / Map.TILE_SIZE;
        int row = clickY / Map.TILE_SIZE;
        
        for (Tower tower : towers) {
            if (tower.getCol() == col && tower.getRow() == row) {
                selectedTower = tower;
                selectedTowerType = null;
                repaint();
                return;
            }
        }
        
        if (isPaused) {
            return; 
        }
        
        if (selectedTowerType != null && map.canPlaceTower(col, row)) {
            for (Tower t : towers) {
                if (t.getCol() == col && t.getRow() == row) {
                    return;
                }
            }
            
            if (money >= selectedTowerType.getCost()) {
                Tower tower = new Tower(col, row, selectedTowerType);
                towers.add(tower);
                money -= selectedTowerType.getCost();
                moneySpent += selectedTowerType.getCost();
                selectedTower = null;
            }
        }
        
        repaint();
    }	
    
    @Override
    public void mouseMoved(MouseEvent e) {
        mouseX = e.getX();
        mouseY = e.getY();
        repaint();
    }
    
    @Override public void mousePressed(MouseEvent e) {}
    @Override public void mouseReleased(MouseEvent e) {}
    @Override public void mouseEntered(MouseEvent e) {}
    @Override public void mouseExited(MouseEvent e) {}
    @Override public void mouseDragged(MouseEvent e) {}
}