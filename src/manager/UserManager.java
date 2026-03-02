package manager;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class UserManager {
    
    private static final String FILE_PATH = "data/users.txt";
    private static String currentUser = null; 
    
    public static boolean register(String username, String password) {
        if (userExists(username)) {
            return false;
        }
        
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_PATH, true))) {
            writer.println(username + "," + password);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public static boolean login(String username, String password) {
        Map<String, String> users = loadUsers();
        
        if (users.containsKey(username) && users.get(username).equals(password)) {
            currentUser = username;
            return true;
        }
        return false;
    }
    
    public static boolean userExists(String username) {
        Map<String, String> users = loadUsers();
        return users.containsKey(username);
    }

    private static Map<String, String> loadUsers() {
        Map<String, String> users = new HashMap<>();
        File file = new File(FILE_PATH);

        if (!file.exists()) {
            file.getParentFile().mkdirs();
            return users;
        }
        
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    users.put(parts[0], parts[1]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return users;
    }
    
    public static String getCurrentUser() {
        return currentUser;
    }
    
    public static boolean isLoggedIn() {
        return currentUser != null;
    }
    
    public static void logout() {
        currentUser = null;
    }
}