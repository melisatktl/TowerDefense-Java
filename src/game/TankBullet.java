package game;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class TankBullet {
    
    private double x, y;
    private double targetX, targetY;
    private Tower targetTower;
    private int damage;
    private double speed = 5.0;
    private boolean active;
    
    private static BufferedImage bulletImage;
    private static boolean imageLoaded = false;
    
    public TankBullet(double startX, double startY, Tower target, int damage) {
        this.x = startX;
        this.y = startY;
        this.targetTower = target;
        this.targetX = target.getCenterX();
        this.targetY = target.getCenterY();
        this.damage = damage;
        this.active = true;
        
        loadImage();
    }
    
    private void loadImage() {
        if (imageLoaded) return;
        try {
            bulletImage = ImageIO.read(new File("assets/towerDefense_tile297.png"));
        } catch (Exception e) {
            bulletImage = null;
        }
        imageLoaded = true;
    }
    
    public void update() {
        if (!active) return;
        
        double dx = targetX - x;
        double dy = targetY - y;
        double distance = Math.sqrt(dx * dx + dy * dy);
        
        if (distance < speed) {
            if (targetTower != null) {
                targetTower.takeDamage(damage);
            }
            active = false;
        } else {
            x += (dx / distance) * speed;
            y += (dy / distance) * speed;
        }
    }
    
    public void draw(Graphics g) {
        if (!active) return;
        
        if (bulletImage != null) {
            double dx = targetX - x;
            double dy = targetY - y;
            double angle = Math.atan2(dy, dx);
            
            Graphics2D g2d = (Graphics2D) g;
            
            g2d.translate(x, y);
            g2d.rotate(angle);
            g2d.drawImage(bulletImage, -16, -8, 32, 16, null);
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
