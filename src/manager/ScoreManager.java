package manager;

import java.io.*;
import java.util.*;

public class ScoreManager {
    
    private static final String FILE_PATH = "data/scores.txt";
   
    public static void saveScore(String username, int score, String date, String mapName) {
        try {
            new File("data").mkdirs();
            
            PrintWriter writer = new PrintWriter(new FileWriter(FILE_PATH, true));
            writer.println(username + "," + score + "," + date + "," + mapName);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
   
    public static List<String[]> getHighScores(int limit) {
        List<String[]> scores = new ArrayList<>();
        File file = new File(FILE_PATH);
        
        if (!file.exists()) {
            return scores;
        }
        
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 3) {
                    scores.add(parts);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        scores.sort((a, b) -> Integer.parseInt(b[1]) - Integer.parseInt(a[1]));
        
        if (scores.size() > limit) {
            scores = scores.subList(0, limit);
        }
        
        return scores;
    }
}