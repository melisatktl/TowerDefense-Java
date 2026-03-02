package game;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;


import javax.imageio.ImageIO;

public class Bullet {
    
    private double x, y;
    private double speed = 8.0;
    private int damage;
    private boolean active;
    private Enemy target;
    private static BufferedImage bulletImage;
    private static boolean imageLoaded = false;
    
    public Bullet(int startX, int startY, Enemy target, int damage) {
        this.x = startX;
        this.y = startY;
        this.target = target;
        this.damage = damage;
        this.active = true;
        
        loadImage();
    }
    
    public void update() {
        if (!active) return;
        
        if (!target.isAlive() || target.hasReachedEnd()) {
            active = false;
            return;
        }
       
        double dx = target.getX() - x;
        double dy = target.getY() - y;
        double distance = Math.sqrt(dx * dx + dy * dy);
        
        if (distance < speed) {
            target.takeDamage(damage);
            active = false;
        } else {
            x += (dx / distance) * speed;
            y += (dy / distance) * speed;
        }
    }
    
    private void loadImage() {
        if (imageLoaded) return;
        try {
            bulletImage = ImageIO.read(new File("assets/towerDefense_tile295.png"));
        } catch (Exception e) {
            bulletImage = null;
        }
        imageLoaded = true;
    }
    
    public void draw(Graphics g) {
        if (!active) return;
        
        if (bulletImage != null) {
            double dx = target.getX() - x;
            double dy = target.getY() - y;
            double angle = Math.atan2(dy, dx);
            
            Graphics2D g2d = (Graphics2D) g;
            
            g2d.translate(x, y);
            g2d.rotate(angle);
            g2d.drawImage(bulletImage, -16, -8, 32, 32, null);
            g2d.rotate(-angle);
            g2d.translate(-x, -y);
        } else {
            g.setColor(Color.ORANGE);
            g.fillOval((int)x - 8, (int)y - 8, 16, 16);
        }
    }
    
    public boolean isActive() {
        return active;
    }
}