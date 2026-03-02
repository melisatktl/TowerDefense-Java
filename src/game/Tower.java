package game;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import java.util.ArrayList;

public class Tower {
    
    private int x, y;
    private int col, row;
    private int cost;
    private int damage;
    private int range;
    private int fireRate;
    private long lastFireTime;
    private TowerType type;
    private BufferedImage image;
    
    private int health;    
    private int maxHealth;  
    
    public enum TowerType {
        BASIC(100, 10, 150, 1000, "towerDefense_tile249.png", 100),
        SNIPER(200, 25, 250, 2000, "towerDefense_tile250.png", 80),
        RAPID(250, 5, 170, 300, "towerDefense_tile226.png", 120),
        CANNON(300, 50, 120, 2500, "towerDefense_tile203.png", 150);
        
        private int cost, damage, range, fireRate, health;
        private String imageName;
        
        TowerType(int cost, int damage, int range, int fireRate, String imageName, int health) {
            this.cost = cost;
            this.damage = damage;
            this.range = range;
            this.fireRate = fireRate;
            this.imageName = imageName;
            this.health = health;
        }
        
        public int getCost() { return cost; }
        public int getDamage() { return damage; }
        public int getRange() { return range; }
        public int getFireRate() { return fireRate; }
        public String getImageName() { return imageName; }
        public int getHealth() { return health; }
    }
    
    public Tower(int col, int row, TowerType type) {
        this.col = col;
        this.row = row;
        this.x = col * Map.TILE_SIZE;
        this.y = row * Map.TILE_SIZE;
        this.type = type;
        this.cost = type.getCost();
        this.damage = type.getDamage();
        this.range = type.getRange();
        this.fireRate = type.getFireRate();
        this.health = type.getHealth();
        this.maxHealth = type.getHealth();
        this.lastFireTime = 0;
        loadImage();
    }
    
    private void loadImage() {
        try {
            image = ImageIO.read(new File("assets/" + type.getImageName()));
        } catch (Exception e) {
            image = null;
        }
    }
    
    
    public Enemy findTarget(ArrayList<Enemy> enemies) {
        Enemy closest = null;
        double closestDist = Double.MAX_VALUE;
        
        int centerX = getCenterX();
        int centerY = getCenterY();
        
        for (Enemy enemy : enemies) {
            if (!enemy.isAlive()) continue;
            
            double dx = enemy.getX() - centerX;
            double dy = enemy.getY() - centerY;
            double dist = Math.sqrt(dx * dx + dy * dy);
            
            if (dist <= range && dist < closestDist) {
                closest = enemy;
                closestDist = dist;
            }
        }
        
        return closest;
    }
    
    public void takeDamage(int damage) {
        health -= damage;
        if (health < 0) health = 0;
    }
    
    public boolean isDestroyed() {
        return health <= 0;
    }
    
    public void draw(Graphics g) {
        if (image != null) {
            g.drawImage(image, x, y, Map.TILE_SIZE, Map.TILE_SIZE, null);
        } else {
            g.setColor(Color.GRAY);
            g.fillRect(x, y, Map.TILE_SIZE, Map.TILE_SIZE);
            g.setColor(Color.RED);
            g.fillOval(x + 15, y + 15, 34, 34);
        }
        
        if (health < maxHealth) {
            drawHealthBar(g);
        }
    }
    
    private void drawHealthBar(Graphics g) {
        int barWidth = 50;
        int barHeight = 5;
        int barX = x + 7;
        int barY = y + Map.TILE_SIZE - 8;
        
        g.setColor(Color.RED);
        g.fillRect(barX, barY, barWidth, barHeight);
        
        g.setColor(Color.BLUE);
        int healthWidth = (int) ((double) health / maxHealth * barWidth);
        g.fillRect(barX, barY, healthWidth, barHeight);
        
        g.setColor(Color.BLACK);
        g.drawRect(barX, barY, barWidth, barHeight);
    }
    
    public void drawRange(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(new Color(255, 255, 255, 50));
        int centerX = getCenterX();
        int centerY = getCenterY();
        g2d.fillOval(centerX - range, centerY - range, range * 2, range * 2);
    }
    
    public boolean canFire() {
        return System.currentTimeMillis() - lastFireTime >= fireRate;
    }
    
    public void resetFireTime() {
        lastFireTime = System.currentTimeMillis();
    }
    
    public int getX() { return x; }
    public int getY() { return y; }
    public int getCenterX() { return x + Map.TILE_SIZE / 2; }
    public int getCenterY() { return y + Map.TILE_SIZE / 2; }
    public int getCol() { return col; }
    public int getRow() { return row; }
    public int getCost() { return cost; }
    public int getDamage() { return damage; }
    public int getRange() { return range; }
    public int getHealth() { return health; }
    public TowerType getType() { return type; }
}
