package game;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Random;
import javax.imageio.ImageIO;

public class Enemy {
    
    private double x, y;
    private int health;
    private int maxHealth;
    private double speed;
    private boolean alive;
    private boolean reachedEnd;
    
    private int pathIndex;
    private int[][] path;
    
    private BufferedImage image;
    private static BufferedImage[] enemyImages;
    private static BufferedImage[] tankBodies;
    private static BufferedImage[] tankTurrets;
    private static boolean imagesLoaded = false;
    
    private boolean isTank;
    private int tankType;
    private long lastAttackTime;
    private static final int TANK_ATTACK_RATE = 2000;
    private static final int TANK_DAMAGE = 15;
    
    public Enemy(int[][] path, int health, double speed) {
        this(path, health, speed, false);
    }
    
    public Enemy(int[][] path, int health, double speed, boolean isTank) {
        this.path = path;
        this.isTank = isTank;
        
        if (isTank) {
            this.health = health * 4;
            this.speed = speed * 0.5;
        } else {
            this.health = health;
            this.speed = speed;
        }
        
        this.maxHealth = this.health;
        this.alive = true;
        this.reachedEnd = false;
        this.pathIndex = 0;
        this.lastAttackTime = 0;
        
        this.x = path[0][0] * Map.TILE_SIZE + Map.TILE_SIZE / 2;
        this.y = path[0][1] * Map.TILE_SIZE + Map.TILE_SIZE / 2;
        
        loadImages();
        
        if (isTank) {
            Random rand = new Random();
            tankType = rand.nextInt(2);
        } else {
            selectRandomImage();
        }
    }
    
    private void loadImages() {
        if (imagesLoaded) return;
        
        enemyImages = new BufferedImage[4];
        String[] imageNames = {
            "towerDefense_tile245.png",
            "towerDefense_tile246.png",
            "towerDefense_tile247.png",
            "towerDefense_tile248.png"
        };
        
        for (int i = 0; i < 4; i++) {
            try {
                enemyImages[i] = ImageIO.read(new File("assets/" + imageNames[i]));
            } catch (Exception e) {
                enemyImages[i] = null;
            }
        }
        
        tankBodies = new BufferedImage[2];
        tankTurrets = new BufferedImage[2];
        
        try {
            tankBodies[0] = ImageIO.read(new File("assets/towerDefense_tile268.png"));
            tankBodies[1] = ImageIO.read(new File("assets/towerDefense_tile269.png"));
            tankTurrets[0] = ImageIO.read(new File("assets/towerDefense_tile291.png"));
            tankTurrets[1] = ImageIO.read(new File("assets/towerDefense_tile292.png"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        imagesLoaded = true;
    }
    
    private void selectRandomImage() {
        Random rand = new Random();
        int type = rand.nextInt(4);
        if (enemyImages != null && enemyImages[type] != null) {
            image = enemyImages[type];
        }
    }
    
    public void update() {
        if (!alive || reachedEnd) return;
        
        int targetX = path[pathIndex][0] * Map.TILE_SIZE + Map.TILE_SIZE / 2;
        int targetY = path[pathIndex][1] * Map.TILE_SIZE + Map.TILE_SIZE / 2;
        
        double dx = targetX - x;
        double dy = targetY - y;
        double distance = Math.sqrt(dx * dx + dy * dy);
        
        if (distance < speed) {
            pathIndex++;
            
            if (pathIndex >= path.length) {
                reachedEnd = true;
            }
        } else {
            x += (dx / distance) * speed;
            y += (dy / distance) * speed;
        }
    }
    
    public TankBullet attackTower(ArrayList<Tower> towers) {
        if (!isTank || !alive) return null;
        
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastAttackTime < TANK_ATTACK_RATE) return null;
        
        Tower closest = null;
        double closestDist = 150;
        
        for (Tower tower : towers) {
            double dx = tower.getCenterX() - x;
            double dy = tower.getCenterY() - y;
            double dist = Math.sqrt(dx * dx + dy * dy);
            
            if (dist < closestDist) {
                closest = tower;
                closestDist = dist;
            }
        }
        
        if (closest != null) {
            lastAttackTime = currentTime;
            return new TankBullet(x, y, closest, TANK_DAMAGE);
        }
        
        return null;
    }
    
    public void draw(Graphics g) {
        if (!alive) return;
        
        int drawX = (int) x - Map.TILE_SIZE / 2;
        int drawY = (int) y - Map.TILE_SIZE / 2;
        
        if (isTank) {
            if (tankBodies[tankType] != null) {
                g.drawImage(tankBodies[tankType], drawX + 5, drawY + 15, 54, 40, null);
            }
            if (tankTurrets[tankType] != null) {
                g.drawImage(tankTurrets[tankType], drawX + 10, drawY + 5, 44, 44, null);
            }
        } else {
            if (image != null) {
                g.drawImage(image, drawX + 10, drawY + 10, 44, 44, null);
            } else {
                g.setColor(Color.RED);
                g.fillOval(drawX + 10, drawY + 10, 44, 44);
            }
        }
        
        drawHealthBar(g, drawX, drawY);
    }
    
    private void drawHealthBar(Graphics g, int drawX, int drawY) {
        int barWidth = 50;
        int barHeight = 6;
        int barX = drawX + 7;
        int barY = drawY - 5;
        
        if (isTank) {
            barHeight = 10;
            barY = drawY - 8;
        }
        
        g.setColor(Color.RED);
        g.fillRect(barX, barY, barWidth, barHeight);
        
        g.setColor(isTank ? Color.ORANGE : Color.GREEN);
        int healthWidth = (int) ((double) health / maxHealth * barWidth);
        g.fillRect(barX, barY, healthWidth, barHeight);
        
        g.setColor(Color.BLACK);
        g.drawRect(barX, barY, barWidth, barHeight);
    }
    
    public void takeDamage(int damage) {
        health -= damage;
        if (health <= 0) {
            health = 0;
            alive = false;
        }
    }
    
    public double getX() { return x; }
    public double getY() { return y; }
    public int getHealth() { return health; }
    public boolean isAlive() { return alive; }
    public boolean hasReachedEnd() { return reachedEnd; }
    public boolean isTank() { return isTank; }
    public static int getTankDamage() { return TANK_DAMAGE; }
}