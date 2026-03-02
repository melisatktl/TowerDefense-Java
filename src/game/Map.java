package game;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Random;
import javax.imageio.ImageIO;

public class Map {
    
    public static final int TILE_SIZE = 64;
    public static final int COLS = 20;
    public static final int ROWS = 13;
    
    private int[][] tiles;
    private int level;
    
    private BufferedImage grassTile;
    private BufferedImage pathTile;
    private BufferedImage sandTile;
    private BufferedImage grayPathTile;
    
    private BufferedImage[] bushTiles;
    private BufferedImage[] rockTiles;
    
    private ArrayList<int[]> decorations;
    
    public Map(int level) {
        this.level = level;
        loadTiles();
        loadLevel(level);
        generateDecorations();
    }
    
    private BufferedImage loadImage(String filename) {
        try {
            File file = new File("assets/" + filename);
            if (file.exists()) {
                return ImageIO.read(file);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    private void loadTiles() {
        grassTile = loadImage("towerDefense_tile024.png");
        pathTile = loadImage("towerDefense_tile093.png");
        sandTile = loadImage("towerDefense_tile098.png");
        grayPathTile = loadImage("towerDefense_tile159.png");
        
        bushTiles = new BufferedImage[5];
        for (int i = 0; i < 5; i++) {
            bushTiles[i] = loadImage("towerDefense_tile" + (130 + i) + ".png");
        }
        
        rockTiles = new BufferedImage[3];
        for (int i = 0; i < 3; i++) {
            rockTiles[i] = loadImage("towerDefense_tile" + (135 + i) + ".png");
        }
    }
    
    private void loadLevel(int level) {
        switch (level) {
            case 1: loadLevel1(); break;
            case 2: loadLevel2(); break;
            case 3: loadLevel3(); break;
        }
    }
    
    private void loadLevel1() {
        tiles = new int[][] {
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1},
            {0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}
        };
    }
    
    private void loadLevel2() {
        tiles = new int[][] {
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0},
            {0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 0, 0},
            {0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1, 0, 0},
            {0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1, 0, 0},
            {0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1, 0, 0},
            {0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}
        };
    }
    
    private void loadLevel3() {
        tiles = new int[][] {
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}
        };
    }
    
    private void generateDecorations() {
        decorations = new ArrayList<>();
        Random rand = new Random(level * 100);
        
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                if (tiles[row][col] == 0) {
                    if (rand.nextInt(100) < 20) {
                        if (rand.nextInt(100) < 70) {
                            decorations.add(new int[]{col, row, rand.nextInt(5)});
                        } else {
                            decorations.add(new int[]{col, row, 5 + rand.nextInt(3)});
                        }
                    }
                }
            }
        }
    }
    
    public void draw(Graphics g) {
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                int x = col * TILE_SIZE;
                int y = row * TILE_SIZE;
                
                BufferedImage groundImg;
                BufferedImage pathImg;
                
                if (level == 2) {
                    groundImg = sandTile;
                    pathImg = grayPathTile;
                } else if (level == 3) {
                    groundImg = grassTile;
                    pathImg = grayPathTile;
                } else {
                    groundImg = grassTile;
                    pathImg = pathTile;
                }
                
                if (tiles[row][col] == 0) {
                    if (groundImg != null) {
                        g.drawImage(groundImg, x, y, TILE_SIZE, TILE_SIZE, null);
                    } else {
                        g.setColor(new Color(34, 139, 34));
                        g.fillRect(x, y, TILE_SIZE, TILE_SIZE);
                    }
                } else {
                    if (pathImg != null) {
                        g.drawImage(pathImg, x, y, TILE_SIZE, TILE_SIZE, null);
                    } else {
                        if (level == 1) {
                            g.setColor(new Color(139, 90, 43));
                        } else {
                            g.setColor(new Color(128, 128, 128));
                        }
                        g.fillRect(x, y, TILE_SIZE, TILE_SIZE);
                    }
                }
            }
        }
        
        for (int[] deco : decorations) {
            int col = deco[0];
            int row = deco[1];
            int type = deco[2];
            
            int x = col * TILE_SIZE;
            int y = row * TILE_SIZE;
            
            BufferedImage img = null;
            if (type < 5 && bushTiles != null) {
                img = bushTiles[type];
            } else if (type >= 5 && rockTiles != null) {
                img = rockTiles[type - 5];
            }
            
            if (img != null) {
                g.drawImage(img, x, y, TILE_SIZE, TILE_SIZE, null);
            }
        }
    }
    
    public int[][] getPath() {
        switch (level) {
            case 1:
                return new int[][] {
                    {0, 2}, {6, 2}, {6, 6}, {13, 6}, {13, 2}, {19, 2}
                };
            case 2:
                return new int[][] {
                    {0, 1}, {17, 1}, {17, 9}, {19, 9}
                };
            case 3:
                return new int[][] {
                    {0, 1}, {4, 1}, {4, 3}, {8, 3}, {8, 5}, {12, 5}, {12, 7}, {16, 7}, {16, 9}, {19, 9}
                };
            default:
                return new int[][] {{0, 2}, {19, 2}};
        }
    }
    
    public boolean isPath(int col, int row) {
        if (row < 0 || row >= ROWS || col < 0 || col >= COLS) {
            return false;
        }
        return tiles[row][col] == 1;
    }
    
    public boolean canPlaceTower(int col, int row) {
        if (row < 0 || row >= ROWS || col < 0 || col >= COLS) {
            return false;
        }
        return tiles[row][col] == 0;
    }
    
    public int getLevel() {
        return level;
    }
}